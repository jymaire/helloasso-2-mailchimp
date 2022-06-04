package process;

import model.XlsxModel;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XlsxService {

    public List<XlsxModel> readXlss(String path) {
        List<XlsxModel> xlsxData = new ArrayList<>();

        try (InputStream inp = new FileInputStream(path)) {
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow){
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
                    currentLine.setEntrpriseProjet(entrepriseProjet.getStringCellValue());
                    xlsxData.add(currentLine);

                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return xlsxData;
    }
}
