package app.process;

import app.model.BenevoleCsv;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
public class BenevoleWriter {

    public String write(List<BenevoleCsv> benevoleCsvList, String path) {

        Workbook wb = new XSSFWorkbook();
        String filePath = path + File.separator + "benevoles.xlsx";
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            Sheet benevoles = wb.createSheet("benevoles");
            List<String> headers = Arrays.asList("date", "nom prenom", "email", "telephone", "poles");
            Row header = benevoles.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            for (int i = 0; i < benevoleCsvList.size(); i++) {
                Row line = benevoles.createRow(i + 1);
                Cell dateCell = line.createCell(0);
                dateCell.setCellValue(benevoleCsvList.get(i).getDate());
                Cell nomCell = line.createCell(1);
                nomCell.setCellValue(benevoleCsvList.get(i).getNomPrenom());

            }


            wb.write(fileOut);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "ok" + filePath;
    }
}
