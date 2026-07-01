package vn.edu.eaut.lab8.bean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import vn.edu.eaut.lab8.model.SinhVien;
import vn.edu.eaut.lab8.repository.SinhVienRepository;
import java.io.Serializable;
import java.util.List;

@Named("sinhVienBean")
@SessionScoped
public class SinhVienBean implements Serializable {
    private SinhVien sinhVien = new SinhVien();
    private final SinhVienRepository repo = new SinhVienRepository();
    private boolean isEdit = false;
    private String keyword = "";
    
    public String save() {
        if (isEdit) {
            repo.update(sinhVien);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da cap nhat sinh vien"));
            isEdit = false;
        } else {
            repo.add(sinhVien);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da luu sinh vien"));
        }
        sinhVien = new SinhVien();
        keyword = "";
        return "sinhvien-list";
    }
    
    public String edit(int id) {
        SinhVien sv = repo.findById(id);
        if (sv != null) {
            this.sinhVien = sv;
            this.isEdit = true;
        }
        return "sinhvien-form";
    }
    
    public void delete(int id) {
        repo.delete(id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da xoa sinh vien"));
    }
    
    public List<SinhVien> getDsSinhVien() {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll();
        }
        return repo.search(keyword.toLowerCase());
    }
    
    public SinhVien getSinhVien() { return sinhVien; }
    public void setSinhVien(SinhVien sinhVien) { this.sinhVien = sinhVien; }
    public boolean isEdit() { return isEdit; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}