package projekt.nieruchomosci.controller;

import java.util.ArrayList;
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
    public String addApartment(Model model, @ModelAttribute("apartment") Apartment apartment,
    @ModelAttribute ApartmentPhoto photos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userService.findByEmail(currentUser);
        
        ApartmentPhoto photo1 = new ApartmentPhoto();
        photo1.setPhoto(photos.getPhoto().split(",")[0]);
        photo1.setApartment(apartment);
    
        ApartmentPhoto photo2 = new ApartmentPhoto();
        photo2.setPhoto(photos.getPhoto().split(",")[1]);
        photo2.setApartment(apartment);
    
        List<ApartmentPhoto> photosList = new ArrayList<>();
        photosList.add(photo1);
        photosList.add(photo2);
    
        apartment.setPhotos(photosList);
        apartment.setUser(user);
        apartment.setBusiness(user.getBusiness());
    
        apartmentService.save(apartment);
    
        return "redirect:/employee";
    }

    @GetMapping("/{id}")
    public String showApartmentDetails(@PathVariable int id, Model model) {
        // Pobierz informacje o apartamencie na podstawie ID
        Apartment apartment = apartmentService.findById(id);

        // Przekaż obiekt apartamentu do widoku
        model.addAttribute("apartment", apartment);

        return "employee/employee_apartment"; 
    }
}
