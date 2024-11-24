/*
package com.example.agency.controllers;


import com.example.agency.services.HotelServiceClient;
import com.example.agency.Repositories.AgenceRepository;
import com.example.agency.models.Agence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agences")
public class AgenceController {

    @Autowired
    private HotelServiceClient hotelServiceClient;

    @Autowired
    private AgenceRepository agenceRepository;

    // Service pour vérifier les disponibilités
    @GetMapping("/{idAgence}/checkAvailability")
    public ResponseEntity<List<Map<String, Object>>> checkAvailability(
            @PathVariable Long idAgence,
            @RequestParam Long hotelId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam int numberOfGuests
    ) {
        // Récupérer l'agence par son ID
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new IllegalArgumentException("No agency found with ID " + idAgence));

        // Appeler le service REST de l'hôtel pour obtenir les disponibilités
        List<Map<String, Object>> offers = hotelServiceClient.getDisponibilites(
                hotelId, agence.getLogin(), startDate, endDate, numberOfGuests
        );

        return ResponseEntity.ok(offers);
    }

    //services reservation
    @PostMapping("/{idAgence}/reservations")
    public ResponseEntity<Map<String, Object>> reserver(
            @PathVariable Long idAgence,
            @RequestBody Map<String, Object> reservationData
    ) {
        // Vérifier que l'agence existe
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new IllegalArgumentException("No agency found with ID " + idAgence));

        // Vérification des paramètres essentiels
        if (!reservationData.containsKey("hotelId") || !reservationData.containsKey("offreId")) {
            throw new IllegalArgumentException("Missing required fields: 'hotelId' or 'offreId'");
        }

        // Appeler le service REST de l'hôtel pour effectuer la réservation
        Long hotelId = Long.valueOf(reservationData.get("hotelId").toString());
        Map<String, Object> reservationResponse = hotelServiceClient.effectuerReservation(hotelId, idAgence, reservationData);

        // Retourner la réponse avec la confirmation
        return ResponseEntity.ok(reservationResponse);
    }
}

*/


package com.example.agency.controllers;

import com.example.agency.Repositories.OffreRepository;

import com.example.agency.models.Offre;
import com.example.agency.services.ClientService;
import com.example.agency.services.HotelServiceClient;
import com.example.agency.Repositories.AgenceRepository;

import com.example.agency.models.Agence;
import com.example.agency.services.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agences")
public class AgenceController {

    @Autowired
    private HotelServiceClient hotelServiceClient;

    @Autowired
    private AgenceRepository agenceRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private OffreRepository offreRepository;

    /**
     * Service pour vérifier les disponibilités.
     */
    @Transactional
    @GetMapping("/{idAgence}/checkAvailability")
    public ResponseEntity<List<Map<String, Object>>> checkAvailability(
            @PathVariable Long idAgence,
            @RequestParam String city,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam int numberOfGuests
    ) {
        // Étape 1 : Récupérer les offres depuis les services Hôtel
        List<Map<String, Object>> offers = hotelServiceClient.getDisponibilitesByCity(
                idAgence, city, startDate, endDate, numberOfGuests
        );

        // Étape 2 : Mettre à jour les prix depuis la base de données de l'agence
        for (Map<String, Object> offer : offers) {
            Long offreId = Long.valueOf(offer.get("id").toString());

            // Récupérer le prix actualisé depuis la base de données de l'agence
            Double prixApresReduction = offreRepository.findPrixAgenceById(offreId);
            if (prixApresReduction == null) {
                throw new IllegalStateException("PrixAgence introuvable pour l'offre ID : " + offreId);
            }

            // Mettre à jour le prix dans la réponse
            offer.put("prixAgence", prixApresReduction);

            // Ajouter un champ indiquant le taux de réduction (formaté)
            Double reductionRate = getReductionRate(idAgence);
            offer.put("reductionRate", String.format("%.2f%%", (1 - reductionRate) * 100));
        }

        return ResponseEntity.ok(offers);
    }



    /**
     * Méthode pour obtenir le taux de réduction en fonction de l'agence.
     */
    private Double getReductionRate(Long idAgence) {
        if (idAgence == 1L) { // Airbnb
            return 0.90; // Réduction de 10%
        } else if (idAgence == 2L) { // Booking
            return 0.85; // Réduction de 15%
        } else if (idAgence == 3L) { // Expedia
            return 0.80; // Réduction de 20%
        }
        return 1.00; // Pas de réduction par défaut
    }

    /**
     * Service pour effectuer une réservation.
     */
    @PostMapping("/{idAgence}/reservations")
    public ResponseEntity<Map<String, Object>> reserver(
            @PathVariable Long idAgence,
            @RequestBody Map<String, Object> reservationData
    ) {
        // Vérification des champs requis
        String[] requiredFields = {"hotelId", "offreId", "clientEmail", "clientName", "clientSurname", "cardNumber", "expiryDate", "startDate", "endDate"};
        for (String field : requiredFields) {
            if (!reservationData.containsKey(field) || reservationData.get(field) == null || reservationData.get(field).toString().isEmpty()) {
                throw new IllegalArgumentException("Missing or null field: " + field);
            }
        }

        // Extraction des données de la requête
        Long hotelId = Long.valueOf(reservationData.get("hotelId").toString());
        Long offreId = Long.valueOf(reservationData.get("offreId").toString());
        String clientEmail = reservationData.get("clientEmail").toString();
        String clientName = reservationData.get("clientName").toString();
        String clientSurname = reservationData.get("clientSurname").toString();
        String cardNumber = reservationData.get("cardNumber").toString();
        String expiryDate = reservationData.get("expiryDate").toString();

        // Récupérer l'offre pour calculer le prix total
        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("No offer found with ID " + offreId));
        Double prixTotal = offre.getPrixAgence();

        // Récupération ou création du client
        Long clientId = clientService.findOrCreateClient(clientEmail, Map.of(
                "clientName", clientName,
                "clientSurname", clientSurname
        ));

        // Ajouter ou mettre à jour la carte bancaire
        Long carteBancaireId = clientService.addOrUpdateCard(clientId, cardNumber, expiryDate);

        // Appeler le service REST de l'Hôtel pour effectuer la réservation
        Map<String, Object> reservationResponse = hotelServiceClient.effectuerReservation(hotelId, idAgence, reservationData);

        // Récupérer l'ID de réservation généré par l'hôtel
        Long reservationId = Long.valueOf(reservationResponse.get("reservationId").toString());

        // Sauvegarder localement la réservation dans la base de données de l'agence
        reservationService.saveReservationLocally(
                reservationId, // Utiliser l'ID de l'hôtel
                idAgence,
                clientId,
                carteBancaireId,
                offreId,
                prixTotal,
                reservationResponse.get("status").toString()
        );

        // Retourner la réponse du service de l'hôtel
        return ResponseEntity.ok(reservationResponse);
    }



}






