package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;

import java.io.IOException;

@WebServlet("/san-pham")
public class SanPhamController extends HttpServlet {
    private final SanPhamRepository repo = new SanPhamRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            req.setAttribute("sp", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if ("detail".equals(action)) {
            req.setAttribute("sp", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sanpham/detail.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            repo.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/san-pham");
            return;
        }
        req.setAttribute("dsSanPham", repo.findAll());
        req.getRequestDispatcher("/views/sanpham/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        double gia = Double.parseDouble(req.getParameter("gia"));
        int soLuong = Integer.parseInt(req.getParameter("soLuong"));
        SanPham sp = new SanPham(
                id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("ma"),
                req.getParameter("ten"),
                req.getParameter("moTa"),
                gia,
                soLuong
        );
        if (gia <= 0 || soLuong < 0) {
            req.setAttribute("error", "Giá phải > 0 và số lượng phải >= 0");
            req.setAttribute("sp", sp);
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
            return;
        }
        if (sp.getId() == 0) repo.add(sp);
        else repo.update(sp);
        resp.sendRedirect(req.getContextPath() + "/san-pham");
    }
}
