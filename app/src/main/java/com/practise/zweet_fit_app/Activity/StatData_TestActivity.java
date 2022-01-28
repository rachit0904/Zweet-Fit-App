package com.practise.zweet_fit_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Step_Item;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatData_TestActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    TextView steps,cal,dist;
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    GoogleApiClient fitApiClient;
    private String daily_steps = "0";
    private String daily_cal = "0";
    private String daily_dist = "0";
    private static List<Step_Item> stepsData = new ArrayList<>();
    private static List<Step_Item> calData = new ArrayList<>();
    private static List<Step_Item> distData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_data_test);
        steps=findViewById(R.id.steps);
        cal=findViewById(R.id.cal);
        dist=findViewById(R.id.distance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fitApiClient == null){
            check_permission();
            buildClient();
        }
        else if(fitApiClient.isConnected()) {
            request_data();
        }
    }

    private void request_data(){
        new RetriveSteps().execute();
        //new RetrieveStepsCurrentDate().execute();
    }

    //build client for the fit history access
    private void buildClient() {
        fitApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addConnectionCallbacks(this)
                .useDefaultAccount()
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        Log.i(TAG, "Connection failed. Cause: " + result.toString());
                        if (!result.hasResolution()) {
                            // Show the localized error dialog
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), StatData_TestActivity.this, 0).show();
                            return;
                        }
                        // The failure has a resolution. Resolve it.
                        if (!authInProgress) {
                            try {
                                Log.i(TAG, "Attempting to resolve failed connection");
                                authInProgress = true;
                                result.startResolutionForResult(StatData_TestActivity.this, REQUEST_OAUTH);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(TAG, "Exception while starting resolution activity", e);
                            }
                        }
                    }
                })
                .build();

        fitApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OAUTH) {
            Log.e(TAG, "Auth request processed ");
            authInProgress = false;
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "result ok" );
                Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                if (!fitApiClient.isConnecting() && !fitApiClient.isConnected()) {
                    fitApiClient.connect();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Auth failure!");
            }
        }
    }

    //check if app has the history access permissions, if not then request them
    void check_permission(){
        String [] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
        if (ContextCompat.checkSelfPermission(StatData_TestActivity.this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StatData_TestActivity.this,permissions , REQUEST_PERMISSIONS);
            Log.e(TAG, "Permission not granted");
        }
        else {
            Log.e(TAG, "Permission granted");
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
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG,"APP connected to fit");
        request_data();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG,"APP connection suspended to fit");

    }


    //create request to retrieve step history for specific weeks
    public static DataReadRequest getDataRequestForWeeks(int weeks){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1 * weeks);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.e(TAG, "Range End: " + dateFormat.format(endTime));

        final DataSource ds = new DataSource.Builder()
                .setAppPackageName("com.google.android.gms")
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ds,DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    private void update_daily_counter(){
        steps.setText(getString(R.string.daily_counter, daily_steps));
        cal.setText(getString(R.string.daily_counter, daily_cal));
        dist.setText(getString(R.string.daily_counter, daily_dist));
    }

    class RetrsieveStepsCurrentDate extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(fitApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(30, TimeUnit.SECONDS);
            if (result.getStatus().isSuccess()) {
                Log.e(TAG, "Daily success!");
                DataSet dataset = result.getTotal();
                for (DataPoint dp : dataset.getDataPoints()) {
                    for (Field field : dp.getDataType().getFields()) {
                        if (field.getName().equals("steps")) {
                            daily_steps = dp.getValue(field).toString();
                            Log.e(TAG, "Steps " + daily_steps);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            update_daily_counter();
        }
    }

    class RetriveSteps extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //clear the previous data
            stepsData = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DataReadRequest readRequest = getDataRequestForWeeks(2);
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

            //get current date steps
            DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(fitApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(30, TimeUnit.SECONDS);
            if (result.getStatus().isSuccess()) {
                Log.e(TAG, "Daily success!");
                DataSet dataset = result.getTotal();
                updateDataList(dataset);
                for (DataPoint dp : dataset.getDataPoints()) {
                    for (Field field : dp.getDataType().getFields()) {
                        if (field.getName().equals("steps")) {
                            daily_steps = dp.getValue(field).toString();
                            Log.e(TAG, "Steps " + daily_steps);
                        }
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
        }
    }


    //Update the data List object with the steps values retrieved from the Fit History API
    private static void updateDataList(DataSet dataSet) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        if(dataSet.getDataPoints().size() > 0){
            int nSteps = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            long ts =dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS);
            String date = dateFormat.format(ts);
            Step_Item new_item = new Step_Item(date,nSteps+"",ts);

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



}