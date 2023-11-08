package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import projekt.nieruchomosci.entity.Client;
import projekt.nieruchomosci.service.ClientService;

@Controller
@RequestMapping("/clients")
public class ClientController {
    
    public ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getAllClients(Model model) {
        List<Client> clients = clientService.getAllClients(); // pobranie danych użytkowników z serwisu
        model.addAttribute("users", clients); // przekazanie danych do widoku
        return "clients"; // nazwa pliku szablonu Thymeleaf
    }

    @GetMapping("/{id}")
    public String getClientById(@PathVariable("id") int id, Model model) {
        List<Client> clients = clientService.getClientById(id); // pobranie danych użytkowników z serwisu
        model.addAttribute("users", clients); // przekazanie danych do widoku
        return "clients"; // nazwa pliku szablonu Thymeleaf
    }

    @GetMapping("/showFormAdd")
    public String addClient(Model theModel) {
        Client newClient =  new Client();
        theModel.addAttribute("client", newClient);
        return "clients/clients_form";
    }

    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") Client client) {
        clientService.addClient(client);
        return "redirect:/clients";
    }

}
