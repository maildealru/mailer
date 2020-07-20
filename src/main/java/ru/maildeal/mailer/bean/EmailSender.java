package ru.maildeal.mailer.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailSender {
    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private Integer smtpPort;

    @Value("${spring.mail.is_ssl}")
    private Boolean smtpIsSSL;

    @Value("${spring.mail.username}")
    private String smtpUser;

    @Value("${spring.mail.password}")
    private String smtpPass;

    @Value("${mailer.debug}")
    private Boolean smtpIsDebug;

    @Bean
    @Primary
    public JavaMailSender getJavaMailSender() {
        var sender = new JavaMailSenderImpl();

        sender.setHost(smtpHost);
        sender.setPort(smtpPort);
        sender.setUsername(smtpUser);
        sender.setPassword(smtpPass);

        var p = sender.getJavaMailProperties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.socketFactory.port", smtpPort);
        if (smtpIsSSL) {
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.starttls.required", "true");
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        if (smtpIsDebug)
            p.put("mail.debug", "true");

        return sender;
    }
}
