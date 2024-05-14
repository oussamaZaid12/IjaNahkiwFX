package utils;


        import javafx.concurrent.Service;
        import javafx.concurrent.Task;
        import javax.mail.*;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeMessage;
        import java.util.Properties;

public class EmailService extends Service<Void> {
    private static final String HOST = "smtp-relay.brevo.com";
    private static final String USERNAME = "karat6657@gmail.com";
    private static final String PASSWORD = "Jd0z7k2DxNbn6QZ8";

    private String recipientEmail;

    public EmailService(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                sendEmail(recipientEmail);
                return null;
            }
        };
    }

    private void sendEmail(String recipientEmail) {
        try {
            // Configure email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", HOST);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587"); // You may need to change the port

            // Enable debugging
            props.put("mail.debug", "true");

            // Create session with authentication
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }
                    });

            // Set debug mode for the session
            session.setDebug(true);

            // Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreplay@coolConnect.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Consultation Confirmed");
            message.setText("The consultation has been modified.");

            // Send the message
            Transport.send(message);

            // Print success message
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            // Print error message
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
    }
}

