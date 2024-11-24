package org.example.hotel_service.models;



import jakarta.persistence.*;

@Entity
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nombreLits;
    private double prixBase;
    private String pictureUrl;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getNombreLits() {
        return nombreLits;
    }
    public void setNombreLits(int nombreLits) {
        this.nombreLits = nombreLits;
    }
    public double getPrixBase() {
        return prixBase;
    }
    public void setPrixBase(double prixBase) {
        this.prixBase = prixBase;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public Hotel getHotel() {
        return hotel;
    }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
