package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentCsvStatsFrame extends JFrame {
    private JButton btnChooseFile;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblFile;
    private JLabel lblAverage;
    private JLabel lblTopStudent;
    private JProgressBar progressBar;

    public StudentCsvStatsFrame() {
        setTitle("Bài 8 - Thống kê điểm sinh viên từ CSV");
        setSize(720, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnChooseFile = new JButton("Chọn file CSV");
        lblFile = new JLabel("File: chưa chọn");
        lblAverage = new JLabel("Điểm trung bình: chưa có dữ liệu");
        lblTopStudent = new JLabel("Sinh viên điểm cao nhất: chưa có dữ liệu");
        progressBar = new JProgressBar();

        tableModel = new DefaultTableModel(new Object[]{"Mã SV", "Họ tên", "Điểm"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(lblFile, BorderLayout.CENTER);
        topPanel.add(btnChooseFile, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 6, 6));
        bottomPanel.add(lblAverage);
        bottomPanel.add(lblTopStudent);
        bottomPanel.add(progressBar);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

        btnChooseFile.addActionListener(e -> chooseAndReadFile());
    }

    private void chooseAndReadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (!"csv".equals(getFileExtension(selectedFile))) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file .csv");
                return;
            }

            readCsvFile(selectedFile);
        }
    }

    private void readCsvFile(File selectedFile) {
        btnChooseFile.setEnabled(false);
        tableModel.setRowCount(0);
        lblFile.setText("File: " + selectedFile.getAbsolutePath());
        lblAverage.setText("Đang đọc file...");
        lblTopStudent.setText("Sinh viên điểm cao nhất: đang tính...");
        progressBar.setIndeterminate(true);

        SwingWorker<StudentStats, Void> worker = new SwingWorker<>() {
            @Override
            protected StudentStats doInBackground() throws Exception {
                List<Student> students = new ArrayList<>();

                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    boolean firstLine = true;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        if (firstLine) {
                            firstLine = false;
                            if (line.toLowerCase(Locale.ROOT).startsWith("masv,")) {
                                continue;
                            }
                        }

                        students.add(parseStudent(line));
                    }
                }

                return buildStats(students);
            }

            @Override
            protected void done() {
                try {
                    StudentStats stats = get();
                    for (Student student : stats.students()) {
                        tableModel.addRow(new Object[]{
                                student.id(),
                                student.name(),
                                student.score()
                        });
                    }

                    lblAverage.setText(String.format(
                            Locale.ROOT,
                            "Điểm trung bình: %.2f",
                            stats.averageScore()));
                    lblTopStudent.setText(
                            "Sinh viên điểm cao nhất: "
                                    + stats.topStudent().id()
                                    + " - "
                                    + stats.topStudent().name()
                                    + " ("
                                    + stats.topStudent().score()
                                    + ")");
                } catch (Exception ex) {
                    lblAverage.setText("Lỗi khi đọc file CSV");
                    lblTopStudent.setText("Sinh viên điểm cao nhất: chưa có dữ liệu");
                    JOptionPane.showMessageDialog(
                            StudentCsvStatsFrame.this,
                            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                }

                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                btnChooseFile.setEnabled(true);
            }
        };

        worker.execute();
    }

    private Student parseStudent(String line) {
        List<String> columns = parseCsvLine(line);
        if (columns.size() != 3) {
            throw new IllegalArgumentException("Dòng CSV không hợp lệ: " + line);
        }

        String id = columns.get(0).trim();
        String name = columns.get(1).trim();
        double score;
        try {
            score = Double.parseDouble(columns.get(2).trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Điểm không hợp lệ: " + columns.get(2));
        }

        return new Student(id, name, score);
    }

    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                columns.add(value.toString());
                value.setLength(0);
            } else {
                value.append(ch);
            }
        }

        columns.add(value.toString());
        return columns;
    }

    private StudentStats buildStats(List<Student> students) {
        if (students.isEmpty()) {
            throw new IllegalArgumentException("File CSV không có dữ liệu sinh viên");
        }

        double totalScore = 0;
        Student topStudent = students.get(0);
        for (Student student : students) {
            totalScore += student.score();
            if (student.score() > topStudent.score()) {
                topStudent = student;
            }
        }

        return new StudentStats(students, totalScore / students.size(), topStudent);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return "";
        }
        return name.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private record Student(String id, String name, double score) {
    }

    private record StudentStats(List<Student> students, double averageScore, Student topStudent) {
    }
}
