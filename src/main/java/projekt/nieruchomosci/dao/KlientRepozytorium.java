package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.Klient;

public interface KlientRepozytorium  extends JpaRepository<Klient, Integer> {
    
}
