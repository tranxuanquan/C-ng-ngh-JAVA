package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.Sach;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SachRepository {
    private static final List<Sach> data = new ArrayList<>();
    private static int autoId = 3;

    static {
        data.add(new Sach(1, "S001", "Lập trình Java", "Nguyễn Văn A", "Giáo dục Việt Nam", 2022));
        data.add(new Sach(2, "S002", "Cấu trúc dữ liệu", "Trần Thị B", "Đại học Quốc gia", 2021));
    }

    public List<Sach> findAll() { return data; }

    public Sach findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(Sach sach) {
        sach.setId(autoId++);
        data.add(sach);
    }

    public void update(Sach sach) {
        Sach old = findById(sach.getId());
        if (old != null) {
            old.setMaSach(sach.getMaSach());
            old.setTenSach(sach.getTenSach());
            old.setTacGia(sach.getTacGia());
            old.setNhaXuatBan(sach.getNhaXuatBan());
            old.setNamXuatBan(sach.getNamXuatBan());
        }
    }

    public void delete(int id) {
        data.removeIf(x -> x.getId() == id);
    }

    public List<Sach> search(String key) {
        if (key == null || key.trim().isEmpty()) return data;
        String k = key.toLowerCase();
        return data.stream()
                .filter(x -> x.getTenSach().toLowerCase().contains(k) || x.getTacGia().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
