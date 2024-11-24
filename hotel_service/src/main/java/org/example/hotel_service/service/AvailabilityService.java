/*package org.example.hotel_service.service;

import org.example.hotel_service.repositories.HotelRepository;
import org.example.hotel_service.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> findAvailableOffersForHotel(
            Long hotelId,
            String city,
            Integer stars,
            String category,
            Integer capacity,
            LocalDate start,
            LocalDate end,
            Double maxPrice
    ) {
        //hotelby id
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        //hotel criteria
        if (!hotel.getAddress().getCity().equalsIgnoreCase(city)) {
            throw new IllegalArgumentException("Hotel does not match the requested city");
        }

        if (hotel.getStarsNb() < stars) {
            throw new IllegalArgumentException("Hotel does not meet the minimum star rating");
        }

        if (!hotel.getHotelCategory().equalsIgnoreCase(category)) {
            throw new IllegalArgumentException("Hotel does not match the requested category");
        }

        //total capacity for rooms in the  hotel
        int totalRoomCapacity = hotel.getRooms().stream()
                .mapToInt(Room::getCapacity)
                .sum();

        if (totalRoomCapacity < capacity) {
            throw new IllegalArgumentException("Total capacity of the hotel is insufficient for the requested group size");
        }

        //offers for the hotel
        return hotel.getRooms().stream()
                .flatMap(room -> offerRepository.findByRoomId(room.getId()).stream())
                .filter(offer -> {
                    Room room = offer.getRoom();
                    double discountedPrice = room.getPricePerNight() - (room.getPricePerNight() * offer.getPercentage() / 100);
                    offer.setNewPrice(discountedPrice);

                    return totalRoomCapacity >= capacity
                            && !room.getStartAvailability().isAfter(start)
                            && !room.getEndAvailability().isBefore(end)
                            && discountedPrice <= maxPrice;
                })
                .collect(Collectors.toList());
    }
}


 */