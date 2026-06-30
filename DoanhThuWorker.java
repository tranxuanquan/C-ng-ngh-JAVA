package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.ThongKeBUS;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DoanhThuWorker extends SwingWorker<BigDecimal, Void> {
    private final LocalDate tuNgay;
    private final LocalDate denNgay;
    private final ThongKeBUS thongKeBUS;
    private final JLabel lblKetQua;

    public DoanhThuWorker(LocalDate tuNgay, LocalDate denNgay,
                          ThongKeBUS thongKeBUS, JLabel lblKetQua) {
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.thongKeBUS = thongKeBUS;
        this.lblKetQua = lblKetQua;
    }

    @Override
    protected BigDecimal doInBackground() throws Exception {
        return thongKeBUS.tinhDoanhThu(tuNgay, denNgay);
    }

    @Override
    protected void done() {
        try {
            BigDecimal doanhThu = get();
            lblKetQua.setText("Doanh thu: " + doanhThu + " VND");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Loi thong ke: " + e.getMessage());
        }
    }
}
