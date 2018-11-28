package com.emailbot.tmp;

import com.emailbot.model.EmailAuthentificator;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void ReadMail() {
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imaps.host", Constants.IMAP_SERVER_MAIL_RU);
        properties.put("mail.imap.port", Constants.IMAP_PORT);

        //Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

        Session session = Session.getDefaultInstance(properties, null);
        session.setDebug(false);
        try {
            Store store = session.getStore("imaps");

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
                Address address = messages[i].getFrom()[0];
                String stringAddress = address.toString();

                System.out.println("From address : " + stringAddress.substring(stringAddress.indexOf("<")+1,stringAddress.indexOf(">")));
                System.out.println("From : " + messages[i].getFrom()[0]);
                System.out.println("Subject : " + messages[i].getSubject());
                System.out.println("Sent Date : " + messages[i].getSentDate());
                String stringMessage = getTextFromMessage(messages[i]);
                stringMessage = stringMessage.substring(stringMessage.indexOf("<http://")+1);
                String link = stringMessage.substring(0,stringMessage.indexOf(">"));
                String email = stringAddress.substring(stringAddress.indexOf("<")+1,stringAddress.indexOf(">"));
                System.out.println(link);
            }
        } catch (NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        message.isExpunged();
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    public static void main(String[] args){
        ReadMail();
    }
}
