package vn.edu.eaut.lab8.repository;
import vn.edu.eaut.lab8.model.SinhVien;
import java.util.*;
import java.util.stream.Collectors;

public class SinhVienRepository {
    private static final List<SinhVien> data = new ArrayList<>();
    private static int autoId = 3;
    
    static {
        data.add(new SinhVien(1, "20240001", "Nguyễn Văn An", "an@gmail.com", "DCCNTT15.10.1"));
        data.add(new SinhVien(2, "20240002", "Trần Thị Bình", "binh@gmail.com", "DCCNTT15.10.2"));
    }
    
    public List<SinhVien> findAll() { 
        return data; 
    }
    
    public SinhVien findById(int id) {
        return data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }
    
    public List<SinhVien> search(String keyword) {
        return data.stream()
                .filter(sv -> sv.getHoTen().toLowerCase().contains(keyword) || 
                             sv.getLop().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }
    
    public void add(SinhVien sv) { 
        sv.setId(autoId++); 
        data.add(sv); 
    }
    
    public void update(SinhVien sv) {
        SinhVien existing = findById(sv.getId());
        if (existing != null) {
            existing.setMaSinhVien(sv.getMaSinhVien());
            existing.setHoTen(sv.getHoTen());
            existing.setEmail(sv.getEmail());
            existing.setLop(sv.getLop());
        }
    }
    
    public void delete(int id) { 
        data.removeIf(x -> x.getId() == id); 
    }
}