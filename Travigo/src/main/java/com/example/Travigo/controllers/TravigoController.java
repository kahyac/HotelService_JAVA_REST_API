package com.example.Travigo.controllers;

import com.example.Travigo.services.ComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/travigo")
public class TravigoController {

    @Autowired
    private ComparatorService comparatorService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchOffers(
            @RequestParam String city,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam int numberOfGuests,
            @RequestParam int minStars,
            @RequestParam double minBudget,
            @RequestParam double maxBudget
    ) {
        return comparatorService.searchOffers(city, startDate, endDate, numberOfGuests, minStars, minBudget, maxBudget);
    }


    @PostMapping("/reserve")
    public Map<String, Object> reserveOffer(
            @RequestBody Map<String, Object> reservationRequest
    ) {
        // Liste des champs obligatoires
        String[] requiredFields = {"offer", "clientName", "clientSurname", "clientEmail", "cardNumber", "expiryDate", "startDate", "endDate"};

        // Validation des champs
        for (String field : requiredFields) {
            if (!reservationRequest.containsKey(field) || reservationRequest.get(field) == null || reservationRequest.get(field).toString().isEmpty()) {
                throw new IllegalArgumentException("Missing or null field: " + field);
            }
        }

        // Extraction et validation de l'offre
        @SuppressWarnings("unchecked")
        Map<String, Object> offer = (Map<String, Object>) reservationRequest.get("offer");
        if (offer == null || !offer.containsKey("agencyId") || !offer.containsKey("hotelId") || !offer.containsKey("id")) {
            throw new IllegalArgumentException("Invalid offer data: Missing required fields (agencyId, hotelId, id).");
        }

        // Conversion et validation des dates
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(reservationRequest.get("startDate").toString());
            endDate = LocalDate.parse(reservationRequest.get("endDate").toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format for startDate or endDate. Expected format: YYYY-MM-DD");
        }

        // Appel du service pour effectuer la réservation
        return comparatorService.reserveOffer(
                offer,
                reservationRequest.get("clientName").toString(),
                reservationRequest.get("clientSurname").toString(),
                reservationRequest.get("clientEmail").toString(),
                reservationRequest.get("cardNumber").toString(),
                reservationRequest.get("expiryDate").toString(),
                startDate,
                endDate
        );
    }


}
