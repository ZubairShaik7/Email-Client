package com.main.controller.services;

import com.main.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {
    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.stringBuffer = new StringBuffer();
        this.setOnSucceeded(event -> {
            displayMessage();
        });
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e) {

                }
                return null;
            }
        };
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());
    }

    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0);
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if (isSimpleType(contentType)) {
            stringBuffer.append(message.getContentType().toString());
        } else if (isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultiPart(multipart, stringBuffer);
        }
    }

    private void loadMultiPart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String bodyPartContentType = bodyPart.getContentType();
            if (isSimpleType(bodyPartContentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if (isMultipartType(bodyPartContentType)) {
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultiPart(multipart2, stringBuffer);
            } else if (!isTextPlain(bodyPartContentType)) {
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mbp);
            }
        }
    }

    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT?PLAIN");
    }

    private boolean isSimpleType(String contentType) {
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }

    private boolean isMultipartType(String contentType) {
        return contentType.contains("multipart");
    }
}
