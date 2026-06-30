package vn.edu.eaut.lab5.dal;
import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.PageResult;
import vn.edu.eaut.lab5.model.SanPham;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SanPhamDAL {
    private SanPham mapProduct(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSp(rs.getInt("ma_sp"));
        sp.setTenSp(rs.getString("ten_sp"));
        sp.setDonGia(rs.getBigDecimal("don_gia"));
        sp.setSoLuong(rs.getInt("so_luong"));
        int maDm = rs.getInt("ma_dm");
        sp.setMaDm(rs.wasNull() ? null : maDm);
        sp.setTenDm(rs.getString("ten_dm"));
        return sp;
    }

    public List<SanPham> findAll() throws SQLException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.ma_sp, sp.ten_sp, sp.don_gia, sp.so_luong, sp.ma_dm, dm.ten_dm " +
                "FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm = dm.ma_dm";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        }
        return list;
    }
    public boolean insert(SanPham sp) throws SQLException {
        String sql = "INSERT INTO san_pham(ten_sp, don_gia, so_luong, ma_dm) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSp());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuong());
            if (sp.getMaDm() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, sp.getMaDm());
            }
            return ps.executeUpdate() > 0;
        }
    }
    public boolean update(SanPham sp) throws SQLException {
        String sql = "UPDATE san_pham SET ten_sp = ?, don_gia = ?, so_luong = ?, ma_dm = ? WHERE ma_sp = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSp());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuong());
            if (sp.getMaDm() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, sp.getMaDm());
            }
            ps.setInt(5, sp.getMaSp());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean delete(int maSp) throws SQLException {
        String sql = "DELETE FROM san_pham WHERE ma_sp = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSp);
            return ps.executeUpdate() > 0;
        }
    }
    public List<SanPham> searchByName(String keyword) throws SQLException {
        return search(keyword, null);
    }

    public List<SanPham> search(String keyword, Integer maDm) throws SQLException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.ma_sp, sp.ten_sp, sp.don_gia, sp.so_luong, sp.ma_dm, dm.ten_dm " +
                "FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm = dm.ma_dm " +
                "WHERE sp.ten_sp LIKE ? AND (? IS NULL OR sp.ma_dm = ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            if (maDm == null) {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(2, maDm);
                ps.setInt(3, maDm);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        }
        return list;
    }

    public PageResult<SanPham> searchAdvanced(String keyword, BigDecimal minPrice, BigDecimal maxPrice,
                                              Integer minQuantity, Integer maxQuantity, Integer maDm,
                                              String sortBy, boolean ascending, int page, int pageSize) throws SQLException {
        List<Object> params = new ArrayList<>();
        String where = buildAdvancedWhere(keyword, minPrice, maxPrice, minQuantity, maxQuantity, maDm, params);
        String orderBy = resolveProductSort(sortBy) + (ascending ? " ASC" : " DESC");
        int offset = Math.max(0, (page - 1) * pageSize);

        List<SanPham> items = new ArrayList<>();
        String sql = "SELECT sp.ma_sp, sp.ten_sp, sp.don_gia, sp.so_luong, sp.ma_dm, dm.ten_dm " +
                "FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm = dm.ma_dm " +
                where + " ORDER BY " + orderBy + " LIMIT ? OFFSET ?";
        try (Connection conn = DBHelper.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                bindParams(ps, params);
                ps.setInt(params.size() + 1, pageSize);
                ps.setInt(params.size() + 2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        items.add(mapProduct(rs));
                    }
                }
            }
            int totalRows = countAdvanced(conn, where, params);
            return new PageResult<>(items, totalRows);
        }
    }

    private int countAdvanced(Connection conn, String where, List<Object> params) throws SQLException {
        String sql = "SELECT COUNT(*) FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm = dm.ma_dm " + where;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private String buildAdvancedWhere(String keyword, BigDecimal minPrice, BigDecimal maxPrice,
                                      Integer minQuantity, Integer maxQuantity, Integer maDm, List<Object> params) {
        StringBuilder where = new StringBuilder("WHERE 1 = 1");
        if (keyword != null && !keyword.isBlank()) {
            where.append(" AND sp.ten_sp LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }
        if (minPrice != null) {
            where.append(" AND sp.don_gia >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            where.append(" AND sp.don_gia <= ?");
            params.add(maxPrice);
        }
        if (minQuantity != null) {
            where.append(" AND sp.so_luong >= ?");
            params.add(minQuantity);
        }
        if (maxQuantity != null) {
            where.append(" AND sp.so_luong <= ?");
            params.add(maxQuantity);
        }
        if (maDm != null) {
            where.append(" AND sp.ma_dm = ?");
            params.add(maDm);
        }
        return where.toString();
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    private String resolveProductSort(String sortBy) {
        if ("ten".equals(sortBy)) {
            return "sp.ten_sp";
        }
        if ("don_gia".equals(sortBy)) {
            return "sp.don_gia";
        }
        return "sp.ma_sp";
    }
}
