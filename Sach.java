package vn.edu.eaut.lab8.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Sach {
    private int id;
    
    @NotBlank(message = "Ten sach khong duoc de trong")
    private String tenSach;
    
    @NotBlank(message = "Tac gia khong duoc de trong")
    private String tacGia;
    
    @Min(value = 1000, message = "Nam xuat ban phai >= 1000")
    private int namXuatBan;

    public Sach() {
    }

    public Sach(int id, String tenSach, String tacGia, int namXuatBan) {
        this.id = id;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.namXuatBan = namXuatBan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getNamXuatBan() {
        return namXuatBan;
    }

    public void setNamXuatBan(int namXuatBan) {
        this.namXuatBan = namXuatBan;
    }
}