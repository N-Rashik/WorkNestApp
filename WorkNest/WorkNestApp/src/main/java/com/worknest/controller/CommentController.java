package com.worknest.controller;

import com.worknest.dao.CommentDAO;
import com.worknest.dao.TaskDAO;
import com.worknest.model.Comment;
import com.worknest.model.Task;
import com.worknest.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentDAO commentDAO = new CommentDAO();
    private final TaskDAO taskDAO = new TaskDAO();
    // Add Comment to a Task
    @PostMapping("/add/{taskId}")
    public String add(@PathVariable Long taskId, @RequestParam String content, HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/user/login";
        Task t = taskDAO.findById(taskId);
        if (t != null) {
            Comment c = new Comment();
            c.setTask(t);
            c.setUser(u);
            c.setContent(content);
            commentDAO.save(c);
            return "redirect:/tasks/view/" + t.getSlug();
        }
        return "redirect:/tasks/my";
    }
}
