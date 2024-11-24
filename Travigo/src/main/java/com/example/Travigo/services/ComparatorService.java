package com.example.Travigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ComparatorService {

    @Autowired
    private AgencyService agencyService;

    public List<Map<String, Object>> searchOffers(String city, LocalDate startDate, LocalDate endDate, int numberOfGuests, int minStars, double minBudget, double maxBudget) {
        List<Long> agencyIds = List.of(1L, 2L, 3L);
        List<Map<String, Object>> allOffers = new ArrayList<>();

        for (Long agencyId : agencyIds) {
            try {
                List<Map<String, Object>> offers = agencyService.getOffersFromAgency(agencyId, city, startDate, endDate, numberOfGuests, minStars);
                if (offers != null) {
                    offers.removeIf(offer -> {
                        double price = (double) offer.get("prixAgence");
                        return price < minBudget || price > maxBudget;
                    });
                    allOffers.addAll(offers);
                }
            } catch (Exception e) {
                // Ignorer les agences qui ne répondent pas correctement
                System.err.println("Agency " + agencyId + " failed: " + e.getMessage());
            }
        }

        return allOffers;
    }

    public Map<String, Object> reserveOffer(
            Map<String, Object> offer,
            String clientName,
            String clientSurname,
            String clientEmail,
            String cardNumber,
            String expiryDate,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Long agencyId = Long.valueOf(offer.get("agencyId").toString());
        Long hotelId = Long.valueOf(offer.get("hotelId").toString());
        Long offerId = Long.valueOf(offer.get("id").toString());

        Map<String, Object> reservationRequest = Map.of(
                "hotelId", hotelId,
                "offreId", offerId,
                "clientName", clientName,
                "clientSurname", clientSurname,
                "clientEmail", clientEmail,
                "cardNumber", cardNumber,
                "expiryDate", expiryDate,
                "startDate", startDate.toString(),
                "endDate", endDate.toString()
        );

        return agencyService.reserveFromAgency(agencyId, reservationRequest);
    }
}
