package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mychatapp.Adapter.ChatAdapter;
import com.example.mychatapp.Contacts.ContactsActivity;
import com.example.mychatapp.Model.MessageModel;
import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.ActivityChatDetailBinding;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    private boolean isAction = false;
    private int IMAGE_GALLARY_REQUEST = 111;
    private Uri imageuri;
    private String cureentUserid;
    SharedPreferences preferences;
    private String myname,myimage,myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




//        preferences = getSharedPreferences("chatApp", MODE_PRIVATE);
//
//       // Log.d("tag","name"+myname);
//       String  myname = preferences.getString("userName", "");
//        Log.d("tag","name"+myname);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //  apiService=Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        getSupportActionBar().hide();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
        firebaseDatabase.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User users=snapshot.getValue(User.class);

               myname= users.getUsername();
               myimage=users.getProfilepic();
               myid=firebaseAuth.getUid();
                Log.d("tag","users"+ users.getUsername());
                Log.d("tag","users"+ firebaseAuth.getUid());
                Log.d("tag","users"+  users.getProfilepic());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        final String senderid = firebaseAuth.getUid();
        String reciveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("username");
        String userProfilePic = getIntent().getStringExtra("userprofilepic");
      //String senderroom=getIntent().getStringExtra("senderId");

        Log.d("tag","sender id"+senderid);
        Log.d("tag","reciverid"+ reciveId);
        Log.d("tag","recivername"+ userName);
        Log.d("tag","reciveruserpic"+ userProfilePic);

        binding.usernamechat.setText(userName);

        Picasso.get().load(userProfilePic).placeholder(R.drawable.useruser).into(binding.profileimage);

        binding.gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChatDetailActivity.this,CurrentLocationMaps.class);
              startActivity(intent);
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, reciveId);

        binding.chatrecylerview.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        binding.chatrecylerview.setLayoutManager(layoutManager);

        final String senderroom = senderid + reciveId;
        final String recevierrroom = reciveId + senderid;
        Log.d("tag","senderroom"+senderroom);
        Log.d("tag","recevierroom"+recevierrroom);

        // show message in recycler view and firebase get  value(read)

        firebaseDatabase.getReference().child("chats")
                .child(senderroom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    MessageModel model1 = dataSnapshot1.getValue(MessageModel.class);
                   // Log.d("tag","data"+snapshot);
                    model1.setMessageId(dataSnapshot1.getKey());
                    messageModels.add(model1);


                }
                // recyler view run time update
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });


        binding.btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAction) {
                    binding.layoutAtions.setVisibility(View.GONE);
                    isAction = false;
                } else {
                    binding.layoutAtions.setVisibility(View.VISIBLE);
                    isAction = true;
                }
            }
        });


binding.contactss.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent= new Intent(ChatDetailActivity.this, ContactsActivity.class);
        startActivity(intent);
    }
});

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String message = binding.eMessage.getText().toString();



                Log.d("tag","message"+message);

                if (binding.eMessage.getText().toString().isEmpty()) {
                    binding.eMessage.setError("Enter Your Message");
                    return;
                }

                final MessageModel messageModel = new MessageModel(senderid, message);
                messageModel.setTimetamp(new Date().getTime());

                getToken(message, reciveId, myimage,senderroom);

                binding.eMessage.setText("");

                // seprate node create in database in chats

                firebaseDatabase.getReference().child("chats").child(senderroom)
                        .push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseDatabase.getReference().child("chats").child(recevierrroom)
                                .push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                            }
                        });
                    }
                });

            }
        });

    }
    private void getToken(String message, String reciveId, String myimage,String senderroom) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(reciveId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
//                 String name=snapshot.child("name").getValue().toString();
                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try {

                    data.put("title", myname);
                    data.put("message", message);
                    data.put("senderId", senderroom);
                    data.put(" userId",myid );
                     data.put("userprofilepic", myimage);

                    Log.d("tag","username"+ myname);
                    Log.d("tag"," message"+ message);
                    Log.d("tag","senderid"+ myid);
                    Log.d("tag","senderpic"+myimage);
                    Log.d("tag","bothchat"+senderroom);
                    Log.d("tag", "reciveruser" + token);

                    to.put("to", token);
                    to.put("data", data);
                    sendNotification(to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL, to, response ->
        {
            Log.d("notifiaction", "sendnotification" + response);
        }, error -> {
            Log.d("notifiaction", "sendnotification" + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + AllConstants.SERVER_KEY);
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_GALLARY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLARY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            //         uploadToFirbase();
        }
    }


}