package com.emailbot.tmp;

import com.emailbot.model.EmailAuthentificator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static String ReadMail() {
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imaps.host", Constants.IMAP_SERVER_MAIL_RU);
        properties.put("mail.imap.port", Constants.IMAP_PORT);

        Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(false);
        try {
            Store store = session.getStore("imaps");

            // Подключение к почтовому сервису
            store.connect(Constants.IMAP_SERVER_MAIL_RU, Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

            // Папка входящих сообщений
            Folder inbox = store.getFolder("INBOX");

            // Открываем папку в режиме только для чтения
            inbox.open(Folder.READ_WRITE);

            if (inbox.getMessageCount() == 0)
                return "";

            // unseen messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message messages[] = inbox.search(unseenFlagTerm);

            for (int i = 0; i < messages.length; i++) {
                Address address = messages[i].getFrom()[0];
                String stringAddress = address.toString();
                String email = stringAddress.substring(stringAddress.indexOf("<") + 1, stringAddress.indexOf(">"));
                if (email.equalsIgnoreCase(Constants.CHECK_EMAIL))
                {
                    messages[i].setFlag(Flags.Flag.SEEN,true);
                    String stringMessage = getTextFromMessage(messages[i]);
                    stringMessage = stringMessage.substring(stringMessage.indexOf("<http://") + 1);
                    String link = stringMessage.substring(0, stringMessage.indexOf(">"));
                    return transitionToMail(link);
                }
            }
        } catch (NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String transitionToMail(String link) throws IOException {
        String result ="";
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements elements = document.select(".detail-booking__item");
        for(int i =0; i<4 && i<elements.size(); i++)
        {
            result += elements.get(i).text() + " ";
        }
        return result;
    }

    public static void sendMail(String text)
    {
        Properties properties = new Properties();
        properties.put("mail.smtp.host"               , Constants.STMP_SERVER_MAIL_RU   );
        properties.put("mail.smtp.port"               , Constants.STMP_PORT             );
        properties.put("mail.smtp.auth"               , "true"                          );
        properties.put("mail.smtp.ssl.enable"         , "true"                          );
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Authenticator authenticator = new EmailAuthentificator(Constants.AUTH_EMAIL, Constants.AUTH_PASSWORD);

        try {
            Session session = Session.getInstance(properties, authenticator);
            session.setDebug(false);
            Message message = new MimeMessage(session);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text,"text/plain; charset=utf-8");
            multipart.addBodyPart(bodyPart);
            message.setFrom(new InternetAddress(Constants.AUTH_EMAIL));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(Constants.SEND_EMAIL));
            message.setSubject(Constants.MAIL_THEME);
            message.setContent(multipart);
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
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
        String result = ReadMail();
        if (result != "")
            sendMail(result);
    }
}
