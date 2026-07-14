package com.berkay.atm_simulation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**").permitAll() // ensure everyone can see these
                        .anyRequest().authenticated() // only logged in users can see these
                )
                //.formLogin(Customizer.withDefaults()) // spring default login page
                .formLogin(form ->  form
                        .loginPage("/login")
                        .permitAll()) // swap default login page with a custom one.
                .logout(Customizer.withDefaults()); // /logout endpoint

        return http.build();
    }
}
