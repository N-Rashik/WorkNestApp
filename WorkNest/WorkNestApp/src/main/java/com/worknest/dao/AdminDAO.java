package com.worknest.dao;

import com.worknest.model.Admin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AdminDAO {
	// Save Admin to Database
    public void save(Admin admin) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(admin);
        tx.commit();
        s.close();
    }
    // Find Admin by Email and Password
    public Admin findByEmailAndPassword(String email, String password) {
        Session s = HibernateUtil.getSession();
        Query<Admin> q = s.createQuery("from Admin where email=:e and password=:p", Admin.class);
        q.setParameter("e", email);
        q.setParameter("p", password);
        Admin a = q.uniqueResult();
        s.close();
        return a;
    }
}
