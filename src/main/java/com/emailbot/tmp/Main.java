package com.emailbot.tmp;

import com.emailbot.controller.ReadMailController;
import com.emailbot.controller.SendMailController;
import com.emailbot.util.Parser;

import javax.mail.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ReadMailController readMailController = new ReadMailController();
        SendMailController sendMailController = new SendMailController();
        int sleepTime = 0;
        while (true) {
            Thread.sleep(Constants.sleepInterval + sleepTime);
            sleepTime = 0;
            Message[] messages = readMailController.getUnseenMessages();
            if (messages != null) {
                Parser parser = Parser.getParser();
                String link = parser.getLinkFromMessages(messages);
                if (link != Constants.EXCEPTION_TEXT) {
                    String[] text = parser.transitionToMail(link);
                    if (text != null) {
                        sendMailController.setThemeMail(text[0]);
                        sendMailController.setTextMail(text[1]);
                        sendMailController.sendMail();
                        sleepTime = 5000;
                    }
                }
            }
        }
    }
}
