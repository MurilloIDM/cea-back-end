package com.cea.provider;

import com.cea.utils.ConfigProperties;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SendGridMail {

    private final ConfigProperties configProperties;

    public void sendMail(String mailTo, String firstName, String codeReset) throws IOException {
        Mail mail = new Mail();

        Email to = new Email(mailTo);
        Email from = new Email(configProperties.getProperty("sendgrid.email.from"));
        String templateId = configProperties.getProperty("sendgrid.email.templateId");

        mail.setFrom(from);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("first_name", firstName);
        personalization.addDynamicTemplateData("code_reset", codeReset);

        mail.addPersonalization(personalization);

        Request request = new Request();
        SendGrid sendGrid = new SendGrid(configProperties.getProperty("sendgrid.apikey"));

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sendGrid.api(request);
    }

}
