package vn.edu.eaut.lab7.repository;
import vn.edu.eaut.lab7.model.SinhVien;
import java.util.*;
import java.util.stream.Collectors;
public class SinhVienRepository {
    private static final List<SinhVien> data = new ArrayList<>();
    private static int autoId = 7;
    static {
        data.add(new SinhVien(1, "20240001", "Nguyễn Văn An", "an@gmail.com", "DCCNTT15.10.1"));
        data.add(new SinhVien(2, "20240002", "Trần Thị Bình", "binh@gmail.com", "DCCNTT15.10.2"));
        data.add(new SinhVien(3, "20240003", "Lê Văn Cường", "cuong@gmail.com", "DCCNTT15.10.1"));
        data.add(new SinhVien(4, "20240004", "Phạm Thị Dung", "dung@gmail.com", "DCCNTT15.10.2"));
        data.add(new SinhVien(5, "20240005", "Hoàng Văn Em", "em@gmail.com", "DCCNTT15.10.3"));
        data.add(new SinhVien(6, "20240006", "Vũ Thị Phương", "phuong@gmail.com", "DCCNTT15.10.3"));
    }
    public List<SinhVien> findAll() { return new ArrayList<>(data); }
    public SinhVien findById(int id) { return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null); }
    public void add(SinhVien sv) { sv.setId(autoId++); data.add(sv); }
    public void update(SinhVien sv) {
        SinhVien old = findById(sv.getId());
        if (old != null) { old.setMaSinhVien(sv.getMaSinhVien()); old.setHoTen(sv.getHoTen()); old.setEmail(sv.getEmail());
            old.setLop(sv.getLop()); }
    }
    public void delete(int id) { data.removeIf(x -> x.getId() == id); }
    public List<SinhVien> search(String key) {
        if (key == null || key.trim().isEmpty()) return data;
        String k = key.toLowerCase();
        return data.stream().filter(x -> x.getHoTen().toLowerCase().contains(k) ||
                x.getLop().toLowerCase().contains(k)).collect(Collectors.toList());
    }
}