package com.ul.customerprofile.controller;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.model.Tour;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import com.ul.customerprofile.repository.TourRepository;
import com.ul.customerprofile.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")  
public class CustomerProfileController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerProfileController.class);
    
    private final CustomerProfileService service;
    private final CustomerProfileRepository repository;
    private final TourRepository tourRepository;

    @Autowired
    public CustomerProfileController(CustomerProfileService service, CustomerProfileRepository repository, TourRepository tourRepository){
        this.service = service;
        this.repository = repository;
        this.tourRepository = tourRepository;
    }

    @GetMapping
    public List<CustomerProfile> getAllProfiles() {
        return service.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfile> getProfileById(@PathVariable Long id) {
        Optional<CustomerProfile> profile = repository.findById(id);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CustomerProfile> createProfile(@RequestBody CustomerProfile profile) {
        try {
            CustomerProfile savedProfile = service.createProfile(profile);
            return ResponseEntity.ok(savedProfile);
        } catch (RuntimeException e) {
            logger.error("Profile creation failed"); // ← Generic message
        if (logger.isDebugEnabled()) {
            logger.debug("Creation error: {}", e.getClass().getSimpleName()); // ← Only log error type
        }
        return ResponseEntity.badRequest().body(new CustomerProfile());
    }
}
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerProfile> updateProfile(@PathVariable Long id, @RequestBody CustomerProfile updatedProfile) {
        return repository.findById(id).map(profile -> {
            profile.setName(updatedProfile.getName());
            profile.setPhone(updatedProfile.getPhone());
            profile.setPassword(updatedProfile.getPassword());
            return ResponseEntity.ok(repository.save(profile));
        }).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/login")
    public ResponseEntity<CustomerProfile> login(@RequestBody CustomerProfile profile) {
        logger.debug("Authentication attempt"); // Generic message
    Optional<CustomerProfile> customer = service.login(profile.getEmail(), profile.getPassword());
    
    return customer
        .map(ResponseEntity::ok)
        .orElseGet(() -> {
            logger.warn("Authentication failed"); // ← Generic message
            if (logger.isTraceEnabled()) { // Only log details in trace mode
                logger.trace("Failed login attempt for: {}", 
                    profile.getEmail().replaceAll("(?<=.).(?=.*@)", "*")); // Obscured email
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CustomerProfile()); // Empty profile for failed auth
        });
}
    
    @PutMapping("/{id}/preferences")
    public ResponseEntity<CustomerProfile> updatePreferences(@PathVariable Long id, @RequestBody Map<String, String> newPreferences) {
        return repository.findById(id).map(profile -> {
            profile.setPreferences(newPreferences);
            return ResponseEntity.ok(repository.save(profile));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/preferences")
    public ResponseEntity<Map<String, String>> getPreferences(@PathVariable Long id) {
        return repository.findById(id)
                .map(profile -> ResponseEntity.ok(profile.getPreferences()))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/past-tours")
    public ResponseEntity<List<Tour>> getPastTours(@PathVariable Long id) {
        List<Tour> pastTours = tourRepository.findByCustomerIdAndStatus(id, "Completed");
        return ResponseEntity.ok(pastTours);
    }
    
    @GetMapping("/{id}/upcoming-tours")
    public ResponseEntity<List<Tour>> getUpcomingTours(@PathVariable Long id) {
        List<Tour> upcomingTours = tourRepository.findByCustomerIdAndStatus(id, "Upcoming");
        return ResponseEntity.ok(upcomingTours);
    }
}

