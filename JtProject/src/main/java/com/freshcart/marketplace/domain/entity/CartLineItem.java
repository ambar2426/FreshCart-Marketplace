package com.freshcart.marketplace.domain.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "CART_PRODUCT")
public class CartLineItem {

    @EmbeddedId
    private CartLineItemKey id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Merchandise product;

    public CartLineItem() {
    }

    public CartLineItem(ShoppingCart cart, Merchandise product) {
        this.cart = cart;
        this.product = product;
        this.id = new CartLineItemKey(cart.getId(), product.getId());
    }

    public CartLineItemKey getId() {
        return this.id;
    }

    public void setId(CartLineItemKey id) {
        this.id = id;
    }

    public ShoppingCart getCart() {
        return this.cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public Merchandise getProduct() {
        return this.product;
    }

    public void setProduct(Merchandise product) {
        this.product = product;
    }
}
