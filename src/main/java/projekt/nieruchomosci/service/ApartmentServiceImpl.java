package projekt.nieruchomosci.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import projekt.nieruchomosci.dao.ApartmentRepository;
import projekt.nieruchomosci.entity.Apartment;

@Service
public class ApartmentServiceImpl implements ApartmentService{
    private final ApartmentRepository aparmentRepository;

    public ApartmentServiceImpl(ApartmentRepository aparmentRepository) {
        this.aparmentRepository = aparmentRepository;
    }

    @Override
    public Apartment findById(int id) {
        Optional<Apartment> optional = aparmentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void save(Apartment apartment) {
        aparmentRepository.save(apartment);
    }
    
}
