package projekt.nieruchomosci.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/apartment")
public class ApartmentController {
    
    private final ApartmentService apartmentService;
    private final UserService userService;

    public ApartmentController(ApartmentService apartmentService, UserService userService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public String showApartmentDetails(@PathVariable int id, Model model) {
        Apartment apartment = apartmentService.findById(id);

        model.addAttribute("apartment", apartment);

        return "apartment/apartment_details"; 
    }


}
