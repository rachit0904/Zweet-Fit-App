package com.practise.zweet_fit_app.Server;

import static com.practise.zweet_fit_app.Util.Constant.ServerUrl;

import android.util.Log;

import com.practise.zweet_fit_app.Server.AsyncRequestGet;
import com.practise.zweet_fit_app.Server.AsyncRequestPost;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ServerRequests {
    AsyncRequestGet get;
    AsyncRequestPost post;

    public ServerRequests() {
        get = new AsyncRequestGet();
        post = new AsyncRequestPost();
    }

    public String checkServer() {
        return (String) get.Request(ServerUrl);
    }

    public String getSelectData(String tableName) {
        String url = ServerUrl + "/select?table=" + tableName;
        return (String) get.Request(url);
    }

    public String getSelectWqueryData(String tableName, String query, String value) {
        String url = ServerUrl + "/select?table=" + tableName + "&query=" + query + "&value" + value;
        return (String) get.Request(url);
    }


    public String addUserToGroupEvent(String eid, String uid) {
        String url = ServerUrl + "/addUserToGroupEvent";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid)
           .add("uid", uid).build();
            return (String) post.Request(url, formBody);
    }
//
    public String removeUserToGroupEvent(String eid, String uid) {
        String url = ServerUrl + "/removeUserToGroupEvent";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid)
           .add("uid", uid).build();
            return (String) post.Request(url,formBody);
    }
//
//
    public String AddPeerEvent(String p1id, String p2id, String title, String entry_coin, String target) {
        String url = ServerUrl + "/AddPeerEvent";
        RequestBody formBody = new FormBody.Builder()
           .add("p1id", p1id)
           .add("p2id", p2id)
           .add("title", title)
           .add("entry_coin", entry_coin)
           .add("target", target).build();
            return (String) post.Request(url, formBody);
    }
//
    public String addCoin(String tid, String uid, String formatted_date, String source, String eid, String amount) {
        String url = ServerUrl + "/addCoin";
        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("tid", tid)
           .add("uid", uid)
           .add("date", formatted_date)
           .add("source", source)
           .add("eid", eid)
           .add("amount", amount).build();
            return (String) post.Request(url, formBody);
    }
//
    public String addEvent(String p1id, String p2id, String title, String duration, String eid, String entry_coin, String target) {
        String url = ServerUrl + "/addEvent";
        RequestBody formBody = new FormBody.Builder()
           .add("p1id", p1id)
           .add("p2id", p2id)
           .add("eid", eid)
           .add("title", title)
           .add("duration", duration)
           .add("target", target).build();
            return (String) post.Request(url, formBody);
    }
//
    public String removeEvent(String eid) {
        String url = ServerUrl + "/removeEvent";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid).build();
            return (String) post.Request(url, formBody);
    }
//
//
    public String addStep(String uid, String steps) {
        String url = ServerUrl + "/addStep";
        RequestBody formBody = new FormBody.Builder()
           .add("uid", uid)
           .add("steps", steps).build();
            return (String) post.Request(url, formBody);
    }
//
//
    public String updateStep(String eid) {
        String url = ServerUrl + "/updateStep";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid).build();
            return (String) post.Request(url, formBody);
    }
//
//
    public String addFriend(String fid, String uid) {
        String url = ServerUrl + "/addFriend";


        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("fid", fid)
           .add("uid", uid).build();
            return (String) post.Request(url, formBody);
    }
//
    public String removeFriend(String id) {
        String url = ServerUrl + "/removeFriend";
        RequestBody formBody = new FormBody.Builder()
           .add("id", id).build();
            return (String) post.Request(url, formBody);
    }
//
    public String addInvitation(String id, String oid, String rid, String eid) {
        String url = ServerUrl + "/addInvitation";

        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("id", id)
           .add("oid", oid)
           .add("rid", rid)
           .add("eid", eid).build();
            return (String) post.Request(url, formBody);
    }

    public String removeInvitation(String id) {
        String url = ServerUrl + "/removeInvitation";
        RequestBody formBody = new FormBody.Builder()
           .add("id", id).build();
            return (String) post.Request(url, formBody);
    }


    public String addLeaderboard(String eid, String w1, String w2, String w3, String duration, String target, String participates) {
        String url = ServerUrl + "/addLeaderboard";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid)
           .add("w1", w1)
           .add("w2", w2)
           .add("w3", w3)
           .add("duration", duration)
           .add("target", target)
           .add("participates", participates).build();
            return (String) post.Request(url, formBody);

    }

    public String updateLeaderboard(String id, String eid, String w1, String w2, String w3, String duration, String target, String participates) {
        String url = ServerUrl + "/updateLeaderboard";

        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("id", id)
           .add("eid", eid)
           .add("w1", w1)
           .add("w2", w2)
           .add("w3", w3)
           .add("duration", duration)
           .add("target", target)
           .add("participates", participates).build();
            return (String) post.Request(url, formBody);
    }

    public String addStats(String uid, String date, String steps, String cal, String km, String hrs, String coins, String level) {
        String url = ServerUrl + "/addStats";
        RequestBody formBody = new FormBody.Builder()
           .add("uid", uid)
           .add("date", date)
           .add("steps", steps)
           .add("cal", cal)
           .add("km", km)
           .add("hrs", hrs)
           .add("coins", coins)
           .add("level", level).build();
            return (String) post.Request(url, formBody);
    }

    public String updateStats(String sid, String uid, String date, String steps, String cal, String km, String hrs, String coins, String level) {
        String url = ServerUrl + "/updateStats";

        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("sid", sid)
           .add("uid", uid)
           .add("date", date)
           .add("steps", steps)
           .add("cal", cal)
           .add("km", km)
           .add("hrs", hrs)
           .add("coins", coins)
           .add("level", level).build();
            return (String) post.Request(url, formBody);
    }

    public String addStreak(String uid, String date, String status) {
        String url = ServerUrl + "/addStreak";
        RequestBody formBody = new FormBody.Builder()
           .add("uid", uid)
           .add("date", date)
           .add("status", status).build();
            return (String) post.Request(url, formBody);
    }

    public String updateStreak(String id, String uid, String date, String status) {
        String url = ServerUrl + "/updateStreak";
        RequestBody formBody = new FormBody.Builder()
           .add("id", id)
           .add("uid", uid)
           .add("date", date)
           .add("status", status).build();
            return (String) post.Request(url, formBody);
    }


    public String updateStreak(String id, String eid, String w1, String w2, String w3, String duration, String target, String participates) {
        String url = ServerUrl + "/updateLeaderboard";

        JSONObject jsonObj = new JSONObject();
        RequestBody formBody = new FormBody.Builder()
           .add("id", id)
           .add("eid", eid)
           .add("w1", w1)
           .add("w2", w2)
           .add("w3", w3)
           .add("duration", duration)
           .add("target", target)
           .add("participates", participates).build();
            return (String) post.Request(url, formBody);

    }

    public String addUsers(String uid, String name, String date, String weight, String height, String target, String streak,
                           String subscription, String coins, String level, String win_rate, String mobile, String dp_url) {
        String url = ServerUrl + "/addUsers";

        JSONObject jsonObj = new JSONObject();


        RequestBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .add("name", name)
                .add("dob", date)
                .add("weight", weight)
                .add("height", height)
                .add("streak", streak)
                .add("target", target)
                .add("subscription", subscription)
                .add("coins", coins)
                .add("level", level)
                .add("win_rate", win_rate)
                .add("mobile", mobile)
                .add("dp_url", dp_url).build();


        Log.i("data:", formBody.toString());
        return (String) post.Request(url, formBody);
        //run this one
    }


    public String updateUsers(String uid, String name, String date, String weight, String height, String target, String streak,
                              String subscription, String coins, String level, String win_rate, String mobile, String dp_url) {
        String url = ServerUrl + "/updateUsers";
        RequestBody formBody = new FormBody.Builder()
          .add("uid", uid)
           .add("name", name)
           .add("date", date)
           .add("weight", weight)
           .add("height", height)
           .add("streak", streak)
           .add("target", target)
           .add("subscription", subscription)
           .add("coins", coins)
           .add("level", level)
           .add("win_rate", win_rate)
           .add("mobile", mobile)
           .add("dp_url", dp_url).build();
            return (String) post.Request(url, formBody);
    }
//
    public String invite(String id, String sid, String rid, String eid) {
        String url = ServerUrl + "/invite";
        RequestBody formBody = new FormBody.Builder()
           .add("id", id)
           .add("sid", sid)
           .add("rid", rid)
           .add("eid", eid).build();
         return (String) post.Request(url, formBody);
    }
//
    public String deleteinvite(String id) {
        String url = ServerUrl + "/deleteinvite";
        RequestBody formBody = new FormBody.Builder()
           .add("id", id).build();
            return (String) post.Request(url, formBody);
    }
//
    public String acceptinvite(String eid, String rid) {
        String url = ServerUrl + "/acceptinvite";
        RequestBody formBody = new FormBody.Builder()
           .add("eid", eid)
           .add("rid", rid).build();
            return (String) post.Request(url, formBody);
    }
//
}