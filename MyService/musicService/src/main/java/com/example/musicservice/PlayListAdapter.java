package com.example.musicservice;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Song> play_list;
	Handler handler;

	public PlayListAdapter(Context context,Handler handler) {
		this.context=context;
		this.handler=handler;
		inflater=LayoutInflater.from(context);
	}
	public void setData(ArrayList<Song> play_list){
		this.play_list=play_list;
	}
	
	@Override
    public int getCount() {  
        return play_list.size();  
    }  

    public Object getItem(int position) {  
        return play_list.get(position);  
    }  

    public long getItemId(int position) {  
        return position;  
    }  
    /*
	* 以下方法可以通知适配器重新刷新数据，而不用通知ACTIVITY更新
	*/
 	public void updateData(ArrayList<Song> play_list){
 		Log.i("MusicListAdapter:", "能够启动更新列表的方法");
 		this.play_list=play_list;
 		notifyDataSetChanged();
 	}

    public View getView(final int position, View convertView, ViewGroup parent) {  
    	Holder holder;
    	
        if (convertView == null) {  
            convertView = inflater.inflate(R.layout.playlist_item, null);  
            holder=new Holder(convertView);
            convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
        Song song=play_list.get(position);
        holder.songnum_tv.setText(""+(position+1));
        holder.songname_tv.setText(song.getSongName());
        holder.delete_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Message msg=Message.obtain();
				msg.what=position;
				handler.sendMessage(msg);
			}
		});
        holder.song_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(context,MusicService.class);
				intent.setAction(MusicService.ACTION_PLAY_PLAYLIST_MUSIC);
				intent.putExtra("position", position);
				
				context.startService(intent);
			}
		});
   
        return convertView;  
    }  
    
   class Holder{
	   public TextView songnum_tv;
	   public TextView songname_tv;
	   public ImageView delete_img;
	   public LinearLayout song_layout;
	   public Holder(View view){
		   songnum_tv=(TextView) view.findViewById(R.id.id_songnum_tv);
		   songname_tv=(TextView) view.findViewById(R.id.id_songname_tv);
		   delete_img=(ImageView) view.findViewById(R.id.id_delete_img);
		   song_layout=(LinearLayout) view.findViewById(R.id.id_song_layout);
	   }
   }

}