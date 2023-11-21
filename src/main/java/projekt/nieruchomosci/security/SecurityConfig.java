package projekt.nieruchomosci.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import projekt.nieruchomosci.service.UserService;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> 
            configurer
                .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/register/**").permitAll()
                .anyRequest().authenticated() // Every request has to be authenticated
        )
        .formLogin(form -> 
            form
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
        )
        .logout(logout -> logout.permitAll()
        )
        .exceptionHandling(configurer -> 
                configurer
                        .accessDeniedPage("/access-denied")
        );
        return http.build();
    }
}
