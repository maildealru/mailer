package ru.maildeal.mailer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class MailerResponse {
    @Getter
    @JsonProperty("status")
    private String status;

    @Getter
    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public static MailerResponse Ok() {
        var r = new MailerResponse();
        r.status = "ok";
        return r;
    }

    public static MailerResponse Fail(String message) {
        var r = new MailerResponse();
        r.status = "fail";
        r.message = message;
        return r;
    }
}
