package com.freshcart.marketplace.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.freshcart.marketplace.application.service.CustomerAccountService;
import com.freshcart.marketplace.domain.entity.Customer;

@Configuration
public class WebSecurityConfig {

    private final CustomerAccountService accountService;

    public WebSecurityConfig(CustomerAccountService accountService) {
        this.accountService = accountService;
    }

    @Configuration
    @Order(1)
    public static class AdminSecurityChain {

        @Bean
        SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(new AntPathRequestMatcher("/admin/login")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                )
                .formLogin(form -> form
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/loginvalidate")
                    .successHandler((req, res, auth) -> res.sendRedirect("/admin/"))
                    .failureHandler((req, res, ex) -> res.sendRedirect("/admin/login?error=true"))
                )
                .logout(logout -> logout
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login")
                    .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
                .csrf(csrf -> csrf.disable());

            return http.build();
        }
    }

    @Configuration
    @Order(2)
    public static class CustomerSecurityChain {

        @Bean
        SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth -> auth
                    .antMatchers("/", "/user/products", "/catalog-images/**", "/images/**", "/static/**", "/login", "/register", "/newuserregister", "/test", "/test2").permitAll()
                    .antMatchers("/**").hasRole("USER")
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/userloginvalidate")
                    .successHandler((req, res, auth) -> res.sendRedirect("/"))
                    .failureHandler((req, res, ex) -> res.sendRedirect("/login?error=true"))
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
                .csrf(csrf -> csrf.disable());

            return http.build();
        }
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Customer customer = this.accountService.findByUsername(username);
            if (customer == null) {
                throw new UsernameNotFoundException("No account found for: " + username);
            }

            String springRole = customer.getRole().equals("ROLE_ADMIN") ? "ADMIN" : "USER";

            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .passwordEncoder(raw -> passwordEncoder().encode(raw))
                    .password(customer.getPassword())
                    .roles(springRole)
                    .build();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
