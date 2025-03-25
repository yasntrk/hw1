package view;

import model.CourseSchedule;

import java.util.*;

public class HtmlTabView {

    public static String renderHtml(CourseSchedule pdf, CourseSchedule excel) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tab\">\n")
          .append("  <button class=\"tablinks\" onclick=\"openTab(event, 'PDF')\">PDF</button>\n")
          .append("  <button class=\"tablinks\" onclick=\"openTab(event, 'Excel')\">Excel</button>\n")
          .append("</div>\n");

        sb.append(renderTable("PDF", pdf));
        sb.append(renderTable("Excel", excel));

        return sb.toString();
    }

    private static String renderTable(String tabName, CourseSchedule schedule) {
        // Tüm gün adlarını al
        Set<String> daySet = schedule.getSchedule().keySet(); // Örnek: ["Monday","Tuesday","Wednesday",...]
        
        // Tüm saatleri toplamak için geçici bir set
        Set<String> timeSet = new HashSet<>();
        for (String day : daySet) {
            timeSet.addAll(schedule.getSchedule().get(day).keySet()); 
        }
        
        // Günleri ve saatleri sıralayalım (alfabetik veya istediğin sıraya göre)
        List<String> sortedDays = new ArrayList<>(daySet);
        Collections.sort(sortedDays);  // ["Friday","Monday","Thursday","Tuesday","Wednesday"] gibi çıkabilir
        List<String> sortedTimes = new ArrayList<>(timeSet);
        Collections.sort(sortedTimes); // ["08:45-09:30","09:45-10:30","10:45-11:30","11:45-12:30", ...]

        // HTML tablo başlangıcı
        StringBuilder sb = new StringBuilder();
        sb.append("<div id=\"").append(tabName).append("\" class=\"tabcontent\">\n")
          .append("<table border=\"1\" style=\"border-collapse: collapse; text-align: center;\">\n");

        // Tablo başlığı satırı (günler)
        sb.append("<tr><th></th>");
        for (String day : sortedDays) {
            sb.append("<th>").append(day).append("</th>");
        }
        sb.append("</tr>\n");

        // Her saat satırı
        for (String time : sortedTimes) {
            sb.append("<tr>");
            // Sol sütuna saat yazalım
            sb.append("<td style='background-color:lightgray;'>").append(time).append("</td>");
            
            // Gün sütunları
            for (String day : sortedDays) {
                String course = "";
                if (schedule.getSchedule().containsKey(day)) {
                    Map<String, String> timeMap = schedule.getSchedule().get(day);
                    if (timeMap.containsKey(time)) {
                        course = timeMap.get(time);
                    }
                }
                sb.append("<td>").append(course).append("</td>");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n</div>\n");
        return sb.toString();
    }
}
