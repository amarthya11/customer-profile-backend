package com.ul.customerprofile.service;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerProfileService {
    @Autowired
    private CustomerProfileRepository repository;

    public List<CustomerProfile> getAllProfiles() {
        return repository.findAll();
    }

    public Optional<CustomerProfile> getProfileById(Long id) {
        return repository.findById(id);
    }

    public CustomerProfile createProfile(CustomerProfile profile) {
        return repository.save(profile);
    }

    public Optional<CustomerProfile> login(String email, String password) {
        return repository.findByEmail(email)
                .filter(customer -> {
                    System.out.println("🔍 Checking login for email: " + email);
                    System.out.println("✅ Database password: " + customer.getPassword());
                    System.out.println("✅ Entered password: " + password);
                    return customer.getPassword().equals(password);
                });
    }


}

