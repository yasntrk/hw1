package controller;

import model.CourseSchedule;
import model.ScheduleEntry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfScheduleParser {
    // Ardışık tekrarları silen metot
    private static List<String> removeConsecutiveDuplicates(String[] tokens) {
        List<String> result = new ArrayList<>();
        String prev = null;
        for (String t : tokens) {
            t = t.trim();
            if (!t.isEmpty()) {
                if (!t.equals(prev)) {
                    result.add(t);
                }
                prev = t;
            }
        }
        return result;
    }

    public static CourseSchedule parsePDF(String filePath) {
        CourseSchedule schedule = new CourseSchedule();

        // Sabit 5 gün, 4 saat (örnek)
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] times = {
            "08:45-09:30",
            "09:45-10:30",
            "10:45-11:30",
            "11:45-12:30"
        };

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            String text = new PDFTextStripper().getText(document);
            // PDF'yi satırlara böl
            String[] lines = text.split("\\r?\\n");

            int timeIndex = 0; // 0..3 arası gider
            for (String line : lines) {
                line = line.trim();
                // Boş satırsa geç
                if (line.isEmpty()) {
                    continue;
                }
                // Ardışık tekrarları sil
                List<String> tokens = removeConsecutiveDuplicates(line.split("\\s+"));
                // 3’lü bloklar hâlinde birleştir (D1 ENG102 SA)
                List<String> cellDataList = new ArrayList<>();

                for (int i = 0; i < tokens.size(); i += 3) {
                    if (i + 2 < tokens.size()) {
                        String cellData = tokens.get(i) + " " + tokens.get(i+1) + " " + tokens.get(i+2);
                        cellDataList.add(cellData);
                    }
                }

                // cellDataList => bu zaman aralığı için en fazla 5 hücre
                // (5 günden fazla varsa, fazlasını at)
                for (int d = 0; d < cellDataList.size() && d < 5; d++) {
                    schedule.addEntry(new ScheduleEntry(
                            days[d],         // Monday..Friday
                            times[timeIndex],// 08:45.. vs
                            cellDataList.get(d)
                    ));
                }

                timeIndex++;
                // Yalnızca 4 zaman aralığı gösterilecekse
                if (timeIndex >= times.length) {
                    break; // Fazla satırlar varsa okumadan çıkar
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedule;
    }
}
