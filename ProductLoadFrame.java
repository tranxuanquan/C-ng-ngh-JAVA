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

public class ProductLoadFrame extends JFrame {
    private final File defaultCsvFile = new File("products.csv");

    private JTextField txtProductId;
    private JTextField txtProductName;
    private JTextField txtPrice;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReadCsv;
    private JButton btnSaveCsv;
    private JTable table;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private File selectedCsvFile;

    public ProductLoadFrame() {
        setTitle("Bài 10 - Quản lý sản phẩm bằng file CSV");
        setSize(820, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        selectedCsvFile = defaultCsvFile;

        txtProductId = new JTextField();
        txtProductName = new JTextField();
        txtPrice = new JTextField();
        btnAdd = new JButton("Thêm sản phẩm");
        btnUpdate = new JButton("Sửa sản phẩm");
        btnDelete = new JButton("Xóa sản phẩm");
        btnReadCsv = new JButton("Đọc file CSV");
        btnSaveCsv = new JButton("Lưu file CSV");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblStatus = new JLabel("File hiện tại: " + selectedCsvFile.getAbsolutePath());

        tableModel = new DefaultTableModel(new Object[]{"Mã SP", "Tên SP", "Đơn giá"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.add(new JLabel("Mã SP:"));
        formPanel.add(txtProductId);
        formPanel.add(new JLabel("Tên SP:"));
        formPanel.add(txtProductName);
        formPanel.add(new JLabel("Đơn giá:"));
        formPanel.add(txtPrice);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(btnAdd);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnReadCsv);
        actionPanel.add(btnSaveCsv);

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        bottomPanel.add(progressBar);
        bottomPanel.add(lblStatus);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnReadCsv.addActionListener(e -> chooseAndReadCsv());
        btnSaveCsv.addActionListener(e -> chooseAndSaveCsv());
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());
    }

    private void addProduct() {
        Product product = readProductFromForm();
        if (product == null) {
            return;
        }

        if (findRowByProductId(product.id()) != -1) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại");
            return;
        }

        tableModel.addRow(new Object[]{product.id(), product.name(), product.price()});
        clearForm();
        lblStatus.setText("Đã thêm sản phẩm: " + product.id());
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa");
            return;
        }

        Product product = readProductFromForm();
        if (product == null) {
            return;
        }

        int existedRow = findRowByProductId(product.id());
        if (existedRow != -1 && existedRow != row) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại");
            return;
        }

        tableModel.setValueAt(product.id(), row, 0);
        tableModel.setValueAt(product.name(), row, 1);
        tableModel.setValueAt(product.price(), row, 2);
        lblStatus.setText("Đã cập nhật sản phẩm: " + product.id());
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
            return;
        }

        String productId = tableModel.getValueAt(row, 0).toString();
        tableModel.removeRow(row);
        clearForm();
        lblStatus.setText("Đã xóa sản phẩm: " + productId);
    }

    private void chooseAndReadCsv() {
        JFileChooser chooser = createCsvFileChooser();
        chooser.setSelectedFile(selectedCsvFile);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedCsvFile = chooser.getSelectedFile();
            readCsvWithWorker(selectedCsvFile);
        }
    }

    private void chooseAndSaveCsv() {
        JFileChooser chooser = createCsvFileChooser();
        chooser.setSelectedFile(selectedCsvFile);

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedCsvFile = ensureCsvExtension(chooser.getSelectedFile());
            saveCsvWithWorker(selectedCsvFile);
        }
    }

    private void readCsvWithWorker(File csvFile) {
        setWorking(true);
        tableModel.setRowCount(0);
        progressBar.setIndeterminate(true);
        lblStatus.setText("Đang đọc file CSV...");

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                List<Product> products = new ArrayList<>();

                try (BufferedReader reader = Files.newBufferedReader(csvFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    boolean firstLine = true;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        if (firstLine) {
                            firstLine = false;
                            if (line.toLowerCase(Locale.ROOT).startsWith("masp,")) {
                                continue;
                            }
                        }
                        products.add(parseProduct(line));
                    }
                }

                return products;
            }

            @Override
            protected void done() {
                try {
                    List<Product> products = get();
                    for (Product product : products) {
                        tableModel.addRow(new Object[]{product.id(), product.name(), product.price()});
                    }
                    lblStatus.setText("Đã đọc " + products.size() + " sản phẩm từ " + csvFile.getName());
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi khi đọc file CSV");
                    JOptionPane.showMessageDialog(
                            ProductLoadFrame.this,
                            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                }

                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                setWorking(false);
            }
        };

        worker.execute();
    }

    private void saveCsvWithWorker(File csvFile) {
        setWorking(true);
        progressBar.setIndeterminate(true);
        lblStatus.setText("Đang lưu file CSV...");

        List<Product> products = getProductsFromTable();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<String> lines = new ArrayList<>();
                lines.add("MaSP,TenSP,DonGia");
                for (Product product : products) {
                    lines.add(toCsvLine(product));
                }
                Files.write(csvFile.toPath(), lines, StandardCharsets.UTF_8);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    lblStatus.setText("Đã lưu " + products.size() + " sản phẩm vào " + csvFile.getName());
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi khi lưu file CSV");
                    JOptionPane.showMessageDialog(
                            ProductLoadFrame.this,
                            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                }

                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                setWorking(false);
            }
        };

        worker.execute();
    }

    private Product readProductFromForm() {
        String id = txtProductId.getText().trim();
        String name = txtProductName.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (id.isEmpty() || name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ mã, tên và đơn giá");
            return null;
        }

        int price;
        try {
            price = Integer.parseInt(priceText);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải >= 0");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số nguyên");
            return null;
        }

        return new Product(id, name, price);
    }

    private Product parseProduct(String line) {
        List<String> columns = parseCsvLine(line);
        if (columns.size() != 3) {
            throw new IllegalArgumentException("Dòng CSV không hợp lệ: " + line);
        }

        String id = columns.get(0).trim();
        String name = columns.get(1).trim();
        int price;
        try {
            price = Integer.parseInt(columns.get(2).trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Đơn giá không hợp lệ: " + columns.get(2));
        }

        return new Product(id, name, price);
    }

    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
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

    private String toCsvLine(Product product) {
        return escapeCsv(product.id()) + "," + escapeCsv(product.name()) + "," + product.price();
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private List<Product> getProductsFromTable() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            products.add(new Product(
                    tableModel.getValueAt(i, 0).toString(),
                    tableModel.getValueAt(i, 1).toString(),
                    Integer.parseInt(tableModel.getValueAt(i, 2).toString())
            ));
        }
        return products;
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        txtProductId.setText(tableModel.getValueAt(row, 0).toString());
        txtProductName.setText(tableModel.getValueAt(row, 1).toString());
        txtPrice.setText(tableModel.getValueAt(row, 2).toString());
    }

    private int findRowByProductId(String productId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (productId.equalsIgnoreCase(tableModel.getValueAt(i, 0).toString())) {
                return i;
            }
        }
        return -1;
    }

    private JFileChooser createCsvFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
        return chooser;
    }

    private File ensureCsvExtension(File file) {
        if (file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".csv");
    }

    private void clearForm() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtPrice.setText("");
        table.clearSelection();
    }

    private void setWorking(boolean working) {
        btnAdd.setEnabled(!working);
        btnUpdate.setEnabled(!working);
        btnDelete.setEnabled(!working);
        btnReadCsv.setEnabled(!working);
        btnSaveCsv.setEnabled(!working);
    }

    private record Product(String id, String name, int price) {
    }
}
