package com.example.senEmail;


import com.example.senEmail.BulkEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BulkEmailController {

    private final BulkEmailService bulkEmailService;

    public BulkEmailController(BulkEmailService bulkEmailService) {
        this.bulkEmailService = bulkEmailService;
    }

    @GetMapping("/send-bulk-email")
    public String sendBulkEmail() {
        return bulkEmailService.sendBulkEmails();
    }
}

