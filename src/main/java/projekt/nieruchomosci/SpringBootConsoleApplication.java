package projekt.nieruchomosci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import projekt.nieruchomosci.dao.ClientRepository;

@SpringBootApplication
public class SpringBootConsoleApplication 
  implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
      .getLogger(SpringBootConsoleApplication.class);

      public ClientRepository klientRepozytorium;

      @Autowired
      public SpringBootConsoleApplication(ClientRepository klientRepozytorium) {
        this.klientRepozytorium = klientRepozytorium;
      }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(SpringBootConsoleApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }
 
    @Override
    public void run(String... args) {
      System.out.println(klientRepozytorium.findAll());
    }
}