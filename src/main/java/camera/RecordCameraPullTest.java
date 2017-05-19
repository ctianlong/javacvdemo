package camera;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tianlong on 2017/5/16.
 */
public class RecordCameraPullTest {

    public static void main(String[] args) throws Exception {
        recordCamera("D:\\Tianlong\\Videos\\camera\\output.mp4", 25);//这里将地址换成你的远程视频服务器地址例如recordCamera("rtmp://192.168.30.21/live/record1",25);  即可
    }

    /**
     * @param outputFile 服务器地址，我这里直接将视频输送到我本机目录
     * @param frameRate
     * @throws Exception
     * @throws InterruptedException
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     */
    public static void recordCamera(String outputFile, double frameRate)
        throws Exception {
        Loader.load(opencv_objdetect.class);
        FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
        grabber.start();//开启抓取器

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
        IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        //保存图片
        opencv_imgcodecs.cvSaveImage("data\\hello.jpg", grabbedImage);
        //获取BufferedImage可以给图像帧添加水印
//        Java2DFrameConverter javaconverter = new Java2DFrameConverter();
//        BufferedImage buffImg = javaconverter.convert(grabber.grab());

        FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(frameRate);

        recorder.start();//开启录制器
        long startTime = 0;
        long videoTS = 0;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        Frame rotatedFrame = converter.convert(grabbedImage);//不知道为什么这里不做转换就不能推到rtmp
        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            rotatedFrame = converter.convert(grabbedImage);
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40); //此处40应该为25帧率，应该由入参frameRate来设定
        }
        frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
    }
}
