package com.example.androidfinalproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextCalorie;
    private NumberPicker numberPickerNumber;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Note");

        // find by id
        editTextTitle = (EditText) findViewById(R.id.edit_text_title);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        editTextCalorie = (EditText)findViewById(R.id.edit_text_calorie);
        numberPickerNumber = (NumberPicker) findViewById(R.id.number_picker_number);
        numberPickerNumber.setMinValue(1);
        numberPickerNumber.setMaxValue(99);

        saveButton = (Button)findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                saveNote();
                Intent intent = new Intent(NewNoteActivity.this, MainRecyclerActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        try{
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            int calorie = Integer.valueOf(editTextCalorie.getText().toString());
            int numberFood = numberPickerNumber.getValue();

            CollectionReference notebookRef = FirebaseFirestore
                    .getInstance()
                    .collection("Notebook");
            notebookRef.add(new Note(title,description,numberFood,calorie));
            Toast.makeText(this, "Notes added", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            System.out.println("Error " + e.getMessage());
            Toast.makeText(this,"You didn't input anything", Toast.LENGTH_SHORT).show();
        }
    }
}

