package projekt.nieruchomosci;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projekt.nieruchomosci.dao.ClientRepository;

@SpringBootApplication
public class SpringBootConsoleApplication 
  implements CommandLineRunner {

      public ClientRepository klientRepozytorium;

      @Autowired
      public SpringBootConsoleApplication(ClientRepository klientRepozytorium) {
        this.klientRepozytorium = klientRepozytorium;
      }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    }
 
    @Override
    public void run(String... args) {
      System.out.println(klientRepozytorium.findAll());
    }
}