package com.ul.customerprofile.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double price;
    private LocalDate bookedOn;

    public Tour() {}

    public Tour(Long id, Long customerId, String destination, LocalDate startDate, LocalDate endDate, String status, double price, LocalDate bookedOn) {
        this.id = id;
        this.customerId = customerId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.price = price;
        this.bookedOn = bookedOn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getBookedOn() { return bookedOn; }
    public void setBookedOn(LocalDate bookedOn) { this.bookedOn = bookedOn; }
}