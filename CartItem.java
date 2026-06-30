package vn.edu.eaut.lab7.model;

public class CartItem {
    private int sanPhamId;
    private String ma;
    private String ten;
    private double gia;
    private int soLuong;

    public CartItem() {}

    public CartItem(SanPham sp, int soLuong) {
        this.sanPhamId = sp.getId();
        this.ma = sp.getMa();
        this.ten = sp.getTen();
        this.gia = sp.getGia();
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return gia * soLuong;
    }

    public int getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(int sanPhamId) { this.sanPhamId = sanPhamId; }
    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}
