package com.emailbot.model;

import com.emailbot.tmp.Constants;

import javax.mail.Authenticator;
import javax.mail.Session;
import java.util.Properties;

public class ReadMail {
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private Session session;

    public ReadMail() {
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imaps.host", Constants.IMAP_SERVER_MAIL_RU);
        properties.put("mail.imap.port", Constants.IMAP_PORT);

        Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

        session = Session.getInstance(properties, authenticator);
        session.setDebug(false);
    }
}
