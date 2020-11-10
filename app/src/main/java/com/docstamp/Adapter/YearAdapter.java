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

import com.docstamp.Activity_Files;
import com.docstamp.Activity_Home;
import com.docstamp.Model.tblFolder;
import com.docstamp.Model.tblYear;
import com.docstamp.R;
import com.docstamp.Utils.AppPreferences;
import com.docstamp.Utils.TTextView;

import java.util.List;


public class YearAdapter extends  RecyclerView.Adapter<YearAdapter.ViewHolder> {


    public List<tblYear> list;
    Context context;
    public String name,FolderName,FolderID;

    AppPreferences appPreferences;

    public YearAdapter(Context context, List<tblYear> list){
        this.context=context;
        this.list=list;
        appPreferences=new AppPreferences(context);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TTextView YearName;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);

            YearName=view.findViewById(R.id.YearName);
            menu=view.findViewById(R.id.menu);


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.year_row, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // setFadeAnimation(holder.itemView);

        final tblYear task = list.get(position);

        if(task!=null) {
            holder.YearName.setText(task.YearName);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    context.startActivity(new Intent(context, Activity_Home.class)
                            .putExtra("yearid",task.YearID).
                            putExtra("yearname",task.YearName));

                    appPreferences.set("YEAR_TITLE",task.YearName);


                }
            });
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
