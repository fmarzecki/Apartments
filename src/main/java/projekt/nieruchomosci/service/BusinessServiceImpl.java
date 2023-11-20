package projekt.nieruchomosci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekt.nieruchomosci.dao.BusinessRepository;
import projekt.nieruchomosci.entity.Business;

@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public void add(Business business) {
        businessRepository.save(business);        
    }

    @Override
    public List<Business> getAll() {
        return businessRepository.findAll();
    }

    @Override
    public Business findById(Long id) {
        Optional<Business> existingOptional = businessRepository.findById(id);
        if (existingOptional.isPresent()) {
                return existingOptional.get();
        }

        return null;
    }

    @Override
    public void delete(Business business) {
        businessRepository.delete(business);
    }
    
    
    
}
