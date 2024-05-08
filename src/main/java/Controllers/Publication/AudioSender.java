package Controllers.Publication;

import javax.sound.sampled.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AudioSender {
    private final TargetDataLine microphone;
    private final DataOutputStream out;
    private final boolean running = true;

    public AudioSender(Socket socket) throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void start() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (running) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                try {
                    out.writeInt(bytesRead);
                    out.write(buffer, 0, bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public void stop() {
        microphone.stop();
        microphone.close();
    }
}
