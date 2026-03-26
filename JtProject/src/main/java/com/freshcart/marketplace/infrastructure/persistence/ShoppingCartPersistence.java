package com.freshcart.marketplace.infrastructure.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshcart.marketplace.domain.entity.ShoppingCart;

@Repository
public class ShoppingCartPersistence {

    private final SessionFactory sessionFactory;

    public ShoppingCartPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public ShoppingCart save(ShoppingCart cart) {
        this.sessionFactory.getCurrentSession().save(cart);
        return cart;
    }

    @Transactional
    public List<ShoppingCart> fetchAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from CART", ShoppingCart.class)
                .list();
    }

    @Transactional
    public void modify(ShoppingCart cart) {
        this.sessionFactory.getCurrentSession().update(cart);
    }

    @Transactional
    public void remove(ShoppingCart cart) {
        this.sessionFactory.getCurrentSession().delete(cart);
    }
}
