import java.util.ArrayList;

import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.tables.*;
import edu.wpi.cscore.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Main {
	public static void main(String[] args) {

		// Setup for NetworkTables and OpenCV.
		System.loadLibrary("opencv_java310");
		NetworkTable.setClientMode();
		NetworkTable.setTeam(957); // CHANGE TO YOUR TEAM
		NetworkTable.initialize();

		// These 3 lines of code grabs cameras, creates and MJPEG server, then
		// pipes that camera stream into the server.
		// Access the server on a network setting a static IP to:
		// 10.TE.AM.X and use the port number specified in the MJPEG server.
		// Type in 10.TE.AM.X:PORT# in a browser on the DS to tune into the stream.
		MjpegServer inputStream0 = new MjpegServer("MJPEG Server 1181", 1181);
		UsbCamera camera0 = new UsbCamera("Camera0", 0);
		inputStream0.setSource(camera0);

		// This CV Sink grabs images from a camera that can be later put into a Mat.
		CvSink imageSink = new CvSink("CV Image Grabber");
		imageSink.setSource(camera0);


		// This creates a MJPEG server to stream Mats piped into the Source imageSource.
		CvSource imageSource = new CvSource("CV Image Source", VideoMode.PixelFormat.kMJPEG, 640, 480, 30);
		MjpegServer cvStream = new MjpegServer("CV Image Stream", 1189);
		cvStream.setSource(imageSource);

		// All Mats and Lists should be created outside the loop to avoid allocations
		Mat inputImage = new Mat();

		// Infinitely process image
		while (true) {
			// Grab a frame. If it has a frame time of 0, there was an error.
			// If only having one autonomous camera, skip editing this.
			long frameTime = imageSink.grabFrame(inputImage);
			if (frameTime == 0) continue;

			imageSource.putFrame(inputImage);
		}
	}
}
