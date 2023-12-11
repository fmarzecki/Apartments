package projekt.nieruchomosci.service;

import org.springframework.web.multipart.MultipartFile;

import okhttp3.Response;

public interface EmployeeService {
    Response sendPhoto(MultipartFile file);
    String handleResponse(Response response);
}
