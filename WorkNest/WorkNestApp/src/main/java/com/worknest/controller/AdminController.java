package com.worknest.controller;

import com.worknest.dao.AdminDAO;
import com.worknest.dao.UserDAO;
import com.worknest.dao.TaskDAO;
import com.worknest.model.Admin;
import com.worknest.model.User;
import com.worknest.model.Task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
	 // DAO objects for database operations
    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TaskDAO taskDAO = new TaskDAO();  // ✅ added

    @GetMapping("/login")
    public String showLogin() {
        return "admin-login";
    }
 // Handles login form submission
    @PostMapping("/login")
    public String doLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminDAO.findByEmailAndPassword(email, password);
        if (admin == null) {
            model.addAttribute("error", "Invalid credentials");
            return "admin-login";
        }
        session.setAttribute("admin", admin);
        return "redirect:/admin/dashboard";
    }
    // When admin visits , this method runs
    @GetMapping("/register")
    public String showRegister() {
        return "admin-register";
    }
    // Handles form submission for registering new admin
    @PostMapping("/register")
    public String doRegister(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        Admin a = new Admin();
        a.setName(name);
        a.setEmail(email);
        a.setPassword(password);
        try {
            adminDAO.save(a);
        } catch (Exception e) {
            model.addAttribute("error", "Email already exists");
            return "admin-register";
        }
        return "redirect:/admin/login";
    }

    // Shows dashboard page after login
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        List<User> users = userDAO.findAll();
        List<Task> tasks = taskDAO.findAll();

        
        java.util.Date today = new java.util.Date();
        for (Task t : tasks) {
            if (t.getDueDate() != null &&
                t.getDueDate().before(today) &&
                !"COMPLETED".equals(t.getStatus())) {
                t.setStatus("DELAYED");
                taskDAO.update(t);
            }
        }

  
        tasks = taskDAO.findAll();

        // Filter by status using streams
        List<Task> pendingTasks = tasks.stream().filter(t -> "PENDING".equals(t.getStatus())).toList();
        List<Task> inProgressTasks = tasks.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).toList();
        List<Task> completedTasks = tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).toList();
        List<Task> delayedTasks = tasks.stream().filter(t -> "DELAYED".equals(t.getStatus())).toList();

        model.addAttribute("users", users);
        model.addAttribute("tasks", tasks);
        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("delayedTasks", delayedTasks);

        return "admin-dashboard";
    }
     // Handles admin logout request
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
