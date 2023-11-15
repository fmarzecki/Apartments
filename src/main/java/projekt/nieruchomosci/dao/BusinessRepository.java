package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer>{
}
