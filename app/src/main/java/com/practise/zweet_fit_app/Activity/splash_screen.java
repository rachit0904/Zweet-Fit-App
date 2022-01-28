package com.practise.zweet_fit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practise.zweet_fit_app.R;

public class splash_screen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE=2;
    GoogleSignInClient mGoogleSignInClient;
    FitnessOptions fitnessOptions;
    GoogleSignInAccount account;
    private GoogleApiClient googleApiClient;
    Boolean flag1=false,flag2=false,flag3=false;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(splash_screen.this)
                .enableAutoManage(splash_screen.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        fitnessOptions=FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    flag1=true;
                }
                if (ContextCompat.checkSelfPermission(splash_screen.this, Manifest.permission.ACTIVITY_RECOGNITION)
                        == PackageManager.PERMISSION_GRANTED) {
                    flag3=true;
                }
                if(GoogleSignIn.hasPermissions(account,fitnessOptions)){
                    flag2=true;
                }
                if(flag1 && flag2 && flag3){
                    intent = new Intent(splash_screen.this, MainActivity.class);
                }else {
                    intent = new Intent(splash_screen.this, AuthActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2800);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if(requestCode==GOOGLE_FIT_PERMISSIONS_REQUEST_CODE){
            flag2=true;
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            flag1=true;
        }else{
        }
    }

}