package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.TaiKhoanDAL;
import vn.edu.eaut.lab5.model.TaiKhoan;

import java.sql.SQLException;
import java.util.List;

public class TaiKhoanBUS {
    private final TaiKhoanDAL taiKhoanDAL = new TaiKhoanDAL();

    public TaiKhoan login(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username khong duoc rong");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password khong duoc rong");
        }
        TaiKhoan tk = taiKhoanDAL.findByUsername(username.trim());
        if (tk == null || !password.equals(tk.getPassword())) {
            throw new IllegalArgumentException("Sai username hoac password");
        }
        return tk;
    }

    public List<TaiKhoan> findAll() throws SQLException {
        return taiKhoanDAL.findAll();
    }

    public boolean save(TaiKhoan tk) throws SQLException {
        TaiKhoan existing = taiKhoanDAL.findByUsername(tk.getUsername());
        if (existing != null && (tk.getPassword() == null || tk.getPassword().isEmpty())) {
            tk.setPassword(existing.getPassword());
        }
        validate(tk);
        if (existing == null) {
            return taiKhoanDAL.insert(tk);
        }
        return taiKhoanDAL.update(tk);
    }

    public boolean delete(String username, String currentUsername) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username khong hop le");
        }
        if (username.equals(currentUsername)) {
            throw new IllegalArgumentException("Khong the xoa tai khoan dang dang nhap");
        }
        return taiKhoanDAL.delete(username);
    }

    private void validate(TaiKhoan tk) {
        if (tk.getUsername() == null || tk.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username khong duoc rong");
        }
        if (tk.getPassword() == null || tk.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password khong duoc rong");
        }
        if (tk.getHoTen() == null || tk.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Ho ten khong duoc rong");
        }
        if (!TaiKhoan.ADMIN.equals(tk.getVaiTro())
                && !TaiKhoan.NHANVIEN.equals(tk.getVaiTro())
                && !TaiKhoan.KETOAN.equals(tk.getVaiTro())) {
            throw new IllegalArgumentException("Vai tro khong hop le");
        }
    }
}
