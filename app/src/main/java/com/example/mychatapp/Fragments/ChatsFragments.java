package com.example.mychatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mychatapp.Adapter.UserAdapter;
import com.example.mychatapp.Model.User;
import com.example.mychatapp.databinding.FragmentChatsFragmentsBinding;
import com.example.mychatapp.Notifications.Token;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChatsFragments extends Fragment {


    public ChatsFragments() {
        // Required empty public constructor
    }

   FragmentChatsFragmentsBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
     FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatsFragmentsBinding.inflate(inflater, container, false);
        UserAdapter adapter= new UserAdapter(list,getContext());
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
         binding.chatrecylerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.chatrecylerview.setLayoutManager(linearLayoutManager);

        firebaseDatabase.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                     User user=dataSnapshot.getValue(User.class);
                     user.setUserid(dataSnapshot.getKey());
                     if(!user.getUserid().equals(FirebaseAuth.getInstance().getUid()))
                     list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }


}