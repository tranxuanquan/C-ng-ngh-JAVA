package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai06LoginForm extends JFrame {
    private final JTextField txtTaiKhoan = new JTextField(18);
    private final JPasswordField txtMatKhau = new JPasswordField(18);
    private final JComboBox<String> cboVaiTro = new JComboBox<>(new String[]{"Admin", "User"});
    private final JCheckBox chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");
    private final char defaultEchoChar;

    public Bai06LoginForm() {
        setTitle("Bài 6 - Form đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        defaultEchoChar = txtMatKhau.getEchoChar();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(formPanel, gbc, 0, "Tài khoản:", txtTaiKhoan);
        addRow(formPanel, gbc, 1, "Mật khẩu:", txtMatKhau);
        addRow(formPanel, gbc, 2, "Vai trò:", cboVaiTro);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(chkHienMatKhau, gbc);

        JButton btnDangNhap = new JButton("Đăng nhập");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        buttonPanel.add(btnDangNhap);

        chkHienMatKhau.addActionListener(e -> doiTrangThaiHienMatKhau());
        btnDangNhap.addActionListener(e -> dangNhap());
        getRootPane().setDefaultButton(btnDangNhap);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(input, gbc);
    }

    private void doiTrangThaiHienMatKhau() {
        txtMatKhau.setEchoChar(chkHienMatKhau.isSelected() ? (char) 0 : defaultEchoChar);
    }

    private void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String vaiTro = (String) cboVaiTro.getSelectedItem();

        if (taiKhoan.isEmpty()) {
            baoLoi("Vui lòng nhập tài khoản!", txtTaiKhoan);
            return;
        }
        if (matKhau.isEmpty()) {
            baoLoi("Vui lòng nhập mật khẩu!", txtMatKhau);
            return;
        }

        boolean adminDung = taiKhoan.equals("admin") && matKhau.equals("123456") && "Admin".equals(vaiTro);
        boolean userDung = taiKhoan.equals("user") && matKhau.equals("123456") && "User".equals(vaiTro);

        if (adminDung || userDung) {
            JOptionPane.showMessageDialog(this, "Chào mừng " + taiKhoan + " đăng nhập với vai trò " + vaiTro + "!");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Đăng nhập thất bại! Vui lòng kiểm tra tài khoản, mật khẩu và vai trò.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void baoLoi(String message, JComponent component) {
        JOptionPane.showMessageDialog(this, message, "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
        component.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai06LoginForm().setVisible(true));
    }
}
