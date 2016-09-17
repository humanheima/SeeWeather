package com.humanheima.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnGet)
    Button btnGet;
    @BindView(R.id.btnPost)
    Button btnPost;
    @BindView(R.id.btnFilePost)
    Button btnFilePost;
    @BindView(R.id.btnFileDownLoad)
    Button btnFileDownLoad;
    @BindView(R.id.btnLoadImg)
    Button btnLoadImg;
    @BindView(R.id.btnSupportCallBack)
    Button btnSupportCallBack;
    @BindView(R.id.btnSupportSession)
    Button btnSupportSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnGet, R.id.btnPost, R.id.btnFilePost, R.id.btnFileDownLoad, R.id.btnLoadImg, R.id.btnSupportCallBack, R.id.btnSupportSession})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGet:
                //创建一个OkHttpClient对象
                OkHttpClient client = new OkHttpClient();
                //创建一个OkHttpClient的请求
                final Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .build();
                Call call = client.newCall(request);
                //异步执行任务
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().toString();
                        //onResponse执行的线程并不是UI线程，如果想根据返回的结果更新UI应该这么写
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新ui
                            }
                        });

                    }
                });
                //请求加入调度
                break;
            case R.id.btnPost:
                /**
                 * /ost 提交Json数据,使用Request的post方法来提交请求体RequestBody
                 */
               /*
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient client1 = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, "换成你想要上传的字符串");
                Request request1 = new Request.Builder()
                        .url("")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client1.newCall(request1).execute();
                    //接受返回的字符串
                    //return response.body().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                /**
                 *POST提交键值对
                 */
                OkHttpClient client1 = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("search", "wiki")
                        .add("name", "hongmin")
                        .add("age", "23")
                        .build();
                Request request1 = new Request.Builder()
                        .url("url")
                        .post(body)
                        .build();
                try {
                    Response response = client1.newCall(request1).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("UNEXPECTED CODE" + response);
                    } else {
                        Log.e("tag", response.body().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btnFilePost:
                break;
            case R.id.btnFileDownLoad:
                break;
            case R.id.btnLoadImg:
                break;
            case R.id.btnSupportCallBack:
                break;
            case R.id.btnSupportSession:
                break;
        }
    }

    public void postFile() {

    }

    /**
     * 上传图片和键值对
     */
    public void postMultipart() {
        final String IMGUR_CLIENT_ID = "9199fdef135c122";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "good")
                .addFormDataPart("image", "logo.png", RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("UNEXPECTED CODE" + response);
            } else {
                Log.e("tag", response.body().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
