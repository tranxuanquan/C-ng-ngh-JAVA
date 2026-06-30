<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<h2>Dashboard</h2>

<p>Người dùng: ${sessionScope.username}</p>
<p>Vai trò: ${sessionScope.role}</p>
<p>Thời gian đăng nhập: ${sessionScope.loginTime}</p>
<p>Tổng số sinh viên: ${totalStudents}</p>

<h3>Số sinh viên theo từng lớp</h3>
<c:choose>
    <c:when test="${empty studentsByClass}">
        <p>Chưa có sinh viên nào.</p>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <th>Lớp</th>
                <th>Số sinh viên</th>
            </tr>
            <c:forEach var="entry" items="${studentsByClass}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${entry.value}</td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/students">Quản lý sinh viên</a>
<br><br>
<a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</body>
</html>
