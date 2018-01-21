package com.example.musicservice;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.content.res.Resources;


public class SonglistQueueAdapter extends BaseAdapter {
	private static final String SDCARD = Environment.getExternalStorageDirectory().getPath();//下载路径
	private ArrayList<Song> song_list;
	int num;
	private Context context;
	private LayoutInflater inflater;
	public String url;
	public String coverUrl;
	private String songName;
	private String singer;
	private String songUrl;
	private Handler handler;
	
	public SonglistQueueAdapter(Context context,ArrayList<Song> song_list,Handler handler) {
		this.song_list=song_list;
		this.context=context;
		this.handler=handler;
		System.out.println(song_list.size());
		inflater=LayoutInflater.from(context);
		// TODO �Զ����ɵĹ��캯�����?
	}
	public SonglistQueueAdapter(Context context){
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	
	public void setData(ArrayList<Song> song_list){
		this.song_list=song_list;
	}
	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������?
		return song_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO �Զ����ɵķ������?
		return song_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO �Զ����ɵķ������?
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������?
		SongHolder songHolder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.songinfo ,null);
			songHolder=new SongHolder(convertView);
			convertView.setTag(songHolder);
		}else{
			songHolder=(SongHolder) convertView.getTag();
		}
		final Song song=song_list.get(position);
		songHolder.songname.setText(song.getSongName());
		songHolder.singer.setText(song.getSinger());
		songHolder.songnum.setText(Integer.toString(position+1)); 
		songHolder.songlinearlayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			//OnlineMusicActivity.onlineMusicActivity.isPlaying=MainActivity.mainActivity.MUSIC_PLAYING;
			//OnlineMusicActivity.onlineMusicActivity.play_img.setImageResource(R.drawable.pausered);
				Message msg=Message.obtain();
				msg.arg1=OnlineMusicActivity.PLAY_MUSIC;
				msg.what=position;
				handler.sendMessage(msg);
				Intent intent=new Intent(OnlineMusicActivity.onlineMusicActivity,MusicService.class);
				intent.setAction(MusicService.ACTION_PLAY_ONLINE_MUSIC);
				intent.putExtra("position", position);
			/*
			intent.putExtra("songname", song.getSongName());
			intent.putExtra("singer", song.getSinger());
			intent.putExtra("songUrl", song.getSongurl());
			*/
				OnlineMusicActivity.onlineMusicActivity.startService(intent);
			}
		});
		
		return convertView;
	}
	
	

}