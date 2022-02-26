package com.practise.zweet_fit_app.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Adapters.YearlyStatAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.Modals.YearlyRecordModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Step_Item;


import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.chrono.Chronology;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class YearlyFragment extends Fragment implements View.OnClickListener {
    GoogleApiClient fitApiClient;
    DataReadRequest readRequest;
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    Float steps=Float.valueOf(0);
    private static List<Step_Item> stepsData = new ArrayList<>();
    HashMap<String,Integer> monthlySteps=new LinkedHashMap<>();
    HashMap<String,String> mSteps=new LinkedHashMap<>();
    TextView yearlySteps,year,noData;
    ImageView prevYear,nextYear;
    LocalDate date=LocalDate.now();
    BarGraphSeries<DataPoint> barGraphSeries;
    int netSteps=0;
    RecyclerView recyclerView;
    List<String> mths=new ArrayList<>();
//    StatViewCardAdapter statViewCardAdapter;
//    List<StatRecordModal> statRecordModalList=new ArrayList<>();
    YearlyStatAdapter yearlyStatAdapter;
    ArrayList<YearlyRecordModal> yearlyRecordModalList=new ArrayList<>();
    String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yearly,container,false);
        year=view.findViewById(R.id.year);
        prevYear=view.findViewById(R.id.prevYear);
        nextYear=view.findViewById(R.id.nextYear);
        noData=view.findViewById(R.id.noData);
        prevYear.setOnClickListener(this);
        nextYear.setOnClickListener(this);
        recyclerView=view.findViewById(R.id.yearlyStatRv);
        GraphView graph = view.findViewById(R.id.yearlyGraphView);
        yearlySteps=view.findViewById(R.id.yearlySteps);
        mths.clear();
        mths.add("Jan");mths.add("Feb");mths.add("Mar");mths.add("Apr");mths.add("May");mths.add("Jun");mths.add("Jul");
        mths.add("Aug");mths.add("Sep");mths.add("Oct");mths.add("Nov");mths.add("Dec");
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), GridLayoutManager.VERTICAL,false));
//        statViewCardAdapter=new StatViewCardAdapter(statRecordModalList);
        yearlyStatAdapter=new YearlyStatAdapter(yearlyRecordModalList);
        recyclerView.setAdapter(yearlyStatAdapter);
        recyclerView.setHasFixedSize(true);
        setDate(date);
        return view;
    }

    public void setDate(LocalDate date) {
        this.date=date;
//        statRecordModalList.clear();
//        statViewCardAdapter.notifyDataSetChanged();
        yearlyRecordModalList.clear();
        yearlyStatAdapter.notifyDataSetChanged();
        year.setText(date.toString().split("-")[0]);
        buildClient();
        String startDate=LocalDate.of(date.getYear(), Month.JANUARY,1).toString();
        if(date.toString().split("-")[0].equals(LocalDate.now().toString().split("-")[0])){
            String endDate=LocalDate.of(date.getYear(), date.getMonth(), Integer.parseInt(date.toString().split("-")[2])).toString();
            readRequest=getDataRequestForWeeks(startDate,endDate);
            request_data();
        }else{
            String endDate=LocalDate.of(date.getYear(), Month.DECEMBER, Integer.parseInt("31")).toString();
            readRequest=getDataRequestForWeeks(startDate,endDate);
            request_data();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==prevYear){
            setDate(date.minusYears(1));
        }
        if(view==nextYear){
            setDate(date.plusYears(1));
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

    private void request_data(){
        new RetriveSteps().execute();
        //new RetrieveStepsCurrentDate().execute();
    }

    //create request to retrieve step history for specific weeks
    public static DataReadRequest getDataRequestForWeeks(String startDate, String endDate){
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

    class RetriveSteps extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //clear the previous data
            steps=Float.valueOf(0);
            stepsData = new ArrayList<>();
            monthlySteps.clear();
            mSteps.clear();
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
        }
    }

    private void update_daily_counter(){
        for(String month:mths) {
            mSteps.put(month,"0");
        }
        if(!monthlySteps.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.INVISIBLE);
            for (Map.Entry mapElement : monthlySteps.entrySet()) {
                String month = (String) mapElement.getKey();
                String steps = String.valueOf(mapElement.getValue());
                if (mSteps.containsKey(month)) {
                    mSteps.replace(month, steps);
                }
            }
            setYearlyStats();
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setYearlyStats() {
//        statRecordModalList.clear();
        yearlyRecordModalList.clear();
        for (Map.Entry mapElement : mSteps.entrySet()) {
            String month = (String)mapElement.getKey();
            String steps = String.valueOf(mapElement.getValue());
            float step= (float)Integer.valueOf(steps);
            int calories=0;
            int distance=0;
            if(step>0) {
                calories = (int) Math.ceil((step * 0.04258));
                distance = (int) Math.ceil(step / 1312.33595801);
            }
            if(Integer.parseInt(steps)!=0) {
                YearlyRecordModal recordModal=new YearlyRecordModal();
                recordModal.setMonthName(month);
                recordModal.setSteps(String.format("%.02f K",(float)Integer.valueOf(steps)/(1000)));
                recordModal.setCal(String.format("%.02f",(float)calories));
                recordModal.setDist(String.format("%.02f",(float)distance));
                yearlyRecordModalList.add(recordModal);
//                StatRecordModal modal = new StatRecordModal(
//                        month,
//                        steps,
//                        false);
//                statRecordModalList.add(modal);
            }
        }
        yearlyStatAdapter.notifyDataSetChanged();
//        statViewCardAdapter.notifyDataSetChanged();
    }

    //Update the data List object with the steps values retrieved from the Fit History API
    private void updateDataList(DataSet dataSet) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        if(dataSet.getDataPoints().size() > 0){
            int nSteps = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            long ts =dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS);
            this.steps= Float.valueOf(nSteps);
            String date = dateFormat.format(ts);
            String month=date.split("-")[1];
            if(monthlySteps.containsKey(month)) {
                int s=monthlySteps.get(month);
                monthlySteps.replace(month,s,s+=nSteps);
            }else{
                monthlySteps.put(date.toString().split("-")[1],nSteps);
            }
            Step_Item new_item = new Step_Item(date,nSteps+"",ts);
//            Log.i("date:",date);
//            Log.i("steps:", String.valueOf(nSteps));

            if(stepsData.contains(new_item)){
                steps-=nSteps;
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
