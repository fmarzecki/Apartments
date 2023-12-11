package projekt.nieruchomosci.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "IS_MANAGER")
    private Boolean isManager;

    @ManyToOne
    @JoinColumn(name = "ID_FIRMY", referencedColumnName = "ID")
    private Business business;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    // Apartamenty pracownika
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Apartment> apartments;

    // Kontrakty klienta
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Contract> contracts;

    // Usterki klienta
    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Defect> defects;

    // Maile klienta
    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Mail> mails;
    
    @OrderBy("reportDate DESC")
    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
    private List<Report> reports;

    public User(String email, String password, boolean enabled) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String password, boolean enabled,
                Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public int howManyContracts() {
        int contracts = 0;
        for (Apartment apartment : apartments) {
            if (apartment.getContract() != null) {
                contracts +=1;
            }
        }
        return contracts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}