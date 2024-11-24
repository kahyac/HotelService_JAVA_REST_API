package org.example.hotel_service.repositories;

import org.example.hotel_service.models.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {

    // Vérifier si des offres existent pour un hôtel donné
    boolean existsByHotelId(Long hotelId);

    // Autres méthodes déjà présentes
    Optional<Offre> findById(Long id);
}
