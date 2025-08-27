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

<div class="row justify-content-center">
  <div class="col-lg-8">
    <div class="card shadow-sm"><div class="card-body">
      <h3 class="mb-3">Create Task</h3>
      <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
      <form method="post" action="<c:url value='/tasks/create'/>">
        <div class="mb-3"><label>Title</label><input name="title" class="form-control" required></div>
        <div class="mb-3"><label>Description</label><textarea name="description" class="form-control" rows="4"></textarea></div>
        <div class="row">
          <div class="col-md-6 mb-3"><label>Start Date</label><input name="startDate" type="date" class="form-control"></div>
          <div class="col-md-6 mb-3"><label>Due Date</label><input name="dueDate" type="date" class="form-control"></div>
        </div>
        <div class="mb-3">
          <label>Assign To</label>
          <select name="assignedUserId" class="form-select" required>
            <c:forEach items="${users}" var="u">
              <option value="${u.userId}">${u.name} (${u.email})</option>
            </c:forEach>
          </select>
        </div>
        <button class="btn btn-primary">Create Task</button>
      </form>
    </div></div>
  </div>
</div>

</div>
<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
