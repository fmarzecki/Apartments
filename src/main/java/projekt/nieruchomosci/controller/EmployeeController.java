package projekt.nieruchomosci.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import projekt.nieruchomosci.dao.ContractRepository;
import projekt.nieruchomosci.dao.DefectRepository;
import projekt.nieruchomosci.dao.MailRepository;
import projekt.nieruchomosci.entity.Apartment;
import projekt.nieruchomosci.entity.ApartmentPhoto;
import projekt.nieruchomosci.entity.Contract;
import projekt.nieruchomosci.entity.Defect;
import projekt.nieruchomosci.entity.Mail;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.service.ApartmentService;
import projekt.nieruchomosci.service.UserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final ApartmentService apartmentService;
    private final UserService userService;
    private final ContractRepository contractRepository;
    private final MailRepository mailRepository;
    private final DefectRepository defectRepository;
    

    public EmployeeController(ApartmentService apartmentService, UserService userService,
            ContractRepository contractRepository, MailRepository mailRepository, DefectRepository defectRepository) {
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.contractRepository = contractRepository;
        this.mailRepository = mailRepository;
        this.defectRepository = defectRepository;
    }

    @GetMapping
    public String redirect() {
        return "redirect:/employee/apartments";
    }

                        /*  ZARZADZANIE APARTAMENTAMI */
    /* --------------------------------------------------------------------------------- */

     // Wyswietlenie formularza dodania apartamentu
    @GetMapping("/addApartment")
    public String showApartmentForm(Model model) {
        model.addAttribute("apartment", new Apartment());
        return "apartment/apartment_form"; 
    }

    // Dodawanie apartamentu
    @PostMapping("/addApartment")
    public String addApartment(@ModelAttribute("apartment") Apartment apartment,
     @RequestParam(value = "photo") MultipartFile file, 
     @RequestParam(value = "photo2") MultipartFile file2) {
        // Pobierz aktualnie zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        Apartment currentApartment = apartmentService.findById(apartment.getId());
        OkHttpClient client = new OkHttpClient();
        String apiKey = "590b6dca1f950b224ae9d8d8afb6e8e8";
        String url = "https://api.imgbb.com/1/upload";      
        ArrayList<ApartmentPhoto> photos = new ArrayList<>();

        // Jesli zdjecia istaniały i nie chcemy dodać nowego
        if (file.isEmpty() && file2.isEmpty() && apartmentService.findById(currentApartment.getId()).getPhotos() != null) {
            apartment.setPhotos(currentApartment.getPhotos());

            apartment.setPhotos(photos);
            apartment.setUser(user);
            apartment.setBusiness(user.getBusiness());
            apartmentService.save(apartment);
            return "redirect:/employee";
        }

        // Jeśli jedno zdjecie istniało i nie chcemy dodać nowego
        if (file.isEmpty() && currentApartment != null && apartmentService.findById(currentApartment.getId()).getPhotos().get(0) != null) {
            photos.add(currentApartment.getPhotos().get(0));
        } else {
            // zdjecie istnialo i chcemy zaktulizoawc
            // zdjecie nie istnialo i chcemy je dodac
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
                    ObjectMapper mapper = new ObjectMapper();
                    String responseData = response.body().string();
                    JsonNode jsonNode = mapper.readTree(responseData);
                    String imgUrl = jsonNode.get("data").get("url").asText();
                    ApartmentPhoto photo1 = new ApartmentPhoto();
                    photo1.setApartment(apartment);
                    photo1.setPhoto(imgUrl);

                    if (currentApartment == null) {
                        photos.add(photo1);
                    }
                    // jesli aktualizujemy zdjecie
                    else {
                        photo1.setId(currentApartment.getPhotos().get(0).getId());
                        photos.add(photo1);
                    }
                } else {
                    ApartmentPhoto photo2 = new ApartmentPhoto();
                    photo2.setApartment(apartment);
                    photos.add(photo2);
                    System.out.println("Błąd podczas przesyłania pliku do ImgBB. Kod błędu: " + response.code());
                }

                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        // Jeśli jedno zdjecie istniało i nie chcemy dodać nowego
        if (file2.isEmpty() && currentApartment != null && apartmentService.findById(currentApartment.getId()).getPhotos().get(1) != null) {
            photos.add(currentApartment.getPhotos().get(1));
        } else {
            try {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("key", apiKey)
                        .addFormDataPart("image", file2.getName(),
                                RequestBody.create(file2.getBytes(), MediaType.parse("application/octet-stream")))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Call call = client.newCall(request);
                Response response = call.execute();

                if (response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();

                    String responseData = response.body().string();
                    JsonNode jsonNode = mapper.readTree(responseData);
                    String imgUrl = jsonNode.get("data").get("url").asText();

                    ApartmentPhoto photo2 = new ApartmentPhoto();
                    photo2.setApartment(apartment);
                    photo2.setPhoto(imgUrl);

                    if (currentApartment == null) {
                        photos.add(photo2);
                    }
                    // jesli aktualizujemy zdjecie
                    else {
                        photo2.setId(currentApartment.getPhotos().get(1).getId());
                        photos.add(photo2);
                    }
                    
                } else {
                    ApartmentPhoto photo2 = new ApartmentPhoto();
                    photo2.setApartment(apartment);
                    photos.add(photo2);
                    System.out.println("Błąd podczas przesyłania pliku do ImgBB. Kod błędu: " + response.code());
                }

                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        // if (photos.size() < 2) {
        //     ApartmentPhoto photo2 = new ApartmentPhoto();
        //     photo2.setApartment(apartment);
        //     photos.add(photo2);
        // }

        apartment.setPhotos(photos);
        apartment.setUser(user);
        apartment.setBusiness(user.getBusiness());
        apartmentService.save(apartment);
        return "redirect:/employee";
    }

    // Wyswietlenie apartamentów zalogowanego użytkownika
    @GetMapping("/apartments")
    public String getApartments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userService.findByEmail(currentUser);

        model.addAttribute("business", user.getBusiness());
        model.addAttribute("apartments", user.getApartments());
        return "employee/employee_all_apartments";
    } 

    // Usunięcie wybranego apartamentu
    @GetMapping("/apartment/delete")
    public String deleteBusiness(@RequestParam("apartmentId") int apartmentId) {
        Apartment apartment = apartmentService.findById(apartmentId);
        apartmentService.delete(apartment);
        return "redirect:/employee";
    }

    // Wyswietlenie formularza aktualizacji apartamentu
    @GetMapping("/apartment/showFormUpdate")
    public String showFormUpdate(@RequestParam("apartmentId") int apartmentId, Model theModel, HttpSession httpSession) {
        Apartment apartment = apartmentService.findById(apartmentId);
        theModel.addAttribute("apartment", apartment);
        return "apartment/apartment_form";
    }

                            /*  ZARZADZANIE KONTRAKTAMI */
    /* --------------------------------------------------------------------------------- */

    // Wyswietlenie wszystkich kontraktów zalogowango użytkownika
    @GetMapping("contract/contracts")
    public String getContracts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail( authentication.getName());

        // Pobierz mieszkania pracownika które mają umowe
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithContract = apartments.stream()
        .filter(apartment -> apartment.getContract() != null)
        .collect(Collectors.toList());

        // Pobierz umowe dla każdego mieszkania
        List<Contract> contracts = new ArrayList<>();
        for (Apartment apartment : apartmentsWithContract) {
            contracts.add(apartment.getContract());
        }

        model.addAttribute("contracts", contracts);

        return "contract/contract_list";
    } 

    // Wyswietlenie formularza dodania kontraktu
    @GetMapping("/contract/addContract")
    public String showContractForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithoutContract = apartments.stream()
        .filter(apartment -> apartment.getContract() == null)
        .collect(Collectors.toList());

        model.addAttribute("contract", new Contract());
        model.addAttribute("apartments", apartmentsWithoutContract);
        return "contract/contract_form"; 
    }
    
    // Dodanie umowy
    @PostMapping("/contract/addContract")
    public String addContract(@ModelAttribute("contract") Contract contract, Model model) {
        // Pobierz dane najemcy po emailu
        User tenant = userService.findByEmail(contract.getUser().getEmail());

        // Jesli taki użytkownik nie istnieje powrót do formularza
        if (tenant  == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByEmail(authentication.getName());

            // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
            List<Apartment> apartments =  user.getApartments();
            List<Apartment> apartmentsWithoutContract = apartments.stream()
            .filter(apartment -> apartment.getContract() == null)
            .collect(Collectors.toList());

            model.addAttribute("contract", contract);
            model.addAttribute("apartments", apartmentsWithoutContract);
            model.addAttribute("info", "Klient o takim email nie istnieje");
            return "contract/contract_form";
        }

        Apartment apartment = apartmentService.findById(contract.getApartment().getId());

        contract.setRent(apartment.getRent());
        // Ustaw numer konta firmy do umowy
        contract.setOwnerAccountNumber(apartment.getBusiness().getAccountNumber());
        // Powiąż dane wynajmującego z umową
        contract.setUser(tenant);
        // Zapisz umowe do bazy
        contractRepository.save(contract);
        return "redirect:/employee/contract/contracts";
    }

    // Usunięcie wybranej umowy
    @GetMapping("/contract/delete")
    public String deleteContract(@RequestParam("contractId") Long contractId) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (contract.isPresent()) {
            contractRepository.delete(contract.get());
        }
        return "redirect:/employee/contract/contracts";
    }

    // Wyswietlenie formularza aktualizacji apartamentu
    @GetMapping("/contract/showFormUpdate")
    public String showContractFormUpdate(@RequestParam("contractId") Long contractId, Model theModel) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (contract.isPresent()) {
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        // Chcemy wyświetlić tylko te apartamenty które nie mają umowy.
        List<Apartment> apartments =  user.getApartments();
        List<Apartment> apartmentsWithoutContract = apartments.stream()
        .filter(apartment -> apartment.getContract() == null)
        .collect(Collectors.toList());

        theModel.addAttribute("contract", contract.get());
        theModel.addAttribute("apartments", apartmentsWithoutContract);
        }
        
        return "contract/contract_form";
    }


                                /*  ZARZADZANIE USTERKAMI */
    /* --------------------------------------------------------------------------------- */

    // Wyświetlenie wszystkich umów użytkownika
    @GetMapping("/defects")
    public String getDefects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        List<Apartment> apartments =  user.getApartments();
        List<Defect> defects = new ArrayList<>();
        for (Apartment apartment : apartments) {
            for (Defect defect : apartment.getDefects()) {
                 defects.add(defect);
                   if (!defect.getIsRead()) {
                    defect.setIsRead(true);
                    defectRepository.save(defect);
                 }
            }
        }

        model.addAttribute("defects", defects);
        return "employee/defect_list";
    }

                                    /*  ZARZADZANIE MAILAMI */
    /* --------------------------------------------------------------------------------- */

    // Wyświetlenie wszystkich maili użytkownika
    @GetMapping("/mails")
    public String getMails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        List<Apartment> apartments =  user.getApartments();
        List<Mail> mails = new ArrayList<>();
        for (Apartment apartment : apartments) {
            for (Mail mail : apartment.getMails()) {
                 mails.add(mail);
                 if (!mail.getIsRead()) {
                    mail.setIsRead(true);
                    mailRepository.save(mail);
                 }
            }
        }
        model.addAttribute("mails", mails);
        return "employee/mail_list";
    }
}
