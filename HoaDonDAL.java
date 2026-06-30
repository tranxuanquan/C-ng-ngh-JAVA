package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.PageResult;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HoaDonDAL {
    private HoaDon mapInvoice(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHd(rs.getInt("ma_hd"));
        String ngayLap = rs.getString("ngay_lap");
        if (ngayLap != null) {
            hd.setNgayLap(LocalDate.parse(ngayLap));
        }
        hd.setMaKh(rs.getInt("ma_kh"));
        hd.setTenKh(rs.getString("ten_kh"));
        hd.setSdt(rs.getString("sdt"));
        hd.setTongTien(rs.getBigDecimal("tong_tien"));
        return hd;
    }

    private BigDecimal tinhTongTien(List<ChiTietHoaDon> chiTietList) {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietList) {
            if (ct.getThanhTien() != null) tong = tong.add(ct.getThanhTien());
        }
        return tong;
    }

    public int insertHoaDon(int maKh, List<ChiTietHoaDon> chiTietList) throws SQLException {
        String sqlHoaDon = "INSERT INTO hoa_don(ngay_lap, ma_kh, tong_tien) VALUES (?, ?, ?)";
        String sqlChiTiet =
                "INSERT INTO chi_tiet_hoa_don(ma_hd, ma_sp, so_luong, don_gia, thanh_tien) " +
                        "VALUES (?, ?, ?, ?, ?)";
        String sqlTonKho = "SELECT ten_sp, so_luong FROM san_pham WHERE ma_sp = ?";
        String sqlTruKho = "UPDATE san_pham SET so_luong = so_luong - ? WHERE ma_sp = ? AND so_luong >= ?";
        Connection conn = null;
        try {
            conn = DBHelper.getConnection();
            conn.setAutoCommit(false);
            Map<Integer, Integer> totalByProduct = groupQuantityByProduct(chiTietList);
            validateStock(conn, totalByProduct, sqlTonKho);

            BigDecimal tongTien = tinhTongTien(chiTietList);
            int maHd;
            try (PreparedStatement ps = conn.prepareStatement(sqlHoaDon, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, maKh);
                ps.setBigDecimal(3, tongTien);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        maHd = rs.getInt(1);
                    } else {
                        throw new SQLException("Khong lay duoc ma hoa don");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlChiTiet)) {
                for (ChiTietHoaDon ct : chiTietList) {
                    ps.setInt(1, maHd);
                    ps.setInt(2, ct.getMaSp());
                    ps.setInt(3, ct.getSoLuong());
                    ps.setBigDecimal(4, ct.getDonGia());
                    ps.setBigDecimal(5, ct.getThanhTien());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlTruKho)) {
                for (Map.Entry<Integer, Integer> entry : totalByProduct.entrySet()) {
                    int soLuong = entry.getValue();
                    ps.setInt(1, soLuong);
                    ps.setInt(2, entry.getKey());
                    ps.setInt(3, soLuong);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("Ton kho khong du de ban san pham ma " + entry.getKey());
                    }
                }
            }
            conn.commit();
            return maHd;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private Map<Integer, Integer> groupQuantityByProduct(List<ChiTietHoaDon> chiTietList) {
        Map<Integer, Integer> result = new HashMap<>();
        for (ChiTietHoaDon ct : chiTietList) {
            result.merge(ct.getMaSp(), ct.getSoLuong(), Integer::sum);
        }
        return result;
    }

    private void validateStock(Connection conn, Map<Integer, Integer> totalByProduct, String sqlTonKho) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sqlTonKho)) {
            for (Map.Entry<Integer, Integer> entry : totalByProduct.entrySet()) {
                ps.setInt(1, entry.getKey());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Khong tim thay san pham ma " + entry.getKey());
                    }
                    String tenSp = rs.getString("ten_sp");
                    int tonKho = rs.getInt("so_luong");
                    if (entry.getValue() > tonKho) {
                        throw new SQLException("San pham " + tenSp + " chi con " + tonKho + " trong kho");
                    }
                }
            }
        }
    }

    public PageResult<HoaDon> searchAdvanced(LocalDate fromDate, LocalDate toDate, Integer maKh,
                                             BigDecimal minTotal, BigDecimal maxTotal,
                                             int page, int pageSize) throws SQLException {
        List<Object> params = new ArrayList<>();
        String where = buildInvoiceWhere(fromDate, toDate, maKh, minTotal, maxTotal, params);
        int offset = Math.max(0, (page - 1) * pageSize);
        List<HoaDon> items = new ArrayList<>();

        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT hd.ma_hd, hd.ngay_lap, hd.ma_kh, kh.ten_kh, kh.sdt, hd.tong_tien " +
                    "FROM hoa_don hd JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh " +
                    where + " ORDER BY hd.ma_hd DESC LIMIT ? OFFSET ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                bindParams(ps, params);
                ps.setInt(params.size() + 1, pageSize);
                ps.setInt(params.size() + 2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        items.add(mapInvoice(rs));
                    }
                }
            }

            String countSql = "SELECT COUNT(*) FROM hoa_don hd JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh " + where;
            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                bindParams(ps, params);
                try (ResultSet rs = ps.executeQuery()) {
                    return new PageResult<>(items, rs.next() ? rs.getInt(1) : 0);
                }
            }
        }
    }

    private String buildInvoiceWhere(LocalDate fromDate, LocalDate toDate, Integer maKh,
                                     BigDecimal minTotal, BigDecimal maxTotal, List<Object> params) {
        StringBuilder where = new StringBuilder("WHERE 1 = 1");
        if (fromDate != null) {
            where.append(" AND hd.ngay_lap >= ?");
            params.add(fromDate.toString());
        }
        if (toDate != null) {
            where.append(" AND hd.ngay_lap <= ?");
            params.add(toDate.toString());
        }
        if (maKh != null) {
            where.append(" AND hd.ma_kh = ?");
            params.add(maKh);
        }
        if (minTotal != null) {
            where.append(" AND hd.tong_tien >= ?");
            params.add(minTotal);
        }
        if (maxTotal != null) {
            where.append(" AND hd.tong_tien <= ?");
            params.add(maxTotal);
        }
        return where.toString();
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }
}
