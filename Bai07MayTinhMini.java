package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai07MayTinhMini extends JFrame {
    private final JTextField txtSoThuNhat = new JTextField();
    private final JTextField txtSoThuHai = new JTextField();
    private final JTextField txtKetQua = new JTextField();
    private final JTextArea txtLichSu = new JTextArea(8, 36);

    public Bai07MayTinhMini() {
        setTitle("Bài 7 - Máy tính mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtKetQua.setEditable(false);
        txtLichSu.setEditable(false);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));
        inputPanel.add(new JLabel("Số thứ nhất:"));
        inputPanel.add(txtSoThuNhat);
        inputPanel.add(new JLabel("Số thứ hai:"));
        inputPanel.add(txtSoThuHai);
        inputPanel.add(new JLabel("Kết quả:"));
        inputPanel.add(txtKetQua);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        JButton btnCong = new JButton("Cộng");
        JButton btnTru = new JButton("Trừ");
        JButton btnNhan = new JButton("Nhân");
        JButton btnChia = new JButton("Chia");
        JButton btnClear = new JButton("Clear");
        buttonPanel.add(btnCong);
        buttonPanel.add(btnTru);
        buttonPanel.add(btnNhan);
        buttonPanel.add(btnChia);
        buttonPanel.add(btnClear);

        JPanel historyPanel = new JPanel(new BorderLayout(6, 6));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        historyPanel.add(new JLabel("Lịch sử phép tính:"), BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(txtLichSu), BorderLayout.CENTER);

        btnCong.addActionListener(e -> tinh("+"));
        btnTru.addActionListener(e -> tinh("-"));
        btnNhan.addActionListener(e -> tinh("*"));
        btnChia.addActionListener(e -> tinh("/"));
        btnClear.addActionListener(e -> lamMoi());

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.SOUTH);

        setSize(520, 360);
        setLocationRelativeTo(null);
    }

    private void tinh(String phepTinh) {
        try {
            double a = Double.parseDouble(txtSoThuNhat.getText().trim());
            double b = Double.parseDouble(txtSoThuHai.getText().trim());
            double ketQua;

            switch (phepTinh) {
                case "+" -> ketQua = a + b;
                case "-" -> ketQua = a - b;
                case "*" -> ketQua = a * b;
                case "/" -> {
                    if (b == 0) {
                        JOptionPane.showMessageDialog(this, "Không thể chia cho 0!", "Lỗi phép chia", JOptionPane.ERROR_MESSAGE);
                        txtSoThuHai.requestFocus();
                        return;
                    }
                    ketQua = a / b;
                }
                default -> throw new IllegalArgumentException("Phép tính không hợp lệ");
            }

            String dongLichSu = formatSo(a) + " " + phepTinh + " " + formatSo(b) + " = " + formatSo(ketQua);
            txtKetQua.setText(formatSo(ketQua));
            txtLichSu.append(dongLichSu + System.lineSeparator());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập hai số hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatSo(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private void lamMoi() {
        txtSoThuNhat.setText("");
        txtSoThuHai.setText("");
        txtKetQua.setText("");
        txtLichSu.setText("");
        txtSoThuNhat.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai07MayTinhMini().setVisible(true));
    }
}
