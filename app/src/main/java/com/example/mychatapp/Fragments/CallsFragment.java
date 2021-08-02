package com.example.mychatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mychatapp.Adapter.CallListAdapter;
import com.example.mychatapp.Adapter.UserAdapter;
import com.example.mychatapp.Model.CallList;
import com.example.mychatapp.Model.User;
import com.example.mychatapp.R;
import com.example.mychatapp.databinding.FragmentCallsBinding;
import com.example.mychatapp.databinding.FragmentChatsFragmentsBinding;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {



    public CallsFragment() {
        // Required empty public constructor
    }
    FragmentCallsBinding binding;
    ArrayList<CallList> list = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCallsBinding.inflate(inflater, container, false);
        list = new ArrayList<>();
        list.add(new CallList("001",
                "Aman",
                "23/07/21 ,9:24 am",
                " https://graph.facebook.com/4141681919254175/picture\""
                ,"income"));

        list.add(new CallList("002",
                "Aman jain",
                "23/07/21 ,9:25 am",
                " https://graph.facebook.com/4141681919254175/picture\""
                ,"missed"));

        list.add(new CallList("003",
                "Aman jai",
                "23/07/21 ,9:30 am",
                " https://graph.facebook.com/4141681919254175/picture\""
                ,"out"));

        CallListAdapter  adapter= new CallListAdapter(list,getContext());
        binding.fragmentrecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.fragmentrecyclerview.setLayoutManager(linearLayoutManager);
        return binding.getRoot();
    }
}