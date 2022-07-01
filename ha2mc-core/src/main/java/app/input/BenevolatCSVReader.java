package app.input;

import app.bean.BenevoleCsv;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BenevolatCSVReader {

    public BenevolatCSVReader() {
    }

    public List<BenevoleCsv> read(File file) {
        List<BenevoleCsv> benevoleCsvList = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            int lineNumber = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (lineNumber > 2) {
                    int colNumber = 0;
                    BenevoleCsv benevoleCsv = new BenevoleCsv();
                    benevoleCsv.setDate(nextRecord[2]);
                    benevoleCsv.setNomPrenom(nextRecord[9]);
                    benevoleCsv.setEmail(nextRecord[10]);
                    benevoleCsv.setTelephone(nextRecord[11]);
                    benevoleCsv.setResolutions(nextRecord[12]);
                    List<String> dispo = remplirDispo(nextRecord);
                    benevoleCsv.setDisponibilites(dispo);
                    List<String> poles = remplirPoles(nextRecord);
                    benevoleCsv.setPoles(poles);
                    List<String> actions = remplirActions(nextRecord);
                    benevoleCsv.setActions(actions);
                    benevoleCsv.setAutonomie(nextRecord[31]);
                    benevoleCsv.setPeurs(nextRecord[32]);
                    benevoleCsv.setAttentes(nextRecord[33]);
                    benevoleCsv.setRemarques(nextRecord[39]);
                    for (String cell : nextRecord) {

                        System.out.print(colNumber + " : " + cell + "\t");
                        colNumber++;
                    }
                    benevoleCsvList.add(benevoleCsv);
                }

                lineNumber++;
                System.out.println();
            }
            System.out.println(benevoleCsvList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return benevoleCsvList;
    }

    private List<String> remplirDispo(String[] nextRecord) {
        List<String> actions = new ArrayList<>();
        if (nextRecord[13] != "") actions.add("soir");
        if (nextRecord[14] != "") actions.add("journée");
        if (nextRecord[15] != "") actions.add("weekend");
        return actions;
    }



    private List<String> remplirPoles(String[] nextRecord) {
        List<String> poles = new ArrayList<>();
        if (nextRecord[16] != "") poles.add("MCMD");
        if (nextRecord[17] != "") poles.add("plaidoyer");
        if (nextRecord[18] != "") poles.add("Abracada'Vrac");
        if (nextRecord[19] != "") poles.add("Stop-Pub");
        if (nextRecord[20] != "") poles.add("Ateliers");
        if (nextRecord[21] != "") poles.add("Evènements");
        if (nextRecord[22] != "") poles.add("Réseau des entrepreneurs.ses\n");
        if (nextRecord[23] != "") poles.add("Les p'tits culottés");
        return poles;
    }

    private List<String> remplirActions(String[] nextRecord) {
        List<String> dispo = new ArrayList<>();
        int i = 24;
        if (nextRecord[i++] != "") dispo.add("communication");
        if (nextRecord[i++] != "") dispo.add("redaction");
        if (nextRecord[i++] != "") dispo.add("action-et-sensibilisation");
        if (nextRecord[i++] != "") dispo.add("veille-juridique-info");
        if (nextRecord[i++] != "") dispo.add("coordination-groupe");
        if (nextRecord[i++] != "") dispo.add("gestion-commu");
        return dispo;
    }

}
