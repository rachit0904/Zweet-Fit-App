package com.practise.zweet_fit_app.Util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Constant {
    public static String ServerUrl = "http://35.207.247.18:3578";
    public static String SeverApiKey = "MyApiKEy";
    public static String Shared_userdata = "zweet_userdata";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("StaticFieldLeak")

    public void senPushdNotification(final String title, final String body, final String iconUrl, final String uid, final String fcmToken, String sid, String cid, String name) {
        String key = "your key ";
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject notificationJson = new JSONObject();
                    JSONObject dataJson = new JSONObject();

                    notificationJson.put("text", body);
                    notificationJson.put("title", title);
                    notificationJson.put("icon", iconUrl);
                    notificationJson.put("click_action", ".Activity.ChatActivity");
                    notificationJson.put("priority", "high");

                    dataJson.put("uid", uid);
                    dataJson.put("sid", sid);
                    dataJson.put("cid", cid);
                    dataJson.put("name", name);
                    dataJson.put("nid", fcmToken);
                    dataJson.put("profile", iconUrl);

                    json.put("notification", notificationJson);
                    json.put("data", dataJson);
                    json.put("to", fcmToken);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + key)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.i("TAG", finalResponse);
                } catch (Exception e) {

                    Log.i("TAG", e.getMessage());
                }
                return null;
            }
        }.execute();
    }



}
