package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.dal.HoaDonDAL;
import vn.edu.eaut.lab5.dal.KhachHangDAL;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.PageResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class HoaDonSearchPanel extends JPanel {
    private static final int PAGE_SIZE = 10;

    private final HoaDonDAL hoaDonDAL = new HoaDonDAL();
    private final JTextField txtTuNgay;
    private final JTextField txtDenNgay;
    private final JTextField txtTongMin;
    private final JTextField txtTongMax;
    private final JComboBox<KhachHang> cboKhachHang;
    private final DefaultTableModel tableModel;
    private final JLabel lblTrang;
    private int currentPage = 1;
    private int totalRows = 0;

    public HoaDonSearchPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTuNgay = new JTextField(10);
        txtDenNgay = new JTextField(10);
        txtTongMin = new JTextField(10);
        txtTongMax = new JTextField(10);
        cboKhachHang = new JComboBox<>();
        lblTrang = new JLabel("Trang 1/1");

        add(createSearchPanel(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new Object[]{"Ma HD", "Ngay lap", "Ma KH", "Khach hang", "SDT", "Tong tien"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);

        loadCustomers();
        loadPage(1);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTim = new JButton("Tim");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnDau = new JButton("Dau");
        JButton btnTruoc = new JButton("Truoc");
        JButton btnSau = new JButton("Sau");
        JButton btnCuoi = new JButton("Cuoi");

        btnTim.addActionListener(e -> loadPage(1));
        btnLamMoi.addActionListener(e -> {
            txtTuNgay.setText("");
            txtDenNgay.setText("");
            txtTongMin.setText("");
            txtTongMax.setText("");
            cboKhachHang.setSelectedIndex(cboKhachHang.getItemCount() > 0 ? 0 : -1);
            loadPage(1);
        });
        btnDau.addActionListener(e -> goToPage(1));
        btnTruoc.addActionListener(e -> goToPage(currentPage - 1));
        btnSau.addActionListener(e -> goToPage(currentPage + 1));
        btnCuoi.addActionListener(e -> goToPage(getTotalPages()));

        panel.add(new JLabel("Tu ngay:"));
        panel.add(txtTuNgay);
        panel.add(new JLabel("Den ngay:"));
        panel.add(txtDenNgay);
        panel.add(new JLabel("Khach hang:"));
        panel.add(cboKhachHang);
        panel.add(new JLabel("Tong:"));
        panel.add(txtTongMin);
        panel.add(new JLabel("-"));
        panel.add(txtTongMax);
        panel.add(btnTim);
        panel.add(btnLamMoi);
        panel.add(btnDau);
        panel.add(btnTruoc);
        panel.add(lblTrang);
        panel.add(btnSau);
        panel.add(btnCuoi);
        return panel;
    }

    private void loadCustomers() {
        try {
            cboKhachHang.removeAllItems();
            cboKhachHang.addItem(new KhachHang(0, "Tat ca", ""));
            for (KhachHang kh : new KhachHangDAL().findAll()) {
                cboKhachHang.addItem(kh);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Khong tai duoc khach hang: " + e.getMessage());
        }
    }

    private void loadPage(int page) {
        currentPage = Math.max(1, page);
        new SwingWorker<PageResult<HoaDon>, Void>() {
            @Override
            protected PageResult<HoaDon> doInBackground() throws Exception {
                return hoaDonDAL.searchAdvanced(
                        parseDate(txtTuNgay),
                        parseDate(txtDenNgay),
                        getSelectedCustomerId(),
                        parseBigDecimal(txtTongMin),
                        parseBigDecimal(txtTongMax),
                        currentPage,
                        PAGE_SIZE
                );
            }

            @Override
            protected void done() {
                try {
                    PageResult<HoaDon> result = get();
                    totalRows = result.getTotalRows();
                    tableModel.setRowCount(0);
                    for (HoaDon hd : result.getItems()) {
                        tableModel.addRow(new Object[]{
                                hd.getMaHd(), hd.getNgayLap(), hd.getMaKh(),
                                hd.getTenKh(), hd.getSdt(), hd.getTongTien()
                        });
                    }
                    lblTrang.setText("Trang " + currentPage + "/" + getTotalPages());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(HoaDonSearchPanel.this,
                            "Tai du lieu hoa don that bai: " + e.getMessage(),
                            "Loi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private Integer getSelectedCustomerId() {
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        if (kh == null || kh.getId() == 0) {
            return null;
        }
        return kh.getId();
    }

    private LocalDate parseDate(JTextField field) {
        String value = field.getText().trim();
        return value.isEmpty() ? null : LocalDate.parse(value);
    }

    private BigDecimal parseBigDecimal(JTextField field) {
        String value = field.getText().trim();
        return value.isEmpty() ? null : new BigDecimal(value);
    }

    private void goToPage(int page) {
        if (page < 1 || page > getTotalPages()) {
            return;
        }
        loadPage(page);
    }

    private int getTotalPages() {
        return Math.max(1, (int) Math.ceil(totalRows / (double) PAGE_SIZE));
    }
}
