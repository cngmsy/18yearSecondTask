package com.example.musicservice;

import java.util.ArrayList;
import com.example.musicservice.MainActivity.TimeServiceReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OnlineMusicActivity extends Activity implements OnClickListener,OnSeekBarChangeListener{
	
	private DrawerLayout drawerLayout;
	private LinearLayout playAll_layout;
	private TextView songcounts_tv;
	private SeekBar seekBar;
	private ImageView circulateMode_img;
	private ImageView play_img;
	private ImageView next_img;
	private ImageView list_img;
	private TextView clearPlaylist_tv;
	private TextView totalTime_tv;
	private TextView currTime_tv;
	private TextView songname_tv;
	private TextView singer_tv;
	private SQLiteDatabase db;
	private String songUrl;
	private String currTime;
	private String totalTime;
	private String songname;
	private String singer;
	private int music_length;
	private int progress;
	private IntentFilter filter;
	private TimeServiceReceiver timeServiceReceiver;
	private ArrayList<Song> song_list;
	private ArrayList<Song> play_list=new ArrayList<Song>();
	private Song song;
	private Song song1;
	private Song song2;
	private Song song3;
	private Song song4;
	private Song song5;
	private Song song6;
	private Song song7;
	private Song song8;
	private Song song9;
	private Song song10;
	private ListView listview;
	private ListView playListView;
	private SonglistQueueAdapter adapter;
	private PlayListAdapter playListAdapter;
	public static OnlineMusicActivity onlineMusicActivity;
	private int MUSICPLAYER_STATE;
	public static final int PLAY_MUSIC=0;
	public static final int STORAGE_MUSIC=1;
	public static final int DOWNLOAD_MUSIC=2;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main_songlist_activity);
		initView();
		initEvent();
		init();
		LoadPlayListData();
	}

	private void init() {
		// TODO 自动生成的方法存根
		onlineMusicActivity=this;
		filter=new IntentFilter();					
		filter.addAction(MusicService.ACTION_UPDATE_TIME);	
		timeServiceReceiver=new TimeServiceReceiver();		
		registerReceiver(timeServiceReceiver, filter);
		db= openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists playerbarinfotb (_id integer primary key autoincrement, "
				+ "songname text not null,singer text not null,songurl text not null, seekbarmax integer not null,seekbarprogress integer not null,currtime text not null,totaltime text not null)");
		Cursor cursor=db.rawQuery("select * from playerbarinfotb where _id=1",null);
		if(cursor.moveToNext()){	
			Log.e("有正在播放的歌曲", ""+cursor.getString(cursor.getColumnIndex("songname")));	
			songname=cursor.getString(cursor.getColumnIndex("songname"));
			singer=cursor.getString(cursor.getColumnIndex("singer"));
			songUrl=cursor.getString(cursor.getColumnIndex("songurl"));
			currTime=cursor.getString(cursor.getColumnIndex("currtime"));
			totalTime=cursor.getString(cursor.getColumnIndex("totaltime"));
			progress=cursor.getInt(cursor.getColumnIndex("seekbarprogress"));
			music_length=cursor.getInt(cursor.getColumnIndex("seekbarmax"));
			songname_tv.setText(songname);
			singer_tv.setText(singer);
			currTime_tv.setText(currTime);
			totalTime_tv.setText(totalTime);
			seekBar.setMax(music_length);
			seekBar.setProgress(progress);
    		Log.e("有正在播放的歌曲", ""+cursor.getString(cursor.getColumnIndex("songname")));
    		
    	}else{																			
    		Log.e("没有未完成的播放任务", "");
    	}
    	cursor.close();
    
    	song_list=new ArrayList<Song>();
		song1=new Song("Long Way Home","Gareth Emery","http://192.168.0.101:8080/Json1/Long Way Home.mp3");
		song2=new Song("Lights","Elli Goulding","http://mp3.haoduoge.com/s/2016-09-12/1473684651.mp3");
		song3=new Song("Red Lights","Tiesto","http://192.168.0.101:8080/Json1/RedLights.mp3");
		song4=new Song("Summer On You","Sam feldt","http://192.168.0.101:8080/Json1/Summer On You.mp3");
		song5=new Song("The Ocean","Mike Posion","http://192.168.0.101:8080/Json1/TheOcean.mp3");
		song6=new Song("Middle","DJ Sanke","http://192.168.0.101:8080/Json1/Middle.mp3");
		song7=new Song("What We Started","Don Diablo & Steve Aoki","http://192.168.0.101:8080/Json1/WhatWeStarted.mp3");
		song8=new Song("Never Leave","DVBBS","http://192.168.0.101:8080/Json1/Never Leave.mp3");
		song9=new Song("Selfie","The Chainsmoker","http://192.168.0.101:8080/Json1/Selfie.mp3");
		song10=new Song("Show Me Love","Sam feldt","http://192.168.0.101:8080/Json1/Show Me Love.mp3");
		song_list.add(song1);
		song_list.add(song2);
		song_list.add(song3);
		song_list.add(song4);
		song_list.add(song5);
		song_list.add(song6);
		song_list.add(song7);
		song_list.add(song8);
		song_list.add(song9);
		song_list.add(song10);
		listview=(ListView) findViewById(R.id.id_listView);
		adapter=new SonglistQueueAdapter(this,song_list,musicOptionsHandler);
		listview.setAdapter(adapter);
		songcounts_tv.setText("共（"+song_list.size()+"）首");
		for(int i=0;i<song_list.size();i++){
			songname=song_list.get(i).getSongName();
			singer=song_list.get(i).getSinger();
			songUrl=song_list.get(i).getSongurl();
			cursor=db.rawQuery("select * from onlinemusictb where songurl=? and songname=?",new String[]{songUrl,songname} );//查询数据库里有没有这个路径
        	if(cursor.moveToNext()){														//索引能够移动说明有	
        		Log.i("已经添加", songUrl);													//所以就不用添加了
        	}else{																			//没有的话就添加
        	db.execSQL("insert into onlinemusictb(songurl,songname,singer) values('"+songUrl+"','"+songname+"','"+singer+"')");
        	System.out.println("添加网络歌曲"+songUrl);
        	}
        	
		}
		db.close();
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		drawerLayout=(DrawerLayout) findViewById(R.id.id_drawerlayout);
		playAll_layout=(LinearLayout) findViewById(R.id.id_playall_layout);
		songcounts_tv=(TextView) findViewById(R.id.id_songcounts_tv);
		seekBar=(SeekBar) findViewById(R.id.id_seekBar);
		circulateMode_img=(ImageView) findViewById(R.id.id_circulate_img);
		play_img=(ImageView) findViewById(R.id.id_play_img);
		next_img=(ImageView) findViewById(R.id.id_next_img);
		list_img=(ImageView) findViewById(R.id.id_list_img);
		clearPlaylist_tv=(TextView) findViewById(R.id.id_clear_tv);
		totalTime_tv=(TextView) findViewById(R.id.id_total_time);
		currTime_tv=(TextView) findViewById(R.id.id_curr_time);
		songname_tv=(TextView) findViewById(R.id.id_playing_songname_tv);
		singer_tv=(TextView) findViewById(R.id.id_playing_singer_tv);
		playListView=(ListView) findViewById(R.id.id_playlist_listview);
		switch (MusicService.MUSICPLAYER_CIRCULATE_MODE) {
		case MusicService.LIST_CIRCULATE:
			circulateMode_img.setImageResource(R.drawable.recyclered);
			break;
		case MusicService.SINGLE_REPEAT:
			circulateMode_img.setImageResource(R.drawable.singlerecyclered);
			break;
		case MusicService.SHAFFULE_PLAY:
			circulateMode_img.setImageResource(R.drawable.ramdomred);
			break;

		default:
			break;
		}
	}

	private void initEvent() {
		// TODO �Զ����ɵķ������
		playAll_layout.setOnClickListener(this);
		play_img.setOnClickListener(this);
		next_img.setOnClickListener(this);
		list_img.setOnClickListener(this);
		clearPlaylist_tv.setOnClickListener(this);
		circulateMode_img.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(this);
	}
	
	private Handler playListHander = new Handler() {  
		public void handleMessage(android.os.Message msg) {  
	        int position =msg.what;
	        Song song=play_list.get(position);
	        deleteFromPlayList(song);        	                     
	        } 
	    };	
	    
	    
	Handler musicOptionsHandler=new Handler(){
		public void handleMessage(android.os.Message msg) { 
			int position=msg.what;
			final Song song=song_list.get(position);
				switch (msg.arg1) {
				case PLAY_MUSIC:		
					addToPlayList(song);
					break;
				case STORAGE_MUSIC:
					System.out.println("收藏");
					break;
				case DOWNLOAD_MUSIC:
					
					break;
				default:
					break;
				}
				 
			}
		};
	    private void LoadPlayListData(){
			String sql="select * from playlisttb";
			db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
			Cursor cursor=db.rawQuery(sql, null);
			if(cursor!=null){
				while(cursor.moveToNext()){
					song=new Song();
					song.setKey(cursor.getInt(cursor.getColumnIndex("_id")));
		 			song.setSongurl(cursor.getString(cursor.getColumnIndex("songurl")));
		 			song.setSongName(cursor.getString(cursor.getColumnIndex("songname")));
		 			song.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
		 			Log.i("从SQLite获取播放列表歌曲", "songUrl:"+cursor.getString(cursor.getColumnIndex("songurl")));
		 			play_list.add(song);
				}
				cursor.close();
			}
			db.close();
			playListAdapter=new PlayListAdapter(this,playListHander);
			playListAdapter.setData(play_list);
			playListView.setAdapter(playListAdapter);
		}
	    
	    public class TimeServiceReceiver extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				int progress=intent.getIntExtra("progress", 0);
				int music_length=intent.getIntExtra("music_length", 0);
				int bufferPercent=intent.getIntExtra("bufferPercent", 0);
				String currTime=intent.getStringExtra("currTime");
				String totalTime=intent.getStringExtra("totalTime");
				String songname=intent.getStringExtra("songname");
				String singer=intent.getStringExtra("singer");
				MUSICPLAYER_STATE=intent.getIntExtra("MUSICPLAYER_STATE", 2);
				currTime_tv.setText(currTime);
				totalTime_tv.setText(totalTime);
				seekBar.setMax(music_length);
				seekBar.setProgress(progress);
				seekBar.setSecondaryProgress(bufferPercent);
				songname_tv.setText(songname);
				singer_tv.setText(singer);
				switch (MUSICPLAYER_STATE) {
				case MusicService.MUSICPLAYER_PLAYING:
					play_img.setImageResource(R.drawable.pausered);
					break;
				case MusicService.MUSICPLAYER_STOP:
					play_img.setImageResource(R.drawable.playred);
					break;
				case MusicService.MUSICPLAYER_PAUSED:
					play_img.setImageResource(R.drawable.playred);
					break;

				default:
					break;
				}
			}
			
		}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.id_playall_layout:
			if(song_list.size()==0){
				Toast.makeText(OnlineMusicActivity.this, "尚无本地歌曲，请点右上角扫描", 2).show();
			}else{
			Intent intent=new Intent(OnlineMusicActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_PLAY_ALL_LOCALMUSIC);
			startService(intent);
			addAllToPlaylist();
			}
			break;
		case R.id.id_play_img:
			Play();
			break;
		case R.id.id_circulate_img:
			circulateMode();
			break;
		case R.id.id_clear_tv:
			clearPlayList();
			break;
		case R.id.id_next_img:
			Next();
			break;
		case R.id.id_list_img:
			showList();
			break;
		default:
			break;
		}
	}
	
	
	private void circulateMode() {
		// TODO 自动生成的方法存根
		switch (MusicService.MUSICPLAYER_CIRCULATE_MODE) {
		case MusicService.LIST_CIRCULATE:
			circulateMode_img.setImageResource(R.drawable.singlerecyclered);
			MusicService.MUSICPLAYER_CIRCULATE_MODE=MusicService.SINGLE_REPEAT;
			break;
		case MusicService.SINGLE_REPEAT:
			circulateMode_img.setImageResource(R.drawable.ramdomred);
			MusicService.MUSICPLAYER_CIRCULATE_MODE=MusicService.SHAFFULE_PLAY;
			break;
		case MusicService.SHAFFULE_PLAY:	
			circulateMode_img.setImageResource(R.drawable.recyclered);
			MusicService.MUSICPLAYER_CIRCULATE_MODE=MusicService.LIST_CIRCULATE;
			break;
		default:
			break;
		}
		Intent intent=new Intent(OnlineMusicActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_CHANGE_CIRCULATE_MODE);
		startService(intent);
	}

	private void Play() {
		// TODO 自动生成的方法存根
		switch (MusicService.MUSICPLAYER_STATE) {
		case MusicService.MUSICPLAYER_STOP:
			Intent intent=new Intent(OnlineMusicActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_PLAY_CURR_MUSIC);
			startService(intent);
			play_img.setImageResource(R.drawable.pausered);
		case MusicService.MUSICPLAYER_PLAYING:
			Intent intent1=new Intent(OnlineMusicActivity.this,MusicService.class);
			intent1.setAction(MusicService.ACTION_PAUSE_PLAY);
			startService(intent1);
			play_img.setImageResource(R.drawable.playred);
			break;
		case MusicService.MUSICPLAYER_PAUSED:
			Intent intent2=new Intent(OnlineMusicActivity.this,MusicService.class);
			intent2.setAction(MusicService.ACTION_START_PLAY);
			startService(intent2);
			play_img.setImageResource(R.drawable.pausered);
			break;

		default:
			break;
		}
	}
	
	private void addAllToPlaylist(){
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		for(int i=0;i<song_list.size();i++){
			Song song=song_list.get(i);
			String songUrl=song.getSongurl();
			String songname=song.getSongName();
			String singer=song.getSinger();
			//db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
			Cursor cursor=db.rawQuery("select * from playlisttb where songurl=?",new String[]{songUrl} );//查询播放列表数据库里有没有这个路径
	    	if(cursor.moveToNext()){																//索引能够移动说明有	
	    		Log.e("已经存在", songUrl);															//所以就不用添加了
	    	}else{																			
	    		db.execSQL("insert into playlisttb(songurl,songname,singer) values('"+songUrl+"','"+songname+"','"+singer+"')");//没有就添加到播放列表数据库
	    		play_list.add(song);																							//添加至播放列表的歌曲集合
	    		//playListAdapter.updateData(play_list);
	    	}
	    	cursor.close();
		}
		db.close();
		playListAdapter.updateData(play_list);
	}

	private void Next() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(OnlineMusicActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY_NEXT);
		startService(intent);
	}

	private void showList() {
		// TODO 自动生成的方法存根
		drawerLayout.openDrawer(Gravity.RIGHT);	
	}

	private void more() {
		// TODO 自动生成的方法存根
		
	}
	
	private void clearPlayList() {
		// TODO 自动生成的方法存根
		
	}
	
	private void addToPlayList(Song song) {
		// TODO 自动生成的方法存根
		String songUrl=song.getSongurl();
		String songname=song.getSongName();
		String singer=song.getSinger();
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		Cursor cursor=db.rawQuery("select * from playlisttb where songurl=?",new String[]{songUrl} );//查询播放列表数据库里有没有这个路径
    	if(cursor.moveToNext()){																//索引能够移动说明有	
    		Log.e("已经存在", songUrl);															//所以就不用添加了
    	}else{																			
    		db.execSQL("insert into playlisttb(songurl,songname,singer) values('"+songUrl+"','"+songname+"','"+singer+"')");//没有就添加到播放列表数据库
    		play_list.add(song);																							//添加至播放列表的歌曲集合
    		playListAdapter.updateData(play_list);
    	}
    	cursor.close();
		db.close();
	}
	    
	private void deleteFromPlayList(Song song) {
		// TODO 自动生成的方法存根
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		String songUrl=song.getSongurl();
		Cursor cursor=db.rawQuery("select * from playlisttb where songurl=?",new String[]{songUrl} );//查询播放列表数据库里有没有这个路径
	    	if(cursor.moveToNext()){																//索引能够移动说明有	
	    		db.execSQL("delete from playlisttb where songurl=?",new String[]{songUrl});			//所以要删除	
	    	}else{																			
	    		Log.e("不存在", songUrl);															//没有就不用删除了																													
	    	}
	    	Log.i("删除前的播放列表size：", play_list.size()+"");
			play_list.remove(song);																//将歌曲从播放列表歌曲集合删除
			Log.i("删除前的播放列表size：", play_list.size()+"");
			playListAdapter.updateData(play_list);
			cursor.close();
			db.close();
		} 
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO 自动生成的方法存根
		if(fromUser){
			MusicService.mp.seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO 自动生成的方法存根
		
	}
}
