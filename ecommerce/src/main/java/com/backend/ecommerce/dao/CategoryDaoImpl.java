package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao{

    private EntityManager entityManager;

    @Autowired
    public CategoryDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public List<Category> findAll() {
        TypedQuery<Category> theQuery = entityManager.createQuery("from Category",Category.class);
        return theQuery.getResultList();
    }

    @Override
    public Category addCategory(Category category) {
        return entityManager.merge(category);
    }

    @Override
    public void deleteCategory(int id) {
        Category category = entityManager.find(Category.class,id);
        entityManager.remove(category);
    }

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class,id);
    }

    @Override
    public Category findByName(String name) {
        String hqlQuery = "from Category c WHERE c.name = :data";
        TypedQuery<Category> query = entityManager.createQuery(hqlQuery, Category.class);
        query.setParameter("data", name); // Bind the category name value to the named parameter

        return query.getSingleResult();
    }
}
