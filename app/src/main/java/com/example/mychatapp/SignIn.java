package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.ActivitySignInBinding;
import com.example.mychatapp.databinding.ActivitySignUpBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog dialog;
    private static final String EMAIL = "email";
    private FirebaseAuth auth;
    GoogleSignInClient mmGoogleSignInClient;
    FirebaseDatabase firebaseDatabase;
    SharedPreferences preferences;
    CallbackManager mCallbackManager;
    private String cureentUserid;
     public  String value ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        firebaseDatabase = FirebaseDatabase.getInstance();

        value= getIntent().getStringExtra("data");
        Log.d("ab","value"+value);

        dialog = new ProgressDialog(SignIn.this);
        dialog.setTitle("Login");
        dialog.setMessage("Login to your acccount");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mmGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.facebookll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignIn.this, "click facebook", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignIn.this, FacebookActivity.class);
                startActivity(intent);


            }
        });


        binding.buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.signuemail.getText().toString().isEmpty()) {
                    binding.signuemail.setError("Enter Your Email");
                    return;
                }
                if (binding.signuppassword.getText().toString().isEmpty()) {
                    binding.signuppassword.setError("Enter Your Password");
                    return;
                }
                dialog.show();

                auth.signInWithEmailAndPassword(binding.signuemail.getText().toString()
                        , binding.signuppassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful() ) {





                            SharedPreferences preferences = getSharedPreferences("chatApp", MODE_PRIVATE);
                            String username = preferences.getString("userName", "");
                            //  preferences.getString("userName","")
                            //Log.d("tag", "debug" + auth.getCurrentUser().getDisplayName());
                            //editor.putString("username", auth.getCurrentUser().getDisplayName()).apply();
                            String token = preferences.getString("fcmtoken", "");
                            Log.d("tag", "debug" + token);
                            Log.d("tag", "debug" + username);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(auth.getUid());
                            Map<String, Object> map = new HashMap<>();
                            map.put("token", token);
                            map.put("userName", username);
                            databaseReference.updateChildren(map);
                           // updateUserStatus("online");
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
// String value="create"


 if(auth.getCurrentUser() != null && value == null ) {
     Log.d("tag","value"+value);
    // Toast.makeText(this, "Signup", Toast.LENGTH_SHORT).show();

    // Log.d("tag","value"+value)

     Intent intent=new Intent(SignIn.this,MainActivity.class);
     startActivity(intent);

 }


        binding.clicksignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });



    }


//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (auth.getCurrentUser() != null) {
//            updateUserStatus("offline stop");
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (auth.getCurrentUser() != null)
//            updateUserStatus("online destory");
//    }

    int RC_SIGN_IN = 65;

    private void signIn() {
        Intent signInIntent = mmGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void updateUserStatus(String state) {
        String savecureenttime, savecureentdate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("mm dd,yyyy");
        savecureentdate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm,a");
        savecureenttime = currentTime.format(calendar.getTime());
        HashMap<String, Object> onlineState = new HashMap<>();
        onlineState.put("time", savecureenttime);
        onlineState.put("date", savecureentdate);
        onlineState.put("state", state);

        cureentUserid = auth.getCurrentUser().getUid();
        firebaseDatabase.getReference().child("User").child(cureentUserid).child("userState")
                .updateChildren(onlineState);


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            //get value  form google and store in firbse
                            User user1 = new User();
                            user1.setUserid(user.getUid());
                            user1.setUsername(user.getDisplayName());
                            user1.setProfilepic(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("User").child(user.getUid()).setValue(user1);

                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignIn.this, "SignIn with google", Toast.LENGTH_SHORT).show();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(binding.getRoot(),"Aunthetication Failed",Snackbar.LENGTH_SHORT).
                            // updateUI(null);
                        }
                    }
                });


    }
//
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d("aman", "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("aman", "signInWithCredential:success");
//                            FirebaseUser user = auth.getCurrentUser();
//                            startActivity(new Intent(SignIn.this,MainActivity.class));
//
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.d("aman", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(SignIn.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//    }


}