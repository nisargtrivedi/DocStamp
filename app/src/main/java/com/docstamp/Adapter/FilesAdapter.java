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

import com.docstamp.Activity_Details;
import com.docstamp.Activity_Year;
import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblFolder;
import com.docstamp.R;
import com.docstamp.Utils.DateUtils;
import com.docstamp.Utils.TTextView;

import java.util.List;


public class FilesAdapter extends  RecyclerView.Adapter<FilesAdapter.ViewHolder> {


    public List<tblFiles> list;
    Context context;
    public String name;


    public FilesAdapter(Context context, List<tblFiles> list){
        this.context=context;
        this.list=list;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TTextView FileName,FilesSize;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            FileName=view.findViewById(R.id.FileName);
            FilesSize=view.findViewById(R.id.FilesSize);
            menu=view.findViewById(R.id.menu);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("VIEW TYPE-------->",viewType+"");
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_files, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // setFadeAnimation(holder.itemView);

        final tblFiles task = list.get(position);

        if(task!=null) {
            holder.FileName.setText(task.DocumentTitle);
            holder.FilesSize.setText(task.FileSize +" Uploaded "+ DateUtils.DaysAgo(task.FileUploadTime));

        }
        if(!task.DownloadPath.isEmpty()) {
                if(task.DownloadPath.toLowerCase().endsWith("pdf")){
                    holder.img.setImageResource(R.drawable.ic_pdf);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("zip")){
                    holder.img.setImageResource(R.drawable.ic_zip);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("doc")){
                    holder.img.setImageResource(R.drawable.ic_doc);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("docx")){
                    holder.img.setImageResource(R.drawable.ic_doc);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("png")){
                    holder.img.setImageResource(R.drawable.ic_img);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("jpeg")){
                    holder.img.setImageResource(R.drawable.ic_img);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("jpg")){
                    holder.img.setImageResource(R.drawable.ic_img);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("xls")){
                    holder.img.setImageResource(R.drawable.xls);
                }
                else if(task.DownloadPath.toLowerCase().endsWith("xlsx")){
                    holder.img.setImageResource(R.drawable.xls);
                }
                else {
                    holder.img.setImageResource(R.drawable.files_ic);
                }
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, Activity_Details.class)
                                .putExtra("file_name",task.DocumentTitle)
                                .putExtra("file_size",task.FileSize)
                                .putExtra("path",task.DownloadPath)
                                .putExtra("date",task.FileUploadTime)

                        );
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
