package com.example.senEmail;

import com.example.senEmail.EmailTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

import java.io.FileInputStream;

@Service
public class BulkEmailService {

    @Value("${excel.file.path}")
    private String excelFilePath;

    @Value("${excel.sheet.index}")
    private int sheetIndex;

    @Value("${excel.email.column.index}")
    private int emailColumnIndex;

    @Value("${excel.email.column.startRowIndex}")
    private int startRowIndex;

    @Value("${excel.email.column.maxEmails}")
    private int maxEmails;

    private final EmailTemplate emailService;

    public BulkEmailService(EmailTemplate emailService) {
        this.emailService = emailService;
    }

    public String sendBulkEmails() {

        int successCount = 0;
        int failedCount = 0;
//        int maxEmails = 100;

        int lastRowIndex=0;

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            long startTime = System.currentTimeMillis();
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            long endTime = System.currentTimeMillis();

            System.out.println("Sheet load time: " + (endTime - startTime) + " ms");

            int sentCount = 0;
            int lastRow = sheet.getLastRowNum();

            for (int i = startRowIndex; i <= lastRow && sentCount < maxEmails; i++) {

                Row row = sheet.getRow(i);
                lastRowIndex=row.getRowNum();
                if (row == null) {
                    failedCount++;
                    continue;
                }

                startTime = System.currentTimeMillis();

                Cell cell = row.getCell(emailColumnIndex);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    failedCount++;
                    continue;
                }

                String toEmail = cell.getStringCellValue().trim();
                if (toEmail.isEmpty() || !toEmail.contains("@")) {
                    failedCount++;
                    continue;
                }

                endTime = System.currentTimeMillis();
//                System.out.println("Cell read time: " + (endTime - startTime) + " ms");

                // ---- Random delay (3–20 seconds) ----
                int delayInSeconds = ThreadLocalRandom.current().nextInt(10, 30);
                Thread.sleep(delayInSeconds * 1000L);

                System.out.println("Sleep method read time: " + delayInSeconds + " second");

                startTime = System.currentTimeMillis();
                emailService.sendEmailWithAttachment(toEmail);
                endTime = System.currentTimeMillis();

                System.out.println("Email sent to " + toEmail
                        + " | Time: " + (endTime - startTime) + " ms | Row line number: "+lastRowIndex);

                successCount++;
                sentCount++;

            }

            System.out.println("Email sent to last row: " + lastRowIndex);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Email sending interrupted.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while sending emails: " + e.getMessage();
        }

        return "Emails sent successfully. Success: "
                + successCount + ", Failed: " + failedCount;
    }
}
