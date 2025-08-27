<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>WorkNest - Task Details</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom CSS -->
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body class="bg-light">

<!-- Navbar -->
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

<!-- Main Container -->
<div class="container my-4">

    <div class="row">
        <div class="col-lg-8">

            <!-- Task Details -->
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <h3 class="card-title">${task.title}</h3>
                    <p class="card-text">${task.description}</p>
                    <p><strong>Status:</strong> ${task.status}</p>
                </div>
            </div>

            <!-- Comments Section -->
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <h4>Comments</h4>

                    <!-- Show all comments -->
                    <c:if test="${not empty comments}">
                        <ul class="list-group mb-3">
                            
                        </ul>
                    </c:if>

                    <c:if test="${empty comments}">
                        <p>No comments yet. Be the first to comment!</p>
                    </c:if>

                    <!-- Add new comment -->
                    <form action="${pageContext.request.contextPath}/tasks/comment/add" method="post">
                        <input type="hidden" name="taskId" value="${task.taskId}" />
                        <div class="mb-3">
                            <textarea name="content" class="form-control" placeholder="Write your comment..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Post Comment</button>
                    </form>

                </div>
            </div>

        </div>
    </div>

</div>

<!-- Footer -->
<footer class="text-center text-muted py-4">
    © WorkNest
</footer>

</body>
</html>
