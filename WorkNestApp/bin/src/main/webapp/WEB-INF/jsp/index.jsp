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

<div class="p-5 bg-white rounded-3 shadow-sm">
  <h1 class="display-6">Welcome to WorkNest</h1>
  <p class="lead">A clean task management app for teams.</p>
  <a class="btn btn-primary me-2" href="<c:url value='/admin/login'/>">Admin Login</a>
  <a class="btn btn-outline-primary" href="<c:url value='/user/login'/>">User Login</a>
</div>

</div>
<footer class="text-center text-muted py-4">© WorkNest</footer>
</body>
</html>
