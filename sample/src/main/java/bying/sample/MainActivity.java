package bying.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();
    private TextView mTvResult;
    private String mBaseUrl = "http://119.29.104.108/info.php";
    private ImageView mIvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private Map<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();

        mTvResult = (TextView) findViewById(R.id.tv_result);
        mIvResult = (ImageView) findViewById(R.id.iv_result);
    }

    /**
     * //1.拿到okHttpClent对象
     * //2.构造Request
     * //2.1构造requestBody
     * //2.2包装requestBody
     * //3.将Request封装为Call
     * //4.执行Call
     * @param view
     */

    public void doPost(View view) {
        //1.拿到okHttpClent对象

        FormBody.Builder requestBuilder = new FormBody.Builder();
        //2.构造Request
        //2.1构造requestBody
        RequestBody requestBody = requestBuilder.add("username", "bying").add("password", "123456").build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "login").post(requestBody).build();

        //3\4
        executeRequest(request);
    }

    public void doPostString(View view) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "{uesrname:bying,password:123456}");

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "postString").post(requestBody).build();

        //3\4
        executeRequest(request);
    }

    public void doPostFile(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (!file.exists()) {
            L.e(file.getAbsolutePath() + "not exist!");
            return;
        }

        //mime type
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "postFile").post(requestBody).build();

        //3\4
        executeRequest(request);
    }

    public void doUpload(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (!file.exists()) {
            L.e(file.getAbsolutePath() + "not exist!");
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
                        .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build())
                .build();


        //mime type

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "uploadInfo").post(requestBody).build();

        //3\4
        executeRequest(request);
    }

    public void doDownload(View view) {
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()//
                .url(mBaseUrl + "文件路径")
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse:");

                long total = response.body().contentLength();
                long sum = 0L;

                InputStream is = response.body().byteStream();

                int len = 0;

                File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
                byte[] buf = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);

                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);

                    sum += len;
                    L.e(sum +" / " +total);

                    final long finalSum = sum;
                    final long finalTotal = total;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvResult.setText(finalSum + " / " + finalTotal);
                        }
                    });

                }

                fos.flush();
                fos.close();
                is.close();

                L.e("download success!");

            }
        });
    }

    public void doDownloadImg(View view) {
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()//
                .url(mBaseUrl + "文件路径")
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse:");
                InputStream is = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIvResult.setImageBitmap(bitmap);
                    }
                });

            }
        });

    }

    public void doGet(View view) {
        //1.拿到okHttpClent对象
        //OkHttpClient okHttpClient = new OkHttpClient();

        //2.构造Request
        Request.Builder builder = new Request.Builder();
        final Request request = builder.get().url(mBaseUrl).build();
//                .get()//
//                .url(mBaseUrl + "login?username=bying&password=123456")
//                .build();

        executeRequest(request);


    }

    private void executeRequest(Request request) {
        //3.将Request封装为Call
        Call call = okHttpClient.newCall(request);

        //4.执行call
        //        Response response = call.execute();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse:");

                final String res = response.body().string();
                L.e(res);

                //                InputStream io = response.body().byteStream();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvResult.setText(res);
                    }
                });

            }
        });
    }
}
