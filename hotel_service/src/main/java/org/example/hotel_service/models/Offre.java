package org.example.hotel_service.models;

import jakarta.persistence.*;
import org.example.hotel_service.models.Agence;
import org.example.hotel_service.models.Chambre;
import org.example.hotel_service.models.Hotel;

import java.time.LocalDate;

@Entity
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agence agence; // Nouvelle relation avec Agence

    private String agencyUsername;
    private String agencyPassword;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;
    private double prixAgence;
    private int numberOfBeds;
    private String pictureUrl;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Chambre getChambre() {
        return chambre;
    }
    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }
    public Hotel getHotel() {
        return hotel;
    }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    public Agence getAgence() {
        return agence;
    }
    public void setAgence(Agence agence) {
        this.agence = agence;
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
    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }
    public void setAvailabilityStart(LocalDate availabilityStart) {
        this.availabilityStart = availabilityStart;
    }
    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }
    public void setAvailabilityEnd(LocalDate availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }
    public double getPrixAgence() {
        return prixAgence;
    }
    public void setPrixAgence(double prixAgence) {
        this.prixAgence = prixAgence;
    }
    public int getNumberOfBeds() {
        return numberOfBeds;
    }
    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
