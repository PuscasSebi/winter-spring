package com.puscas.authentication.event;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.puscas.authentication.encryption.AesEncryptionUtil;
import com.puscas.authentication.model.User;
import com.puscas.authentication.service.interfacew.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {
 
    @Autowired
    private UserService service;
 
    @Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void confirmRegistration(OnRegistrationCompleteEvent event) throws UnsupportedEncodingException {
        String propertyToEncryptEmail = environment.getProperty("custom.email.validationKey");
        String saltToEncrypt = environment.getProperty("custom.email.salt");
        User user = event.getUser();
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        UUID generate = timeBasedGenerator.generate();

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";

        String token = user.getEmail()+"###"+generate;

        token = AesEncryptionUtil.encrypt(token, propertyToEncryptEmail, saltToEncrypt);

        String confirmationUrl = event.getAppUrl() + "/regitrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());
/*        Properties prop = new Properties();
        prop.put(MAIL_SMTP_HOST, SMTP_GMAIL_COM);
        prop.put(MAIL_SMTP_PORT, VALUE);
        prop.put(MAIL_SMTP_AUTH, TRUE_SMTP_AUTH_STARTTTLS);
        prop.put(MAIL_SMTP_STARTTLS_ENABLE, TRUE_SMTP_AUTH_STARTTTLS); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(SEBASTIAN_VOIEVOD_GMAIL_COM, "wdymcsqbdccyhugd");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_GMAIL_COM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(PUSCAS_SEBASTIAN_GMAIL_COM)
            );
            message.setSubject(YOUR_CRYPTO_BOT);
            message.setText(content);

            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }*/


        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:9000" + confirmationUrl);
        mailSender.send(email);
    }
}