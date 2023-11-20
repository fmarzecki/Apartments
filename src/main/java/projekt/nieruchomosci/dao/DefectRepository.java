package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Defect;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long>{
    
}
