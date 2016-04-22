package com.vkei.okhttpdemo.http;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * com.vkei.common.channel
 *
 * @author Chris <br/>
 *         创建日期 2016.04.2016/4/20 <br/>
 *         功能:
 *         修改者，修改日期，修改内容.
 */
public class OkHttpUtils {

    private static final byte[] LOCK = new byte[0];

    public static OkHttpUtils mInstance;

    private OkHttpClient mOkHttpClient;

    private OkHttpUtils() {
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(null);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = getOkHttpClientBuilder().build();
        } else {
            mOkHttpClient = okHttpClient;
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    @NonNull
    private OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder.cookieJar(new CookieJar() {
            HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).hostnameVerifier(new HostnameVerifier() {//绕过 证书验证
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);

        setCertificates(okHttpClientBuilder);

        return okHttpClientBuilder;
    }

    /**
     * 网站证书验证
     */
    private void setCertificates(OkHttpClient.Builder builder) {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory();

        if (sslSocketFactory == null) {
            return;
        }
        builder.sslSocketFactory(sslSocketFactory);
    }

    /**
     * 根据tag取消连接
     * @param obj
     */
    public void cancelByTag(Object obj) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (obj.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void onDestroy() {
        if (null != mInstance) {
            synchronized(LOCK){
                if (null != mInstance) {
                    if (mOkHttpClient != null) {
                        mOkHttpClient = null;
                    }
                    mInstance = null;
                }
            }
        }
    }
}
