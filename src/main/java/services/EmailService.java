package services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailService {

    private final String username = "esser.benahmed@esprit.tn";
    private final String password = "223JMT2827";
    private final String host = "smtp.gmail.com";
    private final int port = 587;

    public void sendEmailToAllUsers(String subject, String content) throws MessagingException {
        // Dummy list of recipients
        List<String> recipients = Arrays.asList("user1@example.com", "user2@example.com");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        for (String to : recipients) {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        }

        System.out.println("Emails sent successfully!");
    }
}
