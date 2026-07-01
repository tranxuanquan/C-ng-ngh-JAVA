package vn.edu.eaut.lab8.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("classBean")
@ApplicationScoped
public class ClassBean {
    private List<String> classList;
    
    public ClassBean() {
        classList = new ArrayList<>();
        classList.add("DCCNTT15.10.1");
        classList.add("DCCNTT15.10.2");
        classList.add("DCCNTT15.10.3");
        classList.add("DCCNTT15.10.4");
        classList.add("DCCNTT15.10.5");
    }
    
    public List<String> getClassList() {
        return classList;
    }
}