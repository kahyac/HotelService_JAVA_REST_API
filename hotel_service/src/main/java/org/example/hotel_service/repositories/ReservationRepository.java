package org.example.hotel_service.repositories;

import org.example.hotel_service.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByOffreId(Long offreId);

}
