package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.SanPhamBanChay;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class ThongKeDAL {

    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) AS doanh_thu FROM hoa_don WHERE ngay_lap BETWEEN ? AND ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("doanh_thu");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public HoaDon getHoaDonCoGiaTriLonNhat() throws SQLException {
        String sql = "SELECT ma_hd, ngay_lap, ma_kh, tong_tien FROM hoa_don ORDER BY tong_tien DESC LIMIT 1";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHd(rs.getInt("ma_hd"));
                Date d = rs.getDate("ngay_lap");
                if (d != null) hd.setNgayLap(d.toLocalDate());
                hd.setMaKh(rs.getInt("ma_kh"));
                hd.setTongTien(rs.getBigDecimal("tong_tien"));
                return hd;
            }
        }
        return null;
    }

    public SanPhamBanChay getSanPhamBanChayNhat() throws SQLException {
        String sql = "SELECT sp.ma_sp, sp.ten_sp, SUM(ct.so_luong) AS tong_so_luong " +
                "FROM chi_tiet_hoa_don ct JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "GROUP BY sp.ma_sp, sp.ten_sp ORDER BY tong_so_luong DESC LIMIT 1";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                SanPhamBanChay spbc = new SanPhamBanChay();
                spbc.setMaSp(rs.getInt("ma_sp"));
                spbc.setTenSp(rs.getString("ten_sp"));
                spbc.setTongSoLuong(rs.getInt("tong_so_luong"));
                return spbc;
            }
        }
        return null;
    }
}
