package projekt.nieruchomosci.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekt.nieruchomosci.dao.ContractRepository;
import projekt.nieruchomosci.dao.DefectRepository;
import projekt.nieruchomosci.dao.MailRepository;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Contract;
import projekt.nieruchomosci.entity.Defect;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/client")
public class ClientController {

    ApartmentService apartmentService;
    ContractRepository contractRepository;
    DefectRepository defectRepository;
    MailRepository mailRepository;
    UserService userService;
    int sortFlag = 0;

    public ClientController(ApartmentService apartmentService, ContractRepository contractRepository,
            DefectRepository defectRepository, MailRepository mailRepository, UserService userService) {
        this.apartmentService = apartmentService;
        this.contractRepository = contractRepository;
        this.defectRepository = defectRepository;
        this.mailRepository = mailRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getApartments(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
        // Pobierz apartamenty które nie są wynajęte
        Sort sort;
        List<Apartment> apartments;
        if (sortBy != null) {
            if (sortFlag == 0) {
                sort = Sort.by(sortBy).ascending().descending();
                sortFlag = 1;
            } else {
                sort = Sort.by(sortBy).ascending().ascending();
                sortFlag = 0;
            }
            apartments = apartmentService.findAll(sort);

        } else {
            apartments = apartmentService.findAll();

        }


        List<Apartment> apartmentsWithoutContract = apartments.stream()
                .filter(apartment -> apartment.getContract() == null)
                .collect(Collectors.toList());

        model.addAttribute("apartments", apartmentsWithoutContract);
        return "client/client_all_apartments";
    }

    @GetMapping("/contracts")
    public String getContracts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("contracts", user.getContracts());
        return "client/client_contract_list";
    }

    @GetMapping("/showDefectForm")
    public String showDefectForm(@RequestParam("contractId") Long contractId, Model model) {
        // TODO sprawdzic czy usterki pochodza od uzytkownika posiadajacego mieszkanie

        Optional<Contract> contract = contractRepository.findById(contractId);
        if (!contract.isPresent()) {
            return "redirect:/client/contracts";
        }

        Defect defect = new Defect();
        defect.setApartment(contract.get().getApartment());
        model.addAttribute("defect", defect);

        return "client/client_defect_form";
    }

    @GetMapping("/showMailForm")
    public String showMailForm(@RequestParam("apartmentId") int apartmentId, Model model) {
        Apartment apartment = apartmentService.findById(apartmentId);

        Mail mail = new Mail();
        mail.setApartment(apartment);
        model.addAttribute("mail", mail);

        return "client/client_mail_form";
    }

    @PostMapping("/addDefect")
    public String addDefect(@ModelAttribute Defect defect) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        defect.setIsRead(false);
        defect.setUser(user);
        defectRepository.save(defect);
        return "redirect:/client/defects";
    }

    @PostMapping("/addMail")
    public String addMail(@ModelAttribute Mail mail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        mail.setIsRead(false);
        mail.setUser(user);
        mailRepository.save(mail);
        return "redirect:/client/mails";
    }

    @GetMapping("/mails")
    public String getMails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("mails", user.getMails());
        return "client/mail_list";
    }

    @GetMapping("/defects")
    public String getDefects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("defects", user.getDefects());
        return "client/defect_list";
    }
}
