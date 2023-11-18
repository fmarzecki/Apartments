package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.ApartmentPhoto;
import projekt.nieruchomosci.entity.User;
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

    @PostMapping
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

    @GetMapping("/{id}")
    public String showApartmentDetails(@PathVariable int id, Model model) {
        Apartment apartment = apartmentService.findById(id);

        model.addAttribute("apartment", apartment);

        return "apartment/apartment_details"; 
    }

    @GetMapping("/delete")
    public String deleteBusiness(@RequestParam("apartmentId") int apartmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        Apartment apartment = apartmentService.findById(apartmentId);
    
        user.getApartments().remove(apartment);
        apartmentService.delete(apartment);
        return "redirect:/employee";
    }

    @GetMapping("/showFormUpdate")
    public String showFormUpdate(@RequestParam("apartmentId") int apartmentId, Model theModel, HttpSession httpSession) {
        Apartment apartment = apartmentService.findById(apartmentId);
        theModel.addAttribute("apartment", apartment);
        return "employee/employee_apartment_form";
    }
}
