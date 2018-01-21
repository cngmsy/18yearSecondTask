package com.example.musicservice;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class MusicListAdapter extends BaseAdapter {
	private Context context;
	private Handler handler;
	private LayoutInflater inflater;
	private ArrayList<Song> song_list;
	
	public MusicListAdapter(Context context,ArrayList<Song> song_list) {
		this.song_list=song_list;
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	public MusicListAdapter(Context context,Handler handler) {
		this.context=context;
		this.handler=handler;
		inflater=LayoutInflater.from(context);
	}
	
	public MusicListAdapter(Context context) {
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	public void setData(ArrayList<Song> song_list){
		this.song_list=song_list;
	}
	
	@Override
    public int getCount() {  
        return song_list.size();  
    }  

    public Object getItem(int position) {  
        return song_list.get(position);  
    }  

    public long getItemId(int position) {  
        return position;  
    }  

    /*
	 * 以下方法可以通知适配器重新刷新数据，而不用通知ACTIVITY更新
	 */
	public void updateData(ArrayList<Song> song_list){
		Log.i("MusicListAdapter:", "能够启动更新列表的方法");
		this.song_list=song_list;
		
		notifyDataSetChanged();
	}
	
    public View getView(final int position, View convertView, ViewGroup parent) {  
    	Holder holder;
    	final Song song=song_list.get(position);
        if (convertView == null) {  
            convertView = inflater.inflate(R.layout.local_music_list_item, null);  
            holder=new Holder(convertView);
            convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
        holder.songnum_tv.setText(""+(position+1));
        holder.songname_tv.setText(song.getSongName());
        holder.more_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Message msg=Message.obtain();
				msg.arg1=LocalMusicActivity.MUSIC_OPTIONS;
				msg.what=position;
				handler.sendMessage(msg);
			}
		
    });
        holder.song_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Message msg=Message.obtain();
				msg.arg1=LocalMusicActivity.MUSIC_PLAY;
				msg.what=position;
				handler.sendMessage(msg);
				
				Intent intent=new Intent(context,MusicService.class);
				intent.setAction(MusicService.ACTION_PLAY_LOCAL_MUSIC);
				intent.putExtra("position", position);
				context.startService(intent);
			}
		});
        return convertView;  
    }  
    
	
    
   class Holder{
	   public TextView songname_tv;
	   public TextView songnum_tv;
	   public ImageView more_img;
	   public LinearLayout song_layout;
	   public Holder(View view){
	   songname_tv=(TextView) view.findViewById(R.id.id_songname_tv);
	   songnum_tv=(TextView) view.findViewById(R.id.id_songnum_tv);
	   more_img=(ImageView) view.findViewById(R.id.id_more_img);
	   song_layout=(LinearLayout) view.findViewById(R.id.id_song_layout);
	   }
   }

}
