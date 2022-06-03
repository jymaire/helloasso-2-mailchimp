package process;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVprocessing {

    public void createCSVfile(){
        String[] header = {"Formule","Moyen de paiement","Nom","Prénom","Société","Date","Email","Champ additionnel: Code Postal","Champ additionnel: Entreprise/Projet"};
        List<String[]> list = new ArrayList<>();

        list.add(header);

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
