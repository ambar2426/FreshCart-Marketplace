package com.freshcart.marketplace.infrastructure.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshcart.marketplace.domain.entity.Merchandise;

@Repository
public class ProductPersistence {

    private final SessionFactory sessionFactory;

    public ProductPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Merchandise> fetchAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from PRODUCT", Merchandise.class)
                .list();
    }

    @Transactional
    public Merchandise save(Merchandise merchandise) {
        this.sessionFactory.getCurrentSession().save(merchandise);
        return merchandise;
    }

    @Transactional
    public Merchandise findById(int id) {
        return this.sessionFactory.getCurrentSession().get(Merchandise.class, id);
    }

    @Transactional
    public Merchandise modify(Merchandise merchandise) {
        this.sessionFactory.getCurrentSession().update(merchandise);
        return merchandise;
    }

    @Transactional
    public boolean removeById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Merchandise existing = session.get(Merchandise.class, id);
        if (existing != null) {
            session.delete(existing);
            return true;
        }
        return false;
    }
}
