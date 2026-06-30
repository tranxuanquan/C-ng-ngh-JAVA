package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.KhachHangDAL;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.PageResult;

import java.sql.SQLException;

public class KhachHangBUS {
    private final KhachHangDAL khachHangDAL = new KhachHangDAL();

    private void validate(KhachHang kh) {
        if (kh.getTenKh() == null || kh.getTenKh().trim().isEmpty()) {
            throw new IllegalArgumentException("Ten khach hang khong duoc rong");
        }
        if (kh.getSdt() == null || !kh.getSdt().matches("\\d{1,10}")) {
            throw new IllegalArgumentException("So dien thoai chi gom so va toi da 10 ky tu");
        }
    }

    public void add(KhachHang kh) {
        validate(kh);
        // TODO: persist via DAL
    }

    public void update(KhachHang kh) {
        validate(kh);
        // TODO: update via DAL
    }

    public PageResult<KhachHang> searchAdvanced(String name, String phone, String address,
                                                int page, int pageSize) throws SQLException {
        return khachHangDAL.searchAdvanced(name, phone, address, page, pageSize);
    }

}
