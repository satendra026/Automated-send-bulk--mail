package com.example.senEmail;

import com.example.senEmail.EmailTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;

//@SpringBootApplication
//public class SenEmailApplication implements CommandLineRunner {
//
//    private final EmailTemplate emailService;
//
//    public SenEmailApplication(EmailTemplate emailService) {
//        this.emailService = emailService;
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(SenEmailApplication.class, args);
//    }
//
//    @Override
//    public void run(String... args) {
//        System.out.println(">>> CommandLineRunner started <<<");
//
//        try (FileInputStream fis = new FileInputStream("C:\\Learning\\emails.xlsx");
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            System.out.println("Total rows: " + sheet.getPhysicalNumberOfRows());
//
//            for (Row row : sheet) {
////                System.out.println("Reading row: " + row.getRowNum());
////                if (row.getRowNum() == 0){
////                    System.out.println("Skipping header row");
////                    continue;}
//
//                Cell cell = row.getCell(0);
//                if (cell == null) {
//                    System.out.println("Empty cell, skipping");
//                    continue;}
//
//                String toEmail = cell.getStringCellValue().trim();
//                if (!toEmail.contains("@")){
//                    System.out.println("Invalid email: " + toEmail);
//                    continue;}
//
//                emailService.sendEmailWithAttachment(toEmail);
//                System.out.println("Mail sent to: " + toEmail);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


@SpringBootApplication
public class SenEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenEmailApplication.class, args);
    }


}
