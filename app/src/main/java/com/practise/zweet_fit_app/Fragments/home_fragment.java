package com.practise.zweet_fit_app.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.practise.zweet_fit_app.PagerAdapter.ActivityHintsViewPagerAdapter;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Server.ServerRequests;
import com.practise.zweet_fit_app.Util.Step_Item;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_fragment extends Fragment implements View.OnClickListener {
    List<GrpEventsModal> grpEventsModalList = new ArrayList<>();
    ProgressBar streakProgressBar;
    TextView streakPercent,goalAchieved;
    CardView streakCard, premiumCard;
    CircleImageView userImg;
    ImageView searchUsers, hintbutton;
    ImageView d1,d2,d3,d4,d5,d6,d7;
    TextView steps;
    DataReadRequest readRequest ;
    ProgressBar progressBar;
    TextView distance, calories, streakProgressPcnt;
    TextView userName;
    int streaks=0;
    String CHANNEL_ID="1";
    String notificationId="101";
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    long numSteps = 0;
    Boolean flag=false;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentuser;
    public static final String TAG = "MainActivity_History";
    public static boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 100;
    public static final int REQUEST_PERMISSIONS = 200;
    GoogleApiClient fitApiClient;
    LocalDate date=LocalDate.now();
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
        hintbutton = view.findViewById(R.id.hintbutton);
        progressBar = view.findViewById(R.id.progressBar);
        goalAchieved=view.findViewById(R.id.goalAchieved);
        d1=view.findViewById(R.id.monStreakStat);d2=view.findViewById(R.id.tuesStreakStat);d3=view.findViewById(R.id.wedStreakStat);
        d4=view.findViewById(R.id.thursStreakStat);d5=view.findViewById(R.id.friStreakStat);d6=view.findViewById(R.id.satStreakStat);
        d7=view.findViewById(R.id.sunStreakStat);
        searchUsers.setOnClickListener(this);
        userImg.setOnClickListener(this);
        hintbutton.setOnClickListener(this);
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
        buildClient();
        readRequest = getDataRequestForWeeks(date.toString(),date.plusDays(1).toString());
        request_data();
        setData();
        getStreakStatus();
//        saveData();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        return view;
    }

    private void saveData() {
        ServerRequests requests=new ServerRequests();
        requests.updateUsers(
                pref.getString("id","0"),pref.getString("name",""),pref.getString("dob",""),
                pref.getString("wt",""),pref.getString("ht",""),pref.getString("target","750"),
                String.valueOf(streaks),
                "true",pref.getString("coins","0"),"1",
                "80","0",pref.getString("dp","")
        );
    }

    private void getStreakStatus() {
        LocalDate start = date;
        while (start.getDayOfWeek() != DayOfWeek.MONDAY) {
            start = start.minusDays(1);
        }
        LocalDate end = date;
        while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
            end = end.plusDays(1);
        }
        buildClient();
        readRequest=getDataRequestForWeeks(start.toString(),end.toString());
        request_data();


    }

    private void setData() {
        userName.setText(pref.getString("name",""));
        if(!pref.getString("dp","").isEmpty()){
            Picasso.get().load(pref.getString("dp","")).into(userImg);
        }
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
        if(v == hintbutton){
            AlertDialog.Builder addhintdialog=new AlertDialog.Builder(getContext());
            View hint=getLayoutInflater().inflate(R.layout.fragment_stephint,null);
            addhintdialog.setView(hint);
            AlertDialog dialog=addhintdialog.create();
            dialog.setCancelable(true);
            dialog.show();
            Button nxt = hint.findViewById(R.id.Next1);
            nxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getFragmentManager().popBackStack();
                    AlertDialog.Builder addhintdialog=new AlertDialog.Builder(getContext());
                    View hint=getLayoutInflater().inflate(R.layout.fragment_coinhint,null);
                    addhintdialog.setView(hint);
                    AlertDialog dialog=addhintdialog.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    Button nxt2 = hint.findViewById(R.id.Next2);
                    nxt2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getActivity().getFragmentManager().popBackStack();
                            AlertDialog.Builder addhintdialog=new AlertDialog.Builder(getContext());
                            View hint=getLayoutInflater().inflate(R.layout.fragment_levelhint,null);
                            addhintdialog.setView(hint);
                            AlertDialog dialog=addhintdialog.create();
                            dialog.setCancelable(false);
                            dialog.show();
                            Button cls = hint.findViewById(R.id.Closebtn);
                            cls.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getActivity().getFragmentManager().popBackStackImmediate();
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
            });
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
        checkTarget(daily_steps,pref.getString("target",""));
        setStreakCardData();
//        saveData();
    }

    private void setStreakCardData() {
        streaks=0;
        if(stepsData.size()>=1){
            for(int i=0;i<stepsData.size();i++){
                Step_Item data=stepsData.get(i);
                updateDayStreak(i+1,data.getData());
            }
        }
        goalAchieved.setText(String.valueOf(streaks).concat("/7 Goals"));
    }

    private void updateDayStreak(int day, String data) {
        boolean streakAchieved= Integer.parseInt(data) >= Integer.parseInt(pref.getString("target", ""));
        switch(day){
            case 1:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d1.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d1.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 2:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d2.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d2.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 3:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d3.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d3.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 4:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d4.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d4.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 5:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d5.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d5.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 6:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d6.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d6.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
            case 7:{
                Log.i(String.valueOf(day),data+","+streakAchieved+","+pref.getString("target",""));
                if(streakAchieved){
                    d7.setImageDrawable(getResources().getDrawable(R.drawable.done));
                    streaks++;
                }else{
                    d7.setImageDrawable(getResources().getDrawable(R.drawable.inprogress));
                }
                break;
            }
        }
    }

    private void checkTarget(String daily_steps, String target) {
        if(Integer.parseInt(daily_steps)>=Integer.parseInt(target) &&!
                pref.getString("last notified","").equalsIgnoreCase(LocalDate.now().toString()))
        {
            creditCoins(10);
            sendNotification();
            preferences=pref.edit();
            preferences.putString("last notified",LocalDate.now().toString());
            preferences.apply();
        }
    }

    private void creditCoins(int coins) {
        preferences=pref.edit();
        int c=Integer.parseInt(pref.getString("coins","0"))+coins;
        preferences.putString("coins",String.valueOf(c));
        preferences.apply();
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.footprints)
                .setContentTitle("Congratulations!")
                .setContentText("You Achieved your daily goal...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You Achieved your daily goal..."))
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        createNotificationChannel();
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(Integer.parseInt(notificationId), builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
            Log.i(date,String.valueOf(nSteps));
            if(stepsData.contains(new_item)){
                int item_index = stepsData.indexOf(new_item);
                Step_Item temp_item = stepsData.get(item_index);
                stepsData.remove(item_index);
            }
            stepsData.add(new_item);
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("How Do We Calculate Steps");
        alert.setMessage("Message");

        alert.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                alert.setTitle("How Do We Distribute Coins");
                alert.setMessage("Message");

                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        setFragment(CoinHistory);
                    }
                });
                alert.show();
            }
        });

        alert.show();
    }
}