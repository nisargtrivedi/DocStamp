package com.docstamp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.docstamp.Activity_Home;
import com.docstamp.Activity_Main;
import com.docstamp.Activity_Year;
import com.docstamp.Model.tblFirm;
import com.docstamp.Model.tblFolder;
import com.docstamp.R;
import com.docstamp.Utils.TTextView;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class FirmAdapter extends  RecyclerView.Adapter<FirmAdapter.ViewHolder> {


    public List<tblFirm> list;
    Context context;
    public String name;


    public FirmAdapter(Context context, List<tblFirm> list){
        this.context=context;
        this.list=list;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TTextView FolderName,Files;
        public CardView menu;
        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            FolderName=view.findViewById(R.id.FolderName);
            Files=view.findViewById(R.id.Files);
            menu=view.findViewById(R.id.Menu);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("VIEW TYPE-------->",viewType+"");
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_firm, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // setFadeAnimation(holder.itemView);

        final tblFirm task = list.get(position);


        if(task!=null) {
            holder.FolderName.setText(task.FirmName);
            holder.img.setImageResource(task.Image);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(task.FirmID==1) {
//                        context.startActivity(new Intent(context, Activity_Home.class));
                        context.startActivity(new Intent(context, Activity_Year.class));
                    }
                    else
                        Toasty.info(context,"Coming Soon",1000,true).show();
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
