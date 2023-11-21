package projekt.nieruchomosci.entity;


import java.sql.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "defects")
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date", insertable = false, updatable = false)
    private Date creationDate;

    @Column(name = "is_read")
    private Boolean isRead;
    
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "ID")
    private Apartment apartment;

    public Defect(String description, User user, Apartment apartment) {
        this.description = description;
        this.user = user;
        this.apartment = apartment;
    }
}
