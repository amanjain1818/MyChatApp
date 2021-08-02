package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mychatapp.Adapter.ChatAdapter;
import com.example.mychatapp.Model.MessageModel;
import com.example.mychatapp.databinding.ActivityChatDetailBinding;
import com.example.mychatapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;
    private boolean isAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String senderid = FirebaseAuth.getInstance().getUid();
        binding.usernamechat.setText("Friends Group");
        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatrecylerview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatrecylerview.setLayoutManager(layoutManager);

// get message from database and show in recycyler view


     database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        messageModels.clear();
        for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
        {
            MessageModel model1=dataSnapshot1.getValue(MessageModel.class);
            messageModels.add(model1);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
});

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message=binding.eMessage.getText().toString();
                if (binding.eMessage.getText().toString().isEmpty()) {
                    binding.eMessage.setError("Enter Your Message");
                    return;
                }
                final MessageModel messageModel=new MessageModel(senderid,message);
                messageModel.setTimetamp(new Date().getTime());
                binding.eMessage.setText("");

                database.getReference().child("Group Chat").push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
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
    }
}