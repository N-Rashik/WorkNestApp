<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Comments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container my-4">

    <h3>Comments for Task: ${task.title}</h3>
    <p><strong>Description:</strong> ${task.description}</p>

    <div class="card mt-3 shadow-sm">
        <div class="card-header">Comments</div>
        <ul class="list-group list-group-flush">
            <c:forEach var="c" items="${comments}">
                <li class="list-group-item">
                    <strong>${c.user.name}</strong> <span class="text-muted">(${c.timestamp})</span><br>
                    ${c.content}
                    <c:if test="${c.seenByAdmin}">
                        <span class="badge bg-success ms-2">Seen</span>
                    </c:if>
                    <c:if test="${!c.seenByAdmin}">
                        <span class="badge bg-danger ms-2">Unseen</span>
                    </c:if>
                </li>
            </c:forEach>
            <c:if test="${empty comments}">
                <li class="list-group-item text-muted text-center">No comments yet.</li>
            </c:if>
        </ul>
    </div>

    <a href="<c:url value='/admin/dashboard'/>" class="btn btn-secondary mt-3">← Back to Dashboard</a>
</div>
</body>
</html>
