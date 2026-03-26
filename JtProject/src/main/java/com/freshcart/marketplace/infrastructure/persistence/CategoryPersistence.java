package com.freshcart.marketplace.infrastructure.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshcart.marketplace.domain.entity.ProductGroup;

@Repository
public class CategoryPersistence {

    private final SessionFactory sessionFactory;

    public CategoryPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public ProductGroup create(String name) {
        ProductGroup group = new ProductGroup();
        group.setName(name);
        this.sessionFactory.getCurrentSession().saveOrUpdate(group);
        return group;
    }

    @Transactional
    public List<ProductGroup> fetchAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from CATEGORY", ProductGroup.class)
                .list();
    }

    @Transactional
    public boolean removeById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        ProductGroup existing = session.get(ProductGroup.class, id);
        if (existing != null) {
            session.delete(existing);
            return true;
        }
        return false;
    }

    @Transactional
    public ProductGroup modify(int id, String name) {
        ProductGroup group = this.sessionFactory.getCurrentSession().get(ProductGroup.class, id);
        group.setName(name);
        this.sessionFactory.getCurrentSession().update(group);
        return group;
    }

    @Transactional
    public ProductGroup findById(int id) {
        return this.sessionFactory.getCurrentSession().get(ProductGroup.class, id);
    }
}
