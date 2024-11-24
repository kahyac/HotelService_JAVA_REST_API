package com.example.agency.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Column(nullable = false)
    private Double prixTotal;

    @Column(nullable = false)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "agence_id", nullable = false) // Relation avec `Agence`
    private Agence agence;

    @ManyToOne
    @JoinColumn(name = "cartebancaire_id", nullable = true) // Relation avec `CarteBancaire`
    private CarteBancaire carteBancaire;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // Relation avec `Client`
    private Client client;

    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false) // Relation avec `Offre`
    private Offre offre;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public CarteBancaire getCarteBancaire() {
        return carteBancaire;
    }

    public void setCarteBancaire(CarteBancaire carteBancaire) {
        this.carteBancaire = carteBancaire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}
