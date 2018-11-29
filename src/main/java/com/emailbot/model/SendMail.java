package com.emailbot.model;

import com.emailbot.tmp.Constants;

import javax.mail.*;
import java.util.Properties;

public class SendMail {
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private Session session;

    public SendMail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", Constants.STMP_SERVER_MAIL_RU);
        properties.put("mail.smtp.port", Constants.STMP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);
        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(false);
    }
}
