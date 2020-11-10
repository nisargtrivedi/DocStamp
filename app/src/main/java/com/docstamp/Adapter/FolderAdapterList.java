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
import android.widget.RelativeLayout;

import com.docstamp.Activity_Files;
import com.docstamp.Activity_Year;
import com.docstamp.Model.tblFolder;
import com.docstamp.R;
import com.docstamp.Utils.TTextView;

import java.util.List;


public class FolderAdapterList extends  RecyclerView.Adapter<FolderAdapterList.ViewHolder> {


    public List<tblFolder> list;
    Context context;
    public String name;
    public String YearID,YearName;

    public FolderAdapterList(Context context, List<tblFolder> list,String YearID,String YearName){
        this.context=context;
        this.list=list;
        this.YearID=YearID;
        this.YearName=YearName;
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
            menu=view.findViewById(R.id.menu);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("VIEW TYPE-------->",viewType+"");
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_folder_two, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // setFadeAnimation(holder.itemView);

        final tblFolder task = list.get(position);


        if(task!=null) {
            holder.FolderName.setText(task.FolderName);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Activity_Files.class)
                                    .putExtra("yearid",YearID+"")
                                    .putExtra("yeartitle",YearName)
                                    .putExtra("folderid",task.FolderID+"")
                                    .putExtra("foldername",task.FolderName));
                    //context.startActivity(new Intent(context, Activity_Year.class).putExtra("foldername",task.FolderName).putExtra("folderid",task.FolderID+""));
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
