package gui;

import javax.swing.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainWindow {

    public MainWindow() {
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

    }
}
