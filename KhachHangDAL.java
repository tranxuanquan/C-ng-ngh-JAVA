package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.PageResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAL {
    private KhachHang mapCustomer(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setId(rs.getInt("ma_kh"));
        kh.setTenKh(rs.getString("ten_kh"));
        kh.setSdt(rs.getString("sdt"));
        kh.setDiaChi(rs.getString("dia_chi"));
        return kh;
    }

    public List<KhachHang> findAll() throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT ma_kh, ten_kh, sdt, dia_chi FROM khach_hang";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
        }
        return list;
    }

    public PageResult<KhachHang> searchAdvanced(String name, String phone, String address,
                                                int page, int pageSize) throws SQLException {
        List<Object> params = new ArrayList<>();
        String where = buildWhere(name, phone, address, params);
        int offset = Math.max(0, (page - 1) * pageSize);
        List<KhachHang> items = new ArrayList<>();

        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT ma_kh, ten_kh, sdt, dia_chi FROM khach_hang " +
                    where + " ORDER BY ma_kh LIMIT ? OFFSET ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                bindParams(ps, params);
                ps.setInt(params.size() + 1, pageSize);
                ps.setInt(params.size() + 2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        items.add(mapCustomer(rs));
                    }
                }
            }

            String countSql = "SELECT COUNT(*) FROM khach_hang " + where;
            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                bindParams(ps, params);
                try (ResultSet rs = ps.executeQuery()) {
                    return new PageResult<>(items, rs.next() ? rs.getInt(1) : 0);
                }
            }
        }
    }

    private String buildWhere(String name, String phone, String address, List<Object> params) {
        StringBuilder where = new StringBuilder("WHERE 1 = 1");
        if (name != null && !name.isBlank()) {
            where.append(" AND ten_kh LIKE ?");
            params.add("%" + name.trim() + "%");
        }
        if (phone != null && !phone.isBlank()) {
            where.append(" AND sdt LIKE ?");
            params.add("%" + phone.trim() + "%");
        }
        if (address != null && !address.isBlank()) {
            where.append(" AND dia_chi LIKE ?");
            params.add("%" + address.trim() + "%");
        }
        return where.toString();
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }
}
