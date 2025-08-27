<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>WorkNest</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">

</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand fw-bold" href="<c:url value='/'/>">WorkNest</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="nav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/login'/>">Admin</a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value='/user/login'/>">User</a></li>
      </ul>
    </div>
  </div>
</nav>
<div class="container my-4">

<div class="d-flex justify-content-between align-items-center mb-3">
  <h3>Admin Dashboard</h3>
  <div>
    <a class="btn btn-outline-secondary" href="<c:url value='/tasks/new'/>">Create Task</a>
    <a class="btn btn-outline-success" href="<c:url value='/admin/user/new'/>">Create User</a>
    <a class="btn btn-danger" href="<c:url value='/admin/logout'/>">Logout</a>
  </div>
</div>

<!-- Users Section -->
<div class="card shadow-sm mb-4">
  <div class="card-header">Users</div>
  <div class="card-body p-0">
    <table class="table mb-0 table-striped">
      <thead>
        <tr><th>Name</th><th>Email</th><th>Role</th><th style="width:150px;">Action</th></tr>
      </thead>
      <tbody>
        <c:forEach items="${users}" var="u">
          <tr>
            <td>${u.name}</td><td>${u.email}</td><td>${u.role}</td>
            <td class="d-flex gap-1">
              <form method="post" action="<c:url value='/admin/user/delete/${u.userId}'/>" onsubmit="return confirm('Delete user?')">
                <button class="btn btn-sm btn-outline-danger">Delete</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<!-- Tasks Section -->
<div class="card shadow-sm mb-4">
  <div class="card-header">Tasks</div>
  <div class="card-body">

    <h5 class="text-primary">Pending</h5>
    <c:forEach var="t" items="${pendingTasks}">
      <p>
        ${t.title} - Assigned to: ${t.assignedUser.name}
        <a class="btn btn-sm btn-outline-warning ms-2" 
           href="<c:url value='/tasks/editTask?taskId=${t.taskId}'/>">Edit</a>
        <a class="btn btn-sm btn-outline-primary ms-2" 
           href="<c:url value='/admin/task/comments/${t.taskId}'/>">View Comments</a>
      </p>
    </c:forEach>

    <h5 class="text-info mt-3">In Progress</h5>
    <c:forEach var="t" items="${inProgressTasks}">
      <p>
        ${t.title} - Assigned to: ${t.assignedUser.name}
        <a class="btn btn-sm btn-outline-warning ms-2" href="<c:url value='/tasks/editTask?taskId=${t.taskId}'/>">Edit</a>
        <a class="btn btn-sm btn-outline-primary ms-2" 
           href="<c:url value='/admin/task/comments/${t.taskId}'/>">View Comments</a>
      </p>
    </c:forEach>

    <h5 class="text-success mt-3">Completed</h5>
    <c:forEach var="t" items="${completedTasks}">
      <p>
        ${t.title} - Assigned to: ${t.assignedUser.name}
        <a class="btn btn-sm btn-outline-warning ms-2" href="<c:url value='/tasks/editTask?taskId=${t.taskId}'/>">Edit</a>
        <a class="btn btn-sm btn-outline-primary ms-2" 
           href="<c:url value='/admin/task/comments/${t.taskId}'/>">View Comments</a>
      </p>
    </c:forEach>

    <h5 class="text-danger mt-3">Delayed</h5>
    <c:forEach var="t" items="${delayedTasks}">
      <p>
        <span class="fw-bold text-danger">${t.title}</span> - Due: ${t.dueDate}
        <a class="btn btn-sm btn-outline-warning ms-2" href="<c:url value='/tasks/editTask?taskId=${t.taskId}'/>">Edit</a>
        <a class="btn btn-sm btn-outline-primary ms-2" 
           href="<c:url value='/admin/task/comments/${t.taskId}'/>">View Comments</a>
      </p>
    </c:forEach>

  </div>
</div>

</div>
<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
