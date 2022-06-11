package app.process;

import app.gui.MainWindow;
import app.model.CsvMailChimpModel;
import app.model.XlsxModel;
import app.model.mailchimp.MailChimpMember;
import app.output.MailChimpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
public class ConvertService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private XlsxService xlsxService;
    private CSVprocessing csvProcessing;
    private MailChimpService mailChimpService;
    private Properties properties;

    public ConvertService(XlsxService xlsxService, CSVprocessing csvProcessing, MailChimpService mailChimpService) {
        this.xlsxService = xlsxService;
        this.csvProcessing = csvProcessing;
        this.mailChimpService = mailChimpService;
        this.properties = MainWindow.properties;
    }

    public String readAndConvertHelloAssoXlsxToCsvMailchimp(String path, String outputPath) {
        List<XlsxModel> xlsxModels = xlsxService.readXlsx(path);
        convert(outputPath, xlsxModels);
        return "fini sans exception, fichier créé dans " + outputPath;
    }

    public void convert(String outputPath, List<XlsxModel> xlsxModels) {
        List<CsvMailChimpModel> csvMailChimpModels = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<MailChimpMember> mailChimpMembers = new ArrayList<>();
        for (XlsxModel xlsxModel : xlsxModels) {
            if ("Validé".equals(xlsxModel.getStatus()) || "Authorized".equals(xlsxModel.getStatus())) {
                CsvMailChimpModel csvMailChimpModel = new CsvMailChimpModel();
                MailChimpMember mailChimpMember = new MailChimpMember();
                HashMap<String, Object> mergeFields = new HashMap<>();
                mergeFields.put("PAYEMENT", "Carte bancaire");
                if (xlsxModel.getTarif() != null) {
                    csvMailChimpModel.setFormule(xlsxModel.getTarif().stripTrailing());
                    mergeFields.put("FORMULE", xlsxModel.getTarif().stripTrailing());
                }
                csvMailChimpModel.setNom(xlsxModel.getNom().stripTrailing());
                csvMailChimpModel.setPrenom(xlsxModel.getPrenom().stripTrailing());
                mergeFields.put("LNAME", xlsxModel.getNom().stripTrailing());
                mergeFields.put("FNAME", xlsxModel.getPrenom().stripTrailing());
                if (xlsxModel.getDate() != null) {
                    csvMailChimpModel.setDate(xlsxModel.getDate().format(dateFormat));
                    mergeFields.put("DATELASTAD", xlsxModel.getDate().format(dateFormat));
                }
                csvMailChimpModel.setEmail(xlsxModel.getEmail().stripTrailing());
                mailChimpMember.setEmail_address(xlsxModel.getEmail().stripTrailing());
                mailChimpMember.setEmailType("html");
                if (xlsxModel.getCodePostal() != null) {
                    csvMailChimpModel.setCodePostal(xlsxModel.getCodePostal().substring(0, 5));
                    mergeFields.put("CP", xlsxModel.getCodePostal().substring(0, 5));
                }
                csvMailChimpModel.setEntrepriseProjet(xlsxModel.getEntrepriseProjet());
                mergeFields.put("MMERGE3", xlsxModel.getEntrepriseProjet());
                mailChimpMember.setMergeFields(mergeFields);
                mailChimpMember.setStatus("subscribed");
                mailChimpMembers.add(mailChimpMember);
                csvMailChimpModels.add(csvMailChimpModel);
            } else {
                LOGGER.info("paiment non valide :" + xlsxModel.getStatus());
            }

        }
        LOGGER.debug("sortie : " + outputPath);
        if ("true".equals(MainWindow.properties.getProperty("MAIL_CHIMP_AUTO"))) {
            mailChimpService.addMembers(mailChimpMembers);
        }
        csvProcessing.createCSVfile(csvMailChimpModels, outputPath);
    }
}
