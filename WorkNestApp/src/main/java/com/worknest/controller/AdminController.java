package com.worknest.controller;

import com.worknest.dao.AdminDAO;
import com.worknest.dao.UserDAO;
import com.worknest.dao.TaskDAO;
import com.worknest.dao.CommentDAO;
import com.worknest.model.Admin;
import com.worknest.model.User;
import com.worknest.model.Task;
import com.worknest.model.Comment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TaskDAO taskDAO = new TaskDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    // Admin login page
    @GetMapping("/login")
    public String showLogin() {
        return "admin-login";
    }

    // Admin login action
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        Admin admin = adminDAO.findByEmailAndPassword(email, password);
        if (admin == null) {
            model.addAttribute("error", "Invalid credentials");
            return "admin-login";
        }
        session.setAttribute("admin", admin);
        return "redirect:/admin/dashboard";
    }

    // Admin registration page
    @GetMapping("/register")
    public String showRegister() {
        return "admin-register";
    }

    // Admin registration action
    @PostMapping("/register")
    public String doRegister(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             Model model) {
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

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        List<User> users = userDAO.findAll();
        List<Task> tasks = taskDAO.findAllWithDelayCheck();

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

    // New User Form
    @GetMapping("/user/new")
    public String newUserForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        return "user-form";
    }

    // Create User
    @PostMapping("/user/create")
    public String createUser(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role,
                             Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        try {
            User existing = userDAO.findByEmail(email);
            if (existing != null) {
                model.addAttribute("error", "Email already exists.");
                return "user-form";
            }
            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(password);
            u.setRole(role);
            userDAO.save(u);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create user: " + e.getMessage());
            return "user-form";
        }
    }

    // Edit User Form
    @GetMapping("/user/edit")
    public String editUserForm(@RequestParam Long userId, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        User u = userDAO.findById(userId);
        if (u == null) return "redirect:/admin/dashboard";
        model.addAttribute("user", u);
        return "user-edit";
    }

    // Update User
    @PostMapping("/user/update")
    public String updateUser(@RequestParam Long userId,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role,
                             HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        User u = userDAO.findById(userId);
        if (u == null) return "redirect:/admin/dashboard";

        u.setName(name);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        userDAO.update(u);

        return "redirect:/admin/dashboard";
    }

    // Delete User
    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        userDAO.delete(userId);
        return "redirect:/admin/dashboard";
    }

    // View Task Comments
    @GetMapping("/task/comments/{taskId}")
    public String viewTaskComments(@PathVariable Long taskId, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        Task task = taskDAO.findById(taskId);
        if (task == null) return "redirect:/admin/dashboard";

        // Fetch comments
        List<Comment> comments = commentDAO.findByTask(task);

        // Mark all comments as seen by admin
        for (Comment c : comments) {
            if (!c.isSeenByAdmin()) {
                c.setSeenByAdmin(true);
                commentDAO.update(c);
            }
        }

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);

        return "admin-task-comments"; 
    }
}
