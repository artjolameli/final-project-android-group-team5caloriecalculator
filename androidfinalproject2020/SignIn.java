package com.example.androidfinalproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidfinalproject2020.model.User;
import com.example.androidfinalproject2020.Common.Common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.PriorityQueue;

public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnSignIn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        setTitle("Go Back");

        editPassword = (MaterialEditText) findViewById(R.id.edit_password);
        editPhone = (MaterialEditText) findViewById(R.id.edit_phone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        // Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tabel_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignIn.this);
                alertDialog.setTitle("Loading...");
                final AlertDialog alert = alertDialog.show();

                tabel_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Check if user exists
                        if(dataSnapshot.child(editPhone.getText().toString()).exists()){
                            // get user information
                            alert.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);

                            if (user != null) {
                                if(user.getPassword().equals(editPassword.getText().toString())){
                                    Toast.makeText(SignIn.this,"Password Correct!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignIn.this,HomeActivity.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Toast.makeText(SignIn.this,"Wrong Password!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            alert.dismiss();
                            Toast.makeText(SignIn.this,"User not exist!",Toast.LENGTH_SHORT).show();
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
