package com.example.SEDS519_G1_HW1.controller;

import com.example.SEDS519_G1_HW1.model.CourseSchedule;
import com.example.SEDS519_G1_HW1.service.ScheduleService;
import com.example.SEDS519_G1_HW1.util.ExcelReader;
import com.example.SEDS519_G1_HW1.util.PDFReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @GetMapping("/")
    public String schedule(Model model) {
        List<List<String>> excelMatrix = ExcelReader.readExcelAsMatrix("2024-25CourseSchedule.xlsx");
        List<List<String>> pdfMatrix = PDFReader.readPdfAsMatrix("2024-25CourseSchedule.pdf");
        model.addAttribute("excelMatrix", excelMatrix);
        model.addAttribute("pdfMatrix", pdfMatrix);
        return "schedule";
    }
   
}
