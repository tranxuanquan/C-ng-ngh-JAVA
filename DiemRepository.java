package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.DiemSinhVien;
import vn.edu.eaut.lab7.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class DiemRepository {
    private static final List<DiemSinhVien> data = new ArrayList<>();
    private static int autoId = 3;
    private final SinhVienRepository sinhVienRepo = new SinhVienRepository();

    static {
        data.add(new DiemSinhVien(1, 1, "20240001", "Nguyễn Văn An", 8.0, 7.5, 8.0));
        data.add(new DiemSinhVien(2, 2, "20240002", "Trần Thị Bình", 9.0, 8.0, 7.0));
    }

    public List<DiemSinhVien> findAll() { return data; }

    public DiemSinhVien findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public DiemSinhVien findBySinhVienId(int sinhVienId) {
        return data.stream().filter(x -> x.getSinhVienId() == sinhVienId).findFirst().orElse(null);
    }

    public void save(int sinhVienId, double chuyenCan, double giuaKy, double cuoiKy) {
        DiemSinhVien existing = findBySinhVienId(sinhVienId);
        if (existing != null) {
            existing.setDiem(chuyenCan, giuaKy, cuoiKy);
            return;
        }
        SinhVien sv = sinhVienRepo.findById(sinhVienId);
        if (sv == null) return;
        DiemSinhVien diem = new DiemSinhVien(autoId++, sinhVienId, sv.getMaSinhVien(), sv.getHoTen(),
                chuyenCan, giuaKy, cuoiKy);
        data.add(diem);
    }
}
