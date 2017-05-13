package bying.imageprotect.base;

import android.graphics.Bitmap;
import android.os.Bundle;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Wu_bying on 2017/5/11.
 */

public class FaceDetectorBase extends LeftMenuBaseActivity {
    //
    static {
                System.loadLibrary("opencv_java");
        ////        System.loadLibrary("openCVLibrary2411");
//        String lib = Core.NATIVE_LIBRARY_NAME;
        //        System.loadLibrary(lib);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Bitmap faceDetect(Bitmap bitmap) {
        //        CascadeClassifier faceDetector = new CascadeClassifier(FaceDetector.class.getResource("haarcascade_frontalface_alt.xml").getPath());
        CascadeClassifier faceDetector = new CascadeClassifier("E:\\OpenCV-2.4.11-android-sdk\\OpenCV-android-sdk\\data\\haarcascade_frontalface_alt.xml");
        //        Mat src = new Mat();
        Mat temp = new Mat();
        //        Mat res = new Mat();
        //        Utils.bitmapToMat(bitmap, src);
        Utils.bitmapToMat(bitmap, temp);
        //        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(temp, faceDetections);


        for (Rect rect : faceDetections.toArray()) {
            System.out.println("正在检测……");
            Core.rectangle(temp, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
        ShowLog("正在检测");
        //        Imgproc.cvtColor(temp, res, Imgproc.COLOR_BGR2BGRA);
        //        Utils.matToBitmap(res, bitmap);
        //        Utils.matToBitmap(image, bitmap);
        Utils.matToBitmap(temp, bitmap);
        return bitmap;
        //        return res;
        //
        //        String filename = "ouput.png";
        //        System.out.println(String.format("Writing %s", filename));
        //        Highgui.imwrite(filename, image);
    }

}
