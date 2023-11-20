package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

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
}
