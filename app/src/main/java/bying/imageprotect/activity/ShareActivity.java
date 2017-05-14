package bying.imageprotect.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import java.io.InputStream;
import java.nio.ByteBuffer;

import bying.imageprotect.R;
import bying.imageprotect.base.LeftMenuBaseActivity;
import bying.imageprotect.util.AES256Util;
import bying.imageprotect.util.BytesToHex;

import static org.opencv.core.CvType.CV_8UC1;

//import android.graphics.Rect;

/**
 * Bitmap是Android系统中的图像处理的最重要类之一。
 * 用它可以获取图像文件信息，进行图像剪切、旋转、缩放等操作，
 * 并可以指定格式保存图像文件。
 * OpenCV的彩色是BGR不是RGB.
 * Mat即矩阵（Matrix）的缩写.
 */

public class ShareActivity extends LeftMenuBaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView nav;

    //图像变量
    private double max_size = 1024;

    public static final int CHOOSE_PHOTO = 2;
    String imagePath = null;

    private ImageView srcImage, openImage, privateImage;
    private Bitmap srcBitmap = null;
    private Bitmap srcFace = null;
    private Button selectImageBtn, detectFaceBtn, cutImageBtn, aesBtn, shareBtn;
    private int state = 0;//判断做了什么操作
    //人脸检测
    FaceDetector faceDetector = null;//人脸识别类的实例
    FaceDetector.Face[] face; //存储多张人脸的数组变量
    final int N_MAX = 5;//最多检测人脸数
    ProgressDialog progress;
    //ProgressBar progressBar = null;
    Face f;
    PointF midPoint;
    float dis;//两眼间的距离
    int dd;//两眼间距离的整数化
    Point eyeLeft, eyeRight;
    android.graphics.Rect faceRect;

    //图像切割变量
    Mat srcMat, dstMat, ropMat, mask;
    Bitmap ropBitmap;


    //    Thread checkFaceThread = new Thread(){
    //
    //        @Override
    //        public void run() {
    //            // TODO Auto-generated method stub
    //            Bitmap faceBitmap = detectFace();
    //            mainHandler.sendEmptyMessage(2);
    //            Message m = new Message();
    //            m.what = 0;
    //            m.obj = faceBitmap;
    //            mainHandler.sendMessage(m);
    //
    //        }
    //
    //    };

    //    Handler mainHandler = new Handler(){
    //
    //        @Override
    //        public void handleMessage(Message msg) {
    //            // TODO Auto-generated method stub
    //            //super.handleMessage(msg);
    //            switch (msg.what){
    //                case 0:
    //                    Bitmap b = (Bitmap) msg.obj;
    //                    image1.setImageBitmap(b);
    //                    ShowToast("检测完毕");
    //                    break;
    //                case 1:
    //                    progress = new ProgressDialog(ShareActivity.this);
    //                    progress.setMessage("正在检测...");
    //                    progress.setCanceledOnTouchOutside(false);
    //                    progress.show();
    ////                    showProcessBar();
    //                    break;
    //                case 2:
    ////                    progressBar.setVisibility(View.GONE);
    //                    progress.dismiss();
    ////                    detectFaceBtn.setClickable(false);
    //                    break;
    //                default:
    //                    break;
    //            }
    //        }
    //
    //    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.share_draw);
        nav = (NavigationView) findViewById(R.id.nav_view);
        loginListener(nav);
        navigationListener(nav, 2);
        initToolbar();//注意和侧滑的顺序，否则无法点击
        initLeftSlip(toolbar, mDrawerLayout);
        //        initToolbar();

        //图像处理
        staticLoadCVLibraries();
        srcImage = (ImageView) findViewById(R.id.image1);
        privateImage = (ImageView) findViewById(R.id.ropImage);
        //        image2 = (ImageView) findViewById(R.id.image2);
        //        image3 = (ImageView) findViewById(R.id.image3);

        selectImageBtn = (Button) findViewById(R.id.btn_selectImage);
        detectFaceBtn = (Button) findViewById(R.id.btn_detectFace);
        cutImageBtn = (Button) findViewById(R.id.btn_cutImage);
        shareBtn = (Button) findViewById(R.id.btn_share);
        aesBtn = (Button) findViewById(R.id.btn_aes);

        selectImageBtn.setOnClickListener(this);
        selectImageBtn.setTag(1);
        detectFaceBtn.setOnClickListener(this);
        detectFaceBtn.setTag(2);
        cutImageBtn.setOnClickListener(this);
        cutImageBtn.setTag(3);
        aesBtn.setOnClickListener(this);
        aesBtn.setTag(4);
        shareBtn.setOnClickListener(this);
        shareBtn.setTag(5);
    }

    private void initToolbar() {
        toolbar.setTitle(R.string.share);//设置Toolbar标题
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        switch (tag) {
            case 1:
                if (ContextCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ShareActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    selectImage();
                    ShowToast("打开相册");
                }
                break;
            case 2:
                if (state != 1)
                    ShowToast("请选择照片");
                else {
                    initFaceDetect();
                    progress = new ProgressDialog(ShareActivity.this);
                    progress.setMessage("正在检测...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    detectFace();
                    // mainHandler.sendEmptyMessage(1);
                    // checkFaceThread.start();
                    // convertGray();
                }
                break;
            case 3:
                if (state != 2)
                    ShowToast("请先进行人脸检测");
                else {
                    cutImage();
                    //state = 3;
                }
                break;
            case 4:
                if (state != 3)
                    ShowToast("请先进行图像切割");
                else {
                    aes256();
                    //state = 4;
                }
                break;
            //            case 5:
            //                break;
            default:
                break;

        }
    }

    //OpenCV库静态加载并初始化
    private void staticLoadCVLibraries() {
        boolean load = OpenCVLoader.initDebug();
        if (load) {
            //            Log.i("CV", "Open CV Libraries loaded...");
            ShowLog("Open CV Libraries loaded...");//Base类有logi的封装
        }
    }

    //打开相册选择图片
    private void selectImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    //灰度化
    //    private void convertGray() {
    //        Mat src = new Mat();
    //        Mat temp = new Mat();
    //        Mat dst = new Mat();
    //        Utils.bitmapToMat(bitmap1, src);
    //        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);//从RGB或BGR图像中删除alpha通道
    //        Log.i("CV", "image type:" + (temp.type() == CvType.CV_8UC3));
    //        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_BGR2GRAY);
    //        Utils.matToBitmap(dst, bitmap1);
    //        image1.setImageBitmap(bitmap1);
    //    }

    //访问权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    ShowToast("你拒绝了访问权限");
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK) {
            srcImage.setVisibility(View.VISIBLE);
            state = 1;
            handleImageOnKitKat(data);
        }
        //        if(requestCode > PICK_IMAGE_REQUEST){
        //            ShowToast("最多选择"+PICK_IMAGE_REQUEST+"张");
        //        }
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Log.d("image-tag", "start to decode selected image now...");
                InputStream input = getContentResolver().openInputStream(imageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                int raw_width = options.outWidth;
                int raw_height = options.outHeight;
                int max = Math.max(raw_width, raw_height);
                int newWidth = raw_width;
                int newHeight = raw_height;
                int inSampleSize = 1;
                if (max > max_size) {//压缩图片
                    newWidth = raw_width / 2;
                    newHeight = raw_height / 2;
                    while ((newWidth / inSampleSize) > max_size || (newHeight / inSampleSize) > max_size) {
                        inSampleSize *= 2;
                    }
                }

                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                srcBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, options);

                srcImage.setImageBitmap(srcBitmap);
                privateImage.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //        displayImage(imagePath); // 根据图片路径显示图片
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //    private void displayImage(String imagePath) {
    //        if (imagePath != null) {
    //            srcBitmap = BitmapFactory.decodeFile(imagePath);
    //            srcImage.setImageBitmap(srcBitmap);
    //        } else {
    //            ShowToast("找不到图片");
    //        }
    //    }


    public void initFaceDetect() {
        srcFace = srcBitmap.copy(Config.RGB_565, true);
        int w = srcFace.getWidth();
        int h = srcFace.getHeight();
        ShowLog("待检测图像: w = " + w + ", h = " + h);
        faceDetector = new FaceDetector(w, h, N_MAX);
        face = new FaceDetector.Face[N_MAX];
    }

    public boolean checkFace(android.graphics.Rect rect) {//判断人脸像素
        int w = rect.width();
        int h = rect.height();
        int s = w * h;
        ShowLog("人脸 宽 w = " + w + ", 高 h = " + h + ", 人脸面积 s = " + s);
        //        if(s < 10000){
        //            ShowLog("无效人脸，舍弃.");
        //            return false;
        //        }
        //        else{
        //            ShowLog("有效人脸，保存.");
        return true;
        //        }
    }

    public void detectFace() {
        //		Drawable d = getResources().getDrawable(R.drawable.face_2);
        //		Log.i(tag, "Drawable尺寸 w = " + d.getIntrinsicWidth() + "h = " + d.getIntrinsicHeight());
        //		BitmapDrawable bd = (BitmapDrawable)d;
        //		Bitmap srcFace = bd.getBitmap();

        int nFace = faceDetector.findFaces(srcFace, face);
        if (nFace == 0) {
            ShowToast("未检测到人脸");
            progress.dismiss();
            return;
        }
        ShowLog("检测到" + nFace + "张人脸");
        for (int i = 0; i < nFace; i++) {
            f = face[i];
            midPoint = new PointF();
            dis = f.eyesDistance();//两眼间的距离
            f.getMidPoint(midPoint);//两眼间中心点
            dd = (int) (dis);//两眼间距离的整数化
            eyeLeft = new Point((int) (midPoint.x - dis / 2), (int) midPoint.y);
            eyeRight = new Point((int) (midPoint.x + dis / 2), (int) midPoint.y);
            //以两眼间距离为基本单位，得到宽度为3倍眼距的正方形矩阵
            faceRect = new android.graphics.Rect((int) (midPoint.x - 1.5 * dd), (int) (midPoint.y - dd), (int) (midPoint.x + 1.5 * dd), (int) (midPoint.y + 2 * dd));

            //安卓的Rect和OpenCV的Rect是不一样的两个类
            //org.opencv.core.Rect da = new org.opencv.core.Rect();

            //输出Log信息以便查看
            ShowLog("midPoint:x = " + midPoint.x + ", y = " + midPoint.y);
            ShowLog("两眼间的距离：" + dd);
            ShowLog("左眼坐标：x = " + eyeLeft.x + ", y = " + eyeLeft.y);
            ShowLog("右眼坐标：x = " + eyeRight.x + ", y = " + eyeRight.y);
            ShowLog("方框坐标：左上角 ( x = " + faceRect.left + ", y = " + faceRect.top + ")");
            ShowLog("方框坐标：右下角 ( x = " + faceRect.right + ", y = " + faceRect.bottom + ")");
            //画图：canvas画布、paint画笔
            if (checkFace(faceRect)) {
                Canvas canvas = new Canvas(srcFace);
                Paint p = new Paint();
                p.setAntiAlias(true);//设置画笔锯齿效果
                p.setStrokeWidth(8);//设置边框的宽度
                p.setStyle(Paint.Style.STROKE);//空心
                p.setColor(Color.GREEN);//颜色
                //画出圆形的左右眼
                //canvas.drawCircle(eyeLeft.x, eyeLeft.y, 20, p);
                //canvas.drawCircle(eyeRight.x, eyeRight.y, 20, p);
                //画出人脸框
                canvas.drawRect(faceRect, p);
            }


        }
        //        ImageUtil.saveJpeg(srcFace);
        //        ShowLog("保存完毕");

        //将绘制完成后的faceBitmap返回
        ShowToast("检测完毕");
        progress.dismiss();
        //        return srcFace;
        //        Bitmap faceBitmap = detectFace();
        //        ShowLog(test.col,test.row);
        srcImage.setImageBitmap(srcFace);
        state = 2;
        //        Mat test = image1(Rect());

    }

    //    public void showProcessBar(){
    //        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.share_draw);
    //        progressBar = new ProgressBar(ShareActivity.this, null, android.R.attr.progressBarStyleLargeInverse); //ViewGroup.LayoutParams.WRAP_CONTENT
    //        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    //        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
    //        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    //        progressBar.setVisibility(View.VISIBLE);
    //        //progressBar.setLayoutParams(params);
    //        mainLayout.addView(progressBar, params);
    //
    //    }
    public void cutImage() {//优化后的简洁版
        srcMat = new Mat();
        Utils.bitmapToMat(srcBitmap, srcMat);
        //dstMat = srcMat.clone();
        //感兴趣区域，即人脸检测的隐私区域
        Rect ropRect = new Rect(faceRect.left, faceRect.top, 3 * dd, 3 * dd);
        ropMat = new Mat(srcMat, ropRect);//ROP矩阵
        //Imgproc.cvtColor(temp,ropMat,Imgproc.COLOR_BGR2BGRA);
        //创建一个和隐私区域大小一样的位图
        ropBitmap = Bitmap.createBitmap(ropMat.width(), ropMat.height(),
                Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(ropMat, ropBitmap);
        //之前重选照片的时候把ropImageView设为不可见，这里重新设为可见
        privateImage.setVisibility(View.VISIBLE);
        privateImage.setImageBitmap(ropBitmap);//切割后的人脸图像

        Scalar s = new Scalar(0);
        mask = Mat.ones(ropMat.size(), CV_8UC1);//全0掩模
        //Mat tempMat = new Mat(dstMat,ropRect);
        ropMat.setTo(s, mask);
        //Utils.matToBitmap(dstMat, srcBitmap);
        Utils.matToBitmap(srcMat, srcBitmap);
        //srcImage.setVisibility(View.GONE);
        //openImage = (ImageView) findViewById(R.id.image1);
        //openImage.setImageBitmap(srcBitmap);
        srcImage.setImageBitmap(srcBitmap);//切割后的公开图像

        state = 3;
        ShowLog(ropMat.cols());
    }

    public void aes256() {
        //将rop区域的bitmap转变为bytes
        int bytes = ropBitmap.getByteCount();//字节数
        ByteBuffer buf = ByteBuffer.allocate(bytes);//分配空间
        ropBitmap.copyPixelsToBuffer(buf);
        byte[] ropByte = buf.array();

        try {
            //获得密钥
            byte[] aesKey = AES256Util.initKey();
            int len = aesKey.length;//aes密钥字节长度
            ShowLog("AES256 密钥长度为：" + len + "字节，" + 8 * len + "位");
            ShowLog("AES256 密钥 : " + BytesToHex.fromBytesToHex(aesKey));
            //加密
            ShowToastLong("加密中");
            byte[] encrypt = AES256Util.encryptAES(ropByte, aesKey);
            ShowLog("隐私区域密文 : " + BytesToHex.fromBytesToHex(encrypt));
            //显示图像
            Bitmap aesBitmap = Bitmap.createBitmap(ropMat.width(), ropMat.height(),
                    Bitmap.Config.ARGB_8888);
            aesBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(encrypt));
            privateImage.setImageBitmap(aesBitmap);
            //解密
            //byte[] plain = AES256Util.decryptAES(encrypt, aesKey);
            //Bitmap decryptBmp = Bitmap.createBitmap(ropMat.width(), ropMat.height(),
            //      Bitmap.Config.ARGB_8888);
            //decryptBmp.copyPixelsFromBuffer(ByteBuffer.wrap(plain));
            //srcImage.setImageBitmap(decryptBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        state = 4;
    }

}
