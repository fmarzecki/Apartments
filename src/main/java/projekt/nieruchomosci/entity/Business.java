package projekt.nieruchomosci.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "business")
public class Business {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "LOGO")
    private String logo;

    @OrderBy("isManager ASC")
    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<User> employees;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<Apartment> apartments;

    public int howManyEmployees() {
        return employees.size();
    }

    public int howManyApartments() {
        return apartments.size();
    }

    public Business(String name, String address) {
        this.name = name;
        this.address = address;
    }
    
}
