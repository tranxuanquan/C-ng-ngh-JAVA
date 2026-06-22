package vn.edu.eaut.lab4;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileLineCounterFrame extends JFrame {
    private JButton btnChoose;
    private JButton btnCount;
    private JButton btnSearch;
    private JTextField txtKeyword;
    private JTextArea txtResult;
    private JLabel lblFile;
    private JLabel lblResult;
    private File selectedFile;

    public FileLineCounterFrame() {
        setTitle("Đếm dòng và tìm kiếm trong file");
        setSize(760, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnChoose = new JButton("Chọn file");
        btnCount = new JButton("Đếm dòng/đoạn");
        btnSearch = new JButton("Tìm kiếm");
        txtKeyword = new JTextField();
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        lblFile = new JLabel("File: chưa chọn");
        lblResult = new JLabel("Kết quả sẽ hiển thị ở đây");

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(lblFile, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        inputPanel.add(txtKeyword, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnChoose);
        buttonPanel.add(btnCount);
        buttonPanel.add(btnSearch);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtResult), BorderLayout.CENTER);
        panel.add(lblResult, BorderLayout.SOUTH);
        add(panel);

        btnChoose.addActionListener(e -> chooseFile());
        btnCount.addActionListener(e -> countLines());
        btnSearch.addActionListener(e -> searchKeyword());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Text/Word files (*.txt, *.docx, *.doc)", "txt", "docx", "doc"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!isSupportedFile(file)) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file .txt, .docx hoặc .doc");
                return;
            }

            selectedFile = file;
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
            txtResult.setText("");
            lblResult.setText("Kết quả sẽ hiển thị ở đây");
        }
    }

    private void countLines() {
        if (!validateSelectedFile()) {
            return;
        }

        setWorking(true);
        txtResult.setText("");
        lblResult.setText("Đang đếm...");

        SwingWorker<Long, Void> worker = new SwingWorker<>() {
            @Override
            protected Long doInBackground() throws Exception {
                return (long) readContentItems(selectedFile).size();
            }

            @Override
            protected void done() {
                try {
                    long count = get();
                    if ("txt".equals(getFileExtension(selectedFile))) {
                        lblResult.setText("Số dòng: " + count);
                    } else {
                        lblResult.setText("Số đoạn văn bản: " + count);
                    }
                } catch (Exception ex) {
                    showReadError(ex);
                }
                setWorking(false);
            }
        };

        worker.execute();
    }

    private void searchKeyword() {
        if (!validateSelectedFile()) {
            return;
        }

        String keyword = txtKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa cần tìm");
            return;
        }

        setWorking(true);
        txtResult.setText("");
        lblResult.setText("Đang tìm kiếm...");

        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                int foundCount = 0;
                int index = 0;
                String lowerKeyword = keyword.toLowerCase(Locale.ROOT);

                for (String item : readContentItems(selectedFile)) {
                    index++;
                    if (item.toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
                        foundCount++;
                        publish(index + ": " + item);
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
                    if ("txt".equals(getFileExtension(selectedFile))) {
                        lblResult.setText("Số dòng tìm thấy: " + foundCount);
                    } else {
                        lblResult.setText("Số đoạn tìm thấy: " + foundCount);
                    }
                } catch (Exception ex) {
                    showReadError(ex);
                }
                setWorking(false);
            }
        };

        worker.execute();
    }

    private List<String> readContentItems(File file) throws Exception {
        String extension = getFileExtension(file);
        if ("txt".equals(extension)) {
            return readTextLines(file);
        }
        if ("docx".equals(extension)) {
            return readDocxParagraphs(file);
        }
        if ("doc".equals(extension)) {
            return readDocParagraphs(file);
        }
        throw new IllegalArgumentException("Chỉ hỗ trợ file .txt, .docx, .doc");
    }

    private List<String> readTextLines(File file) throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private List<String> readDocxParagraphs(File file) throws Exception {
        List<String> paragraphs = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(file.toPath());
             XWPFDocument document = new XWPFDocument(inputStream)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText().trim();
                if (!text.isEmpty()) {
                    paragraphs.add(text);
                }
            }
        }
        return paragraphs;
    }

    private List<String> readDocParagraphs(File file) throws Exception {
        List<String> paragraphs = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(file.toPath());
             HWPFDocument document = new HWPFDocument(inputStream);
             WordExtractor extractor = new WordExtractor(document)) {
            for (String paragraph : extractor.getParagraphText()) {
                String text = paragraph.trim();
                if (!text.isEmpty()) {
                    paragraphs.add(text);
                }
            }
        }
        return paragraphs;
    }

    private boolean validateSelectedFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file trước");
            return false;
        }
        return true;
    }

    private boolean isSupportedFile(File file) {
        String extension = getFileExtension(file);
        return "txt".equals(extension) || "docx".equals(extension) || "doc".equals(extension);
    }

    private void setWorking(boolean working) {
        btnChoose.setEnabled(!working);
        btnCount.setEnabled(!working);
        btnSearch.setEnabled(!working);
    }

    private void showReadError(Exception ex) {
        lblResult.setText("Lỗi khi đọc file");
        JOptionPane.showMessageDialog(
                this,
                ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return "";
        }
        return name.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
