package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Repository
public class ProductDaoImpl implements ProductDao{

    private EntityManager entityManager;

    @Autowired
    public ProductDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> theQuery = entityManager.createQuery("from Product",Product.class);
        return theQuery.getResultList();
    }

    @Override
    public Product findById(int theId) {
        return entityManager.find(Product.class,theId);
    }

    @Override
    public Product save(Product product) {
        return entityManager.merge(product);
    }

    @Override
    public void deleteById(int theId) {
        Product product = entityManager.find(Product.class,theId);
        entityManager.remove(product);
    }

    @Override
    public List<Product> findProductByMerchant(int id) {
        TypedQuery<Product> query = entityManager.createQuery(
                "from Product where user.id=:data", Product.class
        );
        query.setParameter("data",id);

        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public List<Product> findProductByCategory(String name) {
        TypedQuery<Product> query = entityManager.createQuery(
                "from Product where category.name=:data", Product.class
        );
        query.setParameter("data",name);

        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public void likeProduct(int productId ,int userId ) {
        Product product = entityManager.find(Product.class,productId);
        Set<Integer> likes = product.getLikes();
        if(likes.contains(userId)){
            likes.remove(userId);
        }else {
            likes.add(userId);
        }
        product.setLikes(likes);
        entityManager.merge(product);
    }
}
