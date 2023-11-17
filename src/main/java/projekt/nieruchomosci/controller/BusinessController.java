package projekt.nieruchomosci.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.ModelAttribute;

import projekt.nieruchomosci.dao.RoleRepository;
import projekt.nieruchomosci.entity.Business;
import projekt.nieruchomosci.entity.Role;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.BusinessService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/business")
public class BusinessController {
    private final BusinessService businessService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public BusinessController(BusinessService businessService, UserService userService, RoleRepository roleRepository) {
        this.businessService = businessService;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public String addBusiness(@ModelAttribute("business") Business business) {
        businessService.add(business);
        return "redirect:/business";
    }

    @GetMapping
    public String getBusinesses(Model theModel) {
        List<Business> businesses = businessService.getAll();
        theModel.addAttribute("businesses", businesses);
        return "business/businesses";
    }

    @GetMapping("/delete")
    public String deleteBusiness(@RequestParam("businessId") int businessId) {
        Business business = businessService.findById(businessId);
        businessService.delete(business);
        return "redirect:/business";
    }

    @GetMapping("/showFormAdd")
    public String showForm(Model theModel) {
        Business business = new Business();
        theModel.addAttribute("business", business);
        return "business/business_form";
    }

    @GetMapping("/showFormUpdate")
    public String showFormUpdate(@RequestParam("businessId") int businessId, Model theModel) {
        Business business = businessService.findById(businessId);
        theModel.addAttribute("business", business);
        return "business/business_form";
    }

    @GetMapping("/showFormEmployee")
    public String showFormEmployee(@RequestParam("businessId") int businessId, Model theModel) {
        Business business = businessService.findById(businessId);
        theModel.addAttribute("business", business);
        return "business/business_form_employee";
    }

    @Transactional
    @PostMapping("/addEmployeeToBusiness")
    public String addEmployeeToBusiness(
            @RequestParam("employeeEmail") String email,
            @RequestParam("businessId") int businessId,
            Model model) {

        Business business = businessService.findById(businessId);
        User employee = userService.findByEmail(email);

        if (employee == null) {
            model.addAttribute("info", "Uzytkownika o takim emailu nie znaleziono.");
            model.addAttribute("business", business);
            return "business/businessEmployee";
        }

        if (employee.getBusiness() != null) {
            model.addAttribute("info", "Uzytkownik jest już pracownikiem");
            model.addAttribute("business", business);
            return "business/businessEmployee";
        }
       
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
        roles.add(roleRepository.findRoleByName("ROLE_CLIENT"));

        employee.setRoles(roles);
        employee.setBusiness(business);

        business.getEmployees().add(employee);

        model.addAttribute("business", business);
        model.addAttribute("info", "Uzytkownik dodany");
        return "business/businessEmployee";
    }

    @GetMapping("/employeesByBusiness")
    public String getEmployeesByBusiness(@RequestParam("businessId") int id, Model model) {
        model.addAttribute("business", businessService.findById(id));
        return "business/businessEmployee";
    }
}
