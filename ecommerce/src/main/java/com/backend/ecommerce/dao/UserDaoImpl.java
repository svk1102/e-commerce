package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{
@Autowired
private EntityManager entityManager;

@Autowired
public UserDaoImpl(EntityManager theEntityManager){
    entityManager = theEntityManager;
}

    @Override
    public List<User> findAll() {
        TypedQuery<User> theQuery = entityManager.createQuery("from User",User.class);
        return theQuery.getResultList();
    }

    @Override
    public User findById(int theId) {
        return entityManager.find(User.class,theId);
    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void deleteById(int theId) {
        User user = entityManager.find(User.class,theId);
        entityManager.remove(user);
    }
    @Override
    public User findByEmail(String emailAddress) {
        String hqlQuery = "from User u WHERE u.email = :data";
        TypedQuery<User> query = entityManager.createQuery(hqlQuery, User.class);
        query.setParameter("data", emailAddress); // Bind the email address value to the named parameter

        return query.getSingleResult();
    }

    @Override
    public List<User> findAllMerchants() {
        String sqlQuery = "from User u WHERE u.role = 2";
        TypedQuery<User> query = entityManager.createQuery(sqlQuery,User.class);
        return query.getResultList();
    }


}
