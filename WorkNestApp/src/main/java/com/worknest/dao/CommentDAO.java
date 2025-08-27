package com.worknest.dao;

import com.worknest.model.Comment;
import com.worknest.model.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CommentDAO {

    // Save a new comment
    public void save(Comment c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.save(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Update an existing comment
    public void update(Comment c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.update(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Delete a comment
    public void delete(Comment c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.delete(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Fetch all comments for a specific task
    public List<Comment> findByTask(Task task) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Comment> q = session.createQuery(
                "from Comment where task=:t order by timestamp desc", Comment.class
            );
            q.setParameter("t", task);
            return q.list();
        }
    }

    // Fetch all comments in the database
    public List<Comment> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            Query<Comment> q = session.createQuery("from Comment order by timestamp desc", Comment.class);
            return q.list();
        }
    }
    

    
}
