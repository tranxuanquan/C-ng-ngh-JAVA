package vn.edu.eaut.lab7.model;

public class DiemSinhVien {
    private int id;
    private int sinhVienId;
    private String maSinhVien;
    private String hoTen;
    private double chuyenCan;
    private double giuaKy;
    private double cuoiKy;
    private double diemTongKet;
    private String xepLoai;

    public DiemSinhVien() {}

    public DiemSinhVien(int id, int sinhVienId, String maSinhVien, String hoTen,
                        double chuyenCan, double giuaKy, double cuoiKy) {
        this.id = id;
        this.sinhVienId = sinhVienId;
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        setDiem(chuyenCan, giuaKy, cuoiKy);
    }

    public void setDiem(double chuyenCan, double giuaKy, double cuoiKy) {
        this.chuyenCan = chuyenCan;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
        this.diemTongKet = chuyenCan * 0.1 + giuaKy * 0.3 + cuoiKy * 0.6;
        this.xepLoai = tinhXepLoai(diemTongKet);
    }

    private String tinhXepLoai(double diem) {
        if (diem >= 8.5) return "A";
        if (diem >= 7.0) return "B";
        if (diem >= 5.5) return "C";
        if (diem >= 4.0) return "D";
        return "F";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(int sinhVienId) { this.sinhVienId = sinhVienId; }
    public String getMaSinhVien() { return maSinhVien; }
    public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public double getChuyenCan() { return chuyenCan; }
    public double getGiuaKy() { return giuaKy; }
    public double getCuoiKy() { return cuoiKy; }
    public double getDiemTongKet() { return diemTongKet; }
    public String getXepLoai() { return xepLoai; }
}
