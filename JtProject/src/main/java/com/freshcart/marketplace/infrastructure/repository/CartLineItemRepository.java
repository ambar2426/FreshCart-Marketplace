package com.freshcart.marketplace.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freshcart.marketplace.domain.entity.CartLineItem;
import com.freshcart.marketplace.domain.entity.CartLineItemKey;

public interface CartLineItemRepository extends JpaRepository<CartLineItem, CartLineItemKey> {
}
