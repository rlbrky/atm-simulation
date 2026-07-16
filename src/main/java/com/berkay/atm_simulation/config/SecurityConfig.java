package com.berkay.atm_simulation.config;

import com.berkay.atm_simulation.security.LoginFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final LoginFailureHandler failureHandler;

    public SecurityConfig(LoginFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**").permitAll() // ensure everyone can see these
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin only access
                        .anyRequest().authenticated() // only logged in users can see these
                )
                //.formLogin(Customizer.withDefaults()) // spring default login page
                .formLogin(form ->  form
                        .loginPage("/login")
                        .failureHandler(failureHandler)
                        .permitAll()) // swap default login page with a custom one.
                .logout(Customizer.withDefaults()); // /logout endpoint

        return http.build();
    }
}
