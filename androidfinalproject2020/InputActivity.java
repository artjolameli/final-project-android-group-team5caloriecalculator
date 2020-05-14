package com.example.androidfinalproject2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {
    public static final String INPUT_CALORIES = "CALORIES";

    private EditText inputName, inputAge, inputHeight, inputWeight;
    private Button saveInputButton;

    private double totalCalories, height, weight;
    private int sexType, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        saveInputButton = (Button) findViewById(R.id.input_save_button);
        // go back the parent activity
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        setTitle("Go Back");

        saveInputButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    calculate();
                    Intent intent = new Intent(InputActivity.this, HomeActivity.class);
                    intent.putExtra(INPUT_CALORIES, String.valueOf(totalCalories));
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(InputActivity.this, "Please enter your information.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void calculate() {
        // find editText by ID
        inputName = (EditText) findViewById(R.id.input_name_text);
        inputAge = (EditText) findViewById(R.id.input_age_text);
        inputHeight = (EditText) findViewById(R.id.input_height_text);
        inputWeight = (EditText) findViewById(R.id.input_weight_text);

        // get the value of age, height, weight
        age = Integer.parseInt(inputAge.getText().toString());
        height = Double.parseDouble(inputHeight.getText().toString());
        weight = Double.parseDouble(inputWeight.getText().toString());


        if (inputName.length() == 0) {
            inputName.requestFocus();
            inputName.setError("Please enter your name.");

        } else if (sexType == 1){
            // BMR = 13.397W + 4.799H - 5.677A + 88.362
            // BMR = 10W + 6.25H - 5A + 5
//            totalCalories = Math.round(13.397*(weight*0.454) + 4.799*(height*2.54) - 5.677*age + 88.362);

            totalCalories = Math.round(10*(weight*0.45) + 6.25*(height*30.48) - 5*age + 5 + 500);

        }
        else if (sexType == 2){
            // BMR = 9.247W + 3.098H - 4.330A + 447.593
            // BMR = 10W + 6.25H - 5A - 161
//            totalCalories = Math.round(9.247*(weight*0.454) + 3.098*(height*2.54) -4.330*age + 447.593);
            totalCalories = Math.round(10*(weight*0.45) + 6.25*(height*30.48) - 6*age -161 + 500);
        }
        else {
            Toast.makeText(InputActivity.this, "Select your sex.", Toast.LENGTH_SHORT).show();

        }
    }

    public void onRadioButtonClicker(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.input_male_label:
                if(checked)
                    sexType = 1;
                Toast.makeText(InputActivity.this, "male", Toast.LENGTH_SHORT).show();
                break;
            case R.id.input_female_label:
                if(checked)
                    sexType = 2;
                Toast.makeText(InputActivity.this, "female", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}


// Chen: Professor
// this page was depricated, due to have bugs on the max value on homepage.








