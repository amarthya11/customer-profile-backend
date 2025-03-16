package com.ul.customerprofile.controller;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerProfilePictureController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private CustomerProfileRepository repository;

    //API to upload profile picture
    @PostMapping("/{id}/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<CustomerProfile> customer = repository.findById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = id + "_" + System.currentTimeMillis() + "_" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            Path filePath = Paths.get(uploadDir, fileName);

            file.transferTo(filePath.toFile());

            CustomerProfile profile = customer.get();
            profile.setProfilePictureUrl("/uploads/" + fileName);
            repository.save(profile);

            System.out.println("File uploaded successfully: " + filePath); // Debugging
            return ResponseEntity.ok("/uploads/" + fileName);
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage()); // Debugging
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
        String filePath = profile.getProfilePictureUrl().replaceFirst("/", "");

        //deleting the file from storage
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        profile.setProfilePictureUrl(null);
        repository.save(profile);

        return ResponseEntity.ok("Profile picture removed successfully!");
    }
}
