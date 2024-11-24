package com.example.Travigo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AgencyService {

    private final RestTemplate restTemplate;

    public AgencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getOffersFromAgency(Long agencyId, String city, LocalDate startDate, LocalDate endDate, int numberOfGuests, int minStars) {
        String url = "http://localhost:8083/api/agences/" + agencyId + "/checkAvailability" +
                "?city=" + city +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&numberOfGuests=" + numberOfGuests;

        List<Map<String, Object>> offers = restTemplate.getForObject(url, List.class);

        if (offers != null) {
            offers.removeIf(offer -> (int) offer.get("hotelStars") < minStars);
        }

        return offers;
    }

    public Map<String, Object> reserveFromAgency(Long agencyId, Map<String, Object> reservationData) {
        String url = "http://localhost:8083/api/agences/" + agencyId + "/reservations";
        return restTemplate.postForObject(url, reservationData, Map.class);
    }
}
