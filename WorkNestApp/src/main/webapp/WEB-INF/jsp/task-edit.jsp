<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Task</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container my-4">
    <h3>Edit Task</h3>

    <!-- Show error if exists -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Form for editing task -->
    <form method="post" action="<c:url value='/tasks/update'/>">
        <!-- Hidden field for taskId -->
        <input type="hidden" name="taskId" value="${task.taskId}">

        <div class="mb-3">
            <label>Title</label>
            <input type="text" class="form-control" name="title" value="${task.title}" required>
        </div>

        <div class="mb-3">
            <label>Description</label>
            <textarea class="form-control" name="description" rows="4">${task.description}</textarea>
        </div>

        <div class="mb-3">
            <label>Start Date</label>
            <input type="date" class="form-control" name="startDate" 
                   value="<c:out value='${task.startDate}'/>">
        </div>

        <div class="mb-3">
            <label>Due Date</label>
            <input type="date" class="form-control" name="dueDate" 
                   value="<c:out value='${task.dueDate}'/>">
        </div>

        <div class="mb-3">
            <label>Assign To</label>
            <select class="form-control" name="assignedUserId">
                <c:forEach var="u" items="${users}">
                    <option value="${u.userId}" 
                        <c:if test="${u.userId == task.assignedUser.userId}">selected</c:if>>
                        ${u.name} (${u.email})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label>Status</label>
            <select class="form-control" name="status">
                <option value="PENDING" <c:if test="${task.status=='PENDING'}">selected</c:if>>PENDING</option>
                <option value="IN_PROGRESS" <c:if test="${task.status=='IN_PROGRESS'}">selected</c:if>>IN_PROGRESS</option>
                <option value="COMPLETED" <c:if test="${task.status=='COMPLETED'}">selected</c:if>>COMPLETED</option>
                <option value="DELAYED" <c:if test="${task.status=='DELAYED'}">selected</c:if>>DELAYED</option>
            </select>
        </div>

        <button class="btn btn-success" type="submit">Update Task</button>
        <a class="btn btn-secondary" href="<c:url value='/admin/dashboard'/>">Cancel</a>
    </form>
</div>
</body>
</html>
