package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.LopHoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LopHocRepository {
    private static final List<LopHoc> data = new ArrayList<>();
    private static int autoId = 3;

    static {
        data.add(new LopHoc(1, "DCCNTT15.10.1", "CNTT K15.1", "Th.S Nguyễn Văn C", 35));
        data.add(new LopHoc(2, "DCCNTT15.10.2", "CNTT K15.2", "Th.S Trần Thị D", 32));
    }

    public List<LopHoc> findAll() { return data; }

    public LopHoc findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(LopHoc lh) {
        lh.setId(autoId++);
        data.add(lh);
    }

    public void update(LopHoc lh) {
        LopHoc old = findById(lh.getId());
        if (old != null) {
            old.setMaLop(lh.getMaLop());
            old.setTenLop(lh.getTenLop());
            old.setCoVanHocTap(lh.getCoVanHocTap());
            old.setSoLuongSinhVien(lh.getSoLuongSinhVien());
        }
    }

    public void delete(int id) {
        data.removeIf(x -> x.getId() == id);
    }

    public List<LopHoc> search(String key) {
        if (key == null || key.trim().isEmpty()) return data;
        String k = key.toLowerCase();
        return data.stream()
                .filter(x -> x.getMaLop().toLowerCase().contains(k) || x.getTenLop().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
