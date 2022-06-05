package app.process;

import app.model.CsvMailChimpModel;
import app.model.XlsxModel;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertService {

    private XlsxService xlsxService;
    private CSVprocessing csvProcessing;

    public ConvertService(XlsxService xlsxService, CSVprocessing csvProcessing) {
        this.xlsxService = xlsxService;
        this.csvProcessing = csvProcessing;
    }

    public String readAndConvertHelloAssoXlsxToCsvMailchimp(String path, String outputPath) {
        List<XlsxModel> xlsxModels = xlsxService.readXlsx(path);
        convert(outputPath, xlsxModels);
        return "fini sans exception, fichier créé dans " + outputPath;
    }

    public void convert(String outputPath, List<XlsxModel> xlsxModels) {
        List<CsvMailChimpModel> csvMailChimpModels = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (XlsxModel xlsxModel : xlsxModels) {
            if ("Validé".equals(xlsxModel.getStatus()) || "Authorized".equals(xlsxModel.getStatus())) {
                CsvMailChimpModel csvMailChimpModel = new CsvMailChimpModel();
                if (xlsxModel.getTarif() != null) csvMailChimpModel.setFormule(xlsxModel.getTarif().stripTrailing());
                csvMailChimpModel.setNom(xlsxModel.getNom().stripTrailing());
                csvMailChimpModel.setPrenom(xlsxModel.getPrenom().stripTrailing());
                if (xlsxModel.getDate() != null) csvMailChimpModel.setDate(xlsxModel.getDate().format(dateFormat));
                csvMailChimpModel.setEmail(xlsxModel.getEmail().stripTrailing());
                if (xlsxModel.getCodePostal() != null) {
                    csvMailChimpModel.setCodePostal(xlsxModel.getCodePostal().substring(0, 5));
                }
                csvMailChimpModel.setEntrepriseProjet(xlsxModel.getEntrepriseProjet());
                csvMailChimpModels.add(csvMailChimpModel);
            } else {
                System.out.println("paiment non valide :" + xlsxModel.getStatus());
            }

        }
        System.out.println("sortie : " + outputPath);
        csvProcessing.createCSVfile(csvMailChimpModels, outputPath);
    }
}
