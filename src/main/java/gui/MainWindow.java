package gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import process.CSVprocessing;
import process.ConvertService;
import process.XlsxService;

import javax.swing.*;

import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
@Component
public class MainWindow {
    private CSVprocessing csVprocessing;
    private ConvertService convertService;

    public MainWindow() {
        this.csVprocessing = new CSVprocessing();
        this.convertService = new ConvertService();
    }

    public void drawWindow() {
        JFrame mainWindow = new JFrame();

        // Some window basic configurations
        mainWindow.setSize(200, 200);
        mainWindow.setResizable(true);
        mainWindow.setTitle("Conversion Hello Asso vers MailChimp");
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI for humans
        JPanel basePanel = new JPanel();
        JButton importButton = new JButton("Import");

        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportExcelToJtableActionPerformed(evt);
            }
        });
        basePanel.add(importButton);

        mainWindow.setContentPane(basePanel);
        mainWindow.setVisible(true);
    }

    private void jButtonImportExcelToJtableActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser excelFileChooser = new JFileChooser(System.getProperty("user.dir"));
        excelFileChooser.setDialogTitle("Choix du fichier Hello Asso");
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            File selectedFile = excelFileChooser.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            convertService.convertHelloAssoXlsxToCsvMailchimp(selectedFile.getAbsolutePath());
        }
    }
}
