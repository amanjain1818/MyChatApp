 package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.ActivitySignInBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FacebookActivity extends SignIn  {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callbackManager = CallbackManager.Factory.create();
        mAuth=FirebaseAuth.getInstance();
        LoginManager.getInstance().logInWithReadPermissions(FacebookActivity.this, Arrays.asList("public_profile "));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("aman", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("aman", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("aman", "facebook:onError");
                        // App code
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            User user1=new User();
                            user1.setUserid(user.getUid());
                            user1.setUsername(user.getDisplayName());
                            user1.setProfilepic(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("User").child(user.getUid()).setValue(user1);


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(FacebookActivity.this, ""+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                    }
                }


    });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent= new Intent(FacebookActivity.this,MainActivity.class);
        startActivity(intent);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



}



//keytool -exportcert -alias androiddebugkey -keystore "C:\Users\Saloni\.android\debug.keystore" | "C:\OpenSSl\bin\openssl" sha1 -binary | "C:\OpenSSl\bin\openssl" base64

