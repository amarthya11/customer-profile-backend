package com.ul.customerprofile.repository;

import com.ul.customerprofile.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByCustomerIdAndStatus(Long customerId, String status);
}