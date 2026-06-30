package vn.edu.eaut.lab5.model;

public class TaiKhoan {
    public static final String ADMIN = "ADMIN";
    public static final String NHANVIEN = "NHANVIEN";
    public static final String KETOAN = "KETOAN";

    private String username;
    private String password;
    private String hoTen;
    private String vaiTro;

    public TaiKhoan() {
    }

    public TaiKhoan(String username, String password, String hoTen, String vaiTro) {
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public boolean isAdmin() {
        return ADMIN.equals(vaiTro);
    }

    public boolean isNhanVien() {
        return NHANVIEN.equals(vaiTro);
    }

    public boolean isKeToan() {
        return KETOAN.equals(vaiTro);
    }
}
