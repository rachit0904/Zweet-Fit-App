package com.practise.zweet_fit_app.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Step_Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyStatView extends Fragment implements View.OnClickListener {
    TextView statDate,streakPrcnt,target,step,cal,dist,stepUpdate;
    ProgressBar streakProgress;
    LinearProgressIndicator stepProgress;
    ImageView prevDate,nextDate;
    DataReadRequest readRequest;
    Float steps=Float.valueOf(0);
    int streak=0;
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    SimpleDateFormat dateFormat=new SimpleDateFormat("DD-MM-YYYY");
    LocalDate date=LocalDate.now();
    String dd,mm,yy;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    long numSteps = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentuser;
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    GoogleApiClient fitApiClient;
    private String daily_steps = "0";
    private static List<Step_Item> stepsData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_daily_stat_view, container, false);
        step=view.findViewById(R.id.steps);
        cal=view.findViewById(R.id.calorie);
        dist=view.findViewById(R.id.dist);
        target=view.findViewById(R.id.stepsGoal);
        streakProgress=view.findViewById(R.id.progressBar2);
        streakPrcnt=view.findViewById(R.id.streakProgress);
        stepProgress=view.findViewById(R.id.stepProgress);
        stepUpdate=view.findViewById(R.id.stepUpdate);
        prevDate=view.findViewById(R.id.prevDate);
        statDate=view.findViewById(R.id.date);
        nextDate=view.findViewById(R.id.nextDate);
        prevDate.setOnClickListener(this);
        nextDate.setOnClickListener(this);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        if(pref.getString("date","").isEmpty()) {
            setDateFormat(date);
        }else {
            date=LocalDate.parse(pref.getString("date",""));
            preferences=pref.edit();
            preferences.putString("date",String.valueOf(LocalDate.now()));
            preferences.apply();
            setDateFormat(date);
        }
        setData();
        return view;
    }

    void check_permission(){
        String [] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),permissions , REQUEST_PERMISSIONS);
            Log.e(TAG, "Permission not granted");
        }
        else {
            Log.e(TAG, "Permission granted");
        }
    }

    private void buildClient()  {
        fitApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.e(TAG,"APP connected to fit");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.e(TAG,"APP suspended from fit");
                    }
                })
                .useDefaultAccount()
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        Log.i(TAG, "Connection failed. Cause: " + result.toString());
                        if (!result.hasResolution()) {
                            // Show the localized error dialog
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0).show();
                            return;
                        }
                        // The failure has a resolution. Resolve it.
                        if (!authInProgress) {
                            try {
                                Log.i(TAG, "Attempting to resolve failed connection");
                                authInProgress = true;
                                result.startResolutionForResult(getActivity(), REQUEST_OAUTH);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(TAG, "Exception while starting resolution activity", e);
                            }
                        }
                    }
                })
                .build();

        fitApiClient.connect();
    }

    private void setDateFormat(LocalDate date) {
        this.date=date;
        dd=date.toString().split("-")[2];
        mm=date.toString().split("-")[1];
        yy=date.toString().split("-")[0];
        statDate.setText(dd+" "+months[Integer.parseInt(mm)-1]+" "+yy);
        buildClient();
        readRequest = getDataRequestForWeeks(date.toString(),date.plusDays(1).toString());
        request_data();
    }

    @Override
    public void onClick(View view) {
        if(view==prevDate){
            setDateFormat(date.minusDays(1));
        }
        if(view==nextDate){
            setDateFormat(date.plusDays(1));
        }
    }

    private void request_data(){
        new RetriveSteps().execute();
        //new RetrieveStepsCurrentDate().execute();
    }

    //create request to retrieve step history for specific weeks
    public static DataReadRequest getDataRequestForWeeks(String startDate,String endDate){
        long start= LocalDateTime.of(Integer.parseInt(startDate.split("-")[0]),Integer.parseInt(startDate.split("-")[1]),Integer.parseInt(startDate.split("-")[2]), 0,0).toEpochSecond(ZoneOffset.UTC);
        long end=LocalDateTime.of(Integer.parseInt(endDate.split("-")[0]),Integer.parseInt(endDate.split("-")[1]),Integer.parseInt(endDate.split("-")[2]),0,0).toEpochSecond(ZoneOffset.UTC);

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e(TAG, "Range Start: " +(startDate));
        Log.e(TAG, "Range End: " + (endDate));

        final DataSource ds = new DataSource.Builder()
                .setAppPackageName("com.google.android.gms")
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ds,DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(start, end, TimeUnit.SECONDS)
                .build();

        return readRequest;
    }

    class RetriveSteps extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //clear the previous data
            stepsData = new ArrayList<>();
            steps=Float.valueOf(0);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(fitApiClient, readRequest).await(30, TimeUnit.SECONDS);
            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        updateDataList(dataSet);
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //filter and update the recyclerview list and render the view
            update_daily_counter();
            setData();
        }
    }

    private void setData() {
        step.setText(String.valueOf(steps));
        int calories=0;
        int distance=0;
        if(steps>0) {
            calories = (int) Math.ceil((steps * 0.04258));
            distance = (int) Math.ceil(steps / 1312.33595801);
        }
        cal.setText(String.valueOf(calories));
        dist.setText(String.valueOf(distance));
        int prg=(int)Math.ceil((steps / Float.valueOf(pref.getString("target","")) * 100));
        if(prg>=100){
            prg=100;
        }
        stepProgress.setProgress(prg);
        streakProgress.setProgress(prg);
        streakPrcnt.setText(String.valueOf(prg) + "%");
        stepUpdate.setText(String.valueOf(steps)+"/"+pref.getString("target",""));
        target.setText("Goal: "+pref.getString("target",""));
    }


    private void update_daily_counter(){

    }

    //Update the data List object with the steps values retrieved from the Fit History API
    private void updateDataList(DataSet dataSet) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        if(dataSet.getDataPoints().size() > 0){
            int nSteps = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            long ts =dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS);
            this.steps= Float.valueOf(nSteps);
            String date = dateFormat.format(ts);
            Step_Item new_item = new Step_Item(date,nSteps+"",ts);
            Log.i("date:",date);
            Log.i("steps:", String.valueOf(nSteps));
            if(stepsData.contains(new_item)){
                int item_index = stepsData.indexOf(new_item);
                Step_Item temp_item = stepsData.get(item_index);
                int steps = nSteps + Integer.parseInt(temp_item.getData());
                new_item = new Step_Item(date,steps+"",ts);
                stepsData.remove(item_index);
            }
            stepsData.add(new_item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "r code "+requestCode);
        if(requestCode == REQUEST_PERMISSIONS){
            if(grantResults.length > 0){
                Log.e(TAG, "Permission is granted");
            }
            else {
                Log.e(TAG, "Permission is denied");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OAUTH) {
            Log.e(TAG, "Auth request processed ");
            authInProgress = false;
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "result ok" );
                Toast.makeText(getContext(),"success", Toast.LENGTH_SHORT).show();
                if (!fitApiClient.isConnecting() && !fitApiClient.isConnected()) {
                    fitApiClient.connect();
                }
            }
            else {
                Toast.makeText(getContext(),"failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Auth failure!");
            }
        }
    }


}