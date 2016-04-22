package com.vkei.okhttpdemo.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * com.vkei.common.util.http
 *
 * @author Chris <br/>
 *         创建日期 2016.04.2016/4/21 <br/>
 *         功能:
 *         修改者，修改日期，修改内容.
 */
public abstract class BaseCallBack<T> implements Callback {

    @Override
    public void onResponse(Call call, Response response) throws IOException {
    }

    public abstract T onResponse(Response response) throws IOException;
}
