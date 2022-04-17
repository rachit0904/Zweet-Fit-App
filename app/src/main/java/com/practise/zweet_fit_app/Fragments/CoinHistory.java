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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
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


public class CoinHistory extends Fragment implements View.OnClickListener {
    SharedPreferences pref;
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    RecyclerView rv;
    ImageView back;
    LottieAnimationView coinAnim;
    TextView noData;
    List<CoinTransactionParentModal> coinTransactionParentModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_coin_history, container, false);
        rv=view.findViewById(R.id.coinParentRv);
        back=view.findViewById(R.id.back);
        coinAnim=view.findViewById(R.id.coinAnim);
        noData=view.findViewById(R.id.noData);
        rv.setHasFixedSize(true);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        CoinTransactionParentAdapter adapter=new CoinTransactionParentAdapter(getContext(),getTransactions());
        rv.setAdapter(adapter);
        if(coinTransactionParentModalList.isEmpty()){
            coinAnim.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            coinAnim.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
        back.setOnClickListener(this);
        return view;
    }

    private List<CoinTransactionParentModal> getTransactions() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=coin_history&query=uid&value="+pref.getString("id",""))
                .method("GET", null)
                .addHeader("Key", "MyApiKEy")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        String respo = response.body().string();
        JSONObject Jobject = new JSONObject(respo);
        JSONArray Jarray = Jobject.getJSONArray("data");
        for (int i = 0; i < Jarray.length(); i++) {
            List<CoinTransactionChildModal> childModalList=new ArrayList<>();
            JSONObject object = Jarray.getJSONObject(i);
            String date=getDate(object.getString("date"));
            CoinTransactionParentModal modal=new CoinTransactionParentModal();
            modal.setDate(date);
            if(coinTransactionParentModalList.contains(modal)){
                Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            }
           {
                for (int k = i; k < Jarray.length(); k++) {
                    JSONObject object2 = Jarray.getJSONObject(k);
                    String tempdate = getDate(object2.get("date").toString());
                    String src = object2.get("source").toString();
                    String coin = object2.get("amount").toString();
                    if (tempdate.equals(date)) {
                        CoinTransactionChildModal childModal2 = new CoinTransactionChildModal();
                        childModal2.setCoins(coin);
                        childModal2.setSource(src);
                        childModalList.add(childModal2);
                        i++;
                    } else {
                        if (i < Jarray.length()) {
                            i = (k - 1);
                        }
                        break;
                    }
                }
            }
            modal.setChildModalList(childModalList);
            coinTransactionParentModalList.add(modal);
            if(i==(Jarray.length()-2))
            {
                break;
            }
        }
        } catch (IOException | JSONException e) {
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

    @Override
    public void onClick(View view) {
        if(view==back){
            getActivity().finish();
        }
    }
}