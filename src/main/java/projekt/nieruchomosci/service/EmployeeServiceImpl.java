package projekt.nieruchomosci.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public Response sendPhoto(MultipartFile file) {

        try {
            OkHttpClient client = new OkHttpClient();
            String apiKey = "590b6dca1f950b224ae9d8d8afb6e8e8";
            String url = "https://api.imgbb.com/1/upload";

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

            return response;

        } catch (IOException e) {
            // e.printStackTrace();
            return null;
        }
    }

    @Override
    public String handleResponse(Response response) {
        if (response.isSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String responseData = response.body().string();
                JsonNode jsonNode = mapper.readTree(responseData);
                String imgUrl = jsonNode.get("data").get("url").asText();

                return imgUrl;
            } catch (IOException e) {
                // e.printStackTrace();
                return "";
            }
        }
        else {
            return "";
        }
    }
}
