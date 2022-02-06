package com.practise.zweet_fit_app.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.Activity.StatData_TestActivity;
import com.practise.zweet_fit_app.Adapters.GrpEventsHomepageAdapter;
import com.practise.zweet_fit_app.Modals.GrpEventsModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Step_Item;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_fragment extends Fragment implements View.OnClickListener {
    List<GrpEventsModal> grpEventsModalList = new ArrayList<>();
    ProgressBar streakProgressBar;
    TextView streakPercent;
    CardView streakCard, premiumCard;
    CircleImageView userImg;
    ImageView searchUsers;
    TextView steps;
    ProgressBar progressBar;
    TextView distance, calories, streakProgressPcnt;
    TextView userName;
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
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.grpEventsHorizontalRV);
        streakProgressBar = view.findViewById(R.id.progressBar);
        streakProgressPcnt = view.findViewById(R.id.streakProgressPcnt);
        streakPercent = view.findViewById(R.id.streakProgressPcnt);
        userName = view.findViewById(R.id.userName);
        streakCard = view.findViewById(R.id.StreakCard);
        userImg = view.findViewById(R.id.circleImageView);
        searchUsers = view.findViewById(R.id.searchUsers);
        premiumCard = view.findViewById(R.id.PremiumAdCard);
        distance = view.findViewById(R.id.distance);
        calories = view.findViewById(R.id.calories);
        progressBar = view.findViewById(R.id.progressBar);
        searchUsers.setOnClickListener(this);
        userImg.setOnClickListener(this);
        premiumCard.setOnClickListener(this);
        streakPercent.setOnClickListener(this);
        streakProgressBar.setOnClickListener(this);
        streakCard.setOnClickListener(this);
        steps = view.findViewById(R.id.steps);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        GrpEventsHomepageAdapter adapter = new GrpEventsHomepageAdapter(getContext(), getGrpEvents());
        recyclerView.setAdapter(adapter);
        setData();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        return view;
    }

    private void setData() {
        userName.setText(pref.getString("name",""));
        if(!pref.getString("dp","").isEmpty()){
            Picasso.get().load(pref.getString("dp","")).into(userImg);
        }
//        Picasso.get().load().centerInside().into(userImg);
//        Toast.makeText(getActivity(), pref.getString("name",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("dob",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("gender",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("wt",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("ht",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("target",""), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fitApiClient == null){
            check_permission();
            buildClient();
        }
        else if(fitApiClient.isConnected()) {
            request_data();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser();
    }

    @SuppressLint("DefaultLocale")
    private void updateCalories() {

    }


    float streak = 0;

    private void UpdateStreak() {
        if (streak > 100) {
            streakProgressPcnt.setText("100%");
        } else {
            streakProgressPcnt.setText(Math.round(streak) + "%");
        }

        progressBar.setProgress(Math.round(streak));
    }

    private void UpdateDistance() {

    }

    private void UpdateSteps() {
        steps.setText(String.valueOf(numSteps));
    }


    private void update() {
        UpdateDistance();
        UpdateStreak();
        updateCalories();
        UpdateSteps();
    }

    private List<GrpEventsModal> getGrpEvents() {
        for (int i = 0; i < 3; i++) {
            GrpEventsModal modal = new GrpEventsModal(
                    "1", "Daily Group Event- " + (i + 1), "2",
                    "5", "3", "30",
                    "1 Day", "8000", "", "ongoing");
            grpEventsModalList.add(modal);
        }
        return grpEventsModalList;
    }


    @Override
    public void onClick(View v) {
        if (v == userImg) {
            Intent intent = new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity", "profile");
            startActivity(intent);
        }
        if (v == streakPercent || v == streakProgressBar || v == streakCard) {
            setFragment(new StatsFragment());
            TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
        if (v == premiumCard) {
            Intent intent = new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity", "premium");
            startActivity(intent);
        }
        if (v == searchUsers) {
            Intent intent = new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity", "search user");
            startActivity(intent);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private void request_data(){
        new RetriveSteps().execute();
        //new RetrieveStepsCurrentDate().execute();
    }

    //build client for the fit history access
    private void buildClient()  {
        fitApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.e(TAG,"APP connected to fit");
                        request_data();
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

    //check if app has the history access permissions, if not then request them
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
        int cal= (int) Math.ceil((Integer.parseInt(daily_steps)*0.04258));
        calories.setText(getString(R.string.daily_counter, String.valueOf(cal)));
        int dist=  (int) Math.ceil(Integer.parseInt(daily_steps)/1312.33595801);
        distance.setText(getString(R.string.daily_counter, String.valueOf(dist)));
        Float s=Float.valueOf(daily_steps);
        Float t= Float.valueOf(pref.getString("target",""));
        progressBar.setProgress((int) Math.ceil((s/t)*100),true);
        preferences=pref.edit();
        preferences.putString("steps",daily_steps);
        preferences.putString("cal", String.valueOf(cal));
        preferences.putString("dist", String.valueOf(dist));
        if(((int) Math.ceil((s / t) * 100))<100) {
            streakProgressPcnt.setText(new StringBuilder().append(String.valueOf((int) Math.ceil((s / t) * 100))).append("%").toString());
            preferences.putString("progress",String.valueOf((int) Math.ceil((s/t)*100)));
        }else{
            streakProgressPcnt.setText("100%");
            preferences.putString("progress","100");
        }
        preferences.apply();
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