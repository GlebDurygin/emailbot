package com.emailbot.controller;

import com.emailbot.model.SendMail;
import com.emailbot.tmp.Constants;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Random;

public class SendMailController {
    private SendMail sendMail;
    private Random random;

    public void setTextMail(String textMail) {
        this.textMail = textMail;
    }

    private String textMail;

    public void setThemeMail(String themeMail) {
        this.themeMail = themeMail;
    }

    private String themeMail;

    public SendMailController() {
        sendMail = new SendMail();
        random = new Random();
    }

    public void sendMail() {
        try {
            Message message = new MimeMessage(sendMail.getSession());

            Multipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(textMail, "text/plain; charset=utf-8");
            multipart.addBodyPart(bodyPart);
            message.setFrom(new InternetAddress(Constants.BOT_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(Constants.SEND_EMAIL));
            message.setSubject(Constants.MAIL_THEME+ " " + themeMail + " (#"+random.nextInt()+")");
            message.setContent(multipart);
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
