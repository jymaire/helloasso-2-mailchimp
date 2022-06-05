package app.process;

import app.model.CsvMailChimpModel;
import app.model.XlsxModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConvertService {


    private XlsxService xlsxService;
    private CSVprocessing csVprocessing;

    public ConvertService() {
        this.xlsxService = new XlsxService();
        this.csVprocessing = new CSVprocessing();
    }

    public String readAndConvertHelloAssoXlsxToCsvMailchimp(String path, String outputPath) {
        List<XlsxModel> xlsxModels = xlsxService.readXlsx(path);
        List<CsvMailChimpModel> csvMailChimpModels = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        convert(outputPath, xlsxModels, csvMailChimpModels, dateFormat);
        return "fini sans exception, fichier créé dans " + outputPath;
    }

    private void convert(String outputPath, List<XlsxModel> xlsxModels, List<CsvMailChimpModel> csvMailChimpModels, DateTimeFormatter dateFormat) {
        for (XlsxModel xlsxModel : xlsxModels) {
            if ("Validé".equals(xlsxModel.getStatus()) || "Authorized".equals(xlsxModel.getStatus())) {
                CsvMailChimpModel csvMailChimpModel = new CsvMailChimpModel();
                csvMailChimpModel.setFormule(xlsxModel.getTarif().stripTrailing());
                csvMailChimpModel.setNom(xlsxModel.getNom().stripTrailing());
                csvMailChimpModel.setPrenom(xlsxModel.getPrenom().stripTrailing());
                csvMailChimpModel.setDate(xlsxModel.getDate().format(dateFormat));
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
        csVprocessing.createCSVfile(csvMailChimpModels, outputPath);
    }
}
