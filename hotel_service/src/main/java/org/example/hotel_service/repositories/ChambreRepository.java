package org.example.hotel_service.repositories;




import org.example.hotel_service.models.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    // Exemple de méthode pour récupérer les chambres d'un hôtel :
    List<Chambre> findByHotelId(Long hotelId);
}
