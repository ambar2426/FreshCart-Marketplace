package com.freshcart.marketplace.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.freshcart.marketplace.domain.entity.ProductGroup;
import com.freshcart.marketplace.infrastructure.persistence.CategoryPersistence;

@Service
public class CategoryManagementService {

    private final CategoryPersistence categoryPersistence;

    public CategoryManagementService(CategoryPersistence categoryPersistence) {
        this.categoryPersistence = categoryPersistence;
    }

    public ProductGroup createCategory(String name) {
        return this.categoryPersistence.create(name);
    }

    public List<ProductGroup> listAllCategories() {
        return this.categoryPersistence.fetchAll();
    }

    public boolean removeCategory(int id) {
        return this.categoryPersistence.removeById(id);
    }

    public ProductGroup renameCategory(int id, String newName) {
        return this.categoryPersistence.modify(id, newName);
    }

    public ProductGroup findCategoryById(int id) {
        return this.categoryPersistence.findById(id);
    }
}
