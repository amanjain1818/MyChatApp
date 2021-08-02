package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progress;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("chatApp", MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();

        progress=new ProgressDialog(SignUpActivity.this);
        progress.setTitle("Creating Account");
        progress.setMessage("we're create your account");

        binding.buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();



                auth.createUserWithEmailAndPassword(binding.edemail.getText().toString()
                        ,binding.edpassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                progress.dismiss();
                                if(task.isSuccessful()){

                        User user =new User(binding.edusername.getText().toString(),binding.edemail.getText().
                                toString(),binding.edpassword.getText().toString(),binding.edphoneno.getText().toString());
                        Log.d("tag","signup"+user.getPhonenumber());
                        Log.d("tag","signup"+user.getUsername());
                           String id=task.getResult().getUser().getUid();
                                    if (binding.edemail.getText().toString().isEmpty()) {
                                        binding.edemail.setError("Enter Your Email");
                                        return;
                                    }
                                    if (binding.edpassword.getText().toString().isEmpty()) {
                                        binding.edpassword.setError("Enter Your Password");
                                        return;
                                    }
//                                    if (binding.edphoneno.getText().toString().matches("^\\d{10}$"))
//                                    {
//                                        binding.edphoneno.setError("Enter Your Correct Mobile Number");
//                                        return;
//                                    }



                           // user detail stoe in database

                           firebaseDatabase.getReference().child("User").child(id).setValue(user);

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("userName", user.getUsername()).apply();


                                    String token = preferences.getString("fcmtoken", "");
                                    Log.d("tag","debug"+token);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(auth.getUid());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("token", token);
                                    databaseReference.updateChildren(map);


                         Toast.makeText(SignUpActivity.this,"User create sucessfully",Toast.LENGTH_SHORT).show();

                                    Intent intent4 = new Intent(SignUpActivity.this,SignIn.class);
                                    startActivity(intent4);
                        } else{
                           Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }
            });
            }
        });

        binding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(SignUpActivity.this,SignIn.class);
                startActivity(intent4);
            }
        });
    }
}