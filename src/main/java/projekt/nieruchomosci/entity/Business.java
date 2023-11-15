package projekt.nieruchomosci.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    //  OneToMany relationships are lazy by default
    //  This means that the relationship won't be loaded right away,
    //  but only when and if actually needed.
    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<User> employees;

    public int howManyEmployees() {
        return employees.size();
    }

    public Business(String name, String address) {
        this.name = name;
        this.address = address;
    }
    
}
