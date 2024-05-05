package Controllers.Quiz;

import Controllers.Activite.DisplayAllActiviteController;
import Controllers.Consultation.AffichageConsultationpatient;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import entities.Answer;
import entities.Proposition;
import entities.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.ServiceAnswer;
import services.ServiceQuestion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class ResultController {

    @FXML
    private TextArea resultArea;

    private ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ServiceAnswer serviceAnswer = new ServiceAnswer(); // Service for answer-related operations


    public void setResults(String results) {
        resultArea.setText(results);
    }


    public void saveAnswersToPDF() {
        try {
            List<Question> questions = serviceQuestion.getAllQuestionsWithPropositions(2);
            List<Answer> answers = serviceAnswer.fetchAnswersByUserId(9);

            generatePdf("C:\\Users\\Tifa\\Downloads\\Resulat.pdf", questions, answers);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found error: " + e.getMessage());
        }
    }
    private void generatePdf(String dest, List<Question> questions, List<Answer> answers) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Adding a logo to the PDF
            InputStream logoStream = getClass().getResourceAsStream("/images/logo ff.png"); // Adjust the path to where your logo is stored
            if (logoStream != null) {
                byte[] logoData = logoStream.readAllBytes();
                ImageData logo = ImageDataFactory.create(logoData);
                Image pdfImage = new Image(logo);

                pdfImage.setWidth(100); // Set the width as per your requirement
                pdfImage.setHeight(100);// Set the height as per your requirement

                document.add(pdfImage);
            }
            Paragraph title = new Paragraph("Resultat du Quiz")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.RED)
                    .setMarginBottom((50))
                    .setFontSize(26)
                    .setBold();
            document.add(title);

            for (Question question : questions) {
                document.add(new Paragraph("Question: " + question.getTitleQuestion())
                        .setMarginTop(10)
                        .setBold()
               );
                Answer matchingAnswer = answers.stream()
                        .filter(a -> a.getQuestionId() == question.getId())
                        .findFirst()
                        .orElse(null);

                if (matchingAnswer != null) {
                    Proposition chosenProp = question.getPropositions().stream()
                            .filter(p -> p.getId() == matchingAnswer.getPropositionChoisieId())
                            .findFirst()
                            .orElse(null);

                    if (chosenProp != null) {
                        document.add(new Paragraph("Reponse: " + chosenProp.getTitleProposition() )
                        .setFontColor(ColorConstants.BLUE)
                        .setMarginBottom(10)
                        );
                    } else {
                        document.add(new Paragraph("No answer provided."));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("");
        } finally {
            document.close(); // Ensure the document is always closed.
        }


}
    @FXML
    private void goToDoctorConsultationPage() throws IOException {
        try {
            // Logic to navigate to the doctor consultation page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/affichageConsultationpatient.fxml"));
            Parent root = loader.load();
            AffichageConsultationpatient controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Consultation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        @FXML
        private void goToActivitiesPage() throws IOException {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Activite/ActiviteDisplay.fxml"));
                Parent root = loader.load();

                // Create a new Stage and Scene
                Stage stage = new Stage();
                stage.setTitle("Activities");
                stage.setScene(new Scene(root));

                // Show the new Stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error or show an alert
            }
        }

        @FXML
        private void handleClose () {
            Stage stage = (Stage) resultArea.getScene().getWindow();
            stage.close();
        }
}