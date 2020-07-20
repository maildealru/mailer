package ru.maildeal.mailer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maildeal.mailer.model.MailerResponse;
import ru.maildeal.mailer.service.EmailService;

import java.util.HashMap;
import java.util.Map;

@RestController
class EmailController {
    private final EmailService service;

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping(value = "/api/mailer/send",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<MailerResponse> send(@RequestParam Map<String, String> p) {
        var email = p.getOrDefault("email", "");
        if (email.isEmpty()) {
            return this.makeBadParamResponse("email");
        }

        var letterId = p.getOrDefault("letter", "");
        if (letterId.isEmpty()) {
            return this.makeBadParamResponse("letter");
        }

        var templateParams = new HashMap<String, String>();
        for (var entry : p.entrySet()) {
            if (entry.getKey().startsWith("pin.")) {
                templateParams.put(entry.getKey().substring("pin.".length()), entry.getValue());
            }
        }

        var r = this.service.sendMail(email, letterId, templateParams);
        return ResponseEntity.ok(r);
    }

    private ResponseEntity<MailerResponse> makeBadParamResponse(String name) {
        return ResponseEntity.ok(MailerResponse.Fail("bad param: " + name));
    }
}
