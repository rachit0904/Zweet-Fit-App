package com.practise.zweet_fit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.practise.zweet_fit_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestoreAccPage extends AppCompatActivity implements View.OnClickListener {
    LottieAnimationView restoreAnim;
    TextView bckupStatus,bckupDate;
    Button skip,restore,next;
    ProgressBar progressBar;
    SharedPreferences pref=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_acc_page);
        restoreAnim=findViewById(R.id.restoreStatusAnim);
        bckupStatus=findViewById(R.id.textView3);
        bckupDate=findViewById(R.id.backupDateTime);
        pref=getSharedPreferences("user data",MODE_PRIVATE);
        skip=findViewById(R.id.skip);
        restore=findViewById(R.id.restore);
        next=findViewById(R.id.next);
        progressBar=findViewById(R.id.progressBar4);
        skip.setOnClickListener(this);
        restore.setOnClickListener(this);
        next.setOnClickListener(this);
        bckupDate.setText("Last backup date "+pref.getString("creation_date",""));
    }

    @Override
    public void onClick(View view) {
        if(view==skip){
            Intent intent=new Intent(this,SignUp.class);
            intent.putExtra("fragment","personal details");
            startActivity(intent);
            finish();
        }
        if(view==restore){
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    bckupStatus.setText("Account Restored Successfully!");
                    restore.setVisibility(View.INVISIBLE);
                    skip.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            },1200);
        }
        if(view==next){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}