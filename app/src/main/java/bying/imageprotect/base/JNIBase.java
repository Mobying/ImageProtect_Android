package bying.imageprotect.base;

/**
 * Created by Wu_bying on 2017/5/11.
 */

public class JNIBase {

    public JNIBase(){}

    public JNIBase(String libraryName){
        loadLibrary(libraryName);
    }

    private static void loadLibrary(String libraryName){
        System.loadLibrary(libraryName);
    }

}
