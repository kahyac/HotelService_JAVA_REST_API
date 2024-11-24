package com.example.agency.models;

import jakarta.persistence.*;
@Entity
@Table(name = "cartebancaire")
public class CarteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String maskedCardNumber; // Format masqué, ex: **** **** **** 1234

    @Column(nullable = false)
    private String expiryDate;



    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client; // Relation avec l'entité Client

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }



    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
