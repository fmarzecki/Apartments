package projekt.nieruchomosci.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Business;
import projekt.nieruchomosci.entity.User;

public interface ApartmentService {
    Apartment findById(int id);
    void save(Apartment apartment);
    void delete(Apartment apartment);
    List<Apartment> findAll();
    List<Apartment> findAll(Sort sort);
    Page<Apartment> findAll(Pageable pageable);
    Page<Apartment> findApartmentsWithoutContract(Pageable pageable);
    Page<Apartment> findApartmentsByEmployee(Pageable pageable, User user);
    Page<Apartment> findApartmentsByBusiness(Pageable pageable, Business business);
}
