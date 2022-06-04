package process;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import model.CsvMailChimpModel;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVprocessing {

    public void createCSVfile( List<CsvMailChimpModel> csvMailChimpModels){
        String[] header = {"Formule","Moyen de paiement","Nom","Prénom","Société","Date","Email","Champ additionnel: Code Postal","Champ additionnel: Entreprise/Projet"};
        List<String[]> list = new ArrayList<>();

        list.add(header);

        for (CsvMailChimpModel csvMailChimpModel : csvMailChimpModels){
            String[] data = new String[9];
            data[0] = csvMailChimpModel.getFormule();
            data[1] = csvMailChimpModel.getMoyenDePaiement();
            data[2] = csvMailChimpModel.getNom();
            data[3] = csvMailChimpModel.getPrenom();
            data[4] = csvMailChimpModel.getSociete();
            data[5] = csvMailChimpModel.getDate();
            data[6] = csvMailChimpModel.getEmail();
            data[7] = csvMailChimpModel.getCodePostal();
            data[8] = csvMailChimpModel.getEntrepriseProjet();
            list.add(data);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String fileName = "MailChimp" + dtf.format(now) + ".csv";

        try (ICSVWriter writer = new CSVWriterBuilder(
                new FileWriter(fileName))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {
            writer.writeAll(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
