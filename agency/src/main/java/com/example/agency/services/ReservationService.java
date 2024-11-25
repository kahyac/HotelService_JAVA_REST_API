package com.example.agency.services;

import com.example.agency.Repositories.AgenceRepository;
import com.example.agency.Repositories.CarteBancaireRepository;
import com.example.agency.Repositories.ClientRepository;
import com.example.agency.Repositories.ReservationRepository;
import com.example.agency.models.Agence;
import com.example.agency.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


import com.example.agency.Repositories.ReservationRepository;
import com.example.agency.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;



import com.example.agency.Repositories.ReservationRepository;
import com.example.agency.models.*;


import java.time.LocalDateTime;
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public void saveReservationLocally(
            Long reservationId,
            Long agenceId,
            Long clientId,
            Long carteBancaireId,
            Long offreId,
            Double prixTotal,
            String statut) {


        Reservation reservation = new Reservation();


        reservation.setId(reservationId);


        Agence agence = new Agence();
        agence.setId(agenceId);
        reservation.setAgence(agence);

        Client client = new Client();
        client.setId(clientId);
        reservation.setClient(client);

        if (carteBancaireId != null) {
            CarteBancaire carteBancaire = new CarteBancaire();
            carteBancaire.setId(carteBancaireId);
            reservation.setCarteBancaire(carteBancaire);
        }

        Offre offre = new Offre();
        offre.setId(offreId);
        reservation.setOffre(offre);

        reservation.setDateReservation(LocalDateTime.now());
        reservation.setPrixTotal(prixTotal);
        reservation.setStatut(statut);


        reservationRepository.save(reservation);
    }
}



