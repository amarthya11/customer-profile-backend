package com.ul.customerprofile;

import com.ul.customerprofile.model.CustomerProfile;
import com.ul.customerprofile.model.Tour;
import com.ul.customerprofile.repository.CustomerProfileRepository;
import com.ul.customerprofile.repository.TourRepository;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

@SpringBootApplication
public class CustomerProfileApplication {
    private static final Logger logger = LoggerFactory.getLogger(CustomerProfileApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustomerProfileApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CustomerProfileRepository repository, TourRepository tourRepository) {
        return args -> {
            if (repository.findByEmail("admin@gmail.com").isEmpty()) {
                Map<String, String> preferences = new HashMap<>();
                preferences.put("theme", "light");
                preferences.put("notifications", "email");

                CustomerProfile admin = new CustomerProfile(
                        null, "Admin User", "admin@gmail.com", "1234567890", "admin123", preferences
                );

                repository.save(admin);
                logger.info("Default admin user created: admin@gmail.com / admin123");
            }

            if (tourRepository.count() == 0) {
                tourRepository.save(new Tour(null, 1L, "Paris", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 7), "Upcoming", 1200.0, LocalDate.of(2025, 3, 1)));
                tourRepository.save(new Tour(null, 1L, "Tokyo", LocalDate.of(2025, 5, 15), LocalDate.of(2025, 5, 22), "Upcoming", 1500.0, LocalDate.of(2025, 3, 10)));
                tourRepository.save(new Tour(null, 1L, "New York", LocalDate.of(2024, 12, 10), LocalDate.of(2024, 12, 17), "Completed", 1000.0, LocalDate.of(2024, 11, 1)));

                logger.info("Sample tour data added.");
            } else {
                logger.info("Sample tour data already exists");
            }
        };
    }
}
