package com.worknest.model;

import javax.persistence.*;

@Entity
@Table(name = "admins", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    // getters and setters
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
