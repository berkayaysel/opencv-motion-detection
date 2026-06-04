package com.berkay;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;

import nu.pattern.OpenCV;

public class App {
    public static void main(String[] args) {
        OpenCV.loadLocally();

        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Camera could not be opened.");
            return;
        }

        Mat frame = new Mat();

        while (true) {
            boolean success = camera.read(frame);

            if (!success || frame.empty()) {
                System.out.println("Could not read frame from camera.");
                break;
            }

            HighGui.imshow("Motion Detection", frame);

            int key = HighGui.waitKey(30);

            if (key == 27 || key == 'q') {
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
    }
}