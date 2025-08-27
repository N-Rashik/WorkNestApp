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
    <h3>Welcome, ${user.name}</h3>
    <a class="btn btn-outline-secondary" href="<c:url value='/user/logout'/>">Logout</a>
  </div>

  <h4 class="mb-3">My Tasks</h4>

  <c:if test="${empty tasks}">
    <div class="alert alert-info">No tasks assigned to you yet.</div>
  </c:if>

  <div class="row">
    <c:forEach items="${tasks}" var="t">
      <div class="col-md-6">
        <div class="card shadow-sm mb-3">
          <div class="card-body">
            <h5 class="card-title">${t.title}</h5>
            <p class="card-text text-muted">${t.description}</p>
            <span class="badge 
                ${t.status == 'COMPLETED' ? 'bg-success' 
                : t.status == 'IN_PROGRESS' ? 'bg-warning text-dark' 
                : t.status == 'DELAYED' ? 'bg-danger' 
                : 'bg-info'}">
              ${t.status}
            </span>
            <div class="mt-3">
              <form class="d-inline" method="post" action="<c:url value='/tasks/status/${t.taskId}'/>">
                <input type="hidden" name="status" value="IN_PROGRESS">
                <button class="btn btn-sm btn-outline-primary">Mark In Progress</button>
              </form>
              <form class="d-inline" method="post" action="<c:url value='/tasks/status/${t.taskId}'/>">
                <input type="hidden" name="status" value="COMPLETED">
                <button class="btn btn-sm btn-success">Mark Completed</button>
              </form>
              <a class="btn btn-sm btn-link" href="<c:url value='/tasks/view/${t.taskId}'/>">Open</a>
            </div>
            <p class="mt-2 text-muted small">
              Start: ${t.startDate} | Due: ${t.dueDate}
            </p>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

</div>

<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
