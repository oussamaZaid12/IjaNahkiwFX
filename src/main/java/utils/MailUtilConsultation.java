package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtilConsultation{
    private static final String HOST = "smtp-relay.brevo.com";
    private static final String USERNAME = "brahemraed@gmail.com";
    private static final String PASSWORD = "xrvvnqxmpikopuvb";

    public static boolean sendConsultationEditMail(String toEmail) {
        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587"); // You may need to change the port
        System.out.println("To Email: " + toEmail);

        // Create session with authentication
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("noreplay@coolConnect.com"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject("Consultation Edit Notification");

            // Now set the actual message
            String emailBody = "Dear Doctor,\n\n"
                    + "A consultation associated with your account has been edited.\n\n"
                    + "Thank you,\nYour Application Team";
            message.setText(emailBody);

            // Send message
            Transport.send(message);

            System.out.println("Consultation edit notification email sent successfully to " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error sending consultation edit notification email: " + e.getMessage());
            return false;
        }
    }


}