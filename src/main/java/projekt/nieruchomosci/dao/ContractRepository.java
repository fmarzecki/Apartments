package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long>{
    
}
