package com.example.agency.Repositories;

import com.example.agency.models.CarteBancaire;
import com.example.agency.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarteBancaireRepository extends JpaRepository<CarteBancaire, Long> {
    Optional<CarteBancaire> findByClient(Client client); // Trouver la carte par client
}
