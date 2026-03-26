package com.freshcart.marketplace.infrastructure.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshcart.marketplace.domain.entity.Customer;

@Repository
public class CustomerPersistence {

    private static final Logger LOGGER = Logger.getLogger(CustomerPersistence.class.getName());

    private final SessionFactory sessionFactory;

    public CustomerPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Customer> fetchAll() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from CUSTOMER", Customer.class).list();
    }

    @Transactional
    public Customer persist(Customer customer) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(customer);
        LOGGER.log(Level.INFO, "Customer persisted with id: {0}", customer.getId());
        return customer;
    }

    @Transactional
    public Customer findByCredentials(String username, String password) {
        Query<Customer> query = sessionFactory.getCurrentSession()
                .createQuery("from CUSTOMER where username = :username", Customer.class);
        query.setParameter("username", username);

        try {
            Customer customer = query.getSingleResult();
            if (password.equals(customer.getPassword())) {
                return customer;
            }
            return new Customer();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Credential lookup failed: {0}", ex.getMessage());
            return new Customer();
        }
    }

    @Transactional
    public boolean existsByUsername(String username) {
        Query<?> query = sessionFactory.getCurrentSession()
                .createQuery("from CUSTOMER where username = :username");
        query.setParameter("username", username);
        return !query.getResultList().isEmpty();
    }

    @Transactional
    public Customer findByUsername(String username) {
        Query<Customer> query = sessionFactory.getCurrentSession()
                .createQuery("from CUSTOMER where username = :username", Customer.class);
        query.setParameter("username", username);

        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Customer not found for username: {0}", username);
            return null;
        }
    }
}
