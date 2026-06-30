package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAL {
    private TaiKhoan mapAccount(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setUsername(rs.getString("username"));
        tk.setPassword(rs.getString("password"));
        tk.setHoTen(rs.getString("ho_ten"));
        tk.setVaiTro(rs.getString("vai_tro"));
        return tk;
    }

    public TaiKhoan findByUsername(String username) throws SQLException {
        String sql = "SELECT username, password, ho_ten, vai_tro FROM tai_khoan WHERE username = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAccount(rs) : null;
            }
        }
    }

    public List<TaiKhoan> findAll() throws SQLException {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT username, password, ho_ten, vai_tro FROM tai_khoan ORDER BY username";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapAccount(rs));
            }
        }
        return list;
    }

    public boolean insert(TaiKhoan tk) throws SQLException {
        String sql = "INSERT INTO tai_khoan(username, password, ho_ten, vai_tro) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getUsername());
            ps.setString(2, tk.getPassword());
            ps.setString(3, tk.getHoTen());
            ps.setString(4, tk.getVaiTro());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(TaiKhoan tk) throws SQLException {
        String sql = "UPDATE tai_khoan SET password = ?, ho_ten = ?, vai_tro = ? WHERE username = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getPassword());
            ps.setString(2, tk.getHoTen());
            ps.setString(3, tk.getVaiTro());
            ps.setString(4, tk.getUsername());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String username) throws SQLException {
        String sql = "DELETE FROM tai_khoan WHERE username = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        }
    }
}
