package app.process;

import app.model.BenevoleCsv;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
public class BenevoleWriter {

    public String write(List<BenevoleCsv> benevoleCsvList, String path, String existingFilePath) {

        Workbook wb = new XSSFWorkbook();
        if (existingFilePath == null) {
            existingFilePath = path + File.separator + "benevoles.xlsx";
        }
        try (OutputStream fileOut = new FileOutputStream(existingFilePath)) {
            Sheet benevoles = wb.createSheet("benevoles");
            List<String> headers = Arrays.asList("date", "nom prenom", "email", "telephone", "resolutions", "dispo", "poles", "actions", "autonomie", "peurs", "attentes", "remarques");
            Row header = benevoles.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            for (int i = 0; i < benevoleCsvList.size(); i++) {
                int j = 0;
                Row line = benevoles.createRow(i + 1);
                Cell dateCell = line.createCell(j++);
                dateCell.setCellValue(benevoleCsvList.get(i).getDate());
                Cell nomCell = line.createCell(j++);
                nomCell.setCellValue(benevoleCsvList.get(i).getNomPrenom());
                Cell emailCell = line.createCell(j++);
                emailCell.setCellValue(benevoleCsvList.get(i).getEmail());
                Cell telCell = line.createCell(j++);
                telCell.setCellValue(benevoleCsvList.get(i).getTelephone());
                Cell resolutionsCell = line.createCell(j++);
                resolutionsCell.setCellValue(benevoleCsvList.get(i).getResolutions());
                Cell dispoCell = line.createCell(j++);
                dispoCell.setCellValue(benevoleCsvList.get(i).getDisponibilites()
                        .toString().replace("[", "").replace("]", ""));
                Cell polesCell = line.createCell(j++);
                polesCell.setCellValue(benevoleCsvList.get(i).getPoles().toString().replace("[", "").replace("]", ""));
                Cell actionsCell = line.createCell(j++);
                actionsCell.setCellValue(benevoleCsvList.get(i).getActions().toString().replace("[", "").replace("]", ""));
                Cell autonomieCell = line.createCell(j++);
                autonomieCell.setCellValue(benevoleCsvList.get(i).getAutonomie());
                Cell peursCell = line.createCell(j++);
                peursCell.setCellValue(benevoleCsvList.get(i).getPeurs());
                Cell attentesCell = line.createCell(j++);
                attentesCell.setCellValue(benevoleCsvList.get(i).getAttentes());
                Cell remarquesCell = line.createCell(j++);
                remarquesCell.setCellValue(benevoleCsvList.get(i).getRemarques());

                // traitement de fin
                if (i == benevoleCsvList.size() - 1) {
                    // resize columns
                    for (int k = 0; k < headers.size(); k++) {
                        benevoles.autoSizeColumn(k);
                        System.out.println(benevoles.getColumnWidth(k));
                        int maxSizeColumn = 4000;
                        if (benevoles.getColumnWidth(k) > maxSizeColumn) benevoles.setColumnWidth(k, maxSizeColumn);
                    }
                    // Set filter
                    benevoles.setAutoFilter(new CellRangeAddress(0, benevoleCsvList.size(), 0, headers.size() - 1));
                }
            }
            wb.write(fileOut);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "ok, emplacement du fichier : " + existingFilePath;
    }
}
