package com.ul.customerprofile.controller;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.model.Tour;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import com.ul.customerprofile.repository.TourRepository;
import com.ul.customerprofile.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")  
public class CustomerProfileController {
    
    @Autowired
    private CustomerProfileService service;
    
    @Autowired
    private CustomerProfileRepository repository;
    
    @Autowired
    private TourRepository tourRepository;

    @GetMapping
    public List<CustomerProfile> getAllProfiles() {
        return service.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfile> getProfileById(@PathVariable Long id) {
        //return service.getProfileById(id)
               // .map(ResponseEntity::ok)
               // .orElse(ResponseEntity.notFound().build());
        Optional<CustomerProfile> profile = repository.findById(id);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //@PostMapping
    //public CustomerProfile createProfile(@RequestBody CustomerProfile profile) {
    //    return service.createProfile(profile);
    //}
    
    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody CustomerProfile profile) {
        try {
            CustomerProfile savedProfile = service.createProfile(profile);
            return ResponseEntity.ok(savedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerProfile> updateProfile(@PathVariable Long id, @RequestBody CustomerProfile updatedProfile) {
        return repository.findById(id).map(profile -> {
            profile.setName(updatedProfile.getName());
            profile.setPhone(updatedProfile.getPhone());
            profile.setPassword(updatedProfile.getPassword());  // Ensure passwords are securely hashed in production
            return ResponseEntity.ok(repository.save(profile));
        }).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerProfile profile) {
        Optional<CustomerProfile> customer = service.login(profile.getEmail(), profile.getPassword());

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get()); // Return valid customer profile
        } else {
            return ResponseEntity.status(401).body("Invalid credentials"); // Return error message
        }
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

