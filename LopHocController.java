package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab7.model.LopHoc;
import vn.edu.eaut.lab7.repository.LopHocRepository;

import java.io.IOException;

@WebServlet("/lop-hoc")
public class LopHocController extends HttpServlet {
    private final LopHocRepository repo = new LopHocRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            req.setAttribute("lop", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
            return;
        }
        if ("detail".equals(action)) {
            req.setAttribute("lop", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/lophoc/detail.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            repo.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/lop-hoc");
            return;
        }
        req.setAttribute("dsLopHoc", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/lophoc/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        LopHoc lh = new LopHoc(
                id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maLop"),
                req.getParameter("tenLop"),
                req.getParameter("coVanHocTap"),
                Integer.parseInt(req.getParameter("soLuongSinhVien"))
        );
        if (lh.getId() == 0) repo.add(lh);
        else repo.update(lh);
        resp.sendRedirect(req.getContextPath() + "/lop-hoc");
    }
}
