package projekt.nieruchomosci.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projekt.nieruchomosci.entity.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
  
    @Query("SELECT a FROM Apartment a LEFT JOIN Contract c ON a.id = c.apartment.id WHERE c.apartment.id IS NULL")
    Page<Apartment> findApartmentsWithoutContract(Pageable pageable);
    
}
