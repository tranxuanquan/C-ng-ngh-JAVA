package vn.edu.eaut.lab8.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import vn.edu.eaut.lab8.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("productBean")
@SessionScoped
public class ProductBean implements Serializable {
    private Product product = new Product();
    private List<Product> dsProduct = new ArrayList<>();
    private static int autoId = 3;
    
    public ProductBean() {
        dsProduct.add(new Product(1, "Laptop Dell", 15000000, 5));
        dsProduct.add(new Product(2, "Mouse Logitech", 500000, 20));
    }
    
    public String save() {
        product.setId(autoId++);
        dsProduct.add(product);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da luu san pham"));
        product = new Product();
        return null;
    }
    
    public void delete(int id) {
        dsProduct.removeIf(p -> p.getId() == id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanh cong", "Da xoa san pham"));
    }
    
    public List<Product> getDsProduct() { return dsProduct; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}