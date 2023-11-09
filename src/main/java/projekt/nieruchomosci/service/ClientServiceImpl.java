package projekt.nieruchomosci.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekt.nieruchomosci.dao.ClientRepository;
import projekt.nieruchomosci.entity.Client;


/*
    Adnotacja @Service jest specjalnym przypadkiem adnotacji @Component 
    i dodatkowo dostarcza semantykę, która pomaga zrozumieć rolę klasy w
    kontekście biznesowym aplikacji. Oznacza, że dana klasa pełni rolę 
    serwisu, czyli zajmuje się logiką biznesową, przetwarzaniem danych,
    interakcją z repozytoriami danych i innymi operacjami związanymi z 
    obsługą logiki biznesowej aplikacji.
*/

/*
 *  Użycie adnotacji @Override w klasie podrzędnej pozwala kompilatorowi Javy sprawdzić,
 *  czy rzeczywiście przesłaniasz metodę z klasy nadrzędnej. Jeśli metoda oznaczona
 *  adnotacją @Override nie przesłania żadnej metody w klasie nadrzędnej, kompilator 
 *  zgłosi błąd kompilacji.
 */

@Service
public class ClientServiceImpl implements ClientService {

    public ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClientById(Integer Id) {
        return List.of(clientRepository.findById(Id).orElseThrow());
    }

    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    
}
