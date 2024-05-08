package Controllers.Publication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CallController {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Charger la bibliothèque OpenCV
    }

    @FXML
    private ImageView localVideo;
    @FXML
    private ImageView remoteVideo;

    private VideoCapture serverCapture;
    private VideoCapture clientCapture;
    private volatile boolean running = true;
    private AudioSender audioSender;
    private AudioReceiver audioReceiver;

    @FXML
    private void handleStartServer() {
        running = true; // Autoriser la capture et l'envoi
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(6000);
                 ServerSocket audioServerSocket = new ServerSocket(6001)) {

                System.out.println("Serveur en attente d'une connexion...");

                // Accepter les connexions audio et vidéo
                Socket clientSocket = serverSocket.accept();
                Socket audioSocket = audioServerSocket.accept();

                System.out.println("Client connecté !");
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Initialiser la réception du son
                audioReceiver = new AudioReceiver(audioSocket);
                audioReceiver.start();

                // Initialiser l'envoi du son
                audioSender = new AudioSender(audioSocket);
                audioSender.start();

                // Capture vidéo locale du serveur via OpenCV
                serverCapture = new VideoCapture(0);
                Mat frame = new Mat();

                // Thread pour recevoir les images du client
                new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        while (running) {
                            receiveImage(in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                // Envoyer les images capturées au client
                while (running && serverCapture.isOpened()) {
                    serverCapture.read(frame);
                    if (!frame.empty()) {
                        // Mettre à jour la vidéo locale du serveur
                        Image localImage = matToImage(frame);
                        Platform.runLater(() -> localVideo.setImage(localImage));

                        // Envoyer l'image au client
                        sendImage(clientSocket, frame);
                    }
                }

                serverCapture.release(); // Libérer les ressources après usage
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleStartClient() {
        running = true; // Autoriser la capture et l'envoi
        new Thread(() -> {

            try (Socket videoSocket = new Socket("172.16.3.255", 6000);
                 Socket audioSocket = new Socket("172.16.3.255", 6001)) {


                DataInputStream in = new DataInputStream(videoSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(videoSocket.getOutputStream());



                // Initialiser la réception du son
                audioReceiver = new AudioReceiver(audioSocket);
                audioReceiver.start();
// Initialiser l'envoi du son
                audioSender = new AudioSender(audioSocket);
                audioSender.start();

                // Capture vidéo locale du client via OpenCV
                clientCapture = new VideoCapture(0);
                Mat frame = new Mat();

                // Thread pour recevoir les images du serveur
                new Thread(() -> {
                    while (running) {
                        receiveImage(in);
                    }
                }).start();

                // Envoyer les images capturées au serveur
                while (running && clientCapture.isOpened()) {
                    clientCapture.read(frame);
                    if (!frame.empty()) {
                        // Mettre à jour la vidéo locale du client
                        Image localImage = matToImage(frame);
                        Platform.runLater(() -> localVideo.setImage(localImage));

                        // Envoyer l'image au serveur
                        sendImage(videoSocket, frame);
                    }
                }

                clientCapture.release(); // Libérer les ressources après usage
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleStop() {
        running = false; // Arrêter la capture et l'envoi
        if (serverCapture != null) {
            serverCapture.release();
        }
        if (clientCapture != null) {
            clientCapture.release();
        }
        if (audioSender != null) {
            audioSender.stop();
        }
        if (audioReceiver != null) {
            audioReceiver.stop();
        }
    }

    private Image matToImage(Mat mat) {
        // Encapsuler les données de l'image dans un MatOfByte
        MatOfByte buffer = new MatOfByte();

        // Encoder l'image dans un format JPEG
        boolean result = Imgcodecs.imencode(".jpg", mat, buffer);
        if (result) {
            // Créer une image JavaFX à partir des données encodées
            return new Image(new ByteArrayInputStream(buffer.toArray()));
        } else {
            System.out.println("Erreur lors de l'encodage de l'image.");
            return null;
        }
    }

    private void sendImage(Socket clientSocket, Mat frame) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        MatOfByte buffer = new MatOfByte();

        // Encodage de l'image en format JPEG
        if (Imgcodecs.imencode(".jpg", frame, buffer)) {
            byte[] imageBytes = buffer.toArray();

            // Envoyer la longueur de l'image puis les octets réels
            out.writeInt(imageBytes.length);
            out.write(imageBytes);
            out.flush();
        }
    }

    private void receiveImage(DataInputStream in) {
        try {
            // Lire la longueur de l'image
            int length = in.readInt();
            if (length > 0) {
                // Lire les octets de l'image
                byte[] buffer = new byte[length];
                in.readFully(buffer);

                // Convertir les octets en Image JavaFX et mettre à jour `remoteVideo`
                Image img = new Image(new ByteArrayInputStream(buffer));
                Platform.runLater(() -> remoteVideo.setImage(img));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
