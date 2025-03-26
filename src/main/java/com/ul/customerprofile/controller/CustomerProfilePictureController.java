package com.ul.customerprofile.controller;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.repository.CustomerProfileRepository;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerProfilePictureController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerProfilePictureController.class);

    private static final String UPLOAD_DIR = "uploads/";

    private final CustomerProfileRepository repository;

    public CustomerProfilePictureController(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    //API to upload profile picture
    @PostMapping("/{id}/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<CustomerProfile> customer = repository.findById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/" + UPLOAD_DIR;
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = id + "_" + System.currentTimeMillis() + "_" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            Path filePath = Paths.get(uploadDir, fileName);

            file.transferTo(filePath.toFile());

            CustomerProfile profile = customer.get();
            profile.setProfilePictureUrl("/" + UPLOAD_DIR + fileName);
            repository.save(profile);

            logger.info("File uploaded successfully: {}", filePath);
            return ResponseEntity.ok("/" + UPLOAD_DIR + fileName);
        } catch (IOException e) {
            logger.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
        }
    }
    
    //API to retrieve profile picture URL
    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<String> getProfilePictureUrl(@PathVariable Long id) {
        Optional<CustomerProfile> customer = repository.findById(id);
        if (customer.isPresent() && customer.get().getProfilePictureUrl() != null) {
            return ResponseEntity.ok(customer.get().getProfilePictureUrl());
        }
        return ResponseEntity.notFound().build();
    }

    //API to remove profile picture
    @DeleteMapping("/{id}/remove-profile-picture")
    public ResponseEntity<String> removeProfilePicture(@PathVariable Long id) {
        Optional<CustomerProfile> customer = repository.findById(id);
        if (customer.isEmpty() || customer.get().getProfilePictureUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        CustomerProfile profile = customer.get();
        String filePath = profile.getProfilePictureUrl().replaceFirst("/" + UPLOAD_DIR, "");

        //deleting the file from storage
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {  // ← Checks return value
            logger.warn("Failed to delete file: {}", filePath);
        }

        profile.setProfilePictureUrl(null);
        repository.save(profile);

        return ResponseEntity.ok("Profile picture removed successfully!");
    }
}
