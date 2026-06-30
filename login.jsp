<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
<h2>Đăng nhập hệ thống</h2>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
    <p>Tên đăng nhập: <input name="username" required></p>
    <p>Mật khẩu: <input name="password" type="password" required></p>
    <button type="submit">Đăng nhập</button>
</form>
<p>Tài khoản demo: <code>admin / admin</code></p>
<p><a href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a></p>
</body>
</html>
