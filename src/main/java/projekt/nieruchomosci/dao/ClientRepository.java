package projekt.nieruchomosci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Client;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Integer> {
    
    // @Query("SELECT c FROM Client c WHERE c.id = ?1")
    // List<Client> findClientById(Integer Id);
}
