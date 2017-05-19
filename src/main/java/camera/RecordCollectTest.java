package camera;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

/**
 * Created by Tianlong on 2017/5/16.
 */
public class RecordCollectTest {

    private static int imageWidth = 1280;
    private static int imageHeight = 720;

    public static void main(String[] args) throws Exception {
        String inputFile = "D:\\Tianlong\\Videos\\camera\\WIN_20170516_22_29_01_Pro.mp4";
        // Decodes-encodes
        String outputFile = "D:\\Tianlong\\Videos\\camera\\WIN_20170516_22_29_01_Pro_Copy.mp4";
        frameRecord(inputFile, outputFile);
    }

    public static void frameRecord(String inputFile, String outputFile) throws Exception {
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;
        Frame frame;
        try {  //建议在线程中使用该方法
            // 获取视频源
            grabber = new FFmpegFrameGrabber(inputFile);
            // 流媒体输出地址，分辨率（长，高），音频通道数
            recorder = new FFmpegFrameRecorder(outputFile, imageWidth, imageHeight, 2);
            grabber.start();
            recorder.start();
            // 开始取视频源
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
        } finally {
            if (recorder != null) {
                recorder.stop();
            }
            if (grabber != null) {
                grabber.stop();
            }
        }
    }

}
