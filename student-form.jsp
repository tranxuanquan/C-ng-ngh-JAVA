<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${formAction == 'update'}">Sửa sinh viên</c:when>
            <c:otherwise>Thêm sinh viên</c:otherwise>
        </c:choose>
    </title>
</head>
<body>
<h2>
    <c:choose>
        <c:when test="${formAction == 'update'}">Sửa sinh viên</c:when>
        <c:otherwise>Thêm sinh viên</c:otherwise>
    </c:choose>
</h2>

<form action="${pageContext.request.contextPath}/students" method="post">
    <input type="hidden" name="action" value="${formAction}">

    <label>Mã sinh viên:</label><br>
    <c:choose>
        <c:when test="${formAction == 'update'}">
            <input type="text" name="id" value="${student.id}" readonly>
        </c:when>
        <c:otherwise>
            <input type="text" name="id" value="${student.id}">
        </c:otherwise>
    </c:choose>
    <br><br>

    <label>Họ tên:</label><br>
    <input type="text" name="name" value="${student.name}"><br><br>

    <label>Lớp:</label><br>
    <input type="text" name="className" value="${student.className}"><br><br>

    <label>Email:</label><br>
    <input type="email" name="email" value="${student.email}"><br><br>

    <button type="submit">
        <c:choose>
            <c:when test="${formAction == 'update'}">Cập nhật</c:when>
            <c:otherwise>Lưu sinh viên</c:otherwise>
        </c:choose>
    </button>
    <a href="${pageContext.request.contextPath}/students">Quay lại danh sách</a>
</form>
</body>
</html>
