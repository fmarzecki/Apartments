package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Defect;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    // Wyslij informacje o tym czy pracownik ma nieodczytane maile
    @ModelAttribute("hasUnreadMails")
    public boolean hasUnreadMails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return false;
        }
        List<Apartment> apartments = user.getApartments();
        for (Apartment apartment : apartments) {
            for (Mail mail : apartment.getMails()) {
                if (!mail.getIsRead()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Wyslij informacje o tym czy pracownik ma nieodczytane usterki
    @ModelAttribute("hasUnreadDefects")
    public boolean hasUnreadDefects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return false;
        }

        List<Apartment> apartments = user.getApartments();
        for (Apartment apartment : apartments) {
            for (Defect defect : apartment.getDefects()) {
                if (!defect.getIsRead()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Zaladuj logo firmy jesli takowe istnieje
    @ModelAttribute("businessLogo")
    public String getLogo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            return null;
        }

        if (user.getBusiness() != null ) {
            return user.getBusiness().getLogo();
        }
        return null;
    }

}
