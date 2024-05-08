package Controllers.Publication;
import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class AudioReceiver {
    private final SourceDataLine speakers;
    private final DataInputStream in;
    private final boolean running = true;

    public AudioReceiver(Socket socket) throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        speakers = (SourceDataLine) AudioSystem.getLine(info);
        speakers.open(format);
        speakers.start();
        in = new DataInputStream(socket.getInputStream());
    }

    public void start() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (running) {
                try {
                    int bytesRead = in.readInt();
                    if (bytesRead > 0) {
                        in.readFully(buffer, 0, bytesRead);
                        speakers.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public void stop() {
        speakers.stop();
        speakers.close();
    }
}
