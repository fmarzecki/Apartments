package projekt.nieruchomosci.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "rent")
    private Double rent;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "owner_account_number")
    private String ownerAccountNumber;

    @Column(name = "payment_day")
    private int paymentDay;

    @Column(name = "issigned")
    private boolean isSigned;

    @OneToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "ID")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;

    public Contract(Date startDate, Date endDate, Double rent, Double deposit, String ownerAccountNumber, int paymentDay, Apartment apartment, User user, boolean isSigned) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rent = rent;
        this.deposit = deposit;
        this.ownerAccountNumber = ownerAccountNumber;
        this.paymentDay = paymentDay;
        this.apartment = apartment;
        this.user = user;
        this.isSigned = isSigned;
    }
}
