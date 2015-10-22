package com.printserver.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.printserver.base.BaseHelp;
import com.printserver.base.widgets.BaseListView;
import com.printserver.base.zxing.CaptureActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by zhangxin on 2015/9/13.
 */
public class Test extends Activity implements View.OnClickListener {

    private ImageView mCreateView;
    private ImageView mCreateBarView;
    private ImageView mCreate417View;
    private EditText resultTxt;
    private EditText sendmsg;

    private BaseListView listview;
    private ArrayList<String> data;
    private BaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cameratest_layout);
        initWidgets();

        init();
        setListener();
    }

    private void initWidgets() {
        Button btn = (Button) findViewById(R.id.btn_handle_decode);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.btn_create_decode);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.btn_create_barcode);
        btn.setOnClickListener(this);
        btn=(Button)findViewById(R.id.btn_create_pdf417);
        btn.setOnClickListener(this);
        btn=(Button)findViewById(R.id.btn_handle_loading);
        btn.setOnClickListener(this);
        btn=(Button)findViewById(R.id.sentbt);
        btn.setOnClickListener(this);
        mCreateView = (ImageView) findViewById(R.id.iv_result);
        mCreateBarView = (ImageView) findViewById(R.id.iv_result_bar);
        mCreate417View=(ImageView)findViewById(R.id.PDF417_result);
        resultTxt=(EditText)findViewById(R.id.result_txt);
        sendmsg=(EditText)findViewById(R.id.sendtxt);

    }

    public String str="";
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                Toast.makeText(Test.this,str,Toast.LENGTH_LONG).show();
            }
        };
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_handle_decode:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_create_decode:
                mCreateView
                        .setImageBitmap(createQRImage("你好", 400, 400));
                break;
            case R.id.btn_create_barcode:
                mCreateBarView.setImageBitmap(creatBarcode(getApplicationContext(), "692697307889", 600, 400, true));
                break;
            case R.id.btn_create_pdf417:
                mCreate417View.setImageBitmap(createPDF417("",1,1));
                break;
            case R.id.btn_handle_loading:
                Intent intents=new Intent();
                intents.setClass(Test.this, LoadingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("result", "First");
                intents.putExtras(bundle);
                startActivityForResult(intents, 0);
                break;
            case R.id.sentbt:
                new Thread(){
                    public void run(){
                        Socket socket = null;
                        try {
                            socket=new Socket("192.168.1.128",50000);
                            if(socket == null)
                                Log.v("serversocket_error","error");
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();
                            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                            PrintStream printStream = new PrintStream(outputStream);
                            printStream.println(sendmsg.getText().toString());
                            Log.v("向服务端发送：", sendmsg.getText().toString());
                            this.sleep(1000);
                            String receive=bufferedReader.readLine();
                            Log.v("服务端返回：", receive);
                            str=receive;
                            handler.sendEmptyMessage(0x123);
                            bufferedReader.close();
                            printStream.close();
                            inputStream.close();
                            outputStream.close();
                        } catch (ConnectException e){
                            Log.v("connect_error", "服务端未开启！");
                            str="服务端未开启！";
                            handler.sendEmptyMessage(0x123);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        } finally {
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }.start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                if (scanResult != null) {
                    resultTxt.setText(scanResult);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                if (scanResult != null) {
                    resultTxt.setText(scanResult);
                }
            }
        }
    }

    public Bitmap createPDF417(String content, final int width, final int height){
        content="CASCF12015000020";
        Bitmap barcodeBitmap = encodeAsBitmap(content, BarcodeFormat.PDF_417,
                30, 12);
        return barcodeBitmap;
    }

    /**
     * 生成二维码 要转换的地址或字符串,可以是中文
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    public Bitmap createQRImage(String url, final int width, final int height) {
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成条形码
     *
     * @param context
     * @param contents
     *            需要生成的内容
     * @param desiredWidth
     *            生成条形码的宽带
     * @param desiredHeight
     *            生成条形码的高度
     * @param displayCode
     *            是否在条形码下方显示内容
     * @return
     */
    public static Bitmap creatBarcode(Context context, String contents,
                                      int desiredWidth, int desiredHeight, boolean displayCode) {
        Bitmap ruseltBitmap = null;
        /**
         * 图片两端所保留的空白的宽度
         */
        int marginW = 20;
        /**
         * 条形码的编码类型
         */
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        if (displayCode) {
            Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
                    * marginW, desiredHeight, context);
            ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
                    0, desiredHeight));
        } else {
            ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
        }

        return ruseltBitmap;
    }

    /**
     * 生成条形码的Bitmap
     *
     * @param contents
     *            需要生成的内容
     * @param format
     *            编码格式
     * @param desiredWidth
     * @param desiredHeight
     * @return
     * @throws WriterException
     */
    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成显示编码的Bitmap
     *
     * @param contents
     * @param width
     * @param height
     * @param context
     * @return
     */
    protected static Bitmap creatCodeBitmap(String contents, int width,
                                            int height, Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    /**
     * 将两个Bitmap合并成一个
     *
     * @param first
     * @param second
     * @param fromPoint
     *            第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                          PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = 20;
        Bitmap newBitmap = Bitmap.createBitmap(
                first.getWidth() + second.getWidth() + marginW,
                first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, marginW, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

    private void init(){
        data = new ArrayList<String>();
        //虚拟一些数据
        data.add("a");
        data.add("b");
        data.add("c");
        data.add("e");
        data.add("f");
        data.add("g");
        data.add("h");
        data.add("i");
        data.add("j");
        data.add("k");
        data.add("L");
        data.add("M");
        data.add("L");
        data.add("N");
        data.add("O");
        data.add("P");
        data.add("Q");

        listview = (BaseListView) findViewById(R.id.listview_test);

        adapter = new BaseAdapter(){
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(getApplicationContext());
                textView.setHeight(100);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(0x66666666);
                textView.setTextColor(0xaaffffff);
                textView.setText(data.get(position));
                return textView;
            }
        };
        listview.setAdapter(adapter);
    }

    private void setListener(){
        listview.setOnRefreshListner(new BaseListView.OnRefreshListner() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, ArrayList<String>>(){
                    @Override
                    protected ArrayList<String> doInBackground(Void... params) {
                        try {
                            //模拟从服务器获取数据的过程
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ArrayList<String> virtualData = new ArrayList<String>();
                        virtualData.add("Head刷新后的新数据1");
                        virtualData.add("Head刷新后的新数据2");
                        virtualData.add("Head刷新后的新数据3");
                        virtualData.add("Head刷新后的新数据4");
                        virtualData.add("Head刷新后的新数据5");
                        virtualData.add("Head刷新后的新数据6");

                        return virtualData;
                    }
                    //更新UI的方法,系统自动实现
                    @Override
                    protected void onPostExecute(ArrayList<String> result) {
                        data.addAll(0,result);//注意是往前添加数据
                        adapter.notifyDataSetChanged();
                        listview.onRefreshComplete();//完成下拉刷新,这个方法要调用
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
        //创建FootView
        final View footer = View.inflate(Test.this, R.layout.footer, null);
        listview.setOnAddFootListener(new BaseListView.OnAddFootListener() {
            @Override
            public void addFoot() {
                listview.addFooterView(footer);
            }
        });

        listview.setOnFootLoadingListener(new BaseListView.OnFootLoadingListener() {
            @Override
            public void onFootLoading() {
                new AsyncTask<Void, Void, ArrayList<String>>(){
                    @Override
                    protected ArrayList<String> doInBackground(Void... params) {
                        try {
                            //模拟从服务器获取数据的过程
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ArrayList<String> virtualData = new ArrayList<String>();
                        virtualData.add("Foot刷新后的新数据1");
                        virtualData.add("Foot刷新后的新数据2");
                        virtualData.add("Foot刷新后的新数据3");
                        virtualData.add("Foot刷新后的新数据4");
                        virtualData.add("Foot刷新后的新数据5");
                        virtualData.add("Foot刷新后的新数据6");
                        return virtualData;
                    }

                    //在doInBackground后面执行
                    @Override
                    protected void onPostExecute(ArrayList<String> result) {
                        data.addAll(result);//这个是往后添加数据
                        adapter.notifyDataSetChanged();
                        listview.onFootLoadingComplete();//完成上拉刷新,就是底部加载完毕,这个方法要调用
                        //移除footer,这个动作不能少
                        listview.removeFooterView(footer);
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
    }
}
