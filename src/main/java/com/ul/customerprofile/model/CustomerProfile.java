package com.ul.customerprofile.model;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "customers")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String profilePictureUrl;

    @ElementCollection
    @CollectionTable(name = "customer_preferences", joinColumns = @JoinColumn(name = "customer_id"))
    @MapKeyColumn(name = "preference_key")
    @Column(name = "preference_value")
    private Map<String, String> preferences = new HashMap<>();

    public CustomerProfile() {}

    public CustomerProfile(Long id, String name, String email, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePictureUrl = null;
        this.preferences = new HashMap<>();
    }

    public CustomerProfile(Long id, String name, String email, String phone, String password, Map<String, String> preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePictureUrl = null;
        this.preferences = preferences != null ? preferences : new HashMap<>();
    }

    public CustomerProfile(Long id, String name, String email, String phone, String password, String profilePictureUrl, Map<String, String> preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.preferences = preferences != null ? preferences : new HashMap<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Map<String, String> getPreferences() { return preferences; }
    public void setPreferences(Map<String, String> preferences) { this.preferences = preferences; }
}
