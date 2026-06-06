package com.berkay;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
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

        Mat previousFrame = new Mat();
        Mat currentFrame = new Mat();

        Mat previousGray = new Mat();
        Mat currentGray = new Mat();

        Mat difference = new Mat();
        Mat threshold = new Mat();

        boolean firstFrameRead = camera.read(previousFrame);

        if (!firstFrameRead || previousFrame.empty()) {
            System.out.println("Could not read first frame.");
            camera.release();
            return;
        }

        Imgproc.cvtColor(previousFrame, previousGray, Imgproc.COLOR_BGR2GRAY);

        HighGui.namedWindow("Camera", HighGui.WINDOW_NORMAL);
        HighGui.namedWindow("Motion Detection", HighGui.WINDOW_NORMAL);

        HighGui.resizeWindow("Camera", 600, 400);
        HighGui.resizeWindow("Motion Detection", 600, 400);

        HighGui.moveWindow("Camera", 0, 80);
        HighGui.moveWindow("Motion Detection", 620, 80);

        while (true) {
            boolean success = camera.read(currentFrame);

            if (!success || currentFrame.empty()) {
                System.out.println("Could not read frame from camera.");
                break;
            }

            Imgproc.cvtColor(currentFrame, currentGray, Imgproc.COLOR_BGR2GRAY);

            Core.absdiff(previousGray, currentGray, difference);

            Imgproc.threshold(difference, threshold, 25, 255, Imgproc.THRESH_BINARY);

            HighGui.imshow("Camera", currentFrame);
            HighGui.imshow("Motion Detection", threshold);

            currentGray.copyTo(previousGray);

            int key = HighGui.waitKey(30) & 0xFF;

            if (key == 27) {
                System.out.println("Program is closing...");
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
        System.exit(0);
    }
}