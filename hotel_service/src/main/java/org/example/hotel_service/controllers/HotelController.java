package org.example.hotel_service.controllers;

import org.example.hotel_service.exceptions.*;
import org.example.hotel_service.models.*;
import org.example.hotel_service.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private OffreRepository offreRepository;
    @Autowired
    private AgenceRepository agenceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // ----------------------------------------------------
    // Service Web 1 : Consulter les disponibilités
    // ----------------------------------------------------

    @GetMapping("/{hotelId}/disponibilites")
    public ResponseEntity<List<Map<String, Object>>> consulterDisponibilites(
            @PathVariable Long hotelId,
            @RequestParam String agencyUsername,
            @RequestParam String agencyPassword,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam int numberOfGuests
    ) {
        // Vérifier si l'agence est autorisée
        boolean isAuthorized = agenceRepository.existsByLoginAndMotDePasse(agencyUsername, agencyPassword);
        if (!isAuthorized) {
            throw new UnauthorizedAccessException("The provided agency username or password is incorrect.");
        }

        // Vérifier si l'hôtel existe
        boolean hotelExists = offreRepository.existsByHotelId(hotelId);
        if (!hotelExists) {
            throw new ResourceNotFoundException("No hotel found with the provided ID.");
        }


        List<Map<String, Object>> offres = offreRepository.findAll().stream()
                .filter(offre -> offre.getHotel().getId().equals(hotelId))
                .filter(offre -> offre.getAgence().getLogin().equalsIgnoreCase(agencyUsername)) // Filtrer par agence
                .filter(offre -> !offre.getAvailabilityStart().isAfter(endDate) &&
                        !offre.getAvailabilityEnd().isBefore(startDate)) // Vérifier les dates
                .filter(offre -> offre.getNumberOfBeds() >= numberOfGuests)
                .map(offre -> {

                    Hotel hotel = offre.getHotel();
                    Address address = hotel.getAdresse();

                    Map<String, Object> response = new HashMap<>();
                    response.put("id", offre.getId());
                    response.put("numberOfBeds", offre.getNumberOfBeds());
                    response.put("availabilityStart", offre.getAvailabilityStart());
                    response.put("availabilityEnd", offre.getAvailabilityEnd());
                    response.put("prixAgence", offre.getPrixAgence());
                    response.put("pictureUrl", offre.getPictureUrl());

                    // Enrichir avec les détails de l'hôtel
                    response.put("hotelName", hotel.getNom());
                    response.put("city", offre.getHotel().getAdresse().getCity());

                    response.put("hotelStars", hotel.getNombreEtoiles());
                    response.put("address", String.format("%s, %s, %s, %s",
                            address.getStreet(), address.getLocality(), address.getCity(), address.getCountry()));
                    response.put("gps", address.getPositionGps());
                    return response;
                })
                .toList();

        if (offres.isEmpty()) {
            throw new NoOffersAvailableException("No available offers match the specified criteria.");
        }

        return ResponseEntity.ok(offres);
    }


    // ----------------------------------------------------
    // Service Web 2 : Effectuer une réservation
    // ----------------------------------------------------
    @PostMapping("/{hotelId}/reservations")
    public ResponseEntity<Map<String, Object>> effectuerReservation(
            @PathVariable Long hotelId,
            @RequestBody Map<String, Object> requestData // Données de la requête en JSON
    ) {
        // Extraire les paramètres depuis la requête
        String agencyUsername = (String) requestData.get("agencyUsername");
        String agencyPassword = (String) requestData.get("agencyPassword");
        Long offreId = requestData.get("offreId") != null ? Long.valueOf(requestData.get("offreId").toString()) : null;
        String clientName = (String) requestData.get("clientName");
        String clientSurname = (String) requestData.get("clientSurname");

        // Vérification des paramètres requis
        if (agencyUsername == null || agencyPassword == null || offreId == null || clientName == null || clientSurname == null) {
            throw new IllegalArgumentException("Missing required parameters. Please provide all necessary fields.");
        }

        // Vérification des identifiants de l'agence
        boolean isAuthorized = agenceRepository.existsByLoginAndMotDePasse(agencyUsername, agencyPassword);
        if (!isAuthorized) {
            throw new UnauthorizedAccessException("Invalid agency credentials. The username or password is incorrect.");
        }

        // Vérifier si l'offre existe
        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer with ID " + offreId + " not found."));


        if (!offre.getHotel().getId().equals(hotelId)) {
            throw new IllegalArgumentException("Offer does not belong to the specified hotel.");
        }


        boolean isAlreadyReserved = reservationRepository.existsByOffreId(offreId);
        if (isAlreadyReserved) {
            throw new ReservationException("Offer with ID " + offreId + " is already reserved.");
        }


        Reservation reservation = new Reservation();
        reservation.setOffre(offre);
        reservation.setNomClient(clientName);
        reservation.setPrenomClient(clientSurname);
        reservation.setDateDebut(offre.getAvailabilityStart());
        reservation.setDateFin(offre.getAvailabilityEnd());
        reservation.setStatus("Confirmed");


        Reservation savedReservation = reservationRepository.save(reservation);


        long days = savedReservation.getDateFin().toEpochDay() - savedReservation.getDateDebut().toEpochDay();
        double totalPrice = offre.getPrixAgence() * days;


        Hotel hotel = offre.getHotel();
        Address address = hotel.getAdresse();


        Map<String, Object> response = new HashMap<>();
        response.put("reservationId", savedReservation.getId());
        response.put("status", "Confirmed");
        response.put("hotelName", hotel.getNom());
        response.put("address", String.format("%s, %s, %s, %s",
                address.getStreet(), address.getLocality(), address.getCity(), address.getCountry()));
        response.put("gps", address.getPositionGps());
        response.put("hotelStars", hotel.getNombreEtoiles());
        response.put("numberOfBeds", offre.getNumberOfBeds());
        response.put("pricePerNight", offre.getPrixAgence());
        response.put("totalPrice", totalPrice);
        response.put("agency", offre.getAgence().getLogin());
        response.put("stay", Map.of(
                "start", savedReservation.getDateDebut().toString(),
                "end", savedReservation.getDateFin().toString()
        ));
        response.put("client", Map.of(
                "name", clientName + " " + clientSurname,
                "email", requestData.get("clientEmail")
        ));

        return ResponseEntity.ok(response);
    }

}
