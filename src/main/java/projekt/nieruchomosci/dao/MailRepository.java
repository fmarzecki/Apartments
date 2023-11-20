package projekt.nieruchomosci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekt.nieruchomosci.entity.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long>{
    List<Mail> findAllByIsReadFalse();
}
