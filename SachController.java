package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab7.model.Sach;
import vn.edu.eaut.lab7.repository.SachRepository;

import java.io.IOException;

@WebServlet("/sach")
public class SachController extends HttpServlet {
    private final SachRepository repo = new SachRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            req.setAttribute("sach", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
            return;
        }
        if ("detail".equals(action)) {
            req.setAttribute("sach", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sach/detail.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            repo.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/sach");
            return;
        }
        req.setAttribute("dsSach", repo.search(req.getParameter("keyword")));
        req.getRequestDispatcher("/views/sach/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        Sach sach = new Sach(
                id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSach"),
                req.getParameter("tenSach"),
                req.getParameter("tacGia"),
                req.getParameter("nhaXuatBan"),
                Integer.parseInt(req.getParameter("namXuatBan"))
        );
        if (sach.getId() == 0) repo.add(sach);
        else repo.update(sach);
        resp.sendRedirect(req.getContextPath() + "/sach");
    }
}
