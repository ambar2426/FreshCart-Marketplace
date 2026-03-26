package com.freshcart.marketplace.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartLineItemTest {

    @Test
    void shouldCreateWithConstructor() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1);

        Merchandise product = new Merchandise();
        product.setId(100);

        CartLineItem lineItem = new CartLineItem(cart, product);

        assertNotNull(lineItem.getId(), "Composite key must not be null");
        assertEquals(1, lineItem.getId().getCartId(), "Cart ID should match");
        assertEquals(100, lineItem.getId().getProductId(), "Product ID should match");
        assertEquals(cart, lineItem.getCart(), "Cart reference should match");
        assertEquals(product, lineItem.getProduct(), "Product reference should match");
    }

    @Test
    void shouldSupportSetters() {
        CartLineItem lineItem = new CartLineItem();

        ShoppingCart cart = new ShoppingCart();
        cart.setId(2);

        Merchandise product = new Merchandise();
        product.setId(200);

        lineItem.setCart(cart);
        lineItem.setProduct(product);
        lineItem.setId(new CartLineItemKey(cart.getId(), product.getId()));

        assertEquals(2, lineItem.getId().getCartId());
        assertEquals(200, lineItem.getId().getProductId());
        assertEquals(cart, lineItem.getCart());
        assertEquals(product, lineItem.getProduct());
    }
}
