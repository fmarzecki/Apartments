package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Client;

/*
 * 
 * W Spring Framework adnotacja @Repository jest używana do oznaczania klas,
 *  które pełnią rolę repozytoriów lub warstw dostępu do danych w aplikacji.
 *  Klasa oznaczona adnotacją @Repository jest skanowana przez Springa,
 *  a instancje tej klasy są tworzone jako Beany, które mogą być wstrzykiwane
 *  w inne klasy za pomocą mechanizmu wstrzykiwania zależności.
 * 
 * Adnotacja @Repository jest specjalnym przypadkiem adnotacji @Component 
 * i dodatkowo dostarcza semantykę, która pomaga zrozumieć rolę klasy w 
 * kontekście dostępu do danych. Oznacza, że dana klasa pełni rolę repozytorium,
 *  czyli jest odpowiedzialna za komunikację z bazą danych, wykonywanie operacji
 *  CRUD (Create, Read, Update, Delete) i inne operacje związane z dostępem do danych.
 */

 /*
  * Interface ktory sluzy do komunikacji z daną tabelą w bazie danych
  * Client - klasa ktora zmapowalismy na tabele
  * Integer - typ klucza glownego
  */
@Repository
public interface ClientRepository  extends JpaRepository<Client, Integer> {
    
    // @Query("SELECT c FROM Client c WHERE c.id = ?1")
    // List<Client> findClientById(Integer Id);
}
