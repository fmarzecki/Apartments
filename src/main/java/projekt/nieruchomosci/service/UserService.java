package projekt.nieruchomosci.service;


import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.user.WebUser;

public interface UserService extends UserDetailsService {

	User findByEmail(String userName);
	List<User> findByBusinessId(int id);
	List<User> findAll();
	WebUser save(WebUser webUser);
	void update(User user);

}
