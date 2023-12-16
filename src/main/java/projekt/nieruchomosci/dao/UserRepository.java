package projekt.nieruchomosci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projekt.nieruchomosci.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{
    User findByEmail(String email);
    List<User> findByBusinessId(int id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = ?1")
    User findByEmailWithRolesEagerly(String email);
}
