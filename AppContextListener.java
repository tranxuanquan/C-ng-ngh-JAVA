package vn.edu.eaut.lab6.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.eaut.lab6.model.Student;
import vn.edu.eaut.lab6.store.StudentStore;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("SV001", "Nguyen Van An", "DCCNTT12", "an@example.com"));
        students.add(new Student("SV002", "Tran Thi Binh", "DCCNTT12", "binh@example.com"));
        students.add(new Student("SV003", "Le Van Cuong", "DCCNTT13", "cuong@example.com"));
        students.add(new Student("SV004", "Pham Thu Dung", "DCCNTT13", "dung@example.com"));
        students.add(new Student("SV005", "Hoang Minh Duc", "DCCNTT14", "duc@example.com"));

        ServletContext context = sce.getServletContext();
        context.setAttribute(StudentStore.STUDENTS_ATTRIBUTE, students);
        System.out.println("Ung dung Lab 6 da khoi dong. So sinh vien mau: " + students.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        int count = StudentStore.countAll(sce.getServletContext());
        System.out.println("Ung dung Lab 6 da dung. So sinh vien hien co: " + count);
    }
}
