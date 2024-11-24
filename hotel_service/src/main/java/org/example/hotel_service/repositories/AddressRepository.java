package org.example.hotel_service.repositories;


import org.example.hotel_service.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
}

