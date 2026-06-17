package vn.edu.eaut.lab1;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int chon;

        do {
            System.out.println("\n========== MENU BÀI LAB 1 ==========");
            System.out.println("1. Bài 1: Tính tổng số chẵn");
            System.out.println("2. Bài 2: Tính tổng nghịch đảo");
            System.out.println("3. Bài 3: Kiểm tra số nguyên tố");
            System.out.println("4. Bài 4: Kiểm tra và phân loại tam giác");
            System.out.println("5. Bài 5: Hiển thị n số Fibonacci");
            System.out.println("0. Thoát chương trình");
            System.out.print("Mời bạn chọn (0-5): ");

            chon = scanner.nextInt();

            switch (chon) {
                case 1:
                    System.out.print("Nhập n: ");
                    int n1 = scanner.nextInt();
                    System.out.println("Kết quả s = " + So.tinhTongChan(n1));
                    break;
                case 2:
                    System.out.print("Nhập n: ");
                    int n2 = scanner.nextInt();
                    System.out.printf("Kết quả s = %.4f\n", So.tinhTongNghichDao(n2));
                    break;
                case 3:
                    System.out.print("Nhập n: ");
                    int n3 = scanner.nextInt();
                    System.out.println(n3 + (So.isNguyenTo(n3) ? " là số nguyên tố." : " không phải số nguyên tố."));
                    break;
                case 4:
                    System.out.print("Cạnh a: "); double a = scanner.nextDouble();
                    System.out.print("Cạnh b: "); double b = scanner.nextDouble();
                    System.out.print("Cạnh c: "); double c = scanner.nextDouble();
                    System.out.println("Kết quả: " + So.phanLoaiTamGiac(a, b, c));
                    break;
                case 5:
                    System.out.print("Nhập số n: ");
                    int n5 = scanner.nextInt();
                    System.out.println("Dãy Fibonacci: " + So.bieuDienFibonacci(n5));
                    break;
                case 0:
                    System.out.println("Đang thoát...");
                    break;
                default:
                    System.out.println("Chọn sai, mời chọn lại!");
            }
        } while (chon != 0);

        scanner.close();
    }
}