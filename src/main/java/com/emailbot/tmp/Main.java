package com.emailbot.tmp;

import com.emailbot.model.EmailAuthentificator;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class Main {
    public static void ReadMail() {
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", Constants.IMAP_PORT);

        Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

        Session session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(false);
        try {
            Store store = session.getStore();

            // Подключение к почтовому сервису
            store.connect(Constants.IMAP_SERVER_MAIL_RU, Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

            // Папка входящих сообщений
            Folder inbox = store.getFolder("INBOX");

            // Открываем папку в режиме только для чтения
            inbox.open(Folder.READ_ONLY);

            System.out.println("Количество сообщений : " +
                    String.valueOf(inbox.getMessageCount()));
            if (inbox.getMessageCount() == 0)
                return;

            // unseen messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen,false);
            Message messages[] = inbox.search(unseenFlagTerm);

            System.out.println("Unseen Messages Count = "+messages.length);
            for (int i = 0; i < messages.length; i++) {
                System.out.println("Message " + (i + 1));
                System.out.println("From : " + messages[i].getFrom()[0]);
                System.out.println("Subject : " + messages[i].getSubject());
                System.out.println("Sent Date : " + messages[i].getSentDate());
                System.out.println();
            }
            // Последнее сообщение; первое сообщение под номером 1
            /*Message message = inbox.getMessage(inbox.getMessageCount());
            Multipart mp = (Multipart) message.getContent();
            // Вывод содержимого в консоль
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart bp = mp.getBodyPart(i);
                if (bp.getFileName() == null)
                    System.out.println("    " + i + ". сообщение : '" +
                            bp.getContent() + "'");
                else
                    System.out.println("    " + i + ". файл : '" +
                            bp.getFileName() + "'");
            }*/
        } catch (NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        ReadMail();
    }
}
