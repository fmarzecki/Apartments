package projekt.nieruchomosci.service;

import java.util.List;

import projekt.nieruchomosci.entity.Business;

public interface BusinessService {
    void add(Business business);
    void delete(Business business);
    List<Business> getAll();
    Business findById(Long id);
}
