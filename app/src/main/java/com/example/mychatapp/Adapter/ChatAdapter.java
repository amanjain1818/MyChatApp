package com.example.mychatapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.Model.MessageModel;
import com.example.mychatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
ArrayList<MessageModel> messageModels;
Context context;
      int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    String recID;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recID) {
        this.messageModels = messageModels;
        this.context = context;
        this.recID = recID;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.samplesender, parent, false);
            return new SendViewHolder(view);
        }
else{
            View view = LayoutInflater.from(context).inflate(R.layout.samplereciever, parent, false);
            return new ReciverViewVolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
 MessageModel messageModel2=messageModels.get(position);
 if(holder.getClass() == SendViewHolder.class){
     ((SendViewHolder)holder).sendtext.setText(messageModel2.getMessage());
 }else{
     ((ReciverViewVolder)holder).receivertext.setText(messageModel2.getMessage());
 }
 holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
     @Override
     public boolean onLongClick(View view) {
         new AlertDialog.Builder(context).setTitle("Delete")
                 .setMessage("Are You sure you want to delete this message")
                 .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         FirebaseDatabase database=FirebaseDatabase.getInstance();
                         String sender = FirebaseAuth.getInstance().getUid() + recID;
                         database.getReference().child("chats").child(sender).child(messageModel2.getMessageId()).setValue(null);


                     }
                 }).setNegativeButton("no", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
             dialogInterface.dismiss();
             }
         }).show();

         return false;
     }
 });

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

//    public  class ReciverViewHolder extends RecyclerView.ViewHolder {
//TextView receivertext,recivertime;
//        public ReciverViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//            receivertext=itemView.findViewById(R.id.recivertext);
//            recivertime=itemView.findViewById(R.id.recivertime);
//        }

        public  class ReciverViewVolder extends RecyclerView.ViewHolder {
            TextView receivertext,recivertime;
            public ReciverViewVolder(@NonNull @NotNull View itemView) {
                super(itemView);
                receivertext=itemView.findViewById(R.id.recivertext);
                recivertime=itemView.findViewById(R.id.recivertime);
            }

    }
    public  class SendViewHolder extends RecyclerView.ViewHolder {
        TextView sendtext,sendtime;
        public SendViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            sendtext=itemView.findViewById(R.id.sendtext);
            sendtime=itemView.findViewById(R.id.sendertime);

        }
    }
}
