package bying.sample;

import android.util.Log;

/**
 * Created by Wu_bying on 2017/5/15.
 */

public class L {

    private static final String TAG = "okhttp";
    private static boolean debug = true;

    public static void e(String msg){
        if(debug)
        Log.e(TAG,msg);
    }
}
