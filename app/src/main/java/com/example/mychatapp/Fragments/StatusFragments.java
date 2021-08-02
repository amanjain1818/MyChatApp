package com.example.mychatapp.Fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mychatapp.Model.User;
import com.example.mychatapp.R;
import com.example.mychatapp.databinding.FragmentStatusFragmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class StatusFragments extends Fragment {



    public StatusFragments() {
        // Required empty public constructor
    }

  private FragmentStatusFragmentsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_status_fragments, container, false);


database=FirebaseDatabase.getInstance();
auth=FirebaseAuth.getInstance();

//        FirebaseUser user = auth.getCurrentUser();
//        User user2=new User();
//        user2.setUserid(user.getUid());
//        user2.setUsername(user.getDisplayName());
//        user2.setProfilepic(user.getPhotoUrl().toString());
//        database.getReference().child("User").child(user.getUid()).setValue(user2);
        return binding.getRoot();
    }


}