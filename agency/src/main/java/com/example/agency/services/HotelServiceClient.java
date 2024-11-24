/*package com.example.agency.services;

import com.example.agency.Repositories.AgenceRepository;
import com.example.agency.exceptions.AgenceNotFoundException;
import com.example.agency.exceptions.ReservationConflictException;
import com.example.agency.models.Agence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class HotelServiceClient {

    private final RestTemplate restTemplate;
    private final String hotelBaseUrl = "http://localhost:8080/api/hotels";

    @Autowired
    private AgenceRepository agenceRepository;

    public HotelServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Consulter les disponibilités
    public List<Map<String, Object>> getDisponibilites(Long hotelId, String agencyLogin,
                                                       LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        // Récupérer les informations de l'agence
        Agence agence = agenceRepository.findByLogin(agencyLogin)
                .orElseThrow(() -> new AgenceNotFoundException("Agency with login " + agencyLogin + " not found."));

        // Construire l'URL avec l'ancien endpoint
        String url = hotelBaseUrl + "/" + hotelId + "/disponibilites" +
                "?agencyUsername=" + agence.getLogin() +
                "&agencyPassword=" + agence.getMotDePasse() +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&numberOfGuests=" + numberOfGuests;

        try {
            // Appeler le service REST de l'hôtel
            return restTemplate.getForObject(url, List.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error while calling hotel availability service: " + e.getMessage());
        }
    }



    // Effectuer une réservation
    public Map<String, Object> effectuerReservation(Long hotelId, Long idAgence, Map<String, Object> reservationData) {
        // Récupérer les informations de l'agence
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new AgenceNotFoundException("Agency with ID " + idAgence + " not found."));

        // Ajouter les identifiants de l'agence à la requête
        reservationData.put("agencyUsername", agence.getLogin());
        reservationData.put("agencyPassword", agence.getMotDePasse());

        // Construire l'URL pour appeler le service REST de l'hôtel
        String url = hotelBaseUrl + "/" + hotelId + "/reservations";

        try {
            // Effectuer l'appel REST
            return restTemplate.postForObject(url, reservationData, Map.class);
        } catch (HttpClientErrorException.Conflict e) {
            // Conflit (409) : l'offre est déjà réservée
            throw new ReservationConflictException("The offer is already reserved. Details: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            // Autres erreurs REST
            throw new RuntimeException("An error occurred while trying to reserve the offer: " + e.getMessage());
        }
    }
}
*/

package com.example.agency.services;

import com.example.agency.Repositories.AgenceRepository;
import com.example.agency.exceptions.AgenceNotFoundException;
import com.example.agency.exceptions.ReservationConflictException;
import com.example.agency.models.Agence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class HotelServiceClient {

    private final RestTemplate restTemplate;

    @Value("${hotel1.url}")
    private String hotel1Url;

    @Value("${hotel2.url}")
    private String hotel2Url;

    @Value("${hotel3.url}")
    private String hotel3Url;

    @Autowired
    private AgenceRepository agenceRepository;

    public HotelServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtenir l'URL de base pour un hôtel en fonction de son ID.
     */
    private String getHotelUrlById(Long hotelId) {
        return switch (hotelId.intValue()) {
            case 1 -> hotel1Url;
            case 2 -> hotel2Url;
            case 3 -> hotel3Url;
            default -> throw new IllegalArgumentException("No URL configured for hotel ID: " + hotelId);
        };
    }

    /**
     * Obtenir les disponibilités par ville.
     */
    public List<Map<String, Object>> getDisponibilitesByCity(Long idAgence, String city, LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new IllegalArgumentException("No agency found with ID " + idAgence));

        List<Long> hotelIds = List.of(1L, 2L, 3L); // Liste des hôtels connus
        List<Map<String, Object>> availableOffers = hotelIds.stream()
                .flatMap(hotelId -> {
                    String baseUrl = getHotelUrlById(hotelId);
                    String availabilityUrl = baseUrl + "/disponibilites" +
                            "?agencyUsername=" + agence.getLogin() +
                            "&agencyPassword=" + agence.getMotDePasse() +
                            "&startDate=" + startDate +
                            "&endDate=" + endDate +
                            "&numberOfGuests=" + numberOfGuests;

                    try {
                        List<Map<String, Object>> hotelOffers = restTemplate.getForObject(availabilityUrl, List.class);

                        if (hotelOffers != null) {
                            hotelOffers.removeIf(offer -> {
                                if (offer.get("city") == null) {
                                    throw new IllegalArgumentException("Missing 'city' in offer data");
                                }
                                return !city.equalsIgnoreCase(offer.get("city").toString());
                            });
                        }

                        return hotelOffers != null ? hotelOffers.stream() : null;
                    } catch (RestClientException e) {
                        return null;
                    }
                })
                .filter(offer -> offer != null)
                .toList();

        if (availableOffers.isEmpty()) {
            throw new IllegalArgumentException("The requested city '" + city + "' does not exist or has no offers.");
        }

        return availableOffers;
    }

    /**
     * Effectuer une réservation.
     */
    public Map<String, Object> effectuerReservation(Long hotelId, Long idAgence, Map<String, Object> reservationData) {
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new IllegalArgumentException("No agency found with ID " + idAgence));

        reservationData.put("agencyUsername", agence.getLogin());
        reservationData.put("agencyPassword", agence.getMotDePasse());

        String baseUrl = getHotelUrlById(hotelId);
        String reservationUrl = baseUrl + "/reservations";

        try {
            return restTemplate.postForObject(reservationUrl, reservationData, Map.class);
        } catch (HttpClientErrorException.Conflict e) {
            throw new RuntimeException("The offer is already reserved. Details: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            throw new RuntimeException("An error occurred while trying to reserve the offer: " + e.getMessage());
        }
    }

    /**
     * Récupérer les offres depuis le système de l'hôtel.
     */
    public List<Map<String, Object>> getOffers(Long hotelId) {
        String baseUrl = getHotelUrlById(hotelId);
        String offersUrl = baseUrl + "/offres";

        try {
            return restTemplate.getForObject(offersUrl, List.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching offers from the hotel system: " + e.getMessage());
        }
    }
}




