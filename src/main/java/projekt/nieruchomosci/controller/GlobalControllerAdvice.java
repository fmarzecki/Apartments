package projekt.nieruchomosci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import projekt.nieruchomosci.dao.MailRepository;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MailRepository mailRepository;
    private final ApartmentService apartmentService;
    private final UserService userService;





    public GlobalControllerAdvice(MailRepository mailRepository, ApartmentService apartmentService,
            UserService userService) {
        this.mailRepository = mailRepository;
        this.apartmentService = apartmentService;
        this.userService = userService;
    }

    @ModelAttribute("hasUnreadMails")
    public boolean hasUnreadMails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
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
