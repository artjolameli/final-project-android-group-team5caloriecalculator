package com.example.androidfinalproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfinalproject2020.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    EditText editPhone, editName, editPassword;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        setTitle("Go Back");

        editPassword = (MaterialEditText) findViewById(R.id.edit_password);
        editPhone = (MaterialEditText) findViewById(R.id.edit_phone);
        editName = (MaterialEditText) findViewById(R.id.edit_name);
        btnSignUp= (Button) findViewById(R.id.btnSignUp);

        // Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tabel_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
                alertDialog.setTitle("Hold on please...");
                final AlertDialog alert = alertDialog.show();

                tabel_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if user exists
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            // get user information
                            alert.dismiss();

                            Toast.makeText(SignUp.this, "Phone Number Already Exist!", Toast.LENGTH_SHORT).show();
                        } else {
                            alert.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString());
                            tabel_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Success!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
