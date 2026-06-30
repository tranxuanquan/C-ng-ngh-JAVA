<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/common/menu.jsp" %>
<html>
<head><title>Lab 7 - CRUD MVC</title></head>
<body>
<h2>Lab 7 - Ứng dụng MVC tổng hợp</h2>
<p>Hệ thống gồm nhiều module CRUD, đăng nhập session, filter bảo vệ và listener ghi log.</p>
<ul>
    <li><strong>Bài 6:</strong> <a href="${pageContext.request.contextPath}/sach">Quản lý sách</a> — tìm theo tên/tác giả</li>
    <li><strong>Bài 7:</strong> <a href="${pageContext.request.contextPath}/san-pham">Quản lý sản phẩm</a> — validate giá &gt; 0, SL &gt;= 0</li>
    <li><strong>Bài 8:</strong> <a href="${pageContext.request.contextPath}/lop-hoc">Quản lý lớp học</a> — tìm theo mã/tên lớp</li>
    <li><strong>Bài 9:</strong> <a href="${pageContext.request.contextPath}/diem">Quản lý điểm</a> — tính tổng kết &amp; xếp loại A/B/C/D/F</li>
    <li><strong>Bài 10:</strong> <a href="${pageContext.request.contextPath}/gio-hang">Giỏ hàng Session</a></li>
    <li><strong>Bài 11:</strong> <a href="${pageContext.request.contextPath}/sinh-vien">Sinh viên</a> — phân trang 5 dòng/trang</li>
    <li><strong>Bài 12:</strong> Listener ghi log khởi động/dừng &amp; session create/destroy</li>
    <li><strong>Bài 13:</strong> <a href="${pageContext.request.contextPath}/mvc-report.jsp">Báo cáo luồng MVC</a></li>
</ul>
<p>Các module CRUD yêu cầu đăng nhập trước (filter). Dùng tài khoản <code>admin/admin</code>.</p>
</body>
</html>
