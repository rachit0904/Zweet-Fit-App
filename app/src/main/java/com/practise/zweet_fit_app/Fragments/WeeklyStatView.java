package com.practise.zweet_fit_app.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Step_Item;


import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class  WeeklyStatView extends Fragment implements View.OnClickListener {
    List<StatRecordModal> statRecordModalList=new ArrayList<>();
    List<StatRecordModal> graphDataList=new ArrayList<>();
    BarGraphSeries<DataPoint> barGraphSeries;
    TextView steps,week,dist,cal;
    ImageView prevWeek,nextWeek;
    int netSteps=0;
    LocalDate date=LocalDate.now();
    ArrayList<String> datesOfWeek=new ArrayList<>();
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    GraphView graph;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    StaticLabelsFormatter staticLabelsFormatter;
    RecyclerView recyclerView;
    StatViewCardAdapter statViewCardAdapter;
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    GoogleApiClient fitApiClient;
    int weeklySteps=0;
    private String daily_steps = "0";
    String dates[]=new String[7];
    DataReadRequest readRequest;
    private static List<Step_Item> stepsData = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        if(fitApiClient == null){
            buildClient();
        }
    }

    private void request_data(){
        new RetriveSteps().execute();
        //new RetrieveStepsCurrentDate().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weekly_stat_view, container, false);
        graph = view.findViewById(R.id.weeklyGraphView);
        staticLabelsFormatter = new StaticLabelsFormatter(graph);
        recyclerView=view.findViewById(R.id.weekyStatRv);
        week=view.findViewById(R.id.week);
        dist=view.findViewById(R.id.distance);
        cal=view.findViewById(R.id.calories);
        prevWeek=view.findViewById(R.id.prevWeek);
        nextWeek=view.findViewById(R.id.nextWeek);
        steps=view.findViewById(R.id.weeklySteps);
        prevWeek.setOnClickListener(this);
        nextWeek.setOnClickListener(this);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        setWeek(date);
        statViewCardAdapter=new StatViewCardAdapter(statRecordModalList);
        recyclerView.setAdapter(statViewCardAdapter);
        recyclerView.setHasFixedSize(true);
        steps.setText(String.valueOf(netSteps));
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    void setWeek(LocalDate date){
        weeklySteps=0;
        this.date=date;
        String dates[]=new String[7];
        datesOfWeek.clear();
        statRecordModalList.clear();
        try {
            LocalDate start = date;
            while (start.getDayOfWeek() != DayOfWeek.MONDAY) {
                start = start.minusDays(1);
            }
            LocalDate end = date;
            while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
                end = end.plusDays(1);
            }
            //set week start and end date title
            week.setText(
                    new StringBuilder()
                            .append(start.toString().split("-")[2]).append(" ")
                            .append(months[Integer.parseInt(start.toString().split("-")[1])-1])
                            .append(" - ").append(end.toString().split("-")[2])
                            .append(" ")
                            .append(months[Integer.parseInt(end.toString().split("-")[1])-1]).toString()
            );
            //get all dates of the week
            LocalDate s=start;
            int j=0;
            while(s.getDayOfWeek()!=DayOfWeek.SUNDAY){
                datesOfWeek.add(s.toString());
                dates[j]=s.toString().split("-")[2];
                Log.i("dates:",s.toString().split("-")[2]);
                s=s.plusDays(1);
                j++;
            }
            dates[j]=s.toString().split("-")[2];
            Log.i("dates:",s.toString().split("-")[2]);
            s=s.plusDays(1);
            datesOfWeek.add(s.toString());
            this.dates=dates;
            //set data for graph
            setStats(datesOfWeek);
            //feed graph with data
            //set date and range vertical labels
            //reset list data
        }catch(Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setStats(ArrayList<String> dates) {
        {
            Log.i("date:", String.valueOf(LocalDate.now()));
            buildClient();
            readRequest = getDataRequestForDuration(dates.get(0),dates.get(dates.size()-1));
            request_data();
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

    //create request to retrieve step history for specific weeks
    public static DataReadRequest getDataRequestForDuration(String startDate,String endDate){
        long start=LocalDateTime.of(Integer.parseInt(startDate.split("-")[0]),Integer.parseInt(startDate.split("-")[1]),Integer.parseInt(startDate.split("-")[2]), 0,0).toEpochSecond(ZoneOffset.UTC);
        long end=LocalDateTime.of(Integer.parseInt(endDate.split("-")[0]),Integer.parseInt(endDate.split("-")[1]),Integer.parseInt(endDate.split("-")[2]), 0,0).toEpochSecond(ZoneOffset.UTC);

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e(TAG, "Range Start: " + dateFormat.format(start));
        Log.e(TAG, "Range End: " + dateFormat.format(end));

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

    //Update the data List object with the steps values retrieved from the Fit History API
    private void updateDataList(DataSet dataSet) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        if(dataSet.getDataPoints().size() > 0){
            int nSteps = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            long ts =dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS);
            String date = dateFormat.format(ts);
            Step_Item new_item = new Step_Item(date,nSteps+"",ts);
            Log.i("date:",date);
            Log.i("steps:", String.valueOf(nSteps));
            weeklySteps+=nSteps;
            if(nSteps!=0) {
                StatRecordModal statRecordModal = new StatRecordModal(
                        date,
                        String.valueOf(nSteps),
                        (nSteps >= Integer.parseInt(pref.getString("target", "")))
                );
                statRecordModalList.add(statRecordModal);
            }
            if(stepsData.contains(new_item)){
                int item_index = stepsData.indexOf(new_item);
                Step_Item temp_item = stepsData.get(item_index);
                int steps = nSteps + Integer.parseInt(temp_item.getData());
                new_item = new Step_Item(date,steps+"",ts);
                stepsData.remove(item_index);
                statRecordModalList.remove(item_index);
            }
            stepsData.add(new_item);
        }
    }

    private void update_daily_counter(){
        steps.setText(String.valueOf(weeklySteps));
        int calories= (int) Math.ceil((weeklySteps*0.04258));
        cal.setText(getString(R.string.daily_counter, String.valueOf(calories).concat(" cal")));
        int distance=  (int) Math.ceil(weeklySteps/1312.33595801);
        dist.setText(getString(R.string.daily_counter, String.valueOf(distance).concat(" Kms")));
        DataPoint[] data=new DataPoint[statRecordModalList.size()];
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dates);
        staticLabelsFormatter.setVerticalLabels(new String[] {"2000","8500","15000","25000","30,000"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        graph.removeAllSeries();
        statViewCardAdapter.notifyDataSetChanged();
        graphDataList=statRecordModalList;
        if(graphDataList.size()<7){
            for(int i=graphDataList.size();i<7;i++){
                StatRecordModal modal=new StatRecordModal();
                {
                    if (!graphDataList.contains(datesOfWeek.get(i))) {
                        modal.setDate(dates[i]);
                        modal.setSteps("0");
                        modal.setStreakAchieved(false);
                        graphDataList.add(modal);
                    }
                }
            }
        }
        barGraphSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, Integer.parseInt((graphDataList.get(0).getSteps()))),
                new DataPoint(1, Integer.parseInt((graphDataList.get(1).getSteps()))),
                new DataPoint(2, Integer.parseInt((graphDataList.get(1).getSteps()))),
                new DataPoint(3, Integer.parseInt((graphDataList.get(3).getSteps()))),
                new DataPoint(4, Integer.parseInt((graphDataList.get(4).getSteps()))),
                new DataPoint(5, Integer.parseInt((graphDataList.get(5).getSteps()))),
                new DataPoint(6, Integer.parseInt((graphDataList.get(6).getSteps())))
        });
        graph.addSeries(barGraphSeries);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
        barGraphSeries.setSpacing(30);
        barGraphSeries.setColor(Color.parseColor("#1976D2"));
        barGraphSeries.setDataWidth(0.3);
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setAnimated(true);
    }

    @Override
    public void onClick(View view) {
        if(view==prevWeek){
            setWeek(date.minusWeeks(1));
        }
        if(view==nextWeek){
            setWeek(date.plusWeeks(1));
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
