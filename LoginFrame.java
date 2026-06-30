package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.TaiKhoanBUS;
import vn.edu.eaut.lab5.model.TaiKhoan;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Dang nhap");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 190);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtUsername = new JTextField(18);
        txtPassword = new JPasswordField(18);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(form, gbc, 0, "Username:", txtUsername);
        addField(form, gbc, 1, "Password:", txtPassword);
        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton btnLogin = new JButton("Dang nhap");
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        actions.add(btnLogin);
        add(actions, BorderLayout.SOUTH);
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

    private void login() {
        try {
            TaiKhoan tk = taiKhoanBUS.login(txtUsername.getText(), new String(txtPassword.getPassword()));
            MainFrame mainFrame = new MainFrame(tk);
            mainFrame.setVisible(true);
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dang nhap that bai: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
