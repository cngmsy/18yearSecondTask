package com.example.musicservice;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongHolder {
   public LinearLayout songlinearlayout;
   public TextView songnum;
   public ImageView storage_img;
   public ImageView download;
   public ImageView songmv;
   public TextView description;
   public TextView songname;
   public TextView singer;
   public TextView album;
   public ImageView download_img;
   public  SongHolder (View view){
      songlinearlayout=(LinearLayout) view.findViewById(R.id.id_songlinear_layout);
      songnum=(TextView) view.findViewById(R.id.id_song_num_tv);
      songname=(TextView) view.findViewById(R.id.id_songname_tv);
      singer=(TextView) view.findViewById(R.id.id_singer_tv);
      download_img=(ImageView) view.findViewById(R.id.id_download_img);
      
   }
}
