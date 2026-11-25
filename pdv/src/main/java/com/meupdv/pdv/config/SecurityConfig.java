package com.meupdv.pdv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           FalhaDeLoginHandler falhaDeLoginHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/images/**", "/css/**").permitAll()
                        .requestMatchers("/produtos/**", "/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/vendas/*/cancelar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clientes/*/excluir").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(falhaDeLoginHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}