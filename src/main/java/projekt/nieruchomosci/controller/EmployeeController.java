package projekt.nieruchomosci.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import projekt.nieruchomosci.dao.ContractRepository;
import projekt.nieruchomosci.dao.MailRepository;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Contract;
import projekt.nieruchomosci.entity.Defect;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final ApartmentService apartmentService;
    private final UserService userService;
    private final ContractRepository contractRepository;
    private final MailRepository mailRepository;

    public EmployeeController(ApartmentService apartmentService, UserService userService,
             ContractRepository contractRepository, MailRepository mailRepository) {
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.contractRepository = contractRepository;
        this.mailRepository = mailRepository;
    }

    @GetMapping
    public String redirect() {
        return "redirect:/employee/apartments";
    }

                        /*  ZARZADZANIE APARTAMENTAMI */
    /* --------------------------------------------------------------------------------- */

     // Wyswietlenie formularza dodania apartamentu
    @GetMapping("/addApartment")
    public String showApartmentForm(Model model) {
        model.addAttribute("apartment", new Apartment());
        return "apartment/apartment_form"; 
    }

    // Dodawanie apartamentu
    @PostMapping("/addApartment")
    public String addApartment(@ModelAttribute("apartment") Apartment apartment) {
        // Pobierz aktualnie zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        apartment.setUser(user);
        apartment.setBusiness(user.getBusiness());

        if (apartment.getId() != 0) {
            // Aktualizacja istniejacego mieszkania
            Apartment apartment2 = apartmentService.findById(apartment.getId());

            apartment2.getPhotos().get(0).setPhoto(apartment.getPhotos().get(0).getPhoto());
            apartment2.getPhotos().get(1).setPhoto(apartment.getPhotos().get(1).getPhoto());

            apartment.setPhotos(apartment2.getPhotos());

            apartmentService.save(apartment);
            return "redirect:/employee";
        }

        // Dodanie nowego mieszkania
        apartment.getPhotos().get(0).setApartment(apartment);
        apartment.getPhotos().get(1).setApartment(apartment);

        apartmentService.save(apartment);
        return "redirect:/employee";
    }

  
    // Wyswietlenie apartamentów zalogowanego użytkownika
    @GetMapping("/apartments")
    public String getApartments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userService.findByEmail(currentUser);
        model.addAttribute("apartments", user.getApartments());

        return "employee/employee_all_apartments";
    } 

    // Usunięcie wybranego apartamentu
    @GetMapping("/apartment/delete")
    public String deleteBusiness(@RequestParam("apartmentId") int apartmentId) {
        Apartment apartment = apartmentService.findById(apartmentId);
        apartmentService.delete(apartment);
        return "redirect:/employee";
    }

    // Wyswietlenie formularza aktualizacji apartamentu
    @GetMapping("/apartment/showFormUpdate")
    public String showFormUpdate(@RequestParam("apartmentId") int apartmentId, Model theModel, HttpSession httpSession) {
        Apartment apartment = apartmentService.findById(apartmentId);
        theModel.addAttribute("apartment", apartment);
        return "apartment/apartment_form";
    }

                            /*  ZARZADZANIE KONTRAKTAMI */
    /* --------------------------------------------------------------------------------- */

    // Wyswietlenie wszystkich kontraktów zalogowango użytkownika
    @GetMapping("contract/contracts")
    public String getContracts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail( authentication.getName());

        // Pobierz mieszkania pracownika które mają umowe
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithContract = apartments.stream()
        .filter(apartment -> apartment.getContract() != null)
        .collect(Collectors.toList());

        // Pobierz umowe dla każdego mieszkania
        List<Contract> contracts = new ArrayList<>();
        for (Apartment apartment : apartmentsWithContract) {
            contracts.add(apartment.getContract());
        }

        model.addAttribute("contracts", contracts);

        return "contract/contract_list";
    } 

    // Wyswietlenie formularza dodania kontraktu
    @GetMapping("/contract/addContract")
    public String showContractForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithoutContract = apartments.stream()
        .filter(apartment -> apartment.getContract() == null)
        .collect(Collectors.toList());

        model.addAttribute("contract", new Contract());
        model.addAttribute("apartments", apartmentsWithoutContract);
        return "contract/contract_form"; 
    }
    
    // Dodanie umowy
    @PostMapping("/contract/addContract")
    public String addContract(@ModelAttribute("contract") Contract contract, Model model) {
        // Pobierz dane najemcy po emailu
        User tenant = userService.findByEmail(contract.getUser().getEmail());

        // Jesli taki użytkownik nie istnieje powrót do formularza
        if (tenant  == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByEmail(authentication.getName());

            // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
            List<Apartment> apartments =  user.getApartments();
            List<Apartment> apartmentsWithoutContract = apartments.stream()
            .filter(apartment -> apartment.getContract() == null)
            .collect(Collectors.toList());

            model.addAttribute("contract", contract);
            model.addAttribute("apartments", apartmentsWithoutContract);
            model.addAttribute("info", "Klient o takim email nie istnieje");
            return "contract/contract_form";
        }

        Apartment apartment = apartmentService.findById(contract.getApartment().getId());

        contract.setRent(apartment.getRent());
        // Ustaw numer konta firmy do umowy
        contract.setOwnerAccountNumber(apartment.getBusiness().getAccountNumber());
        // Powiąż dane wynajmującego z umową
        contract.setUser(tenant);
        // Zapisz umowe do bazy
        contractRepository.save(contract);
        return "redirect:/employee/contract/contracts";
    }

    // Usunięcie wybranej umowy
    @GetMapping("/contract/delete")
    public String deleteContract(@RequestParam("contractId") Long contractId) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (contract.isPresent()) {
            contractRepository.delete(contract.get());
        }
        return "redirect:/employee/contract/contracts";
    }

    // Wyswietlenie formularza aktualizacji apartamentu
    @GetMapping("/contract/showFormUpdate")
    public String showContractFormUpdate(@RequestParam("contractId") Long contractId, Model theModel) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (contract.isPresent()) {
       

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithoutContract = apartments.stream()
        .filter(apartment -> apartment.getContract() == null)
        .collect(Collectors.toList());

           
        theModel.addAttribute("contract", contract.get());
        theModel.addAttribute("apartments", apartmentsWithoutContract);
        }
        
        return "contract/contract_form";
    }


                                /*  ZARZADZANIE USTERKAMI */
    /* --------------------------------------------------------------------------------- */

    @GetMapping("/defects")
    public String getDefects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        List<Apartment> apartments =  user.getApartments();
        List<Defect> defects = new ArrayList<>();
        for (Apartment apartment : apartments) {
            for (Defect defect : apartment.getDefects()) {
                 defects.add(defect);
            }
        }

        model.addAttribute("defects", defects);
        return "employee/defect_list";
    }

                                    /*  ZARZADZANIE MAILAMI */
    /* --------------------------------------------------------------------------------- */

    @GetMapping("/mails")
    public String getMails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        List<Apartment> apartments =  user.getApartments();
        List<Mail> mails = new ArrayList<>();
        for (Apartment apartment : apartments) {
            for (Mail mail : apartment.getMails()) {
                 mails.add(mail);
                 if (!mail.getIsRead()) {
                    mail.setIsRead(true);
                    mailRepository.save(mail);
                 }
            }
        }
        model.addAttribute("mails", mails);
        return "employee/mail_list";
    }
}
