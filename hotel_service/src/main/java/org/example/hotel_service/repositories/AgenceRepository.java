package org.example.hotel_service.repositories;


import org.example.hotel_service.models.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    // Vérifier si une agence existe avec un login et un mot de passe donnés
    boolean existsByLoginAndMotDePasse(String login, String motDePasse);
}
