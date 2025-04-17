package com.example.SEDS519_G1_HW1.service;

import com.example.SEDS519_G1_HW1.model.CourseSchedule;
import com.example.SEDS519_G1_HW1.util.ExcelReader;
import com.example.SEDS519_G1_HW1.util.PDFReader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    public List<List<String>> getExcelSchedule() {
        return ExcelReader.readExcelAsMatrix("2024-25CourseSchedule.xlsx");
    }

    public List<List<String>> getPdfSchedule() {
        return PDFReader.readPdfAsMatrix("2024-25CourseSchedule.pdf");
    }
}
