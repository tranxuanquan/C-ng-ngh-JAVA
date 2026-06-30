package vn.edu.eaut.lab7.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab7.model.CartItem;
import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/gio-hang")
public class GioHangController extends HttpServlet {
    private static final String CART_ATTR = "cart";
    private final SanPhamRepository sanPhamRepo = new SanPhamRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Map<Integer, CartItem> cart = getCart(req.getSession());

        if ("add".equals(action)) {
            SanPham sp = sanPhamRepo.findById(Integer.parseInt(req.getParameter("id")));
            if (sp != null) {
                CartItem item = cart.get(sp.getId());
                if (item == null) {
                    cart.put(sp.getId(), new CartItem(sp, 1));
                } else {
                    item.setSoLuong(item.getSoLuong() + 1);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/gio-hang");
            return;
        }
        if ("remove".equals(action)) {
            cart.remove(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/gio-hang");
            return;
        }
        if ("clear".equals(action)) {
            cart.clear();
            resp.sendRedirect(req.getContextPath() + "/gio-hang");
            return;
        }

        req.setAttribute("cartItems", cart.values());
        req.setAttribute("tongTien", tinhTongTien(cart));
        req.setAttribute("dsSanPham", sanPhamRepo.findAll());
        req.getRequestDispatcher("/views/giohang/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Map<Integer, CartItem> cart = getCart(req.getSession());
        int id = Integer.parseInt(req.getParameter("id"));
        int soLuong = Integer.parseInt(req.getParameter("soLuong"));
        CartItem item = cart.get(id);
        if (item != null) {
            if (soLuong <= 0) {
                cart.remove(id);
            } else {
                item.setSoLuong(soLuong);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/gio-hang");
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, CartItem> getCart(HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute(CART_ATTR);
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_ATTR, cart);
        }
        return cart;
    }

    private double tinhTongTien(Map<Integer, CartItem> cart) {
        return cart.values().stream().mapToDouble(CartItem::getThanhTien).sum();
    }
}
