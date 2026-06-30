package vn.edu.eaut.lab6.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab6.model.Student;
import vn.edu.eaut.lab6.store.StudentStore;
import java.io.IOException;
@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String id = request.getParameter("id");
            Student student = StudentStore.findById(getServletContext(), id);
            if (student == null) {
                response.sendRedirect(request.getContextPath() + "/students");
                return;
            }
            request.setAttribute("student", student);
            request.setAttribute("formAction", "update");
            request.getRequestDispatcher("/student-form.jsp").forward(request, response);
            return;
        }

        String keyword = request.getParameter("keyword");
        request.setAttribute("keyword", keyword);
        request.setAttribute("students", StudentStore.searchByName(getServletContext(), keyword));
        request.getRequestDispatcher("/student-list.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        if ("delete".equals(action)) {
            StudentStore.deleteById(getServletContext(), id);
            response.sendRedirect(request.getContextPath() + "/students");
            return;
        }

        String name = request.getParameter("name");
        String className = request.getParameter("className");
        String email = request.getParameter("email");
        Student student = new Student(id, name, className, email);
        if ("update".equals(action)) {
            StudentStore.update(getServletContext(), student);
            response.sendRedirect(request.getContextPath() + "/students");
            return;
        }

        StudentStore.add(getServletContext(), student);
        response.sendRedirect(request.getContextPath() + "/students");
    }
}
