<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
<h2>Đăng nhập hệ thống</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label>Tên đăng nhập:</label><br>
    <input type="text" name="username"><br><br>

    <label>Mật khẩu:</label><br>
    <input type="password" name="password"><br><br>

    <button type="submit">Đăng nhập</button>
</form>
<p style="color:red">${error}</p>
<p>Admin: admin / 123456</p>
<p>User: user / 123456</p>
</body>
</html>
