package org.example.hotel_service.repositories;


import org.example.hotel_service.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Exemple de méthode personnalisée :
    Hotel findByNom(String nom);
}
