package com.example.agency.services;

import com.example.agency.Repositories.ClientRepository;
import com.example.agency.Repositories.CarteBancaireRepository;
import com.example.agency.models.Client;
import com.example.agency.models.CarteBancaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarteBancaireRepository carteBancaireRepository;

    /**
     * Trouver ou créer un client.
     */
    public Long findOrCreateClient(String email, Map<String, Object> clientData) {
        return clientRepository.findByEmail(email)
                .map(Client::getId)
                .orElseGet(() -> {
                    // Si le client n'existe pas, le créer
                    Client client = new Client();
                    client.setEmail(email);
                    client.setNom(clientData.get("clientName").toString());
                    client.setPrenom(clientData.get("clientSurname").toString());
                    return clientRepository.save(client).getId();
                });
    }

    /**
     * Ajouter ou mettre à jour une carte bancaire pour un client.
     */
    public Long addOrUpdateCard(Long clientId, String cardNumber, String expiryDate) {
        // Récupérer le client par ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("No client found with ID " + clientId));

        // Ajouter ou mettre à jour une carte pour le client
        CarteBancaire card = carteBancaireRepository.findByClient(client)
                .orElse(new CarteBancaire());
        card.setClient(client);
        card.setMaskedCardNumber(cardNumber.substring(cardNumber.length() - 4));
        card.setExpiryDate(expiryDate);

        return carteBancaireRepository.save(card).getId();
    }
}
