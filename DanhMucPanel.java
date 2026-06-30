package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.DanhMucBUS;
import vn.edu.eaut.lab5.model.DanhMuc;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

public class DanhMucPanel extends JPanel {
    private final DanhMucBUS danhMucBUS = new DanhMucBUS();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtMaDm;
    private final JTextField txtTenDm;

    public DanhMucPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMaDm = new JTextField(8);
        txtMaDm.setEditable(false);
        txtTenDm = new JTextField(24);

        add(createFormPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Ma DM", "Ten danh muc"}, 0) {
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
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(fields, gbc, 0, "Ma DM:", txtMaDm);
        addField(fields, gbc, 1, "Ten danh muc:", txtTenDm);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");

        btnThem.addActionListener(e -> saveCategory(false));
        btnSua.addActionListener(e -> saveCategory(true));
        btnXoa.addActionListener(e -> deleteCategory());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadCategories();
        });

        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnLamMoi);

        wrapper.add(fields, BorderLayout.CENTER);
        wrapper.add(actions, BorderLayout.SOUTH);
        return wrapper;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField textField) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(textField, gbc);
    }

    private void loadCategories() {
        try {
            showCategories(danhMucBUS.findAll());
        } catch (SQLException e) {
            showError("Khong tai duoc danh sach danh muc: " + e.getMessage());
        }
    }

    private void showCategories(List<DanhMuc> categories) {
        tableModel.setRowCount(0);
        for (DanhMuc dm : categories) {
            tableModel.addRow(new Object[]{dm.getMaDm(), dm.getTenDm()});
        }
    }

    private void saveCategory(boolean update) {
        try {
            DanhMuc dm = readCategoryFromForm();
            if (update && dm.getMaDm() == 0) {
                JOptionPane.showMessageDialog(this, "Vui long chon danh muc can sua");
                return;
            }
            if (!update) {
                dm.setMaDm(0);
            }

            danhMucBUS.save(dm);
            clearForm();
            loadCategories();
            JOptionPane.showMessageDialog(this, update ? "Cap nhat danh muc thanh cong" : "Them danh muc thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Luu danh muc that bai: " + e.getMessage());
        }
    }

    private DanhMuc readCategoryFromForm() {
        DanhMuc dm = new DanhMuc();
        String maDm = txtMaDm.getText().trim();
        if (!maDm.isEmpty()) {
            dm.setMaDm(Integer.parseInt(maDm));
        }
        dm.setTenDm(txtTenDm.getText().trim());
        return dm;
    }

    private void deleteCategory() {
        String maDm = txtMaDm.getText().trim();
        if (maDm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon danh muc can xoa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xoa danh muc dang chon?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            danhMucBUS.delete(Integer.parseInt(maDm));
            clearForm();
            loadCategories();
            JOptionPane.showMessageDialog(this, "Xoa danh muc thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Xoa danh muc that bai: " + e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtMaDm.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtTenDm.setText(String.valueOf(tableModel.getValueAt(row, 1)));
    }

    private void clearForm() {
        txtMaDm.setText("");
        txtTenDm.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }
}
