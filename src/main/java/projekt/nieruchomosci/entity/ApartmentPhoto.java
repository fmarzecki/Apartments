package projekt.nieruchomosci.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "apartment_photos")
public class ApartmentPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "ID")
    private Apartment apartment;

    public ApartmentPhoto(String photo, Apartment apartment) {
        this.photo = photo;
        this.apartment = apartment;
    }

}
