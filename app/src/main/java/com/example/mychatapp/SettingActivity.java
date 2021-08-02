package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class
SettingActivity extends AppCompatActivity {
 ActivitySettingBinding binding;
 FirebaseStorage storage;
 FirebaseAuth auth;
 FirebaseDatabase database;
 SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        // preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.backkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=binding.eduusername.getText().toString();
                String status=binding.eduabout.getText().toString();
                // update value in firbase use hash map
                HashMap<String ,Object> obj =new HashMap<>();
                obj.put("username",username);
                obj.put("status",status);


               Log.d("tag","updateuser"+username);


                Toast.makeText(SettingActivity.this, "updateusert"+username, Toast.LENGTH_SHORT).show();
                // update children node

                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
              //  SharedPreferences.Editor editor = preferences.edit();
                //Log.d("tag","updateuser"+ String.valueOf(obj));
               // editor.putString("userName", username).apply();
                Toast.makeText(SettingActivity.this, "Profile Update Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });
                   // firbase  data read nd show  in activity
                  // update image ,update username  set

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        User users=snapshot.getValue(User.class);
                        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.useruser).into(binding.profileimage);

                        binding.eduabout.setText(users.getStatus());
                         binding.eduusername.setText(users.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){

            Uri sFile= data.getData();
            binding.profileimage.setImageURI(sFile);
             // create node  in firebase database

            final StorageReference reference=storage.getReference().child("profile picures")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SettingActivity.this, "pic upload", Toast.LENGTH_SHORT).show();
                  // photo url in store in database
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                               // url show in user list  nd create a node  nd set url nd pic show in recycler view
                            database.getReference().child("User")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("profilepic").setValue(uri.toString());
                              Toast.makeText(SettingActivity.this, "Profile pic updated", Toast.LENGTH_SHORT).show();



                        }
                    });
                }
            });
        }
    }
}