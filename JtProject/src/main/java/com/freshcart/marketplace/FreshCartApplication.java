package com.freshcart.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class FreshCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreshCartApplication.class, args);
    }
}
