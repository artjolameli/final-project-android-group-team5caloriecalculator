package com.example.androidfinalproject2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

public class CalenderActivity extends AppCompatActivity {

    private DatePicker dp;
    public static final String EXTRA_DATE = "com.example.androidfinalproject2020.extra.Date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);


        dp = (DatePicker) findViewById(R.id.datePicker);
        dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String d = ((monthOfYear + 1)+ "-" + dayOfMonth + "-"+year);

                Intent intent = new Intent(CalenderActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_DATE, d);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
