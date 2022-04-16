package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.practise.zweet_fit_app.Adapters.CoinTransactionParentAdapter;
import com.practise.zweet_fit_app.Modals.CoinTransactionChildModal;
import com.practise.zweet_fit_app.Modals.CoinTransactionParentModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class CoinHistory extends Fragment {
    SharedPreferences pref;
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    RecyclerView rv;
    List<CoinTransactionParentModal> coinTransactionParentModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_coin_history, container, false);
        rv=view.findViewById(R.id.coinParentRv);
        rv.setHasFixedSize(true);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        CoinTransactionParentAdapter adapter=new CoinTransactionParentAdapter(getContext(),getTransactions());
        rv.setAdapter(adapter);
        return view;
    }

    private List<CoinTransactionParentModal> getTransactions() {
        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://35.207.233.155:3578/selectwQuery?table=coin_history&query=uid&value="+pref.getString("id",""))
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            for (int i = 0; i < Jarray.length(); i++) {
                List<CoinTransactionChildModal> childModalList=new ArrayList<>();
                JSONObject object = Jarray.getJSONObject(i);
                String date=getDate(object.getString("date"));
                CoinTransactionParentModal modal=new CoinTransactionParentModal();
                modal.setDate(date);
                for(int k=i;k<Jarray.length();k++)
                {
                    JSONObject object2 = Jarray.getJSONObject(k);
                    String tempdate = getDate(object2.get("date").toString());
                    String src = object2.get("source").toString();
                    String coin = object2.get("amount").toString();
                    if(tempdate.equals(date))
                    {
                        CoinTransactionChildModal childModal2=new CoinTransactionChildModal();
                        childModal2.setCoins(coin);
                        childModal2.setSource(src);
                        childModalList.add(childModal2);
                    }
                    else
                    {
                        if(i<Jarray.length())
                        {
                            i=(k-1);
                        }
                        break;
                    }
                }
                modal.setChildModalList(childModalList);
                coinTransactionParentModalList.add(modal);
                if(i==(Jarray.length()-2))
                {
                    break;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return coinTransactionParentModalList;
    }

    private String getDate(String date) {
        String d=date.split(" ")[0];
        String dd=d.split("-")[2];
        String mm=months[Integer.parseInt(d.split("-")[1])-1];
        String yy=d.split("-")[0];
        return dd+" "+mm+" "+yy;
    }
}