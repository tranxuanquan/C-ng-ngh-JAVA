package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab7.model.DiemSinhVien;
import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.DiemRepository;
import vn.edu.eaut.lab7.repository.SinhVienRepository;

import java.io.IOException;

@WebServlet("/diem")
public class DiemController extends HttpServlet {
    private final DiemRepository repo = new DiemRepository();
    private final SinhVienRepository sinhVienRepo = new SinhVienRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("form".equals(action)) {
            int svId = Integer.parseInt(req.getParameter("sinhVienId"));
            SinhVien sv = sinhVienRepo.findById(svId);
            DiemSinhVien diem = repo.findBySinhVienId(svId);
            if (diem == null && sv != null) {
                diem = new DiemSinhVien();
                diem.setSinhVienId(svId);
                diem.setMaSinhVien(sv.getMaSinhVien());
                diem.setHoTen(sv.getHoTen());
            }
            req.setAttribute("diem", diem);
            req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("dsDiem", repo.findAll());
        req.getRequestDispatcher("/views/diem/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        int sinhVienId = Integer.parseInt(req.getParameter("sinhVienId"));
        double chuyenCan = Double.parseDouble(req.getParameter("chuyenCan"));
        double giuaKy = Double.parseDouble(req.getParameter("giuaKy"));
        double cuoiKy = Double.parseDouble(req.getParameter("cuoiKy"));
        repo.save(sinhVienId, chuyenCan, giuaKy, cuoiKy);
        resp.sendRedirect(req.getContextPath() + "/diem");
    }
}
