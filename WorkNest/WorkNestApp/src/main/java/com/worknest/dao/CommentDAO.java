package com.worknest.dao;

import com.worknest.model.Comment;
import com.worknest.model.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class CommentDAO {
	// Save Comment to Database
    public void save(Comment c) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(c);
        tx.commit();
        s.close();
    }
    // Fetch Comments for a Specific Task
    public List<Comment> findByTask(Task task) {
        Session s = HibernateUtil.getSession();
        Query<Comment> q = s.createQuery("from Comment where task=:t order by timestamp desc", Comment.class);
        q.setParameter("t", task);
        List<Comment> list = q.list();
        s.close();
        return list;
    }
}
