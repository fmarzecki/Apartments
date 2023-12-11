package projekt.nieruchomosci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{
    User findByEmail(String email);
    List<User> findByBusinessId(int id);
}
