package com.example.mychatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.Contacts.ContactsActivity;
import com.example.mychatapp.Model.User;
import com.example.mychatapp.Model.UserData;
import com.example.mychatapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<UserData> list;
    private Context context;

    public ContactsAdapter(List<UserData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactsAdapter.ViewHolder holder, int position) {
        UserData user= list.get(position);
        holder.contactdesc.setText(user.getPhonenumber());
        holder.contactusername.setText(user.getUsername());
        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.useruser).into(holder.contactimage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<UserData> list, Context contactsActivity) {
        this.list = list;
        this.context = contactsActivity;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView contactimage;
        private TextView contactusername ,contactdesc;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            contactimage=itemView.findViewById(R.id.contactprofilepic);
            contactusername=itemView.findViewById(R.id.contactusername);
            contactdesc=itemView.findViewById(R.id.contactdesc);

        }
    }
}
