package com.freshcart.marketplace.infrastructure.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshcart.marketplace.domain.entity.CartLineItem;
import com.freshcart.marketplace.domain.entity.Merchandise;

@Repository
public class CartItemPersistence {

    private final SessionFactory sessionFactory;

    public CartItemPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public CartLineItem save(CartLineItem lineItem) {
        this.sessionFactory.getCurrentSession().save(lineItem);
        return lineItem;
    }

    @Transactional
    public List<CartLineItem> fetchAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from CartLineItem", CartLineItem.class)
                .list();
    }

    @Transactional
    public List<Merchandise> findProductsByCartId(Integer cartId) {
        String productIdQuery = "SELECT product_id FROM cart_product WHERE cart_id = :cartId";
        List<Integer> productIds = this.sessionFactory.getCurrentSession()
                .createNativeQuery(productIdQuery)
                .setParameter("cartId", cartId)
                .list();

        String merchandiseQuery = "SELECT * FROM product WHERE id IN (:productIds)";
        return this.sessionFactory.getCurrentSession()
                .createNativeQuery(merchandiseQuery, Merchandise.class)
                .setParameterList("productIds", productIds)
                .list();
    }

    @Transactional
    public void modify(CartLineItem lineItem) {
        this.sessionFactory.getCurrentSession().update(lineItem);
    }

    @Transactional
    public void remove(CartLineItem lineItem) {
        this.sessionFactory.getCurrentSession().delete(lineItem);
    }
}
