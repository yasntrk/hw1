package com.example.SEDS519_G1_HW1.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFReader {

	public static List<List<String>> readPdfAsMatrix(String fileName) {
		List<List<String>> matrix = new ArrayList<>();

		try {
			InputStream inputStream = PDFReader.class.getClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				System.out.println("❌ PDF bulunamadı: " + fileName);
				return matrix;
			}

			PDDocument document = PDDocument.load(inputStream);
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			document.close();

			System.out.println("📄 PDF İçeriği:\n" + text);

			String[] allLines = text.split("\\r?\\n");

			// Zaman blokları ve satır sayısı
			String[] timeSlots = { "08:45-09:30", "09:45-10:30", "10:45-11:30", "11:45-12:30" };
			int[] blockSizes = { 4, 7, 9, 7 };

			int pointer = 0;
			for (int t = 0; t < timeSlots.length; t++) {
				int end = pointer + blockSizes[t];

				while (pointer < allLines.length && pointer < end) {
					String line = allLines[pointer++].trim();
					if (line.isEmpty())
						continue;

					// ⚠️ Gereksiz başlıkları ayıkla
					if (line.matches(".*(Pazartesi|Salı|Çarşamba|Perşembe|Cuma).*"))
						continue;
					if (line.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}"))
						continue;
					if (line.matches(".*\\d{2}:\\d{2}.*"))
						continue; // satırda saat varsa at

					// ✅ Gerçek satır
					List<String> row = new ArrayList<>();
					row.add(timeSlots[t]);

					String[] tokens = line.split("\\s+");

					List<String> days = new ArrayList<>();
					StringBuilder current = new StringBuilder();
					for (int i = 0; i < tokens.length; i++) {
						String token = tokens[i];
						if (token.matches("^(D\\d|L\\d)$") && current.length() > 0) {
							days.add(current.toString().trim());
							current = new StringBuilder();
						}
						current.append(token).append(" ");
					}
					if (current.length() > 0) {
						days.add(current.toString().trim());
					}

					// Sabit 5 güne oturt
					for (int i = 0; i < 5; i++) {
						row.add(i < days.size() ? days.get(i) : "");
					}

					matrix.add(row);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n📊 PDF'den Son Haldeki Matris:");
		for (List<String> row : matrix) {
			System.out.println(row);
		}

		return matrix;
	}

}
