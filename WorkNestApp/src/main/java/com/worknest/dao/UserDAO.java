package com.worknest.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.worknest.model.User;
import com.worknest.dao.HibernateUtil;

public class UserDAO {

    // Save a new User
    public void save(User u) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(u);
        tx.commit();
        s.close();
    }

    // Update an existing User (edit functionality)
    public void update(User u) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.update(u);  // Will update user fields
        tx.commit();
        s.close();
    }

    // Delete a User
    public void delete(Long id) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        User u = s.get(User.class, id);
        if (u != null) s.delete(u);
        tx.commit();
        s.close();
    }

    // Find User by ID
    public User findById(Long id) {
        Session s = HibernateUtil.getSession();
        User u = s.get(User.class, id);
        s.close();
        return u;
    }

    // Find User by Email
    public User findByEmail(String email) {
        Session s = HibernateUtil.getSession();
        Query<User> q = s.createQuery("from User where email=:e", User.class);
        q.setParameter("e", email);
        User u = q.uniqueResult();
        s.close();
        return u;
    }

    // Fetch all Users
    public List<User> findAll() {
        Session s = HibernateUtil.getSession();
        List<User> list = s.createQuery("from User", User.class).list();
        s.close();
        return list;
    }
}
