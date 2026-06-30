<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/common/menu.jsp" %>
<html>
<head><title>Báo cáo luồng MVC</title></head>
<body>
<h2>Bài 13 - Báo cáo luồng MVC</h2>
<h3>Kiến trúc</h3>
<ul>
    <li><strong>Model:</strong> SinhVien, Sach, SanPham, LopHoc, DiemSinhVien, CartItem</li>
    <li><strong>View:</strong> JSP trong thư mục <code>/views/...</code></li>
    <li><strong>Controller:</strong> Servlet annotated <code>@WebServlet</code></li>
    <li><strong>Repository:</strong> Lưu dữ liệu in-memory (ArrayList)</li>
</ul>
<h3>Luồng xử lý CRUD (ví dụ Sinh viên)</h3>
<ol>
    <li>Browser gửi request tới <code>/sinh-vien</code></li>
    <li><code>LoginFilter</code> kiểm tra session <code>username</code>, chưa đăng nhập thì redirect <code>/login.jsp</code></li>
    <li><code>SinhVienController</code> nhận request, gọi <code>SinhVienRepository</code></li>
    <li>Controller set attribute và forward tới JSP (list/form/detail)</li>
    <li>JSP hiển thị dữ liệu, form POST quay lại Controller để lưu/xóa</li>
</ol>
<h3>Session &amp; Filter</h3>
<ul>
    <li><code>LoginController</code> xác thực và lưu <code>username</code> vào session</li>
    <li><code>LoginFilter</code> bảo vệ các URL CRUD: sinh-vien, sach, san-pham, lop-hoc, diem, gio-hang</li>
    <li><code>GioHangController</code> lưu giỏ hàng trong session attribute <code>cart</code></li>
</ul>
<h3>Listener</h3>
<ul>
    <li><code>AppLogListener</code> ghi log ra console khi ứng dụng start/stop và session create/destroy</li>
</ul>
<h3>Module đã hoàn thiện</h3>
<p>Sinh viên, Sách, Sản phẩm, Lớp học, Điểm, Giỏ hàng — đủ 3+ module CRUD theo yêu cầu.</p>
</body>
</html>
