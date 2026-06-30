<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Danh sách sinh viên</title>
</head>
<body>
<h2>Danh sách sinh viên</h2>

<c:if test="${sessionScope.role == 'ADMIN'}">
    <a href="${pageContext.request.contextPath}/student-form.jsp">Thêm sinh viên</a>
    <br><br>
</c:if>

<form action="${pageContext.request.contextPath}/students" method="get">
    <label for="keyword">Tìm theo họ tên:</label>
    <input type="text" id="keyword" name="keyword" value="${keyword}">
    <button type="submit">Tìm kiếm</button>
    <a href="${pageContext.request.contextPath}/students">Hiển thị tất cả</a>
</form>
<br>

<c:choose>
    <c:when test="${empty students}">
        <p>Không tìm thấy sinh viên phù hợp.</p>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <th>Mã SV</th>
                <th>Họ tên</th>
                <th>Lớp</th>
                <th>Email</th>
                <c:if test="${sessionScope.role == 'ADMIN'}">
                    <th>Thao tác</th>
                </c:if>
            </tr>
            <c:forEach var="sv" items="${students}">
                <tr>
                    <td>${sv.id}</td>
                    <td>${sv.name}</td>
                    <td>${sv.className}</td>
                    <td>${sv.email}</td>
                    <c:if test="${sessionScope.role == 'ADMIN'}">
                        <td>
                            <a href="${pageContext.request.contextPath}/students?action=edit&id=${sv.id}">Sửa</a>
                            <form action="${pageContext.request.contextPath}/students" method="post" style="display:inline">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${sv.id}">
                                <button type="submit">Xóa</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
