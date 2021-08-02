package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mychatapp.Adapter.FragmentAdapter;
import com.example.mychatapp.Contacts.ContactsActivity;
import com.example.mychatapp.databinding.ActivityMainBinding;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        auth =FirebaseAuth.getInstance();
         binding.viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
          binding.tablayout.setupWithViewPager(binding.viewpager);
          binding.tablayout.getTabAt(0).setCustomView(R.layout.custom_camera_tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                Intent intent2=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                    auth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
                finish();
                break;

            case R.id.groupchat:
                Intent intent1=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeFabIcon(final  int index)
    {
        binding.fabAction.hide();
        binding.btnEdit.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index){

                    case 0:binding.fabAction.hide();
                    case 1:

                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.mess));
                    binding.fabAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(MainActivity.this, ContactsActivity.class);
                            startActivity(intent);
                        }
                    });

                        break;
                    case 2:binding.fabAction.setImageDrawable(getDrawable(R.drawable.cam));
                        binding.btnEdit.setVisibility(View.VISIBLE);
                        break;
                    case 3:binding.fabAction.setImageDrawable(getDrawable(R.drawable.mess));
                        break;



                }
                binding.fabAction.show();
            }
        },400);

    }

}