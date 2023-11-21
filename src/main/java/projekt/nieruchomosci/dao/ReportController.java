package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Report;

@Repository
public interface ReportController extends JpaRepository<Report, Long> {
    
}
