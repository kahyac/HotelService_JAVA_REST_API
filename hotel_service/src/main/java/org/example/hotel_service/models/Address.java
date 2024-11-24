package org.example.hotel_service.models;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "country", nullable = false)
    private String country;


    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;


    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "locality")
    private String locality;

    @Column(name = "position_gps")
    private String positionGps;

    // Default constructor
    public Address() {
    }

    // Parameterized constructor
    public Address(String country, String city, String street, String number, String locality, String positionGps) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.locality = locality;
        this.positionGps = positionGps;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPositionGps() {
        return positionGps;
    }

    public void setPositionGps(String positionGps) {
        this.positionGps = positionGps;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", locality='" + locality + '\'' +
                ", positionGps='" + positionGps + '\'' +
                '}';
    }
}
