package vn.edu.eaut.lab3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Bai08QuanLySinhVien extends JFrame {
    private final JTextField txtMaSinhVien = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtDiemTrungBinh = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Mã sinh viên", "Họ tên", "Điểm trung bình", "Xếp loại"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblSinhVien = new JTable(tableModel);

    public Bai08QuanLySinhVien() {
        setTitle("Bài 8 - Quản lý sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));
        formPanel.add(new JLabel("Mã sinh viên:"));
        formPanel.add(txtMaSinhVien);
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Điểm trung bình:"));
        formPanel.add(txtDiemTrungBinh);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        tblSinhVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblSinhVien);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        btnThem.addActionListener(e -> themSinhVien());
        btnSua.addActionListener(e -> suaSinhVien());
        btnXoa.addActionListener(e -> xoaSinhVien());
        btnLamMoi.addActionListener(e -> lamMoi());
        tblSinhVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiDongDangChon();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(720, 420);
        setLocationRelativeTo(null);
    }

    private void themSinhVien() {
        Student student = taoSinhVienTuForm();
        if (student == null) {
            return;
        }
        if (timDongTheoMa(student.getMaSinhVien()) != -1) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            txtMaSinhVien.requestFocus();
            return;
        }

        tableModel.addRow(new Object[]{
                student.getMaSinhVien(),
                student.getHoTen(),
                student.getDiemTrungBinh(),
                student.getXepLoai()
        });
        lamMoiForm();
    }

    private void suaSinhVien() {
        int row = tblSinhVien.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần sửa!");
            return;
        }

        Student student = taoSinhVienTuForm();
        if (student == null) {
            return;
        }

        int duplicateRow = timDongTheoMa(student.getMaSinhVien());
        if (duplicateRow != -1 && duplicateRow != row) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setValueAt(student.getMaSinhVien(), row, 0);
        tableModel.setValueAt(student.getHoTen(), row, 1);
        tableModel.setValueAt(student.getDiemTrungBinh(), row, 2);
        tableModel.setValueAt(student.getXepLoai(), row, 3);
        lamMoiForm();
    }

    private void xoaSinhVien() {
        int row = tblSinhVien.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sinh viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            lamMoiForm();
        }
    }

    private void lamMoi() {
        lamMoiForm();
        tblSinhVien.clearSelection();
    }

    private Student taoSinhVienTuForm() {
        String ma = txtMaSinhVien.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String diemText = txtDiemTrungBinh.getText().trim();

        if (ma.isEmpty()) {
            baoLoi("Vui lòng nhập mã sinh viên!", txtMaSinhVien);
            return null;
        }
        if (hoTen.isEmpty()) {
            baoLoi("Vui lòng nhập họ tên!", txtHoTen);
            return null;
        }

        try {
            double diem = Double.parseDouble(diemText);
            if (diem < 0 || diem > 10) {
                baoLoi("Điểm trung bình phải từ 0 đến 10!", txtDiemTrungBinh);
                return null;
            }
            return new Student(ma, hoTen, diem);
        } catch (NumberFormatException ex) {
            baoLoi("Điểm trung bình phải là số hợp lệ!", txtDiemTrungBinh);
            return null;
        }
    }

    private void hienThiDongDangChon() {
        int row = tblSinhVien.getSelectedRow();
        if (row == -1) {
            return;
        }
        txtMaSinhVien.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtHoTen.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtDiemTrungBinh.setText(String.valueOf(tableModel.getValueAt(row, 2)));
    }

    private int timDongTheoMa(String maSinhVien) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (maSinhVien.equalsIgnoreCase(String.valueOf(tableModel.getValueAt(i, 0)))) {
                return i;
            }
        }
        return -1;
    }

    private void lamMoiForm() {
        txtMaSinhVien.setText("");
        txtHoTen.setText("");
        txtDiemTrungBinh.setText("");
        txtMaSinhVien.requestFocus();
    }

    private void baoLoi(String message, JComponent component) {
        JOptionPane.showMessageDialog(this, message, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
        component.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai08QuanLySinhVien().setVisible(true));
    }
}
