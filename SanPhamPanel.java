package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.DanhMucBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.DanhMuc;
import vn.edu.eaut.lab5.model.SanPham;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamPanel extends JPanel {
    private static final int PAGE_SIZE = 10;
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private final DanhMucBUS danhMucBUS = new DanhMucBUS();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtMaSp;
    private final JTextField txtTenSp;
    private final JTextField txtDonGia;
    private final JTextField txtSoLuong;
    private final JTextField txtTimKiem;
    private final JTextField txtGiaMin;
    private final JTextField txtGiaMax;
    private final JTextField txtSoLuongMin;
    private final JTextField txtSoLuongMax;
    private final JComboBox<DanhMuc> cboDanhMuc;
    private final JComboBox<DanhMuc> cboLocDanhMuc;
    private final JComboBox<String> cboSapXep;
    private final JComboBox<String> cboHuongSapXep;
    private final JLabel lblTrang;
    private int currentPage = 1;
    private int totalRows = 0;

    public SanPhamPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMaSp = new JTextField(8);
        txtMaSp.setEditable(false);
        txtTenSp = new JTextField(24);
        txtDonGia = new JTextField(12);
        txtSoLuong = new JTextField(8);
        txtTimKiem = new JTextField(24);
        txtGiaMin = new JTextField(8);
        txtGiaMax = new JTextField(8);
        txtSoLuongMin = new JTextField(6);
        txtSoLuongMax = new JTextField(6);
        cboDanhMuc = new JComboBox<>();
        cboLocDanhMuc = new JComboBox<>();
        cboSapXep = new JComboBox<>(new String[]{"Ma", "Ten", "Don gia"});
        cboHuongSapXep = new JComboBox<>(new String[]{"Tang", "Giam"});
        lblTrang = new JLabel("Trang 1/1");

        add(createFormPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Ma SP", "Ten san pham", "Don gia", "So luong", "Ma DM", "Danh muc", "Canh bao"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadCategories();
        loadProducts();
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));

        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(fields, gbc, 0, "Ma SP:", txtMaSp);
        addField(fields, gbc, 1, "Ten san pham:", txtTenSp);
        addField(fields, gbc, 2, "Don gia:", txtDonGia);
        addField(fields, gbc, 3, "So luong:", txtSoLuong);
        addField(fields, gbc, 4, "Danh muc:", cboDanhMuc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim");
        JButton btnDau = new JButton("Dau");
        JButton btnTruoc = new JButton("Truoc");
        JButton btnSau = new JButton("Sau");
        JButton btnCuoi = new JButton("Cuoi");

        btnThem.addActionListener(e -> saveProduct(false));
        btnSua.addActionListener(e -> saveProduct(true));
        btnXoa.addActionListener(e -> deleteProduct());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            clearSearch();
            loadProducts();
        });
        btnTim.addActionListener(e -> searchProducts());
        txtTimKiem.addActionListener(e -> searchProducts());
        btnDau.addActionListener(e -> goToPage(1));
        btnTruoc.addActionListener(e -> goToPage(currentPage - 1));
        btnSau.addActionListener(e -> goToPage(currentPage + 1));
        btnCuoi.addActionListener(e -> goToPage(getTotalPages()));

        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnLamMoi);
        actions.add(new JLabel("Tim ten:"));
        actions.add(txtTimKiem);
        actions.add(new JLabel("Danh muc:"));
        actions.add(cboLocDanhMuc);
        actions.add(new JLabel("Gia:"));
        actions.add(txtGiaMin);
        actions.add(new JLabel("-"));
        actions.add(txtGiaMax);
        actions.add(new JLabel("SL:"));
        actions.add(txtSoLuongMin);
        actions.add(new JLabel("-"));
        actions.add(txtSoLuongMax);
        actions.add(new JLabel("Sap xep:"));
        actions.add(cboSapXep);
        actions.add(cboHuongSapXep);
        actions.add(btnTim);
        actions.add(btnDau);
        actions.add(btnTruoc);
        actions.add(lblTrang);
        actions.add(btnSau);
        actions.add(btnCuoi);

        wrapper.add(fields, BorderLayout.CENTER);
        wrapper.add(actions, BorderLayout.SOUTH);
        return wrapper;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField textField) {
        addField(panel, gbc, row, label, (java.awt.Component) textField);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void loadCategories() {
        try {
            List<DanhMuc> categories = danhMucBUS.findAll();
            cboDanhMuc.removeAllItems();
            cboLocDanhMuc.removeAllItems();
            cboDanhMuc.addItem(new DanhMuc(0, "Khong chon"));
            cboLocDanhMuc.addItem(new DanhMuc(0, "Tat ca"));
            for (DanhMuc dm : categories) {
                cboDanhMuc.addItem(dm);
                cboLocDanhMuc.addItem(dm);
            }
        } catch (SQLException e) {
            showError("Khong tai duoc danh muc: " + e.getMessage());
        }
    }

    public void refreshData() {
        loadCategories();
        loadProducts();
    }

    private void loadProducts() {
        loadProductsPage(1);
    }

    private void searchProducts() {
        loadProductsPage(1);
    }

    private void loadProductsPage(int page) {
        currentPage = Math.max(1, page);
        new SwingWorker<vn.edu.eaut.lab5.model.PageResult<SanPham>, Void>() {
            @Override
            protected vn.edu.eaut.lab5.model.PageResult<SanPham> doInBackground() throws Exception {
                return sanPhamBUS.searchAdvanced(
                        txtTimKiem.getText().trim(),
                        parseBigDecimal(txtGiaMin),
                        parseBigDecimal(txtGiaMax),
                        parseInteger(txtSoLuongMin),
                        parseInteger(txtSoLuongMax),
                        getSelectedFilterCategoryId(),
                        getSortKey(),
                        "Tang".equals(cboHuongSapXep.getSelectedItem()),
                        currentPage,
                        PAGE_SIZE
                );
            }

            @Override
            protected void done() {
                try {
                    vn.edu.eaut.lab5.model.PageResult<SanPham> result = get();
                    totalRows = result.getTotalRows();
                    showProducts(result.getItems());
                    updatePageLabel();
                } catch (Exception e) {
                    showError("Tai du lieu san pham that bai: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void goToPage(int page) {
        int totalPages = getTotalPages();
        if (page < 1 || page > totalPages) {
            return;
        }
        loadProductsPage(page);
    }

    private int getTotalPages() {
        return Math.max(1, (int) Math.ceil(totalRows / (double) PAGE_SIZE));
    }

    private void updatePageLabel() {
        lblTrang.setText("Trang " + currentPage + "/" + getTotalPages());
    }

    private void showProducts(List<SanPham> products) {
        tableModel.setRowCount(0);
        for (SanPham sp : products) {
            tableModel.addRow(new Object[]{
                    sp.getMaSp(),
                    sp.getTenSp(),
                    sp.getDonGia(),
                    sp.getSoLuong(),
                    sp.getMaDm(),
                    sp.getTenDm() == null ? "" : sp.getTenDm(),
                    sp.getSoLuong() < 5 ? "Sap het hang" : ""
            });
        }
    }

    private void saveProduct(boolean update) {
        try {
            SanPham sp = readProductFromForm();
            if (update && sp.getMaSp() == 0) {
                JOptionPane.showMessageDialog(this, "Vui long chon san pham can sua");
                return;
            }
            if (!update) {
                sp.setMaSp(0);
            }

            sanPhamBUS.save(sp);
            clearForm();
            loadProducts();
            JOptionPane.showMessageDialog(this, update ? "Cap nhat san pham thanh cong" : "Them san pham thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Luu san pham that bai: " + e.getMessage());
        }
    }

    private SanPham readProductFromForm() {
        SanPham sp = new SanPham();
        String maSp = txtMaSp.getText().trim();
        if (!maSp.isEmpty()) {
            sp.setMaSp(Integer.parseInt(maSp));
        }
        sp.setTenSp(txtTenSp.getText().trim());
        sp.setDonGia(new BigDecimal(txtDonGia.getText().trim()));
        sp.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        sp.setMaDm(getSelectedFormCategoryId());
        return sp;
    }

    private Integer getSelectedFormCategoryId() {
        DanhMuc dm = (DanhMuc) cboDanhMuc.getSelectedItem();
        if (dm == null || dm.getMaDm() == 0) {
            return null;
        }
        return dm.getMaDm();
    }

    private Integer getSelectedFilterCategoryId() {
        DanhMuc dm = (DanhMuc) cboLocDanhMuc.getSelectedItem();
        if (dm == null || dm.getMaDm() == 0) {
            return null;
        }
        return dm.getMaDm();
    }

    private void deleteProduct() {
        String maSp = txtMaSp.getText().trim();
        if (maSp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon san pham can xoa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xoa san pham dang chon?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            sanPhamBUS.delete(Integer.parseInt(maSp));
            clearForm();
            loadProducts();
            JOptionPane.showMessageDialog(this, "Xoa san pham thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Xoa san pham that bai: " + e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtMaSp.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtTenSp.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtDonGia.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtSoLuong.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        Object maDm = tableModel.getValueAt(row, 4);
        selectCategory(cboDanhMuc, maDm instanceof Integer ? (Integer) maDm : null);
    }

    private void clearForm() {
        txtMaSp.setText("");
        txtTenSp.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        cboDanhMuc.setSelectedIndex(cboDanhMuc.getItemCount() > 0 ? 0 : -1);
        table.clearSelection();
    }

    private void clearSearch() {
        txtTimKiem.setText("");
        txtGiaMin.setText("");
        txtGiaMax.setText("");
        txtSoLuongMin.setText("");
        txtSoLuongMax.setText("");
        cboLocDanhMuc.setSelectedIndex(cboLocDanhMuc.getItemCount() > 0 ? 0 : -1);
        cboSapXep.setSelectedIndex(0);
        cboHuongSapXep.setSelectedIndex(0);
    }

    private BigDecimal parseBigDecimal(JTextField field) {
        String value = field.getText().trim();
        return value.isEmpty() ? null : new BigDecimal(value);
    }

    private Integer parseInteger(JTextField field) {
        String value = field.getText().trim();
        return value.isEmpty() ? null : Integer.parseInt(value);
    }

    private String getSortKey() {
        String selected = String.valueOf(cboSapXep.getSelectedItem());
        if ("Ten".equals(selected)) {
            return "ten";
        }
        if ("Don gia".equals(selected)) {
            return "don_gia";
        }
        return "ma";
    }

    private void selectCategory(JComboBox<DanhMuc> comboBox, Integer maDm) {
        int target = maDm == null ? 0 : maDm;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            DanhMuc dm = comboBox.getItemAt(i);
            if (dm.getMaDm() == target) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(comboBox.getItemCount() > 0 ? 0 : -1);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }
}
