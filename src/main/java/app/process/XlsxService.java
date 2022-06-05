package app.process;

import app.model.XlsxModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
@Service
public class XlsxService {
    Logger logger = Logger.getLogger("csv");

    public List<XlsxModel> readXlsx(String path) {
        List<XlsxModel> xlsxData = new ArrayList<>();

        File file = new File(path);
        String filePath = file.getAbsolutePath();
        File xlFile = new File(filePath);
        if(!xlFile.exists() || xlFile.isDirectory()){
            JPopupMenu erreur = new JPopupMenu();
            erreur.add(new JMenuItem("Le fichier n'existe pas"));
            erreur.setVisible(true);
            return new ArrayList<>();
        }


        try (InputStream inp = new FileInputStream(path)) {
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            Sheet sheet = wb.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false;
                } else {
                    XlsxModel currentLine = new XlsxModel();
                    Cell date = row.getCell(0);
                    currentLine.setDate(date.getLocalDateTimeCellValue());
                    Cell email = row.getCell(1);
                    currentLine.setEmail(email.getStringCellValue());
                    Cell nom = row.getCell(2);
                    currentLine.setNom(nom.getStringCellValue());
                    Cell prenom = row.getCell(3);
                    currentLine.setPrenom(prenom.getStringCellValue());
                    Cell status = row.getCell(4);
                    currentLine.setStatus(status.getStringCellValue());
                    Cell tarif = row.getCell(5);
                    currentLine.setTarif(tarif.getStringCellValue());
                    Cell codePostal = row.getCell(8);
                    currentLine.setCodePostal(Double.toString(codePostal.getNumericCellValue()));
                    Cell entrepriseProjet = row.getCell(9);
                    currentLine.setEntrepriseProjet(entrepriseProjet.getStringCellValue());
                    xlsxData.add(currentLine);
                    logger.info(currentLine.toString());
                }
            }
        } catch (FileNotFoundException e) {
            JPopupMenu menu3 = new JPopupMenu();
            menu3.add(new JMenuItem(e.getMessage()));
            menu3.setVisible(true);
            throw new RuntimeException(e);
        } catch (IOException e) {
            JPopupMenu menu3 = new JPopupMenu();
            menu3.add(new JMenuItem(e.getMessage()));
            menu3.setVisible(true);
            throw new RuntimeException(e);
        }
        return xlsxData;
    }
}
