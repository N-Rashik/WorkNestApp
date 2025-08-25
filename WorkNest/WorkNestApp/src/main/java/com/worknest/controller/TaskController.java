package com.worknest.controller;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.worknest.dao.TaskDAO;
import com.worknest.dao.UserDAO;
import com.worknest.model.Task;
import com.worknest.model.User;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDAO taskDAO = new TaskDAO();
    private final UserDAO userDAO = new UserDAO();
    // Show New Task Form 
    @GetMapping("/new")
    public String newTask(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("users", userDAO.findAll());
        return "task-form";
    }
    // Handle Task Creation
    @PostMapping("/create")
    public String createTask(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String startDate,
                             @RequestParam String dueDate,
                             @RequestParam Long assignedUserId,
                             Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        try {
        	// Validate assigned user exists
            User user = userDAO.findById(assignedUserId);
            if (user == null) {
                model.addAttribute("error", "Invalid user selection.");
                model.addAttribute("users", userDAO.findAll());
                return "task-form";
            }
         // Create new Task object
            Task t = new Task();
            t.setTitle(title);
            t.setDescription(description);
            t.setAssignedUser(user);
            t.setStatus("PENDING");

           
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (startDate != null && !startDate.isEmpty()) {
                t.setStartDate(new java.sql.Date(sdf.parse(startDate).getTime()));
            }
            if (dueDate != null && !dueDate.isEmpty()) {
                t.setDueDate(new java.sql.Date(sdf.parse(dueDate).getTime()));
            }

            // Generate unique slug
            String slugBase = toSlug(title);
            String slug = slugBase;
            int i = 1;
            // Ensure slug is unique in database
            while (taskDAO.findBySlug(slug) != null) {
                slug = slugBase + "-" + i++;
            }
            t.setSlug(slug);

            taskDAO.save(t);

            return "redirect:/tasks/view/" + slug;
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create task: " + e.getMessage());
            model.addAttribute("users", userDAO.findAll()); // Always reload users
            return "task-form";
        }
    }
    // View Task Details by Slug 
    @GetMapping("/view/{slug}")
    public String view(@PathVariable String slug, Model model, HttpSession session) {
        Task t = taskDAO.findBySlug(slug);
        if (t == null) return "redirect:/";
        checkAndUpdateDelay(t); // auto-mark delayed if overdue
        model.addAttribute("task", t);
        model.addAttribute("isAdmin", session.getAttribute("admin") != null);
        return "task-view";
    }
    // Show Logged-in User's Tasks
    @GetMapping("/my")
    public String myTasks(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/user/login";
        List<Task> tasks = taskDAO.findByUser(u);
        tasks.forEach(this::checkAndUpdateDelay);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", u);
        return "user-dashboard";
    }
    // Update Task Status
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/user/login";
        Task t = taskDAO.findById(id);
        if (t != null && t.getAssignedUser().getUserId().equals(u.getUserId())) {
            t.setStatus(status);
            taskDAO.update(t);
        }
        return "redirect:/tasks/my";
    }

    // ========== Utility Methods ==========

    /** Converts title to slug */
    private static String toSlug(String input) {
        String nowhitespace = Pattern.compile("\\s").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        return Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
    }

    /** Check if task is overdue -> mark as DELAYED */
    private void checkAndUpdateDelay(Task t) {
        if (t.getDueDate() != null && !"COMPLETED".equals(t.getStatus())) {
            Date today = new Date();
            if (t.getDueDate().before(today) && !"DELAYED".equals(t.getStatus())) {
                t.setStatus("DELAYED");
                taskDAO.update(t);
            }
        }
    }
}
