package ru.maildeal.mailer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.maildeal.mailer.model.MailerResponse;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String smtpUser;

    @Value("${project.name}")
    private String projectName;

    @Value("${project.domain}")
    private String projectDomain;

    @Value("${mailer.verification_letter_id}")
    private String verificationLetterId;

    @Value("${mailer.pass_changed_letter_id}")
    private String passChangedLetterId;

    @Value("${mailer.reset_pass_letter_id}")
    private String resetPassLetterId;

    @Autowired
    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public MailerResponse sendMail(String email, String letterId, Map<String, String> params) {
        var message = new SimpleMailMessage();

        var text = "";
        var subject = "";
        if (letterId.equals(verificationLetterId)) {
            if (!this.areVerificationParamsOk(params)) {
                return MailerResponse.Fail("bad template params");
            }
            text = this.prepareVerificationText(params);
            subject = this.prepareVerificationSubject();
        } else if (letterId.equals(resetPassLetterId)) {
            if (!this.areResetPassParamsOk(params)) {
                return MailerResponse.Fail("bad template params");
            }
            text = this.prepareResetPassText(params);
            subject = this.prepareResetPassSubject();
        } else if (letterId.equals(passChangedLetterId)) {
            if (!this.arePassChangedParamsOk(params)) {
                return MailerResponse.Fail("bad template params");
            }
            text = this.preparePassChangedText(params);
            subject = this.preparePassChangedSubject();
        } else {
            return MailerResponse.Fail("unknown letter_id");
        }

        message.setFrom(smtpUser);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
        return MailerResponse.Ok();
    }

    private String prepareVerificationSubject() {
        return this.prepareSubject("VERIFICATION");
    }

    private String preparePassChangedSubject() {
        return this.prepareSubject("PASSWORD CHANGED");
    }

    private String prepareResetPassSubject() {
        return this.prepareSubject("PASSWORD RESET");
    }

    private String prepareSubject(String subject) {
        return subject + " [" + projectName + " (" + projectDomain + ")]";
    }

    private boolean areVerificationParamsOk(Map<String, String> params) {
        return !params.getOrDefault("ConfirmUrl", "").isEmpty();
    }

    private String prepareVerificationText(Map<String, String> params) {
        var confirmUrl = params.getOrDefault("ConfirmUrl", "");
        return "Verification URL: " + confirmUrl;
    }

    private boolean arePassChangedParamsOk(Map<String, String> params) {
        return !params.getOrDefault("Login", "").isEmpty();
    }

    private String preparePassChangedText(Map<String, String> params) {
        var login = params.getOrDefault("Login", "");
        return "Your password was changed! Login: " + login;
    }

    private boolean areResetPassParamsOk(Map<String, String> params) {
        return !params.getOrDefault("ResetUrl", "").isEmpty();
    }

    private String prepareResetPassText(Map<String, String> params) {
        var resetUrl = params.getOrDefault("ResetUrl", "");
        return "Password reset URL: " + resetUrl;
    }
}
