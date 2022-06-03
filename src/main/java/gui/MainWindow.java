package gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import process.CSVprocessing;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
@Component
public class MainWindow {
    private CSVprocessing csVprocessing;

    public MainWindow() {
        this.csVprocessing = new CSVprocessing();
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

        //here only for test purposes
        csVprocessing.createCSVfile();
    }

    private void jButtonImportExcelToJtableActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser excelFileChooser = new JFileChooser(System.getProperty("user.dir"));
        excelFileChooser.setDialogTitle("Choix du fichier Hello Asso");
        int excelChooser = excelFileChooser.showOpenDialog(null);

    }
}
