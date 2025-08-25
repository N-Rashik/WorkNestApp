<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>WorkNest</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">

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

<div class="row">
  <div class="col-lg-8">
    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <h3>${task.title}</h3>
        <p class="text-muted">${task.description}</p>
        <span class="badge bg-info">${task.status}</span>
        <p class="mt-2"><strong>Assigned To:</strong> ${task.assignedUser.name} (${task.assignedUser.email})</p>
      </div>
    </div>

    <div class="card shadow-sm">
      <div class="card-header">Add Comment</div>
      <div class="card-body">
        <form method="post" action="<c:url value='/comments/add/${task.taskId}'/>">
          <textarea name="content" class="form-control mb-2" rows="3" required></textarea>
          <button class="btn btn-primary">Post</button>
        </form>
      </div>
    </div>
  </div>
</div>

</div>
<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
