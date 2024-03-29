package projekt.nieruchomosci.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "number_of_rooms")
    private int numberOfRooms;

    @Column(name = "area")
    private double area;

    @Column(name = "number_of_bathrooms")
    private int numberOfBathrooms;

    @Column(name = "floor")
    private int floor;

    @Column(name = "has_elevator")
    private boolean hasElevator;

    @Column(name = "rent")
    private double rent;

    @Column(name = "creation_date", insertable = false, updatable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "ID")
    private Business business;

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ApartmentPhoto> photos;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "apartment",fetch = FetchType.LAZY)
    private List<Contract> contract;

    @BatchSize(size = 5)
    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<Defect> defects;
    
    @BatchSize(size = 5)
    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<Mail> mails;

    public Apartment(String photo, String address, int numberOfRooms, double area, int numberOfBathrooms, int floor,
            boolean hasElevator, double rent) {
        this.address = address;
        this.numberOfRooms = numberOfRooms;
        this.area = area;
        this.numberOfBathrooms = numberOfBathrooms;
        this.floor = floor;
        this.hasElevator = hasElevator;
        this.rent = rent;
    }
    
}
