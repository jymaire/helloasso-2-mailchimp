package app.process;

import app.model.BenevoleCsv;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class BenevoleWriter {

    public String write(List<BenevoleCsv> benevoleCsvList, String path, String existingFilePath) {

        Workbook wb;
        boolean emptyFile = false;
        InputStream inp = null;
        if (existingFilePath == null) {
            existingFilePath = path + File.separator + "benevoles.xlsx";
            emptyFile = true;
            wb = new XSSFWorkbook();
        } else {

            try {
                inp = new FileInputStream(existingFilePath);
                System.out.println(existingFilePath);
                wb = WorkbookFactory.create(inp);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (OutputStream fileOut = new FileOutputStream(existingFilePath)) {
            Sheet benevoles;
            if (emptyFile) {
                benevoles = wb.createSheet("benevoles");
            } else {
                benevoles = wb.getSheetAt(0);
            }

            // Gestion des lignes déjà écrites
            // On stock les dates qui serviront d'identifiants
            int dateIndex = 0;
            Set<String> dates = new HashSet<>();
            if (!emptyFile) {
                Iterator<Row> rowIterator = benevoles.rowIterator();
                while (rowIterator.hasNext()) {
                    Row currentRow = rowIterator.next();
                    if (currentRow.getCell(dateIndex) != null) {
                        dates.add(currentRow.getCell(dateIndex).getStringCellValue());
                        System.out.println("date found" + currentRow.getRowNum());

                    } else {
                        System.out.println("no date found");
                    }
                }
                System.out.println("date" + dates);

            }

            int firstEmptyLineIndex = 1;
            if (!emptyFile) {
                // get index of first empty line
                int rowCount = 0;
                Iterator<Row> rowIterator = benevoles.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (checkIfRowIsEmpty(row)) {
                        firstEmptyLineIndex = rowCount;
                        System.out.println("row count : " + rowCount);
                        break;
                    }
                    rowCount++;
                    if (!rowIterator.hasNext()) {
                        firstEmptyLineIndex = rowCount;
                        System.out.println("end row count : " + rowCount);

                    }
                }
            }

            List<String> headers = Arrays.asList("date", "nom prenom", "email", "telephone", "resolutions", "dispo", "poles", "actions", "autonomie", "peurs", "attentes", "remarques");
            Row header = benevoles.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers.get(i));
            }


            int rowIndex = 0;
            for (int benevoleIndex = 0; benevoleIndex < benevoleCsvList.size(); benevoleIndex++) {
                int j = 0;
                if (!dates.contains(benevoleCsvList.get(benevoleIndex).getDate())) {
                    System.out.println("ligne nouvelle");
                    rowIndex = addNewLine(benevoleCsvList, benevoles, firstEmptyLineIndex, rowIndex, benevoleIndex, j);

                }
                endProcess(benevoleCsvList, benevoles, headers, benevoleIndex);

            }
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if  (inp != null) {
                try {
                    inp.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return "ok, emplacement du fichier : " + existingFilePath;
    }

    private void endProcess(List<BenevoleCsv> benevoleCsvList, Sheet benevoles, List<String> headers, int benevoleIndex) {
        // traitement de fin
        if (benevoleIndex == benevoleCsvList.size() - 1) {
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

    private int addNewLine(List<BenevoleCsv> benevoleCsvList, Sheet benevoles, int firstEmptyLineIndex, int rowIndex, int benevoleIndex, int j) {
        Row line = benevoles.createRow(rowIndex + firstEmptyLineIndex);
        rowIndex++;
        Cell dateCell = line.createCell(j++);
        dateCell.setCellValue(benevoleCsvList.get(benevoleIndex).getDate());
        Cell nomCell = line.createCell(j++);
        nomCell.setCellValue(benevoleCsvList.get(benevoleIndex).getNomPrenom());
        Cell emailCell = line.createCell(j++);
        emailCell.setCellValue(benevoleCsvList.get(benevoleIndex).getEmail());
        Cell telCell = line.createCell(j++);
        telCell.setCellValue(benevoleCsvList.get(benevoleIndex).getTelephone());
        Cell resolutionsCell = line.createCell(j++);
        resolutionsCell.setCellValue(benevoleCsvList.get(benevoleIndex).getResolutions());
        Cell dispoCell = line.createCell(j++);
        dispoCell.setCellValue(benevoleCsvList.get(benevoleIndex).getDisponibilites()
                .toString().replace("[", "").replace("]", ""));
        Cell polesCell = line.createCell(j++);
        polesCell.setCellValue(benevoleCsvList.get(benevoleIndex).getPoles().toString().replace("[", "").replace("]", ""));
        Cell actionsCell = line.createCell(j++);
        actionsCell.setCellValue(benevoleCsvList.get(benevoleIndex).getActions().toString().replace("[", "").replace("]", ""));
        Cell autonomieCell = line.createCell(j++);
        autonomieCell.setCellValue(benevoleCsvList.get(benevoleIndex).getAutonomie());
        Cell peursCell = line.createCell(j++);
        peursCell.setCellValue(benevoleCsvList.get(benevoleIndex).getPeurs());
        Cell attentesCell = line.createCell(j++);
        attentesCell.setCellValue(benevoleCsvList.get(benevoleIndex).getAttentes());
        Cell remarquesCell = line.createCell(j++);
        remarquesCell.setCellValue(benevoleCsvList.get(benevoleIndex).getRemarques());
        return rowIndex;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }
}
