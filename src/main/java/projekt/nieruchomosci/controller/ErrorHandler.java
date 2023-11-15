package projekt.nieruchomosci.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorHandler {
    
    @GetMapping("/access-denied")
    public String handleAccessDenied() {
        return "/security/access-denied";
    }

    

}
