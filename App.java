package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuFrame().setVisible(true));
    }

    private static class MainMenuFrame extends JFrame {
        public MainMenuFrame() {
            setTitle("Lab04 - Menu");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            String[] apps = {"Countdown", "Keyword Search", "Fibonacci", "File Tools", "Prime Sum", "Student CSV Stats", "Product Manager"};
            JList<String> list = new JList<>(apps);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JButton btnOpen = new JButton("Open");
            btnOpen.addActionListener(e -> {
                String sel = list.getSelectedValue();
                if (sel == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ứng dụng");
                    return;
                }
                final JFrame[] frameRef = new JFrame[1];
                switch (sel) {
                    case "Countdown": frameRef[0] = new CountdownFrame(); break;
                    case "Keyword Search": frameRef[0] = new ProgressDemoFrame(); break;
                    case "Fibonacci": frameRef[0] = new FibonacciFrame(); break;
                    case "File Tools": frameRef[0] = new FileLineCounterFrame(); break;
                    case "Prime Sum": frameRef[0] = new PrimeSumFrame(); break;
                    case "Student CSV Stats": frameRef[0] = new StudentCsvStatsFrame(); break;
                    case "Product Manager": frameRef[0] = new ProductLoadFrame(); break;
                }
                if (frameRef[0] != null) {
                    // hide menu while child frame is open
                    MainMenuFrame.this.setVisible(false);
                    // add a Back button to the top of the child frame
                    JButton btnBackToMenu = new JButton("Quay lại");
                    btnBackToMenu.addActionListener(ae -> {
                        frameRef[0].dispose();
                        MainMenuFrame.this.setVisible(true);
                    });
                    try {
                        frameRef[0].getContentPane().add(btnBackToMenu, BorderLayout.NORTH);
                    } catch (Exception ignored) {
                    }
                    // ensure menu re-appears when child frame closes
                    frameRef[0].addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            MainMenuFrame.this.setVisible(true);
                        }

                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            MainMenuFrame.this.setVisible(true);
                        }
                    });
                    frameRef[0].setVisible(true);
                }
            });

            JPanel panel = new JPanel(new BorderLayout(10,10));
            panel.add(new JScrollPane(list), BorderLayout.CENTER);
            JPanel south = new JPanel();
            south.add(btnOpen);
            panel.add(south, BorderLayout.SOUTH);
            add(panel);
        }
    }
}
