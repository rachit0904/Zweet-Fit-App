package com.practise.zweet_fit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AuthActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button signWithGoogle,installFit,allowPermissions;
    Button nextPage;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FitnessOptions fitnessOptions;
    GoogleSignInAccount account;
    private GoogleApiClient googleApiClient;
    Boolean flag1=false,flag2=false,flag3=false;
    private static final int RC_SIGN_IN = 1;
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE=2;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        signWithGoogle=findViewById(R.id.signWithGoogle);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        fitnessOptions=FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .build();
        account =GoogleSignIn.getAccountForExtension(this,fitnessOptions);
        installFit=findViewById(R.id.googleFit);
        allowPermissions=findViewById(R.id.allowPermissions);
        nextPage=findViewById(R.id.next);
        signWithGoogle.setOnClickListener(this);
        installFit.setOnClickListener(this);
        allowPermissions.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        pref=getSharedPreferences("user data", Context.MODE_PRIVATE);
        check();
    }

    private void check() {
        if(!pref.getString("id","").isEmpty()){
            signWithGoogle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_logo, 0, R.drawable.verified, 0);
            flag1=true;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED) {
            allowPermissions.setCompoundDrawablesWithIntrinsicBounds(R.drawable.permission, 0, R.drawable.verified, 0);
            flag3=true;
        }
        if(GoogleSignIn.hasPermissions(account,fitnessOptions)){
            installFit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_fit_logo, 0, R.drawable.verified, 0);
            flag2=true;
        }
        if(flag1 && flag2 && flag3){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==signWithGoogle){
            googleSignIn();
        }
        if(view==installFit){
            checkGoogleFitApp();
        }
        if(view==allowPermissions){
            seekPermissions();
        }
        if(view==nextPage){
            if(flag1&&flag2&&flag2) {
                UserData d=new UserData();
                d.id=pref.getString("id","");
                d.context=AuthActivity.this;
                d.pref=pref;
                d.preferences=preferences;
                d.execute();
            }else{
                if(!flag1){
                    Snackbar.make(view,"Complete Step 1!",Snackbar.LENGTH_SHORT).show();
                }else if(!flag2){
                    Snackbar.make(view,"Complete Step 2!",Snackbar.LENGTH_SHORT).show();
                }else if(!flag3){
                    Snackbar.make(view,"Complete Step 3!",Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void googleSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    private void checkGoogleFitApp() {
        if(!GoogleSignIn.hasPermissions(account,fitnessOptions)){
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions
            );
        }else{
            accessGoogleFit();
        }
    }

    private void accessGoogleFit() {
        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

        GoogleSignInAccount googleSignInAccount =
                GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        // Samples the user's activity once per minute.
        Task<Void> stepSubs = Fitness.getRecordingClient(this, googleSignInAccount)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA);
        Task<Void> calSubs = Fitness.getRecordingClient(this, googleSignInAccount)
                .subscribe(DataType.AGGREGATE_CALORIES_EXPENDED);
        Task<Void> distSubs = Fitness.getRecordingClient(this, googleSignInAccount)
                .subscribe(DataType.AGGREGATE_DISTANCE_DELTA);
    }

    private void seekPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    AuthActivity.this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.BODY_SENSORS},
                    100);
            allowPermissions.setCompoundDrawablesWithIntrinsicBounds(R.drawable.permission, 0, R.drawable.verified, 0);
            flag3=true;
        }else{
            flag3=false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if(requestCode==GOOGLE_FIT_PERMISSIONS_REQUEST_CODE){
            installFit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_fit_logo, 0, R.drawable.verified, 0);
            flag2=true;
            accessGoogleFit();
        }else{
            flag2=false;
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            preferences=pref.edit();
            preferences.putString("id",result.getSignInAccount().getId());
            preferences.apply();
            Log.d("result","id saved");
            signWithGoogle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_logo, 0, R.drawable.verified, 0);
            flag1=true;
        }else{
            Snackbar.make(signWithGoogle,"Sign in attempt failed!",Snackbar.LENGTH_SHORT).show();
            flag1=false;
            Log.d("result",result.getStatus().toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class UserData extends AsyncTask<String,Void,Boolean> {

        private String resp;
        String id;
        SharedPreferences pref;
        SharedPreferences.Editor preferences;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(Constant.ServerUrl+"/selectwQuery?table=users&query=uid&value="+id)
                        .method("GET", null)
                        .addHeader("key", "MyApiKEy")
                        .build();
                Response response = client.newCall(request).execute();
                String data=response.body().string();
                JSONObject obj=new JSONObject(data);
                JSONArray arr=obj.getJSONArray("data");
                if(arr.length()>0){
                    for(int i=0;i<arr.length();i++){
                        JSONObject d=arr.getJSONObject(i);
                        preferences = pref.edit();
                        preferences.putString("name", d.getString("name"));
                        preferences.putString("subs", d.getString("subscription"));
                        preferences.putString("usname", d.getString("username"));
                        preferences.putString("coins", d.getString("coins"));
                        preferences.putString("dob", d.getString("dob"));
                        preferences.putString("pts", d.getString("points"));
                        preferences.putString("wt", d.getString("weight"));
                        preferences.putString("level", d.getString("level"));
                        preferences.putString("ht", d.getString("height"));
                        preferences.putString("wr", d.getString("win_rate"));
                        preferences.putString("target", d.getString("target"));
                        preferences.putString("no", d.getString("mobile"));
                        preferences.putString("steps", d.getString("steps"));
                        preferences.putString("dp", d.getString("dp_url"));
                        preferences.putString("streak", d.getString("streak"));
                        preferences.putString("creation_date", d.getString("creation_date"));
                        preferences.apply();
                    }
                    Intent intent=new Intent(context,RestoreAccPage.class);
                    context.startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(context,SignUp.class);
                    intent.putExtra("fragment","personal details");
                    context.startActivity(intent);
                    finish();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            Log.i("result",result.toString());
        }

    }

}
