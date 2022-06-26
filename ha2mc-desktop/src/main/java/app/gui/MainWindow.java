package app.gui;

import app.input.BenevolatCSVReader;
import app.input.HelloAssoService;
import app.model.BenevoleCsv;
import app.output.BenevoleWriter;
import app.process.ConvertService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Service
public class MainWindow {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private ConvertService convertService;
    private HelloAssoService helloAssoService;
    private BenevolatCSVReader csvReader;
    private BenevoleWriter benevoleWriter;
    public static Properties properties;
    private String existingFilePath = null;
    private StringBuilder importResult;
    private ConfigurableEnvironment configurableEnvironment;

    public MainWindow(HelloAssoService helloAssoService, ConvertService convertService, BenevolatCSVReader csvReader, BenevoleWriter benevoleWriter, StringBuilder importResult, ConfigurableEnvironment configurableEnvironment) {
        this.convertService = convertService;
        this.helloAssoService = helloAssoService;
        this.csvReader = csvReader;
        this.benevoleWriter = benevoleWriter;
        this.importResult = importResult;
        this.configurableEnvironment = configurableEnvironment;
    }

    @PostConstruct
    public void drawWindow() {

        properties = loadConfig();

        JFrame mainWindow = new JFrame();

        // Some window basic configurations
        mainWindow.setSize(500, 200);
        mainWindow.setResizable(true);
        mainWindow.setTitle("Conversion Hello Asso vers MailChimp");
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI for humans
        JTabbedPane tabs = new JTabbedPane();

        JPanel helloAssoBasePanel = createHelloAssoPanel(mainWindow);
        tabs.add("Hello Asso", helloAssoBasePanel);

        JPanel benevolePanel = createBenevolePanel(mainWindow);

        tabs.add("Benevoles", benevolePanel);
        mainWindow.add(tabs);
        mainWindow.setVisible(true);
    }

    private JPanel createBenevolePanel(JFrame mainWindow) {

        JPanel benevoleBasePanel = new JPanel();
        benevoleBasePanel.setLayout(new GridLayout(4, 1, 3, 3));

        // CSV Import

        JButton loadExistingFileButton = new JButton("Choix fichier existant à completer");
        loadExistingFileButton.setSize(40, 20);

        loadExistingFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String retour = selectOldFile(evt);
                Popup ok = new Popup();
                ok.actionPerformed(retour);
            }
        });

        JButton importButton = new JButton("Choix nouveau fichier Framaform");
        importButton.setSize(40, 20);

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String retour = jButtonBenevoleCSVActionPerformed(evt);
                Popup ok = new Popup();
                ok.actionPerformed(retour);
            }
        });
        benevoleBasePanel.add(loadExistingFileButton);
        benevoleBasePanel.add(importButton);
        return benevoleBasePanel;
    }

    private JPanel createHelloAssoPanel(JFrame mainWindow) {
        JPanel helloAssoBasePanel = new JPanel();
        helloAssoBasePanel.setLayout(new BorderLayout(10, 10));

        // Excel Import
        JButton importButton = new JButton("Choix fichier Excel HA");
        importButton.setSize(40, 20);

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String retour = jButtonImportExcelToJtableActionPerformed(evt);
                Popup ok = new Popup();
                ok.actionPerformed(retour);
            }
        });

        // API Import
        JPanel nbDayPanel = new JPanel(new GridLayout(3, 1));
        JTextArea nbDayText = new JTextArea();
        nbDayText.setText("Nombre de jours à récupérer");
        nbDayPanel.add(nbDayText);
        JSpinner nbDay = new JSpinner();
        nbDay.setSize(20, 20);
        nbDay.setValue(1);
        nbDayPanel.add(nbDay);
        JButton helloAssoImportButton = new JButton("Import automatique depuis Hello Asso");
        helloAssoImportButton.setSize(80, 20);

        helloAssoImportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importResult = new StringBuilder();
                importResult.append("<html>");
                try {
                    helloAssoService.getPaymentsFor((Integer) nbDay.getValue());
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage());
                    LOGGER.error(ex.getStackTrace().toString());
                    importResult.append("<br/>");
                    importResult.append("une erreur est survenue")
                            .append(ex.getMessage());

                } finally {
                    Popup recap = new Popup();
                    recap.pack();
                    importResult.append("</html>");
                    recap.actionPerformed(importResult.toString(),1000,300);

                }
            }
        });

        helloAssoBasePanel.add(importButton, BorderLayout.NORTH);
        helloAssoBasePanel.add(nbDayPanel, BorderLayout.LINE_START);
        helloAssoBasePanel.add(helloAssoImportButton, BorderLayout.LINE_END);
        return helloAssoBasePanel;
    }

    private Properties loadConfig() {
        Path currentRelativePath = Paths.get("");
        String absoluteCurrentPath = currentRelativePath.toAbsolutePath().toString();
        Properties prop = new Properties();
        try {
            final Map<String, ?> configuratinFiles = configurableEnvironment.getPropertySources().stream()
                    .filter(propertySource -> propertySource.getName()
                            .contains("application.properties"))
                    .collect(Collectors.toMap(PropertySource::getName, PropertySource::getSource));
            // Load config.properties only if there is not already an application.properties file loaded automatically
            // just the time to do the refacto I guess
            if (configuratinFiles == null || configuratinFiles.isEmpty()){
                prop.load(new FileReader(absoluteCurrentPath + File.separator + "config.properties"));
            }
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

    private String jButtonBenevoleCSVActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser csvFileChooser = new JFileChooser(System.getProperty("user.dir"));
        csvFileChooser.setDialogTitle("Choix du fichier Framaform");
        int excelChooser = csvFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            File selectedFile = csvFileChooser.getSelectedFile();
            List<BenevoleCsv> benevoleCsvList = csvReader.read(selectedFile);
            String retour = benevoleWriter.write(benevoleCsvList, selectedFile.getParent(),existingFilePath);
            System.out.println(retour);
            return retour;
        }
        return "une erreur d'est produite";
    }

    private String selectOldFile(java.awt.event.ActionEvent evt) {
        JFileChooser existingCsvFileChooser = new JFileChooser(System.getProperty("user.dir"));
        existingCsvFileChooser.setDialogTitle("Choix du fichier existant");
        int excelChooser = existingCsvFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            File selectedFile = existingCsvFileChooser.getSelectedFile();
            existingFilePath = selectedFile.getAbsolutePath();
            return existingFilePath;
        }
        return "une erreur d'est produite";
    }
}
