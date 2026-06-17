package vn.edu.eaut.lab1;

public class So {

    // Bài 1: Tính tổng số chẵn từ 2 đến n
    public static int tinhTongChan(int n) {
        int sum = 0;
        if (n % 2 != 0) n--; // Nếu n lẻ thì giảm n xuống 1 đơn vị để thành số chẵn
        for (int i = 2; i <= n; i += 2) {
            sum += i;
        }
        return sum;
    }

    // Bài 2: Tính tổng nghịch đảo s = 1 + 1/2 + ... + 1/n
    public static double tinhTongNghichDao(int n) {
        double sum = 0.0;
        for (int i = 1; i <= n; i++) {
            sum += 1.0 / i;
        }
        return sum;
    }

    // Bài 3: Kiểm tra số nguyên tố
    public static boolean isNguyenTo(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Bài 4: Kiểm tra và phân loại tam giác
    public static String phanLoaiTamGiac(double a, double b, double c) {
        if (a + b > c && a + c > b && b + c > a) {
            if (a == b && b == c) return "Tam giác đều";
            if (a == b || a == c || b == c) {
                if (isVuong(a, b, c)) return "Tam giác vuông cân";
                return "Tam giác cân";
            }
            if (isVuong(a, b, c)) return "Tam giác vuông";
            return "Tam giác thường";
        }
        return "Không phải tam giác";
    }

    private static boolean isVuong(double a, double b, double c) {
        double eps = 0.000001; // Sai số cho phép khi so sánh số thực
        return Math.abs(a * a + b * b - c * c) < eps ||
                Math.abs(a * a + c * c - b * b) < eps ||
                Math.abs(b * b + c * c - a * a) < eps;
    }

    // Bài 5: Hiển thị dãy Fibonacci gồm n số
    public static String bieuDienFibonacci(int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            sb.append(a).append(" ");
            int next = a + b;
            a = b;
            b = next;
        }
        return sb.toString().trim();
    }
}