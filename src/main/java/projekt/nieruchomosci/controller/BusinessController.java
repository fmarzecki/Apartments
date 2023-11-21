package projekt.nieruchomosci.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    public String addBusiness(@ModelAttribute("business") Business business, @RequestParam("photo") MultipartFile file) {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "590b6dca1f950b224ae9d8d8afb6e8e8";
        String url = "https://api.imgbb.com/1/upload";
        
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", apiKey)
                .addFormDataPart("image", file.getName(),
                    RequestBody.create(file.getBytes(), MediaType.parse("application/octet-stream")))
                .build();
    
            Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    
            Call call = client.newCall(request);
            Response response = call.execute();
    
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JSONPObject result = mapper.readValue(responseData, JSONPObject.class);
                

                System.out.println(result.getValue().toString());
                // business.setLogo(responseData);
                // businessService.add(business);

            } else {
                System.out.println("Błąd podczas przesyłania pliku do ImgBB. Kod błędu: " + response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/business";
    }
    

    @GetMapping
    public String getBusinesses(Model theModel) {
        List<Business> businesses = businessService.getAll();
        theModel.addAttribute("businesses", businesses);
        return "business/businesses";
    }

    @GetMapping("/delete")
    public String deleteBusiness(@RequestParam("businessId") Long businessId) {
        Business business = businessService.findById(businessId);
        // Wraz z usunieciem firmy, musimy
        List<User> employees = userService.findByBusinessId(businessId.intValue());

        for (User user : employees) {
            user.getRoles().remove(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            user.getRoles().remove(roleRepository.findRoleByName("ROLE_MANAGER"));
        }

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
    public String showFormUpdate(@RequestParam("businessId") Long businessId, Model theModel) {
        Business business = businessService.findById(businessId);
        theModel.addAttribute("business", business);
        return "business/business_form";
    }

    @GetMapping("/showFormEmployee")
    public String showFormEmployee(@RequestParam("businessId") Long businessId, Model theModel) {
        Business business = businessService.findById(businessId);
        theModel.addAttribute("business", business);
        return "business/business_employee";
    }

    @PostMapping("/addEmployeeToBusiness")
    public String addEmployeeToBusiness(
            @RequestParam("employeeEmail") String email,
            @RequestParam("businessId") Long businessId,
            @RequestParam(name = "isManager", defaultValue = "false") Boolean isManager,
            Model model) {

        Business business = businessService.findById(businessId);
        User employee = userService.findByEmail(email);

        if (employee == null) {
            model.addAttribute("info", "Uzytkownika o takim emailu nie znaleziono.");
            model.addAttribute("business", business);
            return "business/business_employee";
        }

        if (employee.getBusiness() != null) {
            model.addAttribute("info", "Uzytkownik jest już pracownikiem");
            model.addAttribute("business", business);
            return "business/business_employee";
        }
       
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
        roles.add(roleRepository.findRoleByName("ROLE_CLIENT"));

        if (isManager) {
            roles.add(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.setIsManager(true);
        }

        employee.setRoles(roles);
        employee.setBusiness(business);

        userService.update(employee);
        business.getEmployees().add(employee);

        model.addAttribute("business", business);
        model.addAttribute("info", "Uzytkownik dodany");
        return "business/business_employee";
    }

    @GetMapping("/employeesByBusiness")
    public String getEmployeesByBusiness(@RequestParam("businessId") Long id, Model model) {
        model.addAttribute("business", businessService.findById(id));
        return "business/business_employee";
    }
    
    @GetMapping("/makeEmployeeManager")
    public String makeEmployeeManager(@RequestParam("employeeEmail") String employeeEmail, Model model) {
        User employee = userService.findByEmail(employeeEmail);

        if (employee.getIsManager() == null) {
            employee.getRoles().add(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.setIsManager(true);
            userService.update(employee);
        }
      

        model.addAttribute("business", employee.getBusiness());
        return "business/business_employee";
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("employeeEmail") String employeeEmail, Model model) {
        User employee = userService.findByEmail(employeeEmail);
        model.addAttribute("business", employee.getBusiness()); 

        if (employee.getBusiness() != null) {
            employee.getBusiness().getEmployees().remove(employee);
            employee.getRoles().remove(roleRepository.findRoleByName("ROLE_MANAGER"));
            employee.getRoles().remove(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            employee.setBusiness(null);
            employee.setIsManager(null);
            userService.update(employee);
            return "business/business_employee";
        }
        
        return "redirect:/";
    }
}
