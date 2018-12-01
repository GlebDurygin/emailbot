package com.emailbot.controller;

import com.emailbot.model.ReadMail;
import com.emailbot.tmp.Constants;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadMailController {
    private ReadMail readMail;

    public ReadMailController()
    {
        readMail = new ReadMail();
    }

    public Message[] getUnseenMessages()
    {
        try {
            Store store = readMail.getSession().getStore("imaps");
            store.connect(Constants.IMAP_SERVER_MAIL_RU, Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            if (inbox.getMessageCount() == 0)
                return null;
            // unseen messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message messages[] = inbox.search(unseenFlagTerm);
            System.out.println("UnseenMessges= " + messages.length);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            return messages;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
