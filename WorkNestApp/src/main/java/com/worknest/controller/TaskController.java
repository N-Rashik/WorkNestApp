package com.worknest.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.worknest.dao.CommentDAO;
import com.worknest.dao.TaskDAO;
import com.worknest.dao.UserDAO;
import com.worknest.model.Comment;
import com.worknest.model.Task;
import com.worknest.model.User;

@Controller
@RequestMapping("/tasks")
public class TaskController {
	private final TaskDAO taskDAO = new TaskDAO();
	private final UserDAO userDAO = new UserDAO();

	// --- Show New Task Form (Admin) ---
	@GetMapping("/new")
	public String newTask(HttpSession session, Model model) {
		if (session.getAttribute("admin") == null)
			return "redirect:/admin/login";
		model.addAttribute("users", userDAO.findAll());
		return "task-form";
	}

	// --- Handle Task Creation (Admin) ---
	@PostMapping("/create")
	public String createTask(@RequestParam String title, @RequestParam String description,
			@RequestParam String startDate, @RequestParam String dueDate, @RequestParam Long assignedUserId,
			Model model, HttpSession session) {
		if (session.getAttribute("admin") == null)
			return "redirect:/admin/login";
		try {
			User user = userDAO.findById(assignedUserId);
			if (user == null) {
				model.addAttribute("error", "Invalid user selection.");
				model.addAttribute("users", userDAO.findAll());
				return "task-form";
			}
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
			taskDAO.save(t);
			return "redirect:/admin/dashboard";

		} catch (Exception e) {
			model.addAttribute("error", "Failed to create task: " + e.getMessage());
			model.addAttribute("users", userDAO.findAll());
			return "task-form";
		}
	}

	// --- View Task Details by ID ---
	@GetMapping("/view/{taskId}")
	public String view(@PathVariable Long taskId, Model model, HttpSession session) {
		Task t = taskDAO.findById(taskId);
		if (t == null)
			return "redirect:/tasks/my";
		checkAndUpdateDelay(t);
		CommentDAO commentDAO = new CommentDAO();
		List<Comment> comments = commentDAO.findByTask(t);
		model.addAttribute("task", t);
		model.addAttribute("comments", comments);
		model.addAttribute("isAdmin", session.getAttribute("admin") != null);
		return "task-view";
	}

	// --- Show Logged-in User's Tasks ---
	@GetMapping("/my")
	public String myTasks(HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		if (u == null)
			return "redirect:/user/login";
		List<Task> tasks = taskDAO.findByUser(u);
		tasks.forEach(this::checkAndUpdateDelay);
		model.addAttribute("tasks", tasks);
		model.addAttribute("user", u);
		return "user-dashboard";
	}

	// --- Update Task Status (User) ---
	@PostMapping("/status/{id}")
	public String updateStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
		User u = (User) session.getAttribute("user");
		if (u == null)
			return "redirect:/user/login";
		Task t = taskDAO.findById(id);
		if (t != null && t.getAssignedUser().getUserId().equals(u.getUserId())) {
			t.setStatus(status);
			taskDAO.update(t);
		}
		return "redirect:/tasks/my";
	}

	// --- Show Edit Task Form (Admin) ---
	@GetMapping("/editTask")
	public String editTaskForm(@RequestParam Long taskId, Model model, HttpSession session) {
		if (session.getAttribute("admin") == null)
			return "redirect:/admin/login";
		Task t = taskDAO.findById(taskId);
		if (t == null)
			return "redirect:/admin/dashboard";
		model.addAttribute("task", t);
		model.addAttribute("users", userDAO.findAll());
		return "task-edit";
	}

	// --- Handle Task Update (Admin) ---
	@PostMapping("/update")
	public String updateTask(@RequestParam Long taskId, @RequestParam String title, @RequestParam String description,
			@RequestParam String startDate, @RequestParam String dueDate, @RequestParam Long assignedUserId,
			@RequestParam String status, Model model, HttpSession session) {
		if (session.getAttribute("admin") == null)
			return "redirect:/admin/login";
		try {
			Task t = taskDAO.findById(taskId);
			if (t == null)
				return "redirect:/admin/dashboard";
			User user = userDAO.findById(assignedUserId);
			if (user == null) {
				model.addAttribute("error", "Invalid user selection.");
				model.addAttribute("task", t);
				model.addAttribute("users", userDAO.findAll());
				return "task-edit";
			}
			t.setTitle(title);
			t.setDescription(description);
			t.setAssignedUser(user);
			t.setStatus(status);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (startDate != null && !startDate.isEmpty()) {
				t.setStartDate(new java.sql.Date(sdf.parse(startDate).getTime()));
			}
			if (dueDate != null && !dueDate.isEmpty()) {
				t.setDueDate(new java.sql.Date(sdf.parse(dueDate).getTime()));
			}
			taskDAO.update(t);
			return "redirect:/tasks/view/" + t.getTaskId();

		} catch (Exception e) {
			model.addAttribute("error", "Failed to update task: " + e.getMessage());
			return "task-edit";
		}
	}

	// --- Add Comment (User) ---
	@PostMapping("/comment/add")
	public String addComment(@RequestParam Long taskId, @RequestParam String content, HttpSession session,
			Model model) {
		User u = (User) session.getAttribute("user");
		if (u == null)
			return "redirect:/user/login";
		Task t = taskDAO.findById(taskId);
		if (t == null)
			return "redirect:/tasks/my";
		try {
			Comment c = new Comment();
			c.setTask(t);
			c.setUser(u);
			c.setContent(content);
			c.setTimestamp(new Date());
			CommentDAO commentDAO = new CommentDAO();
			commentDAO.save(c);
			return "redirect:/tasks/view/" + taskId;
		} catch (Exception e) {
			model.addAttribute("error", "Failed to add comment: " + e.getMessage());
			CommentDAO commentDAO = new CommentDAO();
			List<Comment> comments = commentDAO.findByTask(t);
			model.addAttribute("task", t);
			model.addAttribute("comments", comments);
			model.addAttribute("isAdmin", session.getAttribute("admin") != null);
			return "task-view";
		}
	}

	// --- Check and Update Overdue Tasks ---
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
