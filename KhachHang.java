package vn.edu.eaut.lab5.model;

public class KhachHang {
    private int id;
    private String tenKh;
    private String sdt;
    private String diaChi;

    public KhachHang() {}

    public KhachHang(int id, String tenKh, String sdt) {
        this.id = id;
        this.tenKh = tenKh;
        this.sdt = sdt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return tenKh;
    }
}
