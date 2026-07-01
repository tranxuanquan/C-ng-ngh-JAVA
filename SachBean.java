package vn.edu.eaut.lab8.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import vn.edu.eaut.lab8.model.Sach;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("sachBean")
@SessionScoped
public class SachBean implements Serializable {
    private Sach sach = new Sach();
    private List<Sach> dsSach = new ArrayList<>();
    private static int autoId = 3;
    
    public SachBean() {
        dsSach.add(new Sach(1, "Lap trinh Java", "Nguyen Van A", 2020));
        dsSach.add(new Sach(2, "Web Development", "Tran Thi B", 2021));
    }
    
    public String save() {
        sach.setId(autoId++);
        dsSach.add(sach);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da luu sach"));
        sach = new Sach();
        return null;
    }
    
    public void delete(int id) {
        dsSach.removeIf(s -> s.getId() == id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da xoa sach"));
    }
    
    public List<Sach> getDsSach() {
        return dsSach;
    }
    
    public Sach getSach() {
        return sach;
    }
    
    public void setSach(Sach sach) {
        this.sach = sach;
    }
}