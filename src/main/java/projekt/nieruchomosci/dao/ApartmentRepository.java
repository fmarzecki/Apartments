package projekt.nieruchomosci.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Business;
import projekt.nieruchomosci.entity.User;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
  
    @Query("SELECT a FROM Apartment a LEFT JOIN Contract c ON a.id = c.apartment.id WHERE c.apartment.id IS NULL")
    Page<Apartment> findApartmentsWithoutContract(Pageable pageable);
    
    @Query("SELECT a FROM Apartment a WHERE a.user = ?1 order by creationDate desc")
    Page<Apartment> findApartmentsByEmployee(Pageable pageable, User user);

    @Query("SELECT a FROM Apartment a WHERE a.business = ?1 order by creationDate desc")
    Page<Apartment> findApartmentsByBusiness(Pageable pageable, Business business);

}
