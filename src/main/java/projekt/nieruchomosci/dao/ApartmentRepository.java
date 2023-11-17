package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

}
