package com.practise.zweet_fit_app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.Activity.MainActivity;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
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

public class MonthlyFragment extends Fragment implements View.OnClickListener {
    final String curDayColorCode="#06417C";
    final String streakAchievedCode="#8053F00F";
    final String defaultDayColorCode="#3C1C1C1C";
    Float steps=Float.valueOf(0);
    int s=0;
    TextView monthlySteps,month,cal,dist;
    ImageView prevMonth,nextMonth;
    ChipGroup days;
    GoogleApiClient fitApiClient;
    DataReadRequest readRequest;
    View view;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    RecyclerView monthlyRv;
    Chip d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15,d16,d17,d18,d19,d20,d21,d22,d23,d24,d25,d26,d27,d28,d29,d30,d31;
    String[] months ={"January","February","March","April","May","June","July","August","September","October","November","December"};
    LocalDate date=LocalDate.now();
    String mm="";
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    private String monthSteps = "0";
    private static List<Step_Item> stepsData = new ArrayList<>();
    List<StatRecordModal> statRecordModalList=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_monthly, container, false);
        initialize();
        initializeMonthChips();
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        setMonth(date);
        setDayChipColor(Integer.parseInt(LocalDate.now().toString().split("-")[2]),curDayColorCode);
        monthlyRv.setHasFixedSize(true);
        monthlyRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        StatViewCardAdapter adapter=new StatViewCardAdapter(getMonthlyStats());
        monthlyRv.setAdapter(adapter);
        d1.setOnClickListener(this);d2.setOnClickListener(this);d3.setOnClickListener(this);d4.setOnClickListener(this);d5.setOnClickListener(this);
        d6.setOnClickListener(this);d7.setOnClickListener(this);d8.setOnClickListener(this);d9.setOnClickListener(this);d10.setOnClickListener(this);
        d11.setOnClickListener(this);d12.setOnClickListener(this);d13.setOnClickListener(this);d14.setOnClickListener(this);d15.setOnClickListener(this);
        d16.setOnClickListener(this);d17.setOnClickListener(this);d18.setOnClickListener(this);d19.setOnClickListener(this);d20.setOnClickListener(this);
        d21.setOnClickListener(this);d22.setOnClickListener(this);d23.setOnClickListener(this);d24.setOnClickListener(this);d25.setOnClickListener(this);
        d26.setOnClickListener(this);d27.setOnClickListener(this);d28.setOnClickListener(this);d29.setOnClickListener(this);d30.setOnClickListener(this);
        d31.setOnClickListener(this);
        return view;
    }

    private void setDayChipColor(int day,String color){
        switch(day){
            case 1: {
                d1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 2:{
                d2.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 3:{
                d3.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 4:{
                d4.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 5: {
                d5.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 6:{
                d6.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 7:{
                d7.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 8:{
                d8.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 9: {
                d9.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 10:{
                d10.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 11: {
                d11.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 12:{
                d12.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 13:{
                d13.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 14:{
                d14.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 15:{
                d15.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 16:{
                d16.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 17: {
                d17.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 18:{
                d18.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 19:{
                d19.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 20:{
                d20.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 21:{
                d21.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 22:{
                d22.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 23: {
                d23.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 24:{
                d24.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 25:{
                d25.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 26:{
                d26.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 27: {
                d27.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 28: {
                d28.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 29:{
                d29.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 30:{
                d30.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 31:{
                d31.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }

        }
    }

    private void initializeMonthChips() {
        d1=view.findViewById(R.id.day1);d2=view.findViewById(R.id.day2);d3=view.findViewById(R.id.day3);d4=view.findViewById(R.id.day4);d5=view.findViewById(R.id.day5);
        d6=view.findViewById(R.id.day6);d7=view.findViewById(R.id.day7);d8=view.findViewById(R.id.day8);d9=view.findViewById(R.id.day9);d10=view.findViewById(R.id.day10);
        d11=view.findViewById(R.id.day11);d12=view.findViewById(R.id.day12);d13=view.findViewById(R.id.day13);d14=view.findViewById(R.id.day14);d15=view.findViewById(R.id.day15);
        d16=view.findViewById(R.id.day16);d17=view.findViewById(R.id.day17);d18=view.findViewById(R.id.day18);d19=view.findViewById(R.id.day19);d20=view.findViewById(R.id.day20);
        d21=view.findViewById(R.id.day21);d22=view.findViewById(R.id.day22);d23=view.findViewById(R.id.day23);d24=view.findViewById(R.id.day24);d25=view.findViewById(R.id.day25);
        d26=view.findViewById(R.id.day26);d27=view.findViewById(R.id.day27);d28=view.findViewById(R.id.day28);d29=view.findViewById(R.id.day29);d30=view.findViewById(R.id.day30);
        d31=view.findViewById(R.id.day31);
    }

    private void initialize() {
        days=view.findViewById(R.id.monlthDays);
        monthlySteps=view.findViewById(R.id.monthlySteps);
        cal=view.findViewById(R.id.cal);
        dist=view.findViewById(R.id.dist);
        month=view.findViewById(R.id.month);
        prevMonth=view.findViewById(R.id.prevMonth);
        nextMonth=view.findViewById(R.id.nextMonth);
        monthlyRv=view.findViewById(R.id.monthlyStatRv);
    }

    private List<StatRecordModal> getMonthlyStats() {
        int i=0,j=1;
        int s=0;
        for(Step_Item modal:stepsData) {
            s+=Integer.parseInt(modal.getData());
            if(i%7==0) {
                StatRecordModal modal1 = new StatRecordModal(
                        j+" "+modal.getDate().split("-")[1]+" - "+modal.getDate().split("-")[2]+" "+modal.getDate().split("-")[1],
                        String.valueOf(s),
                        false
                );
                statRecordModalList.add(modal1);
                j+=7;
            }
            i++;
        }
        return statRecordModalList;
    }

    void setDays(int days) {
        if(days==31){
            d29.setVisibility(View.VISIBLE);
            d30.setVisibility(View.VISIBLE);
            d31.setVisibility(View.VISIBLE);
            d31.setVisibility(View.VISIBLE);
        }
        else if(days==28){
            d29.setVisibility(View.GONE);
            d30.setVisibility(View.GONE);
            d31.setVisibility(View.GONE);
        }else if(days==29){
            d30.setVisibility(View.GONE);
            d31.setVisibility(View.GONE);
        }
        else{
            d29.setVisibility(View.VISIBLE);
            d30.setVisibility(View.VISIBLE);
            d31.setVisibility(View.GONE);
        }
    }

    void setMonth(LocalDate date){
        if(Integer.parseInt(LocalDate.now().toString().split("-")[0])==Integer.parseInt(date.toString().split("-")[0])
             && Integer.parseInt(LocalDate.now().toString().split("-")[1])==Integer.parseInt(date.toString().split("-")[1])
            && Integer.parseInt(LocalDate.now().toString().split("-")[2])==Integer.parseInt(date.toString().split("-")[2])){
            setDayChipColor(Integer.parseInt(this.date.toString().split("-")[2]),curDayColorCode);
        }else{
            setDayChipColor(Integer.parseInt(this.date.toString().split("-")[2]),defaultDayColorCode);
        }
        this.date=date;
        setDays(date.lengthOfMonth());
        mm=months[Integer.parseInt(date.toString().split("-")[1])-1];
        for( int i=1;i<=date.lengthOfMonth();i++){
            setDayChipColor(i,defaultDayColorCode);
        }
        month.setText(mm);
        buildClient();
        String startDate=LocalDate.of(date.getYear(),date.getMonth(),1).toString();
        String endDate = LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth()).plusDays(1).toString();
        readRequest=getDataRequestForWeeks(startDate,endDate);
        request_data();
    }

    @Override
    public void onClick(View view) {
        if(view==prevMonth){
            setMonth(date.minusMonths(1));
        }
        if(view==nextMonth){
            setMonth(date.plusMonths(1));
        }
        if(view==d1){
            setFragment(1);
        }if(view==d2){
            setFragment(2);
        }if(view==d3){
            setFragment(3);
        }if(view==d4){
            setFragment(4); }if(view==d5){
            setFragment(5);
        }
        if(view==d6){ setFragment(6); }if(view==d7){
            setFragment(7);
        }if(view==d8){ setFragment(8); }if(view==d9){
            setFragment(9); }if(view==d10){
            setFragment(10);
        }
        if(view==d11){
            setFragment(11);
        }if(view==d12){
            setFragment(12);
        }if(view==d13){ setFragment(13);
        }if(view==d14){
            setFragment(14); }if(view==d15){
            setFragment(15);
        }if(view==d16){
            setFragment(16);
        }if(view==d17){
            setFragment(17);
        }if(view==d18){ setFragment(18);
        }if(view==d19){
            setFragment(19); }if(view==d20){
            setFragment(20);
        }
        if(view==d21){
            setFragment(21);
        }if(view==d22){
            setFragment(22);
        }if(view==d23){
            setFragment(23);
        }if(view==d24){
            setFragment(24); }if(view==d25){
            setFragment(25);
        }if(view==d26){
            setFragment(26);
        }if(view==d27){
            setFragment(27);
        }if(view==d28){
            setFragment(28);
        }if(view==d29){
            setFragment(29); }if(view==d30){
            setFragment(30);
        }
        if(view==d31){
            setFragment(31);
        }

    }

    private void setFragment(int i) {
        Intent intent=new Intent(getActivity(), MainActivity.class);
        intent.putExtra("frag","stat");
        preferences=pref.edit();
        preferences.putString("date", String.valueOf(LocalDate.of(date.getYear(),date.getMonth(),i)));
        preferences.apply();
        startActivity(intent);
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
            s=0;
            stepsData = new ArrayList<>();
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
        Float steps=(float)s/1000;
        monthlySteps.setText(String.format("%.02f",steps) + "K Steps");
        steps= (float) s;
        int calories=0;
        int distance=0;
        if(steps>0) {
            calories = (int) Math.ceil((steps * 0.04258));
            distance = (int) Math.ceil(steps / 1312.33595801);
        }
        cal.setText(String.valueOf(calories).length()>3 ? String.format("%.02f Kcal",(float)calories/1000) : String.format("%d Kcal",calories));
        dist.setText(String.valueOf(distance).concat(" Kms"));
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
            s+=nSteps;
            Step_Item new_item = new Step_Item(date,nSteps+"",ts);
            Log.i("date:",date);
            Log.i("steps:", String.valueOf(nSteps));
            Log.i("monthly steps", String.valueOf(s));
            setDayChipColor(Integer.parseInt(
                    date.split("-")[0]),
                    (nSteps> Integer.parseInt(pref.getString("target",""))) ? streakAchievedCode : defaultDayColorCode
            );
            if(date.split("-")[0] == LocalDate.now().toString().split("-")[2]){
                setDayChipColor(Integer.parseInt(
                        date.split("-")[0]),
                        curDayColorCode
                );
            }
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