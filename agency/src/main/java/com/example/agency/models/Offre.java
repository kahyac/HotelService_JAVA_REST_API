package com.example.agency.models;

import jakarta.persistence.*;

@Entity
@Table(name = "offre")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String agencyUsername;

    @Column(nullable = false)
    private String agencyPassword;

    @Column(nullable = false)
    private String availabilityStart;

    @Column(nullable = false)
    private String availabilityEnd;

    @Column(nullable = false)
    private int numberOfBeds;

    @Column(nullable = false)
    private Double prixAgence;

    @Column
    private String pictureUrl;

    @ManyToOne
    @JoinColumn(name = "agence_id", nullable = false) // Relation correcte avec `Agence`
    private Agence agence;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgencyUsername() {
        return agencyUsername;
    }

    public void setAgencyUsername(String agencyUsername) {
        this.agencyUsername = agencyUsername;
    }

    public String getAgencyPassword() {
        return agencyPassword;
    }

    public void setAgencyPassword(String agencyPassword) {
        this.agencyPassword = agencyPassword;
    }

    public String getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(String availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public String getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(String availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Double getPrixAgence() {
        return prixAgence;
    }

    public void setPrixAgence(Double prixAgence) {
        this.prixAgence = prixAgence;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }


}
