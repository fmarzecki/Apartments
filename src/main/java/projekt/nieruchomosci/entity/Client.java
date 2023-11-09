package projekt.nieruchomosci.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * @Entity - służy do oznaczania klas, które mają być mapowane na encje w bazie danych. 
 * 
 * @Data - tworzy geterry i settery
 * 
 * @AllArgsConstuctor - tworzy konstruktor dla kazdej zmiennej
 * 
 * @NoArgsConstructor - tworzy konstruktor bezargumentowy, Wymagane przez Hibernate
 *  Kiedy Hibernate pobiera dane z bazy danych, tworzy nowe obiekty encji za pomocą 
 *  konstruktora bezargumentowego, a następnie ustawia wartości pól za pomocą setterów
 *  lub poprzez refleksję. Jeśli konstruktor bezargumentowy nie jest dostępny, 
 *  Hibernate może napotkać problem podczas tworzenia nowych obiektów.
 * 
 * @Table(name = "clients") - podajemy nazwe tabeli w bazie danych na ktora chcemy zmapowac klase
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)  
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Override
    public String toString() {
        return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", idNumber=" + idNumber
                + ", city=" + city + ", street=" + street + "]\n";
    }

    public Client(String firstName, String lastName, String idNumber, String city, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.city = city;
        this.street = street;
    }
}
