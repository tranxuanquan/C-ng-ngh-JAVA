package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.dal.HoaDonDAL;
import vn.edu.eaut.lab5.dal.KhachHangDAL;
import vn.edu.eaut.lab5.dal.SanPhamDAL;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.SanPham;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private final Runnable afterSave;
    private final JComboBox<KhachHang> cbKhachHang;
    private final JComboBox<SanPham> cbSanPham;
    private final JTextField txtSoLuong;
    private final JTable tblChiTiet;
    private final DefaultTableModel tblModel;
    private final JLabel lblTongTien;
    private final JLabel lblTonKho;
    private final JComboBox<String> cbDinhDang;
    private final List<ChiTietHoaDon> chiTietList = new ArrayList<>();

    public HoaDonPanel() {
        this(null);
    }

    public HoaDonPanel(Runnable afterSave) {
        this.afterSave = afterSave;
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbKhachHang = new JComboBox<>();
        cbSanPham = new JComboBox<>();
        cbDinhDang = new JComboBox<>(new String[]{"TXT", "CSV"});
        txtSoLuong = new JTextField(5);
        lblTonKho = new JLabel("Ton kho: 0");

        top.add(new JLabel("Khach hang:"));
        top.add(cbKhachHang);
        top.add(new JLabel("San pham:"));
        top.add(cbSanPham);
        top.add(lblTonKho);
        top.add(new JLabel("So luong:"));
        top.add(txtSoLuong);
        top.add(new JLabel("Xuat file:"));
        top.add(cbDinhDang);

        JButton btnAdd = new JButton("Them dong");
        JButton btnSave = new JButton("Luu hoa don");
        top.add(btnAdd);
        top.add(btnSave);
        add(top, BorderLayout.NORTH);

        tblModel = new DefaultTableModel(new Object[]{"Ma SP", "Ten SP", "So luong", "Don gia", "Thanh tien"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblChiTiet = new JTable(tblModel);
        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tong: 0");
        bottom.add(lblTongTien);
        add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(this::onAddLine);
        btnSave.addActionListener(this::onSave);
        cbSanPham.addActionListener(e -> updateStockWarning());

        loadData();
    }

    private void loadData() {
        try {
            cbKhachHang.removeAllItems();
            KhachHangDAL khDal = new KhachHangDAL();
            for (KhachHang kh : khDal.findAll()) {
                cbKhachHang.addItem(kh);
            }
            reloadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Loi load du lieu: " + e.getMessage());
        }
    }

    public void refreshData() {
        reloadProducts();
    }

    private void reloadProducts() {
        try {
            cbSanPham.removeAllItems();
            SanPhamDAL spDal = new SanPhamDAL();
            for (SanPham sp : spDal.findAll()) {
                cbSanPham.addItem(sp);
            }
            updateStockWarning();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Loi load san pham: " + e.getMessage());
        }
    }

    private void onAddLine(ActionEvent e) {
        SanPham sp = (SanPham) cbSanPham.getSelectedItem();
        if (sp == null) {
            return;
        }
        if (sp.getSoLuong() <= 0) {
            JOptionPane.showMessageDialog(this, "San pham da het hang, khong the ban");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "So luong khong hop le");
            return;
        }

        int soLuongDangChon = getSelectedQuantity(sp.getMaSp());
        if (soLuongDangChon + soLuong > sp.getSoLuong()) {
            JOptionPane.showMessageDialog(this, "So luong ban vuot ton kho. San pham chi con " + sp.getSoLuong());
            return;
        }

        int row = findInvoiceRow(sp.getMaSp());
        if (row >= 0) {
            ChiTietHoaDon existing = chiTietList.get(row);
            existing.setSoLuong(existing.getSoLuong() + soLuong);
            tblModel.setValueAt(existing.getSoLuong(), row, 2);
            tblModel.setValueAt(existing.getThanhTien(), row, 4);
        } else {
            ChiTietHoaDon ct = new ChiTietHoaDon(sp.getMaSp(), sp.getTenSp(), soLuong, sp.getDonGia());
            chiTietList.add(ct);
            tblModel.addRow(new Object[]{sp.getMaSp(), sp.getTenSp(), soLuong, sp.getDonGia(), ct.getThanhTien()});
        }
        txtSoLuong.setText("");
        updateTotal();
    }

    private void updateTotal() {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietList) {
            if (ct.getThanhTien() != null) {
                tong = tong.add(ct.getThanhTien());
            }
        }
        lblTongTien.setText("Tong: " + tong);
    }

    private void onSave(ActionEvent e) {
        KhachHang kh = (KhachHang) cbKhachHang.getSelectedItem();
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon khach hang");
            return;
        }
        if (chiTietList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hoa don trong");
            return;
        }

        HoaDonDAL hdDal = new HoaDonDAL();
        try {
            List<ChiTietHoaDon> invoiceLines = new ArrayList<>(chiTietList);
            BigDecimal tongTien = calculateTotal(invoiceLines);
            LocalDate ngayLap = LocalDate.now();
            int maHd = hdDal.insertHoaDon(kh.getId(), chiTietList);
            String message = "Luu hoa don thanh cong. Ma HD=" + maHd;
            try {
                Path exportPath = exportInvoice(maHd, ngayLap, kh, invoiceLines, tongTien);
                message += "\nDa xuat file: " + exportPath.toAbsolutePath();
            } catch (IOException ex) {
                message += "\nXuat file that bai: " + ex.getMessage();
            }
            JOptionPane.showMessageDialog(this, message);
            chiTietList.clear();
            tblModel.setRowCount(0);
            updateTotal();
            reloadProducts();
            if (afterSave != null) {
                afterSave.run();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Luu hoa don that bai: " + ex.getMessage());
        }
    }

    private Path exportInvoice(int maHd, LocalDate ngayLap, KhachHang khachHang,
                               List<ChiTietHoaDon> invoiceLines, BigDecimal tongTien) throws IOException {
        String format = String.valueOf(cbDinhDang.getSelectedItem()).toLowerCase();
        Path outputDir = Path.of("hoa_don");
        Files.createDirectories(outputDir);
        Path outputPath = outputDir.resolve("HoaDon_" + maHd + "." + format);
        String content = "csv".equals(format)
                ? buildCsvContent(maHd, ngayLap, khachHang, invoiceLines, tongTien)
                : buildTxtContent(maHd, ngayLap, khachHang, invoiceLines, tongTien);
        Files.writeString(outputPath, content, StandardCharsets.UTF_8);
        return outputPath;
    }

    private String buildTxtContent(int maHd, LocalDate ngayLap, KhachHang khachHang,
                                   List<ChiTietHoaDon> invoiceLines, BigDecimal tongTien) {
        StringBuilder sb = new StringBuilder();
        sb.append("HOA DON BAN HANG").append(System.lineSeparator());
        sb.append("Ma hoa don: ").append(maHd).append(System.lineSeparator());
        sb.append("Ngay lap: ").append(ngayLap).append(System.lineSeparator());
        sb.append("Khach hang: ").append(khachHang.getTenKh()).append(System.lineSeparator());
        sb.append("So dien thoai: ").append(khachHang.getSdt()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(String.format("%-30s %10s %15s %15s%n", "Ten san pham", "So luong", "Don gia", "Thanh tien"));
        for (ChiTietHoaDon ct : invoiceLines) {
            sb.append(String.format("%-30s %10d %15s %15s%n",
                    ct.getTenSp(), ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()));
        }
        sb.append(System.lineSeparator());
        sb.append("Tong tien: ").append(tongTien).append(System.lineSeparator());
        return sb.toString();
    }

    private String buildCsvContent(int maHd, LocalDate ngayLap, KhachHang khachHang,
                                   List<ChiTietHoaDon> invoiceLines, BigDecimal tongTien) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ma hoa don,").append(maHd).append(System.lineSeparator());
        sb.append("Ngay lap,").append(csv(ngayLap.toString())).append(System.lineSeparator());
        sb.append("Khach hang,").append(csv(khachHang.getTenKh())).append(System.lineSeparator());
        sb.append("So dien thoai,").append(csv(khachHang.getSdt())).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Ten san pham,So luong,Don gia,Thanh tien").append(System.lineSeparator());
        for (ChiTietHoaDon ct : invoiceLines) {
            sb.append(csv(ct.getTenSp())).append(',')
                    .append(ct.getSoLuong()).append(',')
                    .append(ct.getDonGia()).append(',')
                    .append(ct.getThanhTien()).append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        sb.append("Tong tien,,,").append(tongTien).append(System.lineSeparator());
        return sb.toString();
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private int getSelectedQuantity(int maSp) {
        int total = 0;
        for (ChiTietHoaDon ct : chiTietList) {
            if (ct.getMaSp() == maSp) {
                total += ct.getSoLuong();
            }
        }
        return total;
    }

    private int findInvoiceRow(int maSp) {
        for (int i = 0; i < chiTietList.size(); i++) {
            if (chiTietList.get(i).getMaSp() == maSp) {
                return i;
            }
        }
        return -1;
    }

    private BigDecimal calculateTotal(List<ChiTietHoaDon> lines) {
        BigDecimal total = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : lines) {
            if (ct.getThanhTien() != null) {
                total = total.add(ct.getThanhTien());
            }
        }
        return total;
    }

    private void updateStockWarning() {
        SanPham sp = (SanPham) cbSanPham.getSelectedItem();
        if (sp == null) {
            lblTonKho.setText("Ton kho: 0");
            lblTonKho.setForeground(Color.BLACK);
            return;
        }
        if (sp.getSoLuong() <= 0) {
            lblTonKho.setText("Het hang");
            lblTonKho.setForeground(Color.RED);
        } else if (sp.getSoLuong() < 5) {
            lblTonKho.setText("Canh bao: ton kho " + sp.getSoLuong());
            lblTonKho.setForeground(Color.RED);
        } else {
            lblTonKho.setText("Ton kho: " + sp.getSoLuong());
            lblTonKho.setForeground(Color.BLACK);
        }
    }
}
