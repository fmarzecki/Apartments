package projekt.nieruchomosci.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.ApartmentPhoto;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final ApartmentService apartmentService;
    private final UserService userService;


    public EmployeeController(ApartmentService apartmentService, UserService userService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
    }

    @GetMapping("/addApartment")
    public String showApartmentDetails(Model model) {
        model.addAttribute("apartment", new Apartment());
        model.addAttribute("photo", new ApartmentPhoto());
        return "employee/employee_apartment_form"; 
    }
  
    @GetMapping
    public String getApartments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userService.findByEmail(currentUser);
        model.addAttribute("apartments", user.getApartments());

        return "employee/employee_all_apartments";
    } 
}
