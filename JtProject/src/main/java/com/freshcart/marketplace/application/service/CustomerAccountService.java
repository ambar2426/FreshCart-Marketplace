package com.freshcart.marketplace.application.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.freshcart.marketplace.domain.entity.Customer;
import com.freshcart.marketplace.infrastructure.persistence.CustomerPersistence;

@Service
public class CustomerAccountService {

    private final CustomerPersistence customerPersistence;

    public CustomerAccountService(CustomerPersistence customerPersistence) {
        this.customerPersistence = customerPersistence;
    }

    public List<Customer> getAllCustomers() {
        return this.customerPersistence.fetchAll();
    }

    public Customer registerCustomer(Customer customer) {
        try {
            return this.customerPersistence.persist(customer);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Registration failed — username may already exist");
        }
    }

    public Customer authenticateCustomer(String username, String password) {
        return this.customerPersistence.findByCredentials(username, password);
    }

    public boolean isUsernameTaken(String username) {
        return this.customerPersistence.existsByUsername(username);
    }

    public Customer findByUsername(String username) {
        return this.customerPersistence.findByUsername(username);
    }

    public void updateProfile(int id, String username, String email, String password, String address) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setAddress(address);
        customer.setRole("ROLE_ADMIN"); // Assuming admin update for now based on original controller context
        this.customerPersistence.persist(customer);
    }
}
