package projekt.nieruchomosci.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import projekt.nieruchomosci.dao.RoleRepository;
import projekt.nieruchomosci.dao.UserRepository;
import projekt.nieruchomosci.entity.Role;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.user.WebUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

	public User findByEmail(String userName) {
		// check the database if the user already exists
		return userRepository.findByEmail(userName);
	}

	@Override
	public WebUser save(WebUser webUser) {
		User user = new User();

		// assign user details to the user object
		user.setEmail(webUser.getEmail());
		user.setPassword(passwordEncoder.encode(webUser.getPassword()));
		user.setFirstName(webUser.getFirstName());
		user.setLastName(webUser.getLastName());
		
		// give user default role of "client"
		user.setRoles(Arrays.asList(roleRepository.findRoleByName("ROLE_CLIENT")));

		// save user in the database
		User newUser =  userRepository.save(user);

		WebUser userResponse = new WebUser();
		userResponse.setEmail(newUser.getEmail());
		userResponse.setFirstName(newUser.getFirstName());
		userResponse.setLastName(newUser.getLastName());
		userResponse.setPassword(newUser.getPassword());
		return userResponse;
	}

	public void update(User user){
		userRepository.save(user);
	}

	private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role tempRole : roles) {
			SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
			authorities.add(tempAuthority);
		}

		return authorities;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				authorities);
	}

    @Override
    public List<User> findByBusinessId(int id) {
        return userRepository.findByBusinessId(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
