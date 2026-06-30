package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.ThongKeDAL;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.SanPhamBanChay;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class ThongKeBUS {

    private final ThongKeDAL dal = new ThongKeDAL();

    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        return dal.tinhDoanhThu(tuNgay, denNgay);
    }

    public HoaDon hoaDonCoGiaTriLonNhat() throws SQLException {
        return dal.getHoaDonCoGiaTriLonNhat();
    }

    public SanPhamBanChay sanPhamBanChayNhat() throws SQLException {
        return dal.getSanPhamBanChayNhat();
    }
}
