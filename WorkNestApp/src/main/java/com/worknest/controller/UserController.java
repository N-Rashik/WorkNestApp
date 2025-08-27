package com.worknest.controller;

import com.worknest.dao.TaskDAO;
import com.worknest.dao.UserDAO;
import com.worknest.model.Task;
import com.worknest.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserDAO userDAO = new UserDAO();
    private final TaskDAO taskDAO = new TaskDAO();
    // Show User Registration Page
    @GetMapping("/register")
    public String showRegister() { 
        return "user-register"; 
    }
    // Handle User Registration
    @PostMapping("/register")
    public String doRegister(@RequestParam String name, 
                             @RequestParam String email, 
                             @RequestParam String password, 
                             Model model) {
    	// Create new User object
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole("USER");
        try {
            userDAO.save(u);
        } catch (Exception e) {
            model.addAttribute("error", "Email already exists");
            return "user-register";
        }
        return "redirect:/user/login";
    }
    // Show User Login Page
    @GetMapping("/login")
    public String showLogin() { 
        return "user-login"; 
    }
    // Handle User Login
    @PostMapping("/login")
    public String doLogin(@RequestParam String email, 
                          @RequestParam String password, 
                          HttpSession session, 
                          Model model) {
        User u = userDAO.findByEmail(email);
        if (u == null || !u.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid credentials");
            return "user-login";
        }
        // Save under  key "user"
        session.setAttribute("user", u);

        // Redirect directly to user dashboard
        return "redirect:/user/dashboard";
    }
    // Handle User Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Admin-only: delete user
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userDAO.delete(id);
        return "redirect:/admin/dashboard";
    }

    // Dashboard with user tasks
    @GetMapping("/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user"); 
        if (loggedUser == null) {
            return "redirect:/user/login";  
        }

        // Fetch tasks for this user
        List<Task> userTasks = taskDAO.findByUser(loggedUser);

        model.addAttribute("user", loggedUser);
        model.addAttribute("tasks", userTasks);

        return "user-dashboard"; 
    }
}
