package com.emailbot.tmp;

import java.net.Authenticator;
import java.util.Properties;

public class Main {
    public static void main(String[] args){
    }

    public void ReadMail()
    {
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol" , "imaps"  );
        properties.put("mail.imap.ssl.enable", "true"   );
        properties.put("mail.imap.port"      , Constants.IMAP_PORT);

        //Authenticator authenticator = new EmailAuthentificator
    }
}
