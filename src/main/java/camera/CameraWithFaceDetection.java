package camera;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import javax.swing.*;

/**
 * Created by Tianlong on 2017/5/16.
 */
public class CameraWithFaceDetection {

    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        CascadeClassifier face_cascade = new CascadeClassifier("data\\haarcascade_frontalface_alt.xml");
       OpenCVFrameGrabber grabber = OpenCVFrameGrabber.createDefault(0);
        // FrameGrabber grabber = FrameGrabber.createDefault(0); //不用OpenCVFrameGrabber，可以安全关闭，不需下文中被注释的代码
        grabber.setImageHeight(768);
        grabber.setImageWidth(1024);
        grabber.start();
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        Frame videoFrame;
        Mat videoMat;
        IplImage pFrame = null;
        while (true) {
//            if (!frame.isDisplayable()) {
//                //问题：关闭窗口后也不进入该循环
//                grabber.stop();
//                System.out.println("------------");
//                System.exit(0);
//            }

            // grabber.setImageMode(FrameGrabber.ImageMode.GRAY);
            videoFrame = grabber.grabFrame();
            videoMat = converterToMat.convert(videoFrame);
            Mat videoMatGray = new Mat();
            // Convert the current frame to grayscale:
            cvtColor(videoMat, videoMatGray, COLOR_BGR2GRAY);
            equalizeHist(videoMatGray, videoMatGray);

            RectVector faces = new RectVector();
            // Find the faces in the frame:
            face_cascade.detectMultiScale(videoMatGray, faces);

            // At this point you have the position of the faces in
            // faces. Now we'll get the faces, make a prediction and
            // annotate it in the video. Cool or what?
            for (int i = 0; i < faces.size(); i++) {
                Rect face_i = faces.get(i);

//                Mat face = new Mat(videoMatGray, face_i);
                // If fisher face recognizer is used, the face need to be
                // resized.
                // resize(face, face_resized, new Size(im_width, im_height),
                // 1.0, 1.0, INTER_CUBIC);

                // Now perform the prediction, see how easy that is:
//                IntPointer label = new IntPointer(1);
//                DoublePointer confidence = new DoublePointer(1);
//                lbphFaceRecognizer.predict(face, label, confidence);
//                int prediction = label.get(0);

                // And finally write all we've found out to the original image!
                // First of all draw a green rectangle around the detected face:
                rectangle(videoMat, face_i, new Scalar(0, 255, 0, 2));

                // Create the text we will annotate the box with:
//                String box_text = "Prediction = " + prediction;
                // Calculate the position for annotated text (make sure we don't
                // put illegal values in there):
//                int pos_x = Math.max(face_i.tl().x() - 10, 0);
//                int pos_y = Math.max(face_i.tl().y() - 10, 0);
                // And now put it into the image:
//                putText(videoMat, box_text, new Point(pos_x, pos_y),
//                        FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0));
            }
            // Show the result:
//            imshow("face_recognizer", videoMat);
            frame.showImage(converterToMat.convert(videoMat));
            Thread.sleep(5);
        }
    }

}
