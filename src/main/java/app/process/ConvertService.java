package app.process;

import app.gui.MainWindow;
import app.model.CsvMailChimpModel;
import app.model.XlsxModel;
import app.model.mailchimp.MailChimpMember;
import app.model.mailchimp.MailJetContact;
import app.model.mailchimp.MailJetContactMetadata;
import app.output.MailChimpService;
import app.output.MailJetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ConvertService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private XlsxService xlsxService;
    private CSVprocessing csvProcessing;
    private MailChimpService mailChimpService;

    private MailJetService mailJetService;
    private Properties properties;

    public ConvertService(XlsxService xlsxService, CSVprocessing csvProcessing, MailChimpService mailChimpService, MailJetService mailJetService) {
        this.xlsxService = xlsxService;
        this.csvProcessing = csvProcessing;
        this.mailChimpService = mailChimpService;
        this.mailJetService = mailJetService;
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
        // Key is the email
        Map<String, MailJetContact> mailJetContactMap = new HashMap();
        Map<String, MailJetContactMetadata> mailJetContactMetadataMap = new HashMap<>();
        for (XlsxModel xlsxModel : xlsxModels) {
            try {
                if ("Validé".equals(xlsxModel.getStatus()) || "Authorized".equals(xlsxModel.getStatus())) {
                    CsvMailChimpModel csvMailChimpModel = new CsvMailChimpModel();
                    MailChimpMember mailChimpMember = new MailChimpMember();
                    MailJetContact mailJetContact = new MailJetContact();
                    MailJetContactMetadata mailJetContactMetadata = new MailJetContactMetadata();
                    HashMap<String, Object> mergeFields = new HashMap<>();
                    mergeFields.put("PAYEMENT", "Carte bancaire");
                    mailJetContactMetadata.getListOfMmetadata().put("paiement", "Carte bancaire");
                    if (xlsxModel.getTarif() != null) {
                        csvMailChimpModel.setFormule(xlsxModel.getTarif().stripTrailing());
                        mergeFields.put("FORMULE", xlsxModel.getTarif().stripTrailing());
                        mailJetContactMetadata.getListOfMmetadata().put("formule", xlsxModel.getTarif().stripTrailing());

                    }
                    csvMailChimpModel.setNom(xlsxModel.getNom().stripTrailing());
                    csvMailChimpModel.setPrenom(xlsxModel.getPrenom().stripTrailing());
                    mergeFields.put("LNAME", xlsxModel.getNom().stripTrailing());
                    mailJetContactMetadata.getListOfMmetadata().put("nom", xlsxModel.getNom().stripTrailing());
                    mergeFields.put("FNAME", xlsxModel.getPrenom().stripTrailing());
                    mailJetContactMetadata.getListOfMmetadata().put("prénom", xlsxModel.getPrenom().stripTrailing());

                    if (xlsxModel.getDate() != null) {
                        csvMailChimpModel.setDate(xlsxModel.getDate().format(dateFormat));
                        mergeFields.put("DATELASTAD", xlsxModel.getDate().format(dateFormat));
                        mailJetContactMetadata.getListOfMmetadata().put("date",xlsxModel.getDate().format(dateFormat));

                    }
                    csvMailChimpModel.setEmail(xlsxModel.getEmail().stripTrailing());
                    mailChimpMember.setEmail_address(xlsxModel.getEmail().stripTrailing());
                    mailJetContact.setEmail(xlsxModel.getEmail().stripTrailing());
                    mailJetContactMetadata.getListOfMmetadata().put("email",xlsxModel.getEmail().stripTrailing());

                    mailChimpMember.setEmail_type("html");
                    if (xlsxModel.getCodePostal() != null) {
                        csvMailChimpModel.setCodePostal(xlsxModel.getCodePostal().substring(0, 5));
                        mergeFields.put("CP", xlsxModel.getCodePostal().substring(0, 5));
                        mailJetContactMetadata.getListOfMmetadata().put("postal", xlsxModel.getCodePostal().substring(0, 5));

                    }
                    csvMailChimpModel.setEntrepriseProjet(xlsxModel.getEntrepriseProjet());
                    mergeFields.put("MMERGE3", xlsxModel.getEntrepriseProjet());
                    mailJetContactMetadata.getListOfMmetadata().put("entreprise", xlsxModel.getEntrepriseProjet());
                    mailChimpMember.setMerge_fields(mergeFields);
                    mailChimpMember.setStatus("subscribed");
                    mailChimpMembers.add(mailChimpMember);
                    csvMailChimpModels.add(csvMailChimpModel);
                    mailJetContactMap.put(mailJetContact.getEmail(),mailJetContact);
                    mailJetContactMetadataMap.put(mailJetContact.getEmail(), mailJetContactMetadata);
                } else {
                    LOGGER.info("paiment non valide :" + xlsxModel.getStatus());
                }
            } catch (Exception e) {
                LOGGER.error("Erreur : {}", e.getMessage());
            }
        }
        LOGGER.debug("sortie : " + outputPath);
       /* if ("true".equals(MainWindow.properties.getProperty("MAIL_CHIMP_AUTO"))) {
            mailChimpService.addMembers(mailChimpMembers);
        }*/
        if ("true".equals(MainWindow.properties.getProperty("MAIL_JET_AUTO"))) {
            mailJetService.addMembers(mailJetContactMap,mailJetContactMetadataMap);
        }
        csvProcessing.createCSVfile(csvMailChimpModels, outputPath);
    }
}
