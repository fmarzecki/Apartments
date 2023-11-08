package projekt.nieruchomosci.service;

import java.util.List;

import projekt.nieruchomosci.entity.Client;

public interface ClientService {
    List<Client> getAllClients();
    List<Client> getClientById(Integer Id);
    void addClient(Client client);
}
