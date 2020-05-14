package com.example.androidfinalproject2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity {

    private Button logoutBtn, addBtn, deleteBtn, alarmBtn,messageBtn, callBtn;
    private TextView textConsumedCals, textTotalCals;
    private String stringConsumedCals;
    private String stringTotalCals;

    // Calender by Rene
    private TextView tt;
    public static final int TEXT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 0;

    // Shared preference
    private float mCount = 0f;
    private float mTotalCalories = 2500f;
    private int mColor;
    // Key
    private final String COUNT_KEY = "count";
    private final String COLOR_KEY = "color";
    private final String TOTAL_KEY = "total colories";
    // Shared preferences object
    private SharedPreferences mPreferences;
    // Name of shared preferences file
    private String sharedPrefFile = "sharedpreference";
    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // go back the parent activity
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        setTitle("Go Back");

        sendMessage();
        makeCall();
        alarm();
        logout();
        add();
        delete();
        setCircularProgressBar();
        pickCalender();
    }

    private void makeCall() {
        callBtn = (Button)findViewById(R.id.call_button);
        callBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall() {
        Intent intentCall = new Intent(Intent.ACTION_DIAL);

        if (intentCall.resolveActivity(getPackageManager()) != null) {
            startActivity(intentCall);
        }
    }

    private void sendMessage() {
        messageBtn = (Button)findViewById(R.id.message_button);

        messageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intentMsm = new Intent(Intent.ACTION_SEND);
                intentMsm.putExtra(Intent.EXTRA_TEXT,0);
                intentMsm.setType("text/plain");
                Intent chooser = Intent.createChooser(intentMsm,"Share with");
                if(intentMsm.resolveActivity(getPackageManager()) != null ){
                startActivity(chooser);
                }
            }
        });
    }
    private void alarm() {
        // click button add
        alarmBtn = (Button)findViewById(R.id.alarm_btn);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_alarm = new Intent(HomeActivity.this,
                                               NotificationActivity.class);
                startActivity(intent_alarm);

            }
        });
    }

    private void logout() {
        // click button exit
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentLogout = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intentLogout);

            }
        });
    }

    private void add() {
        // click button add
        addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(HomeActivity.this,
                                               MainRecyclerActivity.class);
                startActivity(intent_add);

            }
        });
    }

    private void delete() {
        // click button add
        deleteBtn = (Button)findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // recall the last calories for the delete button
                if (stringConsumedCals != null){
                    float temp = mCount - Float.valueOf(stringConsumedCals);
                    textConsumedCals.setText(String.valueOf(temp));
                    circularProgressBar.setProgress(temp);
                }
                else{ // make sure not crash if didn't add calories
                    mCount = mCount - 0f;
                    textConsumedCals.setText(String.valueOf(mCount));
                    circularProgressBar.setProgress(mCount);
                }
            }
        });
    }


    private void setCircularProgressBar() {
        // set CircularProgressBar
        circularProgressBar = findViewById(R.id.circularProgressBar);
        // Set Width, RoundBorder, Max
        circularProgressBar.setProgressBarWidth(30); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(30); // in DP
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setProgressMax(2500f);

        // Initialize views, color, preferences
        textConsumedCals = (TextView) findViewById(R.id.text_consumed_calories);
        mColor = ContextCompat.getColor(HomeActivity.this, R.color.colorAccent);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Restore preferences
        mCount = mPreferences.getFloat(COUNT_KEY, 0);
        textConsumedCals.setText(String.format("%s", mCount));
        circularProgressBar.setProgress(mCount);

        mColor = mPreferences.getInt(COLOR_KEY, mColor);
        circularProgressBar.setProgressBarColor(mColor);

        // add up calories from intent
        getIntentCalories();

    }

    private void getIntentCalories() {

        stringConsumedCals = getIntent().getStringExtra(MainRecyclerActivity.CALORIES_RESULT);
        if (stringConsumedCals != null) {
            mCount = mCount + Float.valueOf(stringConsumedCals);
            textConsumedCals.setText(String.valueOf(mCount));
            circularProgressBar.setProgress(mCount);
            if (mCount >= 2500f) {
                Toast.makeText(HomeActivity.this,
                               "Too many calories today.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void changeBackground (View view){
        int color = ((ColorDrawable) view.getBackground()).getColor();
        circularProgressBar.setProgressBarColor(color);
        mColor = color;

    }

    public void reset (View view){
        // Reset count
        mCount = 0f;
        textConsumedCals.setText(String.format("%s", mCount));
        circularProgressBar.setProgress(mCount);

        // Reset color
        mColor = ContextCompat.getColor(this, R.color.colorAccent);
        circularProgressBar.setProgressBarColor(mColor);

        // Clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    public void clear (View view){
        // Reset count
        mCount = 0f;
        textConsumedCals.setText(String.format("%s", mCount));
        circularProgressBar.setProgress(mCount);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putFloat(COUNT_KEY, mCount);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Restores the scores if there is savedInstanceState
        if (savedInstanceState != null) {
            mCount = savedInstanceState.getFloat(COUNT_KEY);
            //Set the score text views
            textConsumedCals.setText(String.valueOf(mCount));
            circularProgressBar.setProgress(mCount);

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause () {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putFloat(COUNT_KEY, mCount);
        preferencesEditor.putInt(COLOR_KEY, mColor);
        preferencesEditor.apply();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }





    // Calender by Rene
    private void pickCalender() {

        tt = (TextView) findViewById(R.id.text_date_label);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = simpleDateFormat.format(c.getTime());

        tt.setText("Date: "+date);

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CalenderActivity.class);
                startActivityForResult(intent, TEXT_REQUEST);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String dd = data.getStringExtra(CalenderActivity.EXTRA_DATE);
                tt.setText("Date: " + dd);
                tt.setVisibility(View.VISIBLE);
            }
        }
    }
}



