package vn.edu.eaut.lab8.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class SinhVien {
    private int id;
    @NotBlank(message = "Ma sinh vien khong duoc de trong")
    private String maSinhVien;
    @NotBlank(message = "Ho ten khong duoc de trong")
    @Size(min = 5, message = "Ho ten toi thieu 5 ky tu")
    private String hoTen;
    @Email(message = "Email khong dung dinh dang")
    private String email;
    @NotBlank(message = "Lop khong duoc de trong")
    private String lop;

    public SinhVien() {
    }

    public SinhVien(int id, String maSinhVien, String hoTen, String email, String lop) {
        this.id = id;
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.lop = lop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
}