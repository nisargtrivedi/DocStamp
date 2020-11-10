package com.docstamp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.docstamp.Activity_Details;
import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblNotification;
import com.docstamp.R;
import com.docstamp.Utils.DateUtils;
import com.docstamp.Utils.TTextView;

import java.util.List;


public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    public List<tblNotification> list;
    Context context;
    public String name;


    public NotificationAdapter(Context context, List<tblNotification> list){
        this.context=context;
        this.list=list;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        public TTextView Name;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);
            Name=view.findViewById(R.id.Name);
            menu=view.findViewById(R.id.menu);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("VIEW TYPE-------->",viewType+"");
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_notification, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // setFadeAnimation(holder.itemView);

        final tblNotification task = list.get(position);

        if(task!=null) {
            holder.Name.setText(task.NotificationText);
        }


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}
