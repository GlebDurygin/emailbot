package com.emailbot.model;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthentificator extends Authenticator {
    public EmailAuthentificator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password);
    }

    private String login;
    private String password;

}
