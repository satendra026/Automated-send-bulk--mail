package com.example.senEmail;

import com.example.senEmail.EmailTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class BulkEmailService {

    @Value("${excel.file.path}")
    private String excelFilePath;

    @Value("${excel.sheet.index}")
    private int sheetIndex;

    @Value("${excel.email.column.index}")
    private int emailColumnIndex;

    private final EmailTemplate emailService;

    public BulkEmailService(EmailTemplate emailService) {
        this.emailService = emailService;
    }

    public String sendBulkEmails() {



        int successCount = 0;
        int failedCount = 0;

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(sheetIndex);

            for (Row row : sheet) {

                Cell cell = row.getCell(emailColumnIndex);
                if (cell == null) {
                    failedCount++;
                    continue;
                }

                String toEmail = cell.getStringCellValue().trim();
                if (!toEmail.contains("@")) {
                    failedCount++;
                    continue;
                }

                emailService.sendEmailWithAttachment(toEmail);
                successCount++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while sending emails: " + e.getMessage();
        }

        return "Emails sent successfully. Success: "
                + successCount + ", Failed: " + failedCount;
    }
}
