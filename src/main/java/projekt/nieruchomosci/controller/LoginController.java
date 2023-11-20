package projekt.nieruchomosci.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {

        return "security/fancy-login";
    }

    @GetMapping
    public String homePage() {
        return "redirect:/client";
    }
}
