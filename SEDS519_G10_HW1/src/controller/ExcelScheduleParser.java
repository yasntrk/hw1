package controller;

import model.CourseSchedule;
import model.ScheduleEntry;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelScheduleParser {

    // (İsteğe Bağlı) Türkçe -> İngilizce gün dönüştürme
    private static final Map<String, String> trToEnMap = new HashMap<>();
    static {
        trToEnMap.put("Pazartesi", "Monday");
        trToEnMap.put("Salı", "Tuesday");
        trToEnMap.put("Çarşamba", "Wednesday");
        trToEnMap.put("Perşembe", "Thursday");
        trToEnMap.put("Cuma", "Friday");
        // Gerekirse "Cumartesi", "Pazar" ekleyebilirsiniz
    }

    public static CourseSchedule parseExcel(String filePath) {
        // Tüm veriyi şu yapıda toplayacağız:
        // scheduleMap[day][time] = StringBuilder (birden fazla satır birikebilir)
        Map<String, Map<String, StringBuilder>> scheduleMap = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return new CourseSchedule();

            // 1) Row 0 => Gün başlıklarını oku
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return new CourseSchedule();

            // colDayMap: hangi sütun hangi güne ait?
            // Örnek: colDayMap[1] = "Monday", colDayMap[2] = "Tuesday", ...
            Map<Integer, String> colDayMap = new LinkedHashMap<>();

            short lastCellNum = headerRow.getLastCellNum();
            // Sütun 0'ı "Saat" olarak kabul ediyoruz, 1..N => günler
            for (int c = 1; c < lastCellNum; c++) {
                Cell dayCell = headerRow.getCell(c);
                if (dayCell != null) {
                    String dayText = dayCell.toString().trim();
                    if (!dayText.isEmpty()) {
                        // Türkçe -> İngilizce çevir (opsiyonel)
                        if (trToEnMap.containsKey(dayText)) {
                            dayText = trToEnMap.get(dayText);
                        }
                        colDayMap.put(c, dayText);
                    }
                }
            }

            // 2) Zaman dilimi takibi
            String currentTime = null;

            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                // A sütunu (col=0): eğer boş değilse yeni bir time slot
                Cell timeCell = row.getCell(0);
                if (timeCell != null) {
                    String timeVal = timeCell.toString().trim();
                    if (!timeVal.isEmpty()) {
                        // Yeni zaman dilimi
                        currentTime = timeVal;
                    }
                }

                // Eğer hâlâ time slot yoksa bu satırı atla
                if (currentTime == null) {
                    continue;
                }

                // 3) colDayMap'te tanımlı sütunları dolaş
                for (Map.Entry<Integer, String> entry : colDayMap.entrySet()) {
                    int colIndex = entry.getKey();
                    String dayName = entry.getValue();

                    Cell cell = row.getCell(colIndex);
                    if (cell != null && cell.getCellType() != CellType.BLANK) {
                        String val = cell.toString().trim();
                        if (!val.isEmpty()) {
                            // scheduleMap'te yoksa oluştur
                            scheduleMap.putIfAbsent(dayName, new LinkedHashMap<>());
                            scheduleMap.get(dayName).putIfAbsent(currentTime, new StringBuilder());

                            // Mevcut hücreye satır sonu ekleyerek devam edelim
                            StringBuilder sb = scheduleMap.get(dayName).get(currentTime);
                            if (sb.length() > 0) {
                                sb.append("<br/>"); // her yeni satırı alt satıra yaz
                            }
                            sb.append(val);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4) scheduleMap'i CourseSchedule nesnesine dönüştür
        CourseSchedule courseSchedule = new CourseSchedule();
        for (String day : scheduleMap.keySet()) {
            Map<String, StringBuilder> timesMap = scheduleMap.get(day);
            for (String time : timesMap.keySet()) {
                String content = timesMap.get(time).toString();
                courseSchedule.addEntry(new ScheduleEntry(day, time, content));
            }
        }

        return courseSchedule;
    }
}
