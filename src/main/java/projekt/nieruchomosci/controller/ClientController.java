package projekt.nieruchomosci.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ClientController(ApartmentService apartmentService, ContractRepository contractRepository,
            DefectRepository defectRepository, MailRepository mailRepository, UserService userService) {
        this.apartmentService = apartmentService;
        this.contractRepository = contractRepository;
        this.defectRepository = defectRepository;
        this.mailRepository = mailRepository;
        this.userService = userService;
    }

    // Wyswietl dostepne apartamenty do wynajecia
    @GetMapping
    public String getApartments(
        Model model, 
        @RequestParam(name = "sortBy", defaultValue = "") String sortBy,
        @RequestParam(name = "currentPage", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortDirection", defaultValue = "") String sortDirection) {

        Sort sort;
        Pageable pageable;
        Page<Apartment> apartmentsPage;

        if (!sortBy.equals("")) {
            if (sortDirection.equals("asc")) {
                sort = Sort.by(sortBy).ascending();
            } else {
                sort = Sort.by(sortBy).descending();
            }
            pageable = PageRequest.of(page-1, size, sort);
            apartmentsPage = apartmentService.findApartmentsWithoutContract(pageable);
        } else {
            pageable = PageRequest.of(page-1, size);
            apartmentsPage = apartmentService.findApartmentsWithoutContract(pageable);
        }

        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("currentPage", page+1);
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        return "client/client_all_apartments";
    }

    // Wysweitl umowy użytownika
    @GetMapping("/contracts")
    public String getContracts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("contracts", user.getContracts());
        return "client/client_contract_list";
    }

    // Wyswietl formularz do wyslania usterki
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

    // Wyswietl formularz do wyslania usterki
    @GetMapping("/signContract")
    public String signContract(@RequestParam("contractId") Long contractId, Model model) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (!contract.isPresent()) {
            return "redirect:/client/contracts";
        }

        contract.get().setSigned(true);
        contractRepository.save(contract.get());
        return "redirect:/client/contracts";
    }


    // Wyswietl formularz do wyslania maila
    @GetMapping("/showMailForm")
    public String showMailForm(@RequestParam("apartmentId") int apartmentId, Model model) {
        Apartment apartment = apartmentService.findById(apartmentId);

        Mail mail = new Mail();
        mail.setApartment(apartment);
        model.addAttribute("mail", mail);

        return "client/client_mail_form";
    }

    // Dodaj usterke
    @PostMapping("/addDefect")
    public String addDefect(@ModelAttribute Defect defect) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        defect.setIsRead(false);
        defect.setUser(user);
        defectRepository.save(defect);
        return "redirect:/client/defects";
    }
    

    // Dodaj maila
    @PostMapping("/addMail")
    public String addMail(@ModelAttribute Mail mail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        mail.setIsRead(false);
        mail.setUser(user);
        mailRepository.save(mail);
        return "redirect:/client/mails";
    }

    // Wyswietl maile uzytkownia
    @GetMapping("/mails")
    public String getMails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("mails", user.getMails());
        return "client/mail_list";
    }

    // Wyswietl usterki uzytkownika
    @GetMapping("/defects")
    public String getDefects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("defects", user.getDefects());
        return "client/defect_list";
    }
}
