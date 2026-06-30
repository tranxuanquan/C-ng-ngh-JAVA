package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.PageResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class KhachHangPanel extends JPanel {
    private static final int PAGE_SIZE = 10;

    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final JTextField txtTenKh;
    public final JTextField txtSdt;
    private final JTextField txtDiaChi;
    private final DefaultTableModel tableModel;
    private final JLabel lblTrang;
    private int currentPage = 1;
    private int totalRows = 0;

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTenKh = new JTextField(16);
        txtSdt = new JTextField(12);
        txtDiaChi = new JTextField(18);
        ((AbstractDocument) txtSdt.getDocument()).setDocumentFilter(new PhoneDocumentFilter());
        lblTrang = new JLabel("Trang 1/1");

        add(createSearchPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Ma KH", "Ten khach hang", "So dien thoai", "Dia chi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);

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
            txtTenKh.setText("");
            txtSdt.setText("");
            txtDiaChi.setText("");
            loadPage(1);
        });
        btnDau.addActionListener(e -> goToPage(1));
        btnTruoc.addActionListener(e -> goToPage(currentPage - 1));
        btnSau.addActionListener(e -> goToPage(currentPage + 1));
        btnCuoi.addActionListener(e -> goToPage(getTotalPages()));

        panel.add(new JLabel("Ten:"));
        panel.add(txtTenKh);
        panel.add(new JLabel("SDT:"));
        panel.add(txtSdt);
        panel.add(new JLabel("Dia chi:"));
        panel.add(txtDiaChi);
        panel.add(btnTim);
        panel.add(btnLamMoi);
        panel.add(btnDau);
        panel.add(btnTruoc);
        panel.add(lblTrang);
        panel.add(btnSau);
        panel.add(btnCuoi);
        return panel;
    }

    private void loadPage(int page) {
        currentPage = Math.max(1, page);
        new SwingWorker<PageResult<KhachHang>, Void>() {
            @Override
            protected PageResult<KhachHang> doInBackground() throws Exception {
                return khachHangBUS.searchAdvanced(
                        txtTenKh.getText().trim(),
                        txtSdt.getText().trim(),
                        txtDiaChi.getText().trim(),
                        currentPage,
                        PAGE_SIZE
                );
            }

            @Override
            protected void done() {
                try {
                    PageResult<KhachHang> result = get();
                    totalRows = result.getTotalRows();
                    tableModel.setRowCount(0);
                    for (KhachHang kh : result.getItems()) {
                        tableModel.addRow(new Object[]{kh.getId(), kh.getTenKh(), kh.getSdt(), kh.getDiaChi()});
                    }
                    lblTrang.setText("Trang " + currentPage + "/" + getTotalPages());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(KhachHangPanel.this,
                            "Tai du lieu khach hang that bai: " + e.getMessage(),
                            "Loi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
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
