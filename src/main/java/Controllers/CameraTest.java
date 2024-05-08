package Controllers;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

public class CameraTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        VideoCapture capture = new VideoCapture();
        Mat frame = new Mat();
        capture.open(0); // 0 for the default camera; change if needed

        if (capture.isOpened()) {
            System.out.println("Camera is opened successfully.");

            // Capture a single frame
            capture.read(frame);

            // Check if the frame was successfully captured
            if (!frame.empty()) {
                String outputFileName = "captured_image_test.png";
                Imgcodecs.imwrite(outputFileName, frame);
                System.out.println("Image captured and saved as: " + outputFileName);
            } else {
                System.out.println("Failed to capture an image.");
            }

            // Release the camera
            capture.release();
        } else {
            System.out.println("Unable to open the camera.");
        }
    }
}
