package com.ul.customerprofile;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.model.Tour;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import com.ul.customerprofile.repository.TourRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CustomerProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerProfileApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CustomerProfileRepository repository, TourRepository tourRepository) {
        return args -> {
            // Add default admin user (if not already present)
            if (repository.findByEmail("admin@example.com").isEmpty()) {
                Map<String, String> preferences = new HashMap<>();
                preferences.put("theme", "light");
                preferences.put("notifications", "email");

                CustomerProfile admin = new CustomerProfile(
                        null, "Admin User", "admin@gmail.com", "1234567890", "admin123", preferences
                );

                repository.save(admin);
                System.out.println("✅ Default admin user created: admin@gmail.com / admin123");
            }

            // Add sample tour data
            tourRepository.save(new Tour(null, 1L, "Paris", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 7), "Upcoming", 1200.0, LocalDate.of(2025, 3, 1)));
            tourRepository.save(new Tour(null, 1L, "Tokyo", LocalDate.of(2025, 5, 15), LocalDate.of(2025, 5, 22), "Upcoming", 1500.0, LocalDate.of(2025, 3, 10)));
            tourRepository.save(new Tour(null, 1L, "New York", LocalDate.of(2024, 12, 10), LocalDate.of(2024, 12, 17), "Completed", 1000.0, LocalDate.of(2024, 11, 1)));

            System.out.println("✅ Sample tour data added.");
        };
    }
}
