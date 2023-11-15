package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import projekt.nieruchomosci.entity.Client;
import projekt.nieruchomosci.service.ClientService;

/*
 * @Controller jest adnotacją używaną w Spring MVC do oznaczania klas,
 *  które pełnią rolę kontrolerów w aplikacji webowej. Kontrolery są 
 *  odpowiedzialne za obsługę żądań HTTP, przetwarzanie danych wejściowych,
 *  wywoływanie odpowiednich operacji biznesowych i renderowanie widoków w
 *  odpowiedzi na żądania użytkowników.
 */

 /*
  * @RequestMapping("/clients")
  *  Określa ona ścieżkę, na której dana metody kontrolera będą reagować na żądania HTTP.
  */
@Controller
@RequestMapping("/clients")
public class ClientController {
    
    public ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /*
     * Model model - obiekt ktory przekazujemy do html, sluzy do
     * komunikacji miedzy Java a HTML
     */
    @GetMapping
    public String getAllClients(Model model) {
        List<Client> clients = clientService.getAllClients(); // pobranie danych użytkowników z serwisu
        model.addAttribute("clients", clients); // przekazanie danych do widoku
        return "clients/clients"; // nazwa pliku szablonu Thymeleaf
    }

    @GetMapping("/{id}")
    public String getClientById(@PathVariable("id") int id, Model model) {
        List<Client> clients = clientService.getClientById(id); // pobranie danych użytkowników z serwisu
        model.addAttribute("clients", clients); // przekazanie danych do widoku
        return "clients/clients"; // nazwa pliku szablonu Thymeleaf
    }

    /*
     * Przekazujemy do formularza pusty obiekt klient
     * Kiedy zatwierdzimy formularz, Thymeleaf automatycznie wywola settery dla pól
     * ktore zostaly wskazane w formularzu
     */
    @GetMapping("/showFormAdd")
    public String addClient(Model theModel) {
        Client newClient =  new Client();
        theModel.addAttribute("client", newClient);
        return "clients/clients_form";
    }

    
    /*
     * Endpoint do zapisywania nowych klientów
     * po wywolaniu robimy redirect do listy klientow
     */
    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") Client client) {
        clientService.addClient(client);
        return "redirect:/clients";
    }

}
