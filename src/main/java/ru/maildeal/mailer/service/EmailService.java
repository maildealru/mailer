package ru.maildeal.mailer.service;

import ru.maildeal.mailer.model.MailerResponse;

import java.util.Map;

public interface EmailService {
    MailerResponse sendMail(String email, String letterId, Map<String, String> params);
}
