package com.example.mychatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mychatapp.Fragments.CallsFragment;
import com.example.mychatapp.Fragments.CameraFragment;
import com.example.mychatapp.Fragments.ChatsFragments;
import com.example.mychatapp.Fragments.StatusFragments;
import com.example.mychatapp.R;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter  extends FragmentPagerAdapter {

    private Object Context;

    public FragmentAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new CameraFragment();
            case 1: return new ChatsFragments();
            case 2: return new StatusFragments();
            case 3: return new CallsFragment();
            default:return new ChatsFragments();

        }


    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0){
            title=" ";



        }
        if(position==1){
            title="Chats";
        }
        if(position==2){
            title="Status";
        }
        if(position==3){
            title="Calls";
        }
        return title;
    }
}
