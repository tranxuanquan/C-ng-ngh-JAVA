package vn.edu.eaut.lab8.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Product {
    private int id;
    
    @NotBlank(message = "Ten san pham khong duoc de trong")
    private String tenSanPham;
    
    @Min(value = 1, message = "Gia phai > 0")
    private double gia;
    
    @Min(value = 0, message = "So luong phai >= 0")
    private int soLuong;

    public Product() {
    }

    public Product(int id, String tenSanPham, double gia, int soLuong) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}