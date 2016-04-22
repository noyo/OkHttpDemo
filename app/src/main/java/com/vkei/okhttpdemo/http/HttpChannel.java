package com.vkei.okhttpdemo.http;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpChannel {
    private final static String TAG = "HttpChannel";
    
    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Context mApp;
    private OkHttpUtils mOkHttpUtils;
    private String mResponse;

    public HttpChannel(Context context) {
        mApp = context;
//        sendCmd("https://vservice.vkei.cn/site/verify", "{\"body\":\"{\\\"csig\\\":\\\"56664c6e53e974edf0de1276486a1548\\\",\\\"imei\\\":\\\"000000001234566\\\",\\\"model\\\":\\\"OPPO R9 Plusm A\\\",\\\"sig\\\":\\\"696cfd75608f08b3d7b2302d2b4aa95f\\\",\\\"vcode\\\":\\\"bab6f2306dcb2d66d4973919b749e735\\\",\\\"vid\\\":\\\"bb012345678901234567890123456789\\\"}\",\"head\":\"{\\\"appid\\\":\\\"android_002\\\",\\\"cmd\\\":\\\"verify\\\",\\\"errorcode\\\":0,\\\"errormsg\\\":\\\"\\\",\\\"network\\\":3,\\\"seq\\\":2,\\\"sid\\\":\\\"bb012345678901234567890123456789\\\",\\\"version\\\":\\\"1.7.1\\\",\\\"versioncode\\\":65}\"}");
        sendCmd("https://vservice.vkei.cn/site/getpos", "{\"body\":\"{}\",\"head\":\"{\\\"appid\\\":\\\"android_002\\\",\\\"cmd\\\":\\\"getpos\\\",\\\"errorcode\\\":0,\\\"errormsg\\\":\\\"\\\",\\\"network\\\":3,\\\"seq\\\":1,\\\"sid\\\":\\\"bb012345678901234567890123456789\\\",\\\"version\\\":\\\"1.7.1\\\",\\\"versioncode\\\":65}\"}");
    }

    public void sendCmd(String url, String body) {
//        String responseStr = httpPost(url, body);
        httpPost(url, body);
//        Log.d(TAG, "Response:" + responseStr);
    }

    public void httpPost(String url, String entity) {

        try {
            mOkHttpUtils = OkHttpUtils.getInstance();
            mOkHttpUtils.setCertificates(mApp.getAssets().open("vkei_server.bks"));
            OkHttpClient client = mOkHttpUtils.getOkHttpClient();


            RequestBody body = RequestBody.create(JSON, entity);
            Request request = new Request.Builder()
                    .tag(this)
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "vkei")
                    .build();

            client.newCall(request).enqueue(new CustomCallBack<String>() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mResponse = onResponse(response);
                }
            });
//            return mResponse;
            /*Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String s = response.body().string();
                return s;
            } else {
                throw new Exception("connect fail");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("----", e.toString());
//            return "";
        }
    }

}
