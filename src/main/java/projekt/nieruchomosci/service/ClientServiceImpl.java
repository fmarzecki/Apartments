package projekt.nieruchomosci.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekt.nieruchomosci.dao.ClientRepository;
import projekt.nieruchomosci.entity.Client;

@Service
public class ClientServiceImpl implements ClientService {

    public ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientById(Integer Id) {
        return List.of(clientRepository.findById(Id).orElseThrow());
    }

    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    
}
