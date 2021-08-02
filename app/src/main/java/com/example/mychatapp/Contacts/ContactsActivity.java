package com.example.mychatapp.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mychatapp.Adapter.ContactsAdapter;
import com.example.mychatapp.MainActivity;
import com.example.mychatapp.Model.User;
import com.example.mychatapp.Model.UserData;
import com.example.mychatapp.R;
import com.example.mychatapp.databinding.ActivityContactsBinding;
import com.example.mychatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private List<UserData> list=new ArrayList<>();
    private ContactsAdapter adapter;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private static  final  String TAG= " ContactsActivity ";
    public static final int REQUEST_READ_CONTACTS = 79;
    ListView contactlist;
    ArrayList mobileArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_contacts);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
        binding.backcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        auth=FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firestore=FirebaseFirestore.getInstance();


        adapter=new ContactsAdapter(list,ContactsActivity.this);
        binding.recyclerview.setAdapter(adapter);

        if(firebaseUser != null){
           getContactFromPhone();
         //   getContactslist();


        }
        if(mobileArray !=null){
           getContactslist();
        }



    }

    private void getContactFromPhone() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();
        } else {
            requestPermission();
        }

    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else {
                    finish();
                }
                return;
            }
        }
    }
    private ArrayList getAllContacts() {
        ArrayList<String> phonelist = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur !=null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
              String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
              //  nameList.add(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phonelist.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return phonelist;
    }
    private void getContactslist() {
        firebaseDatabase.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

//                Log.d(ContactsActivity.TAG,"data is "+ snapshot.getValue().toString());

                for(DataSnapshot snapshot1:snapshot.getChildren() ) {
                    Log.d(ContactsActivity.TAG,"snapshot1 data is "+ snapshot1.getValue().toString());
                    UserData user= snapshot1.getValue(UserData.class);
                  Log.d("aman","amanj "+ (user.getStatus() != null && user.getStatus().isEmpty()));

                    if(user != null && user.getPhonenumber() != null &&   !user.getPhonenumber().isEmpty() ) {
                      //if(mobileArray.contains(user.getUsername()))
                            list.add(user);


                    }
                }
                for(UserData user : list)
                {
                    if(mobileArray.contains(user.getPhonenumber())){
                        Log.d(TAG,"getcontactlist :true"+user.getPhonenumber());
                    }else{
                        Log.d(TAG,"getcontactlist :false"+user.getPhonenumber());
                    }
                }

                 //  String userId=snapshot1.toString();

                   // String username=snapshot1.toString();
                  //  String username=snapshots1.getString(firebaseUser.getDisplayName());
                   // String imageurl=snapshots1.getString(String.valueOf(firebaseUser.getPhotoUrl()));
                    //String desc=snapshots1.getString(firebaseUser.getEmail());
                  //  User user= new User();
                   // user.setUsername(username);
                    //user.setProfilepic(imageurl);
                    //user.setStatus(desc);
                  //  User list1=snapshot1.getValue(User.class);
                    //list1.setUserid(snapshot1.getKey());
                    //list1.setUsername(snapshot1.getKey());
//                    if(userId !=null && !userId.equals(firebaseUser.getUid())) {
//                        list.add(list);
//                    }
                    //list.add(list1);

                adapter.updateAdapter(list, ContactsActivity.this);




            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

//        firestore.collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
//
//                    String userId=snapshots.getString(firebaseUser.getUid());
//                    String username=snapshots.getString(firebaseUser.getDisplayName());
//                    String imageurl=snapshots.getString(String.valueOf(firebaseUser.getPhotoUrl()));
//                    String desc=snapshots.getString(firebaseUser.getEmail());
//                    User user= new User();
//                    user.setUsername(username);
//                    user.setProfilepic(imageurl);
//                    user.setStatus(desc);
//                    user.setUserid(userId);
//                    if(userId !=null && !userId.equals(firebaseUser.getUid())) {
//                        list.add(user);
//                    }
//
//                }
//                adapter=new ContactsAdapter(list,ContactsActivity.this);
//                binding.recyclerview.setAdapter(adapter);
//
//            }
//        });
    }
}
