package com.example.Travigo.cli;

import com.example.Travigo.services.ComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class TravigoCLI {

    @Autowired
    private ComparatorService comparatorService;

    private final Scanner scanner = new Scanner(System.in);

    // Map pour afficher les hôtels disponibles
    private final Map<Long, String> hotelMap = Map.of(
            1L, "Hotel de Paris",
            2L, "Hotel du Soleil",
            3L, "Hotel des Alpes"
    );

    public void startCLI() {
        System.out.println("=== Bienvenue sur Travigo CLI ===");
        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu Principal =====");
            System.out.println("1. Rechercher des offres");
            System.out.println("2. Réserver une offre");
            System.out.println("3. Quitter");

            System.out.print("Choisissez une option : ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un numéro valide !");
                continue;
            }

            switch (choice) {
                case 1:
                    searchOffers();
                    break;
                case 2:
                    reserveOffer();
                    break;
                case 3:
                    System.out.println("Merci d'avoir utilisé Travigo. À bientôt !");
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void searchOffers() {
        try {
            System.out.println("\n===== Recherche d'Offres =====");
            System.out.print("Ville : ");
            String city = scanner.nextLine();

            System.out.print("Date de début (YYYY-MM-DD) : ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Date de fin (YYYY-MM-DD) : ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Nombre de personnes : ");
            int numberOfGuests = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre minimum d'étoiles : ");
            int minStars = Integer.parseInt(scanner.nextLine());

            System.out.print("Budget minimum : ");
            double minBudget = Double.parseDouble(scanner.nextLine());

            System.out.print("Budget maximum : ");
            double maxBudget = Double.parseDouble(scanner.nextLine());

            List<Map<String, Object>> offers = comparatorService.searchOffers(
                    city, startDate, endDate, numberOfGuests, minStars, minBudget, maxBudget
            );

            if (offers.isEmpty()) {
                System.out.println("Aucune offre trouvée correspondant à vos critères.");
            } else {
                System.out.println("\n=== Offres disponibles ===");
                for (int i = 0; i < offers.size(); i++) {
                    Map<String, Object> offer = offers.get(i);
                    System.out.println("----------------------------------");
                    System.out.println("Offre n° " + (i + 1));
                    System.out.println("Hôtel         : " + offer.get("hotelName"));
                    System.out.println("Prix par nuit : " + offer.get("prixAgence") + "€");
                    System.out.println("Étoiles       : " + offer.get("hotelStars"));
                    System.out.println("Adresse       : " + offer.get("address"));
                    System.out.println("ID Hôtel      : " + offer.getOrDefault("hotelId", "Non disponible"));
                    System.out.println("ID Offre      : " + offer.get("id"));
                    System.out.println("----------------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des offres : " + e.getMessage());
        }
    }

    private void reserveOffer() {
        try {
            System.out.println("\n===== Réservation d'une Offre =====");

            // Afficher les hôtels disponibles
            System.out.println("=== Hôtels Disponibles ===");
            for (Map.Entry<Long, String> entry : hotelMap.entrySet()) {
                System.out.println("ID Hôtel : " + entry.getKey() + " - " + entry.getValue());
            }

            // Demander l'ID de l'hôtel
            System.out.print("Entrez l'ID de l'hôtel parmi les résultats : ");
            long hotelId = Long.parseLong(scanner.nextLine());
            if (!hotelMap.containsKey(hotelId)) {
                System.out.println("ID Hôtel invalide !");
                return;
            }

            // Demander l'ID de l'offre
            System.out.print("Entrez l'ID de l'offre parmi les résultats : ");
            long offerId = Long.parseLong(scanner.nextLine());

            // Demander les informations client
            System.out.print("Nom : ");
            String clientName = scanner.nextLine();

            System.out.print("Prénom : ");
            String clientSurname = scanner.nextLine();

            System.out.print("Email : ");
            String clientEmail = scanner.nextLine();

            if (!clientEmail.contains("@")) {
                System.out.println("Email invalide !");
                return;
            }

            System.out.print("Numéro de carte : ");
            String cardNumber = scanner.nextLine();

            System.out.print("Date d'expiration de la carte (MM/YY) : ");
            String expiryDate = scanner.nextLine();

            System.out.print("Date de début de séjour (YYYY-MM-DD) : ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Date de fin de séjour (YYYY-MM-DD) : ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            // Créer la structure pour l'offre
            Map<String, Object> offer = Map.of(
                    "agencyId", 1L, // Remplacez par l'agence correcte si nécessaire
                    "hotelId", hotelId,
                    "id", offerId
            );

            // Effectuer la réservation via le service
            Map<String, Object> reservation = comparatorService.reserveOffer(
                    offer, clientName, clientSurname, clientEmail, cardNumber, expiryDate, startDate, endDate
            );

            // Confirmation
            System.out.println("\n=== Réservation Confirmée ===");
            System.out.println("Détails de la réservation :");
            System.out.println("Adresse         : " + reservation.get("address"));
            System.out.println("Nom de l'hôtel  : " + reservation.get("hotelName"));
            System.out.println("Étoiles         : " + reservation.get("hotelStars"));
            System.out.println("Prix total      : " + reservation.get("totalPrice") + "€");
            System.out.println("Client          : " + reservation.get("client"));
            System.out.println("Statut          : " + reservation.get("status"));
            System.out.println("Dates de séjour : " + reservation.get("stay"));
            System.out.println("ID Réservation  : " + reservation.get("reservationId"));
        } catch (Exception e) {
            System.err.println("Erreur lors de la réservation : " + e.getMessage());
        }
    }
}
