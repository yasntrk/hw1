package com.example.SEDS519_G1_HW1.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

	public static List<List<String>> readExcelAsMatrix(String fileName) {
	    List<List<String>> matrix = new ArrayList<>();

	    try {
	        InputStream inputStream = ExcelReader.class.getClassLoader().getResourceAsStream(fileName);
	        if (inputStream == null) {
	            System.out.println("❌ Dosya bulunamadı: " + fileName);
	            return matrix;
	        }

	        Workbook workbook = new XSSFWorkbook(inputStream);
	        Sheet sheet = workbook.getSheetAt(0);

	        // Saat aralıkları (A sütunu)
	        int[] startRows = {1, 5, 12, 21}; // Satırlar 0-index
	        int[] endRows =   {4, 11, 20, 28};
	        String[] timeSlots = {"08:45-09:30", "09:45-10:30", "10:45-11:30", "11:45-12:30"};

	        int[][] dayCols = {
	            {1, 2, 3},   // Monday
	            {4, 5, 6},   // Tuesday
	            {7, 8, 9},   // Wednesday
	            {10, 11, 12},// Thursday
	            {13, 14, 15} // Friday
	        };

	        for (int t = 0; t < timeSlots.length; t++) {
	            for (int r = startRows[t]; r <= endRows[t]; r++) {
	                Row row = sheet.getRow(r);
	                if (row == null) continue;

	                List<String> rowList = new ArrayList<>();
	                rowList.add(timeSlots[t]); // ilk sütun: saat

	                for (int[] cols : dayCols) {
	                    StringBuilder content = new StringBuilder();
	                    for (int c : cols) {
	                        Cell cell = row.getCell(c);
	                        if (cell != null && cell.getCellType() != CellType.BLANK) {
	                            content.append(cell.toString().trim()).append(" ");
	                        }
	                    }
	                    rowList.add(content.toString().trim());
	                }

	                matrix.add(rowList);
	            }
	        }

	        workbook.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // 👇👇 Ekle: matrisi yazdır
	    System.out.println("📊 Excel Matrisi:");
	    for (List<String> row : matrix) {
	        System.out.println(row);
	    }

	    return matrix;
	}

}
