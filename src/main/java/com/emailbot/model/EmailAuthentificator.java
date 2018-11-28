package com.emailbot.model;

import java.net.PasswordAuthentication;

public class EmailAuthentificator extends javax.mail.Authenticator
{
    public EmailAuthentificator(String login,String password)
    {
        this.login = login;
        this.password = password.toCharArray();
    }

    public PasswordAuthentication getPasswordAuthentification()
    {
        return new PasswordAuthentication(login,password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    private String login;
    private char[] password;

}
