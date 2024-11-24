package com.example.agency.Repositories;

import com.example.agency.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Recherche par identifiant de l'agence
    List<Reservation> findByAgence_Id(Long agenceId);
}
