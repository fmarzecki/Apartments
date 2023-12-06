package projekt.nieruchomosci.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekt.nieruchomosci.dao.ReportController;
import projekt.nieruchomosci.dao.RoleRepository;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Business;
import projekt.nieruchomosci.entity.Report;
import projekt.nieruchomosci.entity.Role;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.BusinessService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final BusinessService businessService;
    private final ReportController reportController;
    private final ApartmentService  apartmentService;


    public ManagerController(UserService userService, RoleRepository roleRepository, BusinessService businessService,
            ReportController reportController, ApartmentService apartmentService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.businessService = businessService;
        this.reportController = reportController;
        this.apartmentService = apartmentService;
    }

    // Wyswietl informacje na temat firmy managera
    @GetMapping
    public String getBusinessInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("business", user.getBusiness());
        return "manager/manager_business";
    }

    // Dodaj pracownika do firmy
    @PostMapping("/addEmployeeToBusiness")
    public String addEmployeeToBusiness(
            @RequestParam("employeeEmail") String email,
            @RequestParam("businessId") Long businessId,
            @RequestParam(name = "isManager", defaultValue = "false") Boolean isManager,
            Model model) {

        Business business = businessService.findById(businessId);
        User employee = userService.findByEmail(email);

        if (employee == null) {
            model.addAttribute("info", "Uzytkownika o takim emailu nie znaleziono.");
            model.addAttribute("business", business);
            return "manager/business_employee";
        }

        if (employee.getBusiness() != null) {
            model.addAttribute("info", "Uzytkownik jest już pracownikiem");
            model.addAttribute("business", business);
            return "manager/business_employee";
        }
       
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
        roles.add(roleRepository.findRoleByName("ROLE_CLIENT"));

        if (isManager) {
            roles.add(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.setIsManager(true);
        }

        employee.getRoles().add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
        employee.setBusiness(business);

        userService.update(employee);
        business.getEmployees().add(employee);

        model.addAttribute("business", business);
        model.addAttribute("info", "Uzytkownik dodany");
        return "manager/business_employee";
    }

    // Wyswietl pracownikow firmy
    @GetMapping("/employeesByBusiness")
    public String getEmployeesByBusiness(@RequestParam("businessId") Long id, Model model) {
        model.addAttribute("business", businessService.findById(id));
        return "manager/business_employee";
    }

    // Nadaj role MANAGER pracownikowi firmy
    @GetMapping("/makeEmployeeManager")
    public String makeEmployeeManager(@RequestParam("employeeEmail") String employeeEmail, Model model) {
        User employee = userService.findByEmail(employeeEmail);

        if (employee.getIsManager() == null) {
            employee.getRoles().add(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.setIsManager(true);
            userService.update(employee);
        }
      
        model.addAttribute("business", employee.getBusiness());
        return "manager/business_employee";
    }

    // Usun pracownika
    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("employeeEmail") String employeeEmail, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        User employee = userService.findByEmail(employeeEmail);
        model.addAttribute("business", employee.getBusiness()); 

        if (employee.getBusiness() != null) {
            employee.getBusiness().getEmployees().remove(employee);
            employee.getRoles().remove(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.getRoles().remove(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            employee.setBusiness(null);
            employee.setIsManager(null);

            for (Apartment apartment : employee.getApartments()) {
                apartment.setUser(user);
                apartmentService.save(apartment);
            }

            userService.update(employee);
            return "manager/business_employee";
        }
        return "redirect:/manager";
    }

    // Generuj raport dotyczacy firmy
    @GetMapping("/generateRaport")
    public String generateRaport(@RequestParam("businessId") Long businessId, Model model) {
        Business business = businessService.findById(businessId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        int numberOfContracts = 0;
        int earnings = 0;
        int expenses = business.getEmployees().size() * 3500;
        List<Apartment> businessApartments = business.getApartments();
        for (Apartment apartment : businessApartments) {
            if (apartment.getContract() != null && apartment.getContract().isSigned()) {
                numberOfContracts += 1;
                earnings += apartment.getRent();
            }
        }

        Report report = new Report();
        report.setBusiness(business);
        report.setManager(user);
        report.setNumberOfApartments(business.howManyApartments());
        report.setNumberOfEmployees(business.howManyEmployees());
        report.setNumberOfContracts(numberOfContracts);
        report.setEarnings(earnings);
        report.setExpenses(expenses);
        user.getReports().add(report);
        reportController.save(report);

        return "redirect:/manager/showRaports";
    }

    // Wyswietl raporty
    @GetMapping("/showRaports")
    public String showRaports(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("reports", user.getReports()); 
        model.addAttribute("business", user.getBusiness()); 
        return "manager/manager_reports";
    }

    // Wyswietlenie apartamentów danej firmy
    @GetMapping("/showApartments")
    public String getApartments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userService.findByEmail(currentUser);

        model.addAttribute("business", user.getBusiness());
        model.addAttribute("apartments", user.getBusiness().getApartments());
        return "manager/business_all_apartments";
    } 
}
