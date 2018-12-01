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
            if (messages != null && messages.length != 0) {
                for (int i = 0; i < messages.length; i++) {
                    Address[] addresses = messages[i].getFrom();
                    if (addresses != null && addresses.length != 0) {
                        Address address = messages[i].getFrom()[0];
                        String stringAddress = address.toString();
                        if (stringAddress.indexOf("<") != -1) {
                            String email = stringAddress.substring(stringAddress.indexOf("<") + 1, stringAddress.indexOf(">"));
                            if (email.equals(Constants.CHECK_EMAIL) || email.equalsIgnoreCase(Constants.CHECK_EMAIL)) {
                                messages[i].setFlag(Flags.Flag.SEEN, true);
                                String stringMessage = getTextFromMessage(messages[i]);
                                if (stringMessage.indexOf("http://demo.booking.agentpassport.ru") != -1) {
                                    stringMessage = stringMessage.substring(stringMessage.indexOf("http://demo.booking.agentpassport.ru"));
                                    String link = stringMessage.substring(0, stringMessage.indexOf(" ")-1);
                                    return link;
                                }
                            }
                        }
                    }
                }
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
            } else if (bodyPart.getContent() instanceof String) {
                result = result + bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public String[] transitionToMail(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements elementsAll = document.getAllElements();
        Element element = document.select("h2.lbc-ttl-h2").first();
        Elements elements = document.select(".detail-booking__item");
        Elements elementsRequest = document.select(":containsOwn(Заявка)");
        Elements elementsButton = document.select(":containsOwn(Перейти к обработке)");
        if (elements == null || element == null || elementsRequest == null) return null;
        String [] strings = new String[2];
        strings[0] = elementsRequest.first().text();
        strings[1] = "Random number: (#" + random.nextInt() + ") ";
        strings[1] += element.text() + " : ";
        for (int i = 0; i < 4 && i < elements.size(); i++) {
            strings[1] += elements.get(i).data() + " ";
        }
        if (strings[1] != "")
        {
            for (Element i : elementsAll)
                strings[1] += (i.toString());
            return strings;
        }
        else return null;
    }
}
