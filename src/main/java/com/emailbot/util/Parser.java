package com.emailbot.util;

import com.emailbot.tmp.Constants;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Random;

public class Parser {
    private static Parser parser = null;
    private Random random;

    private Parser() {
        random = new Random();
    }

    public static Parser getParser() {
        if (parser == null)
            parser = new Parser();
        return parser;
    }

    public String getLinkFromMessages(Message[] messages) {
        try {
            for (int i = 0; i < messages.length; i++) {
                Address address = messages[i].getFrom()[0];
                String stringAddress = address.toString();
                if (stringAddress.indexOf("<") != -1) {
                    String email = stringAddress.substring(stringAddress.indexOf("<") + 1, stringAddress.indexOf(">"));
                    if (email.equalsIgnoreCase(Constants.CHECK_EMAIL)) {
                        messages[i].setFlag(Flags.Flag.SEEN,true);
                        String stringMessage = getTextFromMessage(messages[i]);
                        if (stringMessage.indexOf("<http://") != -1) {
                            stringMessage = stringMessage.substring(stringMessage.indexOf("<http://") + 1);
                            String link = stringMessage.substring(0, stringMessage.indexOf(">"));
                            return link;
                        } else return Constants.EXCEPTION_TEXT;
                    }
                } else return Constants.EXCEPTION_TEXT;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.EXCEPTION_TEXT;
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
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

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public String transitionToMail(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element element = document.select("h2.lbc-ttl-h2").first();
        Elements elements = document.select(".detail-booking__item");
        if (elements == null || element == null) return Constants.EXCEPTION_TEXT;
        String result = "Random number: (#" + random.nextInt() + ") ";
        result += element.text() + " : ";
        for (int i = 0; i < 4 && i < elements.size(); i++) {
            result += elements.get(i).text() + " ";
        }
        if (result != "")
            return result;
        else return Constants.EXCEPTION_TEXT;
    }
}
