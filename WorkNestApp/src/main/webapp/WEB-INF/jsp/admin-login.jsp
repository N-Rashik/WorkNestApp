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
  <div class="col-md-5">
    <div class="card shadow-sm">
      <div class="card-body">
        <h3 class="mb-3">Admin Login</h3>
        <c:if test="${not empty error}">
          <div class="alert alert-danger">${error}</div>
        </c:if>
        <form method="post" action="<c:url value='/admin/login'/>">
          <div class="mb-3"><label>Email</label><input name="email" class="form-control" required></div>
          <div class="mb-3"><label>Password</label><input name="password" type="password" class="form-control" required></div>
          <button class="btn btn-primary w-100">Login</button>
        </form>
        <hr>
        <a href="<c:url value='/admin/register'/>">Create admin account</a>
      </div>
    </div>
  </div>
</div>

</div>
<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
