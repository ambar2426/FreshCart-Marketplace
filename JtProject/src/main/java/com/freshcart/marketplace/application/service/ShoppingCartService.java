package com.freshcart.marketplace.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.freshcart.marketplace.domain.entity.ShoppingCart;
import com.freshcart.marketplace.infrastructure.persistence.ShoppingCartPersistence;

@Service
public class ShoppingCartService {

    private final ShoppingCartPersistence cartPersistence;

    public ShoppingCartService(ShoppingCartPersistence cartPersistence) {
        this.cartPersistence = cartPersistence;
    }

    public ShoppingCart createCart(ShoppingCart cart) {
        return this.cartPersistence.save(cart);
    }

    public List<ShoppingCart> listAllCarts() {
        return this.cartPersistence.fetchAll();
    }

    public void modifyCart(ShoppingCart cart) {
        this.cartPersistence.modify(cart);
    }

    public void removeCart(ShoppingCart cart) {
        this.cartPersistence.remove(cart);
    }
}
