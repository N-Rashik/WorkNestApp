package com.worknest.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.worknest.model.Task;
import com.worknest.model.User;
import com.worknest.dao.HibernateUtil;

public class TaskDAO {

    // Save a new Task
    public void save(Task t) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(t);
        tx.commit();
        s.close();
    }

    // Update an existing Task (edit functionality)
    public void update(Task t) {
        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.update(t);  // Update task fields
        tx.commit();
        s.close();
    }

    // Find Task by ID
    public Task findById(Long id) {
        Session s = HibernateUtil.getSession();
        Task t = s.get(Task.class, id);
        s.close();
        return t;
    }

    // Fetch all Tasks
    public List<Task> findAll() {
        Session s = HibernateUtil.getSession();
        List<Task> list = s.createQuery("from Task", Task.class).list();
        s.close();
        return list;
    }

    // Fetch tasks by User object
    public List<Task> findByUser(User user) {
        Session s = HibernateUtil.getSession();
        Query<Task> q = s.createQuery("from Task where assignedUser=:u", Task.class);
        q.setParameter("u", user);
        List<Task> list = q.list();
        s.close();
        return list;
    }

    // Fetch tasks by userId
    public List<Task> findTasksByUser(Long userId) {
        Session s = HibernateUtil.getSession();
        Query<Task> q = s.createQuery("FROM Task t WHERE t.assignedUser.userId = :userId", Task.class);
        q.setParameter("userId", userId);
        List<Task> tasks = q.list();
        s.close();
        return tasks;
    }

    // Auto-mark overdue tasks as DELAYED
    public List<Task> findAllWithDelayCheck() {
        List<Task> tasks = findAll();
        Date today = new Date();
        for (Task t : tasks) {
            if (t.getDueDate() != null &&
                t.getDueDate().before(today) &&
                !"COMPLETED".equals(t.getStatus())) {
                t.setStatus("DELAYED");
                update(t);
            }
        }
        return tasks;
    }
}
