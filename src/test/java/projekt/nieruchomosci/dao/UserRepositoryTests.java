package projekt.nieruchomosci.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projekt.nieruchomosci.entity.User;

@DataJpaTest
public class UserRepositoryTests {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveUser_ReturnSavedUser() {

        //Arrange
        User user = User.builder()
            .email("ela@wp.pl")
            .lastName("Kowal")
            .firstName("Ela")
            .password("test123")
            .build();

        //Act
        User savedUser = userRepository.save(user);

        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_FindUserByEmail_ReturnUser() {
        
        //Arrange
        User user = User.builder()
            .email("ela@wp.pl")
            .build();
            
        //Act
        userRepository.save(user);
        User foundUser = userRepository.findByEmail(user.getEmail());

        //Assert
        Assertions.assertThat(foundUser).isNotNull();
    }

}
