package projekt.nieruchomosci.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "report_date", insertable = false)
    private Date reportDate;

    @Column(name = "number_of_apartments")
    private int numberOfApartments;

    @Column(name = "number_of_contracts")
    private int numberOfContracts;

    @Column(name = "number_of_employees")
    private int numberOfEmployees;

    @Column(name = "earnings")
    private double earnings;

    @Column(name = "expenses")
    private double expenses;

    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "ID", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "ID", nullable = true)
    private User manager;

}
