package projekt.nieruchomosci.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "klienci")
public class Client {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)  
    @Column(name = "id_kli")
    private int id;

    @Column(name = "imie")
    private String imie;

    @Column(name = "nazwisko")
    private String nazwisko;

    @Column(name = "nr_dowodu")
    private String nrDowodu;

    @Column(name = "miejscowosc")
    private String miejscowosc;

    @Column(name = "ulica")
    private String ulica;


    @Override
    public String toString() {
        return "Klient [id=" + id + ", imie=" + imie + ", nazwisko=" + nazwisko + ", nrDowodu=" + nrDowodu
                + ", miejscowosc=" + miejscowosc + ", ulica=" + ulica + "]\n";
    }


    public Client(String imie, String nazwisko, String nrDowodu, String miejscowosc, String ulica) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrDowodu = nrDowodu;
        this.miejscowosc = miejscowosc;
        this.ulica = ulica;
    }

    
}
