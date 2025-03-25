package controller;

import model.CourseSchedule;

public class ScheduleController {
    private CourseSchedule pdfSchedule;
    private CourseSchedule excelSchedule;

    public void loadSchedules(String pdfPath, String excelPath) {
        pdfSchedule = PdfScheduleParser.parsePDF(pdfPath);
        excelSchedule = ExcelScheduleParser.parseExcel(excelPath);
    }

    public CourseSchedule getPdfSchedule() { return pdfSchedule; }
    public CourseSchedule getExcelSchedule() { return excelSchedule; }
}
