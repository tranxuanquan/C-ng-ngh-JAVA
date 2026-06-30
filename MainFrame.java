package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.model.TaiKhoan;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {
    private final TaiKhoan currentUser;

    public MainFrame(TaiKhoan currentUser) {
        this.currentUser = currentUser;
        setTitle("Quan ly Mini Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel lblUser = new JLabel("Nguoi dung: " + currentUser.getHoTen() + " (" + currentUser.getVaiTro() + ")");
        lblUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Dang xuat");
        btnLogout.addActionListener(e -> logout());
        actions.add(btnLogout);

        header.add(lblUser, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        SanPhamPanel sanPhamPanel = null;
        HoaDonPanel hoaDonPanel = null;

        if (currentUser.isAdmin() || currentUser.isNhanVien()) {
            sanPhamPanel = new SanPhamPanel();
            final SanPhamPanel productPanel = sanPhamPanel;
            hoaDonPanel = new HoaDonPanel(productPanel::refreshData);

            tabs.addTab("San pham", sanPhamPanel);
            tabs.addTab("Danh muc", new DanhMucPanel());
            tabs.addTab("Khach hang", new KhachHangPanel());
            tabs.addTab("Hoa don", hoaDonPanel);
        }

        if (currentUser.isAdmin() || currentUser.isKeToan()) {
            tabs.addTab("Tra cuu hoa don", new HoaDonSearchPanel());
            tabs.addTab("Thong ke", new JLabel("Man hinh thong ke dang duoc phat trien", SwingConstants.CENTER));
        }

        if (currentUser.isAdmin()) {
            tabs.addTab("Tai khoan", new TaiKhoanPanel(currentUser.getUsername()));
        }

        if (tabs.getTabCount() == 0) {
            tabs.addTab("Thong bao", new JLabel("Tai khoan khong co quyen truy cap chuc nang nao", SwingConstants.CENTER));
        }

        SanPhamPanel finalSanPhamPanel = sanPhamPanel;
        HoaDonPanel finalHoaDonPanel = hoaDonPanel;
        tabs.addChangeListener(e -> {
            if (finalSanPhamPanel != null && tabs.getSelectedComponent() == finalSanPhamPanel) {
                finalSanPhamPanel.refreshData();
            } else if (finalHoaDonPanel != null && tabs.getSelectedComponent() == finalHoaDonPanel) {
                finalHoaDonPanel.refreshData();
            }
        });
        return tabs;
    }

    private void logout() {
        dispose();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}
