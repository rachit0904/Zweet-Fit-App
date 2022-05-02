package com.practise.zweet_fit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class RestoreAccPage extends AppCompatActivity implements View.OnClickListener {
    LottieAnimationView restoreAnim;
    TextView bckupStatus,bckupDate;
    Button skip,restore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_acc_page);
        restoreAnim=findViewById(R.id.restoreStatusAnim);
        bckupStatus=findViewById(R.id.textView3);
        bckupDate=findViewById(R.id.backupDateTime);
        skip=findViewById(R.id.skip);
        restore=findViewById(R.id.restore);
        skip.setOnClickListener(this);
        restore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==skip){

        }
        if(view==restore){

        }
    }
}