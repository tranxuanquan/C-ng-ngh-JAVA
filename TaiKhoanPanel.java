package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.TaiKhoanBUS;
import vn.edu.eaut.lab5.model.TaiKhoan;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

public class TaiKhoanPanel extends JPanel {
    private final TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private final String currentUsername;
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JTextField txtHoTen;
    private final JComboBox<String> cboVaiTro;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public TaiKhoanPanel(String currentUsername) {
        this.currentUsername = currentUsername;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtUsername = new JTextField(16);
        txtPassword = new JPasswordField(16);
        txtHoTen = new JTextField(20);
        cboVaiTro = new JComboBox<>(new String[]{TaiKhoan.ADMIN, TaiKhoan.NHANVIEN, TaiKhoan.KETOAN});

        add(createFormPanel(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new Object[]{"Username", "Ho ten", "Vai tro"}, 0) {
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
        loadAccounts();
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(fields, gbc, 0, "Username:", txtUsername);
        addField(fields, gbc, 1, "Password:", txtPassword);
        addField(fields, gbc, 2, "Ho ten:", txtHoTen);
        addField(fields, gbc, 3, "Vai tro:", cboVaiTro);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLuu = new JButton("Luu");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        btnLuu.addActionListener(e -> saveAccount());
        btnXoa.addActionListener(e -> deleteAccount());
        btnLamMoi.addActionListener(e -> clearForm());
        actions.add(btnLuu);
        actions.add(btnXoa);
        actions.add(btnLamMoi);

        wrapper.add(fields, BorderLayout.CENTER);
        wrapper.add(actions, BorderLayout.SOUTH);
        return wrapper;
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

    private void loadAccounts() {
        try {
            tableModel.setRowCount(0);
            for (TaiKhoan tk : taiKhoanBUS.findAll()) {
                tableModel.addRow(new Object[]{tk.getUsername(), tk.getHoTen(), tk.getVaiTro()});
            }
        } catch (SQLException e) {
            showError("Khong tai duoc tai khoan: " + e.getMessage());
        }
    }

    private void saveAccount() {
        try {
            TaiKhoan tk = new TaiKhoan(
                    txtUsername.getText().trim(),
                    new String(txtPassword.getPassword()),
                    txtHoTen.getText().trim(),
                    String.valueOf(cboVaiTro.getSelectedItem())
            );
            taiKhoanBUS.save(tk);
            clearForm();
            loadAccounts();
            JOptionPane.showMessageDialog(this, "Luu tai khoan thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Luu tai khoan that bai: " + e.getMessage());
        }
    }

    private void deleteAccount() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon tai khoan can xoa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xoa tai khoan dang chon?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            taiKhoanBUS.delete(username, currentUsername);
            clearForm();
            loadAccounts();
            JOptionPane.showMessageDialog(this, "Xoa tai khoan thanh cong");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            showError("Xoa tai khoan that bai: " + e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtUsername.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtPassword.setText("");
        txtHoTen.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        cboVaiTro.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 2)));
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtHoTen.setText("");
        cboVaiTro.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }
}
