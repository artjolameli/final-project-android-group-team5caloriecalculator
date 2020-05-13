package com.example.androidfinalproject2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity {

    private Button logoutBtn, addBtn, deleteBtn;
    private TextView textConsumedCals, textTotalCals;
    private String stringConsumedCals;
    private String stringTotalCals;

    // Calender by Rene
    private TextView tt;
    public static final int TEXT_REQUEST = 1;
    public static final int INPUT_REQUEST = 0;

    // Shared preference
    private float mCount = 0f;
    private float mTotalCalories = 3500f;
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

        logout();
        add();
        delete();
        setCircularProgressBar();
        pickCalender();
    }

    private void logout() {
        // click button exit
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);

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
        // set CircularProgressBar Width, RoundBorder, Max
        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgressBarWidth(30); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(30); // in DP
        circularProgressBar.setRoundBorder(true);

        // init total calories on textView
        textTotalCals = (TextView) findViewById(R.id.text_total_calories);

        // Initialize views, color, preferences
        textConsumedCals = (TextView) findViewById(R.id.text_consumed_calories);
        mColor = ContextCompat.getColor(HomeActivity.this, R.color.colorAccent);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // restore color
        mColor = mPreferences.getInt(COLOR_KEY, mColor);
        circularProgressBar.setProgressBarColor(mColor);

        // Restore preferences
        mCount = mPreferences.getFloat(COUNT_KEY, 0);
        textConsumedCals.setText(String.format("%s", mCount));
        circularProgressBar.setProgress(mCount);
        // add up calories from intent
        getIntentCalories();

        mTotalCalories = mPreferences.getFloat(TOTAL_KEY,3500f);
        textTotalCals.setText(String.format("%s",mTotalCalories));
        circularProgressBar.setProgressMax(mTotalCalories);
        // add up calories from intent
        getIntentTotalCalories();

    }

    private void getIntentCalories() {

        stringConsumedCals = getIntent().getStringExtra(MainRecyclerActivity.CALORIES_RESULT);
        if (stringConsumedCals != null) {
            mCount = mCount + Float.valueOf(stringConsumedCals);
            textConsumedCals.setText(String.valueOf(mCount));
            circularProgressBar.setProgress(mCount);
            if (mCount >= 3000f) {
                Toast.makeText(HomeActivity.this,
                               "Too many calories today.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getIntentTotalCalories(){

        stringTotalCals = getIntent().getStringExtra(InputActivity.INPUT_CALORIES);
        if (stringTotalCals != null) {
            mTotalCalories = Float.valueOf(stringTotalCals);
            textTotalCals.setText(String.valueOf(mTotalCalories));
            circularProgressBar.setProgressMax(mTotalCalories);
        }
    }

    private void changeBackground (View view){
        int color = ((ColorDrawable) view.getBackground()).getColor();
        circularProgressBar.setProgressBarColor(color);
        mColor = color;

    }

    public void reset (View view){
        Intent resetIntent = new Intent(HomeActivity.this, InputActivity.class);
        startActivity(resetIntent);
    }

    public void clear (View view){

        // Reset consumed calories
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
        preferencesEditor.putFloat(TOTAL_KEY, mTotalCalories);
        preferencesEditor.putFloat(COUNT_KEY, mCount);
        preferencesEditor.putInt(COLOR_KEY, mColor);
        preferencesEditor.apply();
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



