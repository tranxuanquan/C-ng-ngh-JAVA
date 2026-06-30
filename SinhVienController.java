package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.SinhVienRepository;
import vn.edu.eaut.lab7.util.PaginationUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/sinh-vien")
public class SinhVienController extends HttpServlet {
    private final SinhVienRepository repo = new SinhVienRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("new".equals(action)) {
            req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
            return;
        }
        if ("edit".equals(action)) {
            req.setAttribute("sv", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
            return;
        }
        if ("detail".equals(action)) {
            req.setAttribute("sv", repo.findById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("/views/sinhvien/detail.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            repo.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/sinh-vien");
            return;
        }

        List<SinhVien> all = repo.search(req.getParameter("keyword"));
        int page = parsePage(req.getParameter("page"));
        req.setAttribute("dsSinhVien", PaginationUtil.paginate(all, page));
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", PaginationUtil.totalPages(all.size()));
        req.setAttribute("keyword", req.getParameter("keyword"));
        req.getRequestDispatcher("/views/sinhvien/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        SinhVien sv = new SinhVien(
                id == null || id.isBlank() ? 0 : Integer.parseInt(id),
                req.getParameter("maSinhVien"),
                req.getParameter("hoTen"),
                req.getParameter("email"),
                req.getParameter("lop")
        );
        if (sv.getId() == 0) repo.add(sv);
        else repo.update(sv);
        resp.sendRedirect(req.getContextPath() + "/sinh-vien");
    }

    private int parsePage(String pageParam) {
        try {
            return Math.max(Integer.parseInt(pageParam), 1);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
