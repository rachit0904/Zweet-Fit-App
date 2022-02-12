package com.practise.zweet_fit_app.Server;


import static com.practise.zweet_fit_app.Util.Constant.JSON;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncRequestPost {
    private AsyncRequestPost delegate;

    public AsyncRequestPost() {

    }

    public Object Request(String url, RequestBody body) {

        RequestBody formBody = new FormBody.Builder()
                .add("key","MyApiKEy")
        .build();
//
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("key","MyApiKEy")
                .post(body)
                .build();
        try {

            Response response = client.newCall(request).execute();
            return response.body().toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

