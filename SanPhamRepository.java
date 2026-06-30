package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class SanPhamRepository {
    private static final List<SanPham> data = new ArrayList<>();
    private static int autoId = 4;

    static {
        data.add(new SanPham(1, "SP01", "Bút bi", "Bút bi xanh", 5000, 100));
        data.add(new SanPham(2, "SP02", "Vở học sinh", "Vở 96 trang", 15000, 50));
        data.add(new SanPham(3, "SP03", "Balo", "Balo đi học", 250000, 20));
    }

    public List<SanPham> findAll() { return data; }

    public SanPham findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(SanPham sp) {
        sp.setId(autoId++);
        data.add(sp);
    }

    public void update(SanPham sp) {
        SanPham old = findById(sp.getId());
        if (old != null) {
            old.setMa(sp.getMa());
            old.setTen(sp.getTen());
            old.setMoTa(sp.getMoTa());
            old.setGia(sp.getGia());
            old.setSoLuong(sp.getSoLuong());
        }
    }

    public void delete(int id) {
        data.removeIf(x -> x.getId() == id);
    }
}
