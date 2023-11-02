package projekt.nieruchomosci.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "klienci")
public class Klient {
    
    @Id
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

    

}
