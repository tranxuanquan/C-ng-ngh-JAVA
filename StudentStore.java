package vn.edu.eaut.lab6.store;

import jakarta.servlet.ServletContext;
import vn.edu.eaut.lab6.model.Student;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentStore {
    public static final String STUDENTS_ATTRIBUTE = "students";

    @SuppressWarnings("unchecked")
    private static List<Student> getStudents(ServletContext context) {
        Object value = context.getAttribute(STUDENTS_ATTRIBUTE);
        if (value instanceof List<?>) {
            return (List<Student>) value;
        }

        List<Student> students = new ArrayList<>();
        context.setAttribute(STUDENTS_ATTRIBUTE, students);
        return students;
    }

    public static List<Student> findAll(ServletContext context) {
        return getStudents(context);
    }

    public static int countAll(ServletContext context) {
        return getStudents(context).size();
    }

    public static Map<String, Integer> countByClassName(ServletContext context) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Student student : getStudents(context)) {
            String className = student.getClassName();
            if (className == null || className.trim().isEmpty()) {
                className = "Chưa có lớp";
            }
            result.put(className, result.getOrDefault(className, 0) + 1);
        }
        return result;
    }

    public static List<Student> searchByName(ServletContext context, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(context);
        }

        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        List<Student> result = new ArrayList<>();
        for (Student student : getStudents(context)) {
            String name = student.getName();
            if (name != null && name.toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                result.add(student);
            }
        }
        return result;
    }

    public static Student findById(ServletContext context, String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        for (Student student : getStudents(context)) {
            if (id.equals(student.getId())) {
                return student;
            }
        }
        return null;
    }

    public static void add(ServletContext context, Student student) {
        getStudents(context).add(student);
    }

    public static void update(ServletContext context, Student updatedStudent) {
        if (updatedStudent == null || updatedStudent.getId() == null) {
            return;
        }
        Student student = findById(context, updatedStudent.getId());
        if (student != null) {
            student.setName(updatedStudent.getName());
            student.setClassName(updatedStudent.getClassName());
            student.setEmail(updatedStudent.getEmail());
        }
    }

    public static void deleteById(ServletContext context, String id) {
        if (id == null || id.trim().isEmpty()) {
            return;
        }
        getStudents(context).removeIf(student -> id.equals(student.getId()));
    }
}
