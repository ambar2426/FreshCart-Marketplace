package com.freshcart.marketplace.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CartLineItemKeyTest {

    @Test
    void shouldInitializeViaConstructor() {
        CartLineItemKey key = new CartLineItemKey(1, 2);

        assertEquals(1, key.getCartId());
        assertEquals(2, key.getProductId());
    }

    @Test
    void shouldSupportSetters() {
        CartLineItemKey key = new CartLineItemKey();
        key.setCartId(5);
        key.setProductId(10);

        assertEquals(5, key.getCartId());
        assertEquals(10, key.getProductId());
    }

    @Test
    void shouldBeEqualForSameIds() {
        CartLineItemKey first = new CartLineItemKey(1, 2);
        CartLineItemKey second = new CartLineItemKey(1, 2);
        CartLineItemKey different = new CartLineItemKey(2, 3);

        assertEquals(first, second);
        assertNotEquals(first, different);

        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first.hashCode(), different.hashCode());
    }

    @Test
    void shouldNotEqualNullOrWrongType() {
        CartLineItemKey key = new CartLineItemKey(1, 2);
        assertNotEquals(null, key);
        assertNotEquals("string", key);
    }
}
