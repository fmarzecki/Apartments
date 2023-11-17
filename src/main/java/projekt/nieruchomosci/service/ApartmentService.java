package projekt.nieruchomosci.service;

import projekt.nieruchomosci.entity.Apartment;

public interface ApartmentService {
    Apartment findById(int id);
    void save(Apartment apartment);
}
