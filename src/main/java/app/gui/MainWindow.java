package app.gui;

import app.input.HelloAssoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.process.ConvertService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Component
public class MainWindow {

    private ConvertService convertService;

    @Autowired
    private HelloAssoService helloAssoService;

    public static Properties properties;

    public MainWindow() {
        this.convertService = new ConvertService();
        this.helloAssoService = new HelloAssoService();
    }

    public void drawWindow() {

        properties = loadConfig();

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
                String retour = jButtonImportExcelToJtableActionPerformed(evt);
                basePanel.add(importButton);

                JLabel retourLabel = new JLabel();
                retourLabel.setText(retour);
                retourLabel.setVisible(true);
                basePanel.add(retourLabel);
                mainWindow.setContentPane(basePanel);

                mainWindow.repaint();
                mainWindow.revalidate();
            }
        });

        JButton helloAssoImportButton = new JButton("Import depuis Hello Asso");
        helloAssoImportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("clic bouton");
                try {
                    helloAssoService.getPaymentsFor(1);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });
        basePanel.add(importButton);
        basePanel.add(helloAssoImportButton);

        mainWindow.setContentPane(basePanel);
        mainWindow.setVisible(true);
    }

    private Properties loadConfig() {
        Path currentRelativePath = Paths.get("");
        String absoluteCurrentPath = currentRelativePath.toAbsolutePath().toString();
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(absoluteCurrentPath + File.separator + "config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    private String jButtonImportExcelToJtableActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser excelFileChooser = new JFileChooser(System.getProperty("user.dir"));
        excelFileChooser.setDialogTitle("Choix du fichier Hello Asso");
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            File selectedFile = excelFileChooser.getSelectedFile();
            return convertService.convertHelloAssoXlsxToCsvMailchimp(selectedFile.getAbsolutePath(), selectedFile.getParent());
        }
        return "une erreur d'est produite";
    }
}
