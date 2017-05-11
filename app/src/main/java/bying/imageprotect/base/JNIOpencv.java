package bying.imageprotect.base;

/**
 * Created by Wu_bying on 2017/5/11.
 */

public class JNIOpencv extends JNIBase{

    public JNIOpencv (String libraryName){
        super(libraryName);
    }

    public JNIOpencv(){
        System.loadLibrary("jniOpenCV");
    }

    public native int[] detectFace(int minFaceWidth, int minFaceHeight,
                                   String cascade, String filename);

}