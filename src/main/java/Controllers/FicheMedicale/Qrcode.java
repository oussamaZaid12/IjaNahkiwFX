package Controllers.FicheMedicale;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import entities.FicheMedicale;
import entities.QRGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class Qrcode {

    @FXML
    private ImageView qrCodeImageView;
    private String fiche;

    public void initialize() {
        // Do not generate QR here anymore
    }

    private void generateQRCode() {
        if (fiche == null || fiche.isEmpty()) {
            System.out.println("Fiche information is not set.");
            return;
        }

        String contentToEncode = fiche;
        int qrCodeSize = 300;

        try {
            QRGenerator qrGenerator = new QRGenerator(
                    contentToEncode,
                    BarcodeFormat.QR_CODE,
                    qrCodeSize,
                    qrCodeSize,
                    BufferedImage.TYPE_INT_ARGB
            );

            BufferedImage qrImage = qrGenerator.qrImage();

            // Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(qrImage, null);

            // Set the generated image to the ImageView
            qrCodeImageView.setImage(image);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void setFiche(FicheMedicale currentFiche) {
        if (currentFiche != null) {
            fiche = "date de creation:"+String.valueOf(currentFiche.getDateCreation())+"date de derniere maj:"+currentFiche.getDerniereMaj()+"id patient:"+currentFiche.getIdp()+"id therapeute:"+currentFiche.getIdt();
            generateQRCode();  // Now generate the QR Code
        }
    }
}
