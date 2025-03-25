import controller.ScheduleController;
import view.GuiPreview;
import view.HtmlTabView;

public class Main {
    public static void main(String[] args) {
        String pdfPath = "2024-25CourseSchedule.pdf";
        String excelPath = "2024-25CourseSchedule.xlsx";

        ScheduleController controller = new ScheduleController();
        controller.loadSchedules(pdfPath, excelPath);

        String html = HtmlTabView.renderHtml(controller.getPdfSchedule(), controller.getExcelSchedule());
        GuiPreview.showHtml(html);
    }
}
