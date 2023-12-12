package projekt.nieruchomosci.service;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import projekt.nieruchomosci.dao.RoleRepository;
import projekt.nieruchomosci.dao.UserRepository;
import projekt.nieruchomosci.entity.Role;
import projekt.nieruchomosci.entity.User;
import projekt.nieruchomosci.user.WebUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void UserService_SaveUser_ReturnUserNotNull() {
        // Arrange
        User user = User.builder()
                .email("ela@wp.pl")
                .lastName("Kowal")
                .firstName("Ela")
                .password("test123")
                .build();

        WebUser webUser = WebUser.builder()
                .email("ela@wp.pl")
                .lastName("Kowal")
                .firstName("Ela")
                .password("test123")
                .build();

        Role role = new Role("ROLE_CLIENT");

        // Act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(roleRepository.findRoleByName(Mockito.anyString())).thenReturn(role);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("mockedEncodedPassword");

        WebUser savedUser = userServiceImpl.save(webUser);

        // Asser
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(webUser.getEmail());
        // Assertions.assertThat(savedUser.getPassword()).isNotEqualTo(webUser.getPassword());
    }
    
}
