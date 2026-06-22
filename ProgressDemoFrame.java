package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ProgressDemoFrame extends JFrame {
    private JButton btnChooseFile;
    private JButton btnSearch;
    private JTextField txtKeyword;
    private JTextArea txtResult;
    private JLabel lblFile;
    private JLabel lblStatus;
    private File selectedFile;

    public ProgressDemoFrame() {
        setTitle("Bài 7 - Tìm kiếm từ khóa trong file");
        setSize(720, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnChooseFile = new JButton("Chọn file .txt");
        btnSearch = new JButton("Tìm kiếm");
        txtKeyword = new JTextField();
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        lblFile = new JLabel("File: chưa chọn");
        lblStatus = new JLabel("Số dòng tìm thấy: 0");

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(lblFile, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        inputPanel.add(txtKeyword, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnChooseFile);
        buttonPanel.add(btnSearch);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtResult), BorderLayout.CENTER);
        mainPanel.add(lblStatus, BorderLayout.SOUTH);
        add(mainPanel);

        btnChooseFile.addActionListener(e -> chooseFile());
        btnSearch.addActionListener(e -> searchKeyword());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
            txtResult.setText("");
            lblStatus.setText("Số dòng tìm thấy: 0");
        }
    }

    private void searchKeyword() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file .txt trước");
            return;
        }

        String keyword = txtKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa cần tìm");
            return;
        }

        btnSearch.setEnabled(false);
        btnChooseFile.setEnabled(false);
        txtResult.setText("");
        lblStatus.setText("Đang tìm kiếm...");

        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                int foundCount = 0;
                int lineNumber = 0;
                String lowerKeyword = keyword.toLowerCase();

                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (line.toLowerCase().contains(lowerKeyword)) {
                            foundCount++;
                            publish(lineNumber + ": " + line);
                        }
                    }
                }

                return foundCount;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    txtResult.append(line);
                    txtResult.append(System.lineSeparator());
                }
            }

            @Override
            protected void done() {
                try {
                    int foundCount = get();
                    lblStatus.setText("Số dòng tìm thấy: " + foundCount);
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi khi đọc file");
                }

                btnSearch.setEnabled(true);
                btnChooseFile.setEnabled(true);
            }
        };

        worker.execute();
    }
}
