package projekt.nieruchomosci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public void delete(Apartment apartment) {
        aparmentRepository.delete(apartment);
    }

    @Override
    public List<Apartment> findAll() {
        return aparmentRepository.findAll();
    }

    @Override
	public List<Apartment> findAll(Sort sort) {
		return aparmentRepository.findAll(sort);
	}

    @Override
    public Page<Apartment> findAll(Pageable pageable) {
        return aparmentRepository.findAll(pageable);
    }
    
}
