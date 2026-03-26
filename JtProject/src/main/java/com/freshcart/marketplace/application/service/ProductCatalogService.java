package com.freshcart.marketplace.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.freshcart.marketplace.domain.entity.Merchandise;
import com.freshcart.marketplace.infrastructure.persistence.ProductPersistence;

@Service
public class ProductCatalogService {

    private final ProductPersistence productPersistence;

    public ProductCatalogService(ProductPersistence productPersistence) {
        this.productPersistence = productPersistence;
    }

    public List<Merchandise> listAllProducts() {
        return this.productPersistence.fetchAll();
    }

    public Merchandise createProduct(Merchandise merchandise) {
        return this.productPersistence.save(merchandise);
    }

    public Merchandise findProductById(int id) {
        return this.productPersistence.findById(id);
    }

    public Merchandise updateProduct(int id, Merchandise merchandise) {
        merchandise.setId(id);
        return this.productPersistence.modify(merchandise);
    }

    public boolean removeProduct(int id) {
        return this.productPersistence.removeById(id);
    }
}
