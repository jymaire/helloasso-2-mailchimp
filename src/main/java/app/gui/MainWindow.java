package app.gui;

import app.input.HelloAssoService;
import app.process.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
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

@Service
public class MainWindow {

    private ConvertService convertService;

    @Autowired
    private HelloAssoService helloAssoService;

    public static Properties properties;

    public MainWindow(HelloAssoService helloAssoService) {
        this.helloAssoService = helloAssoService;
        this.convertService = new ConvertService();
  //      this.helloAssoService = new HelloAssoService();
    }

    @PostConstruct
    public void drawWindow() {

        properties = loadConfig();

        JFrame mainWindow = new JFrame();

        // Some window basic configurations
        mainWindow.setSize(400, 100);
        mainWindow.setResizable(true);
        mainWindow.setTitle("Conversion Hello Asso vers MailChimp");
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI for humans
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout());

        // Excel Import
        JButton importButton = new JButton("Import");
        importButton.setSize(40,20);

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

        // API Import
        JSpinner nbDay = new JSpinner();
        nbDay.setSize(20,20);

        JButton helloAssoImportButton = new JButton("Import depuis Hello Asso");
        helloAssoImportButton.setSize(80,20);

        helloAssoImportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("clic bouton");
                try {
                    helloAssoService.getPaymentsFor((Integer) nbDay.getValue());
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });
        basePanel.add(importButton, BorderLayout.NORTH);
        basePanel.add(nbDay, BorderLayout.LINE_START);
        basePanel.add(helloAssoImportButton, BorderLayout.LINE_END);

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
            return convertService.readAndConvertHelloAssoXlsxToCsvMailchimp(selectedFile.getAbsolutePath(), selectedFile.getParent());
        }
        return "une erreur d'est produite";
    }
}
