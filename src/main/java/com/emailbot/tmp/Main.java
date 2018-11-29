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
        while (true) {
            Thread.sleep(Constants.sleepInterval);
            Message[] messages = readMailController.getUnseenMessages();
            if (messages != null) {
                Parser parser = Parser.getParser();
                String link = parser.getLinkFromMessages(messages);
                if (link != Constants.EXCEPTION_TEXT) {
                    String text = parser.transitionToMail(link);
                    if (text != Constants.EXCEPTION_TEXT) {
                        sendMailController.setTextMail(text);
                        sendMailController.sendMail();
                    }
                }
            }
        }
    }
}
