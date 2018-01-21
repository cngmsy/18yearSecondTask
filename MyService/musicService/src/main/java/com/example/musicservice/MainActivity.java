package com.example.musicservice;


import java.net.Inet4Address;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private DrawerLayout drawerLayout;
	private Button localMusic_btn;
	private Button onlineMusic_btn;
	private Button startService_btn;
	private Button stopService_btn;
	private SeekBar seekBar;
	private ImageView play_img;
	private ImageView next_img;
	private ImageView list_img;
	private TextView totalTime_tv;
	private TextView currTime_tv;
	private TextView songname_tv;
	private TextView singer_tv;
	private TextView clearPlaylist_tv;
	private SQLiteDatabase db;
	private String songUrl;
	private String currTime;
	private String totalTime;
	private String songname;
	private String singer;
	private ImageView circulateMode_img;
	private int music_length;
	private int progress;
	private IntentFilter filter;
	private TimeServiceReceiver timeServiceReceiver;
	private ArrayList<Song> play_list=new ArrayList<Song>();
	private Song song;
	private PlayListAdapter playListAdapter;
	private ListView playListView;
	private int MUSICPLAYER_STATE;
	private int MUSICPLAYER_CIRCULATE_MODE;
	
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main_activity);
		initView();
		initEvent();
		initSeekBar();
		LoadPlayListData();
	}

	private void initSeekBar() {
		// TODO 自动生成的方法存根
		Log.i("MainActivity56", "");
		System.out.println("MainActivity56");
		Toast.makeText(MainActivity.this,"MainActivity56" , 3).show();
		db= openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists playerbarinfotb (_id integer primary key autoincrement, "
				+ "songname text not null,singer text not null,songurl text not null, seekbarmax integer not null,seekbarprogress integer not null,currtime text not null,totaltime text not null,circulate text not null)");
		//建立一个本地歌曲列表
		db.execSQL("create table if not exists localmusictb (_id integer primary key autoincrement,"
				+ "songurl text not null,songname text not null,singer text not null)");
		//建立一个网络歌曲列表
		db.execSQL("create table if not exists onlinemusictb (_id integer primary key autoincrement,"
				+ "songurl text not null,songname text not null,singer text not null)");
		//建立一个播放列表
		db.execSQL("create table if not exists playlisttb (_id integer primary key autoincrement,"
				+ "songurl text not null,songname text not null,singer text not null)");
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
			MUSICPLAYER_CIRCULATE_MODE=cursor.getInt(cursor.getColumnIndex("circulate"));
			switch (MUSICPLAYER_CIRCULATE_MODE) {
			case MusicService.LIST_CIRCULATE:
				circulateMode_img.setImageResource(R.drawable.recyclered);
				System.out.println("循环模式为列表循环"+MusicService.MUSICPLAYER_CIRCULATE_MODE);
				break;
			case MusicService.SINGLE_REPEAT:
				circulateMode_img.setImageResource(R.drawable.singlerecyclered);
				System.out.println("循环模式为单曲循环"+MusicService.MUSICPLAYER_CIRCULATE_MODE);
				break;
			case MusicService.SHAFFULE_PLAY:
				circulateMode_img.setImageResource(R.drawable.ramdomred);
				System.out.println("循环模式为随机播放"+MusicService.MUSICPLAYER_CIRCULATE_MODE);
				break;
			default:
				break;
			}
			MusicService.MUSICPLAYER_CIRCULATE_MODE=MUSICPLAYER_CIRCULATE_MODE;
			songname_tv.setText(songname);
			singer_tv.setText(singer);
			currTime_tv.setText(currTime);
			totalTime_tv.setText(totalTime);
			seekBar.setMax(music_length);
			seekBar.setProgress(progress);
			System.out.println(cursor.getString(cursor.getColumnIndex("songname")));	
    	}else{																			
    		ContentValues values=new ContentValues();
    		values.put("songname", "songname");
    		values.put("singer", "singer");
    		values.put("songurl", "songUrl");
    		values.put("seekbarmax", 0);
    		values.put("seekbarprogress", 100);
    		values.put("currtime", "00：00");
    		values.put("totaltime","00:00");
    		values.put("circulate", MusicService.LIST_CIRCULATE);
    		//db.update("playerbarinfotb", values, "_id=?", new String[]{"1"});
    		db.insert("playerbarinfotb", null, values);
    		values.clear();
    		db.close();
    		MusicService.MUSICPLAYER_CIRCULATE_MODE=MusicService.LIST_CIRCULATE;
    		circulateMode_img.setImageResource(R.drawable.recyclered);
    	}
    	cursor.close();
    	db.close();
	}

	private void initView() {
		// TODO 自动生成的方法存根
		drawerLayout=(DrawerLayout) findViewById(R.id.id_drawerlayout);
		localMusic_btn=(Button) findViewById(R.id.id_Localmusic);
		onlineMusic_btn=(Button) findViewById(R.id.id_onlinemusic);
		startService_btn=(Button) findViewById(R.id.id_service);
		stopService_btn=(Button) findViewById(R.id.id_stop_service);
		seekBar=(SeekBar) findViewById(R.id.id_seekBar);
		circulateMode_img=(ImageView) findViewById(R.id.id_circulate_img);
		play_img=(ImageView) findViewById(R.id.id_play_img);
		next_img=(ImageView) findViewById(R.id.id_next_img);
		list_img=(ImageView) findViewById(R.id.id_list_img);
		totalTime_tv=(TextView) findViewById(R.id.id_total_time);
		currTime_tv=(TextView) findViewById(R.id.id_curr_time);
		songname_tv=(TextView) findViewById(R.id.id_playing_songname_tv);
		singer_tv=(TextView) findViewById(R.id.id_playing_singer_tv);
		playListView=(ListView) findViewById(R.id.id_playlist_listview);
		clearPlaylist_tv=(TextView) findViewById(R.id.id_clear_tv);
		filter=new IntentFilter();					
		filter.addAction(MusicService.ACTION_UPDATE_TIME);	
		timeServiceReceiver=new TimeServiceReceiver();		
		registerReceiver(timeServiceReceiver, filter);
	}

	private void initEvent() {
		// TODO 自动生成的方法存根
		localMusic_btn.setOnClickListener(this);
		onlineMusic_btn.setOnClickListener(this);
		startService_btn.setOnClickListener(this);
		stopService_btn.setOnClickListener(this);
		circulateMode_img.setOnClickListener(this);
		play_img.setOnClickListener(this);
		next_img.setOnClickListener(this);
		list_img.setOnClickListener(this);
		clearPlaylist_tv.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(this);
		Log.i("MainActivity109", "执行完毕");
		System.out.println("MainActivity109");
	}
	
	private void LoadPlayListData(){
		play_list.clear();
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
		
			progress=intent.getIntExtra("progress", 0);
			music_length=intent.getIntExtra("music_length", 0);
			int bufferPercent=intent.getIntExtra("bufferPercent", 0);
			currTime=intent.getStringExtra("currTime");
			totalTime=intent.getStringExtra("totalTime");
			songname=intent.getStringExtra("songname");
			singer=intent.getStringExtra("singer");
			seekBar.setMax(music_length);
			seekBar.setProgress(progress);
			MUSICPLAYER_STATE=intent.getIntExtra("MUSICPLAYER_STATE",-1);
			switch (MUSICPLAYER_STATE) {
			case MusicService.MUSICPLAYER_STOP:
				play_img.setImageResource(R.drawable.playred);
				break;
			case MusicService.MUSICPLAYER_PAUSED:
				play_img.setImageResource(R.drawable.playred);
				break;
			case MusicService.MUSICPLAYER_PLAYING:
				play_img.setImageResource(R.drawable.pausered);
				break;
			default:
				break;
			}
			songname_tv.setText(songname);
			singer_tv.setText(singer);
			currTime_tv.setText(currTime);
			totalTime_tv.setText(totalTime);
			seekBar.setMax(music_length);
			seekBar.setProgress(progress);
			seekBar.setSecondaryProgress(bufferPercent);
			
		}
		
	}
	
	private Handler playListHander = new Handler() {  
		public void handleMessage(android.os.Message msg) {  
	        int position =msg.what;
	        Song song=play_list.get(position);
	        deleteFromPlayList(song);        	                     
	        } 
	    };	
	    
	    
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
			//通知MusicService 作判断，看是否需要更改下一首待播歌曲
			Intent intent=new Intent(MainActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_DELETE_PLAYLIST_MUSIC);
			intent.putExtra("songUrl", songUrl);
			startService(intent);
			
		} 
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.id_Localmusic:
			toLocalMusic();
			break;
		case R.id.id_onlinemusic:
			toOnlineMusic();
			break;
		case R.id.id_circulate_img:
			circulateMode();
			break;
		case R.id.id_play_img:
			Play();
			break;
		case R.id.id_next_img:
			Next();
			break;
		case R.id.id_list_img:
			showList();
			break;
		case R.id.id_clear_tv:
			clearPlayList();
			break;
		case R.id.id_service:
			serviceStart();
			break;
		case R.id.id_stop_service:
			serviceStop();
			break;
		default:
			break;
		}
	}


	private void clearPlayList() {
		// TODO 自动生成的方法存根
		//清空当前Activity中播放列表的List
		play_list.clear();
		playListAdapter.updateData(play_list);
		//清空sqlite数据库中的播放列表歌曲数据
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		db.execSQL("delete * from playlisttb");
		//通知Service删除Service里面的播放列表的List
		Intent intent=new Intent(MainActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_CLEAR_ALL_PLAYLIST);
		startService(intent);
	}

	private void serviceStop() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(MainActivity.this,MusicService.class);
		//intent.setAction("COM.EXAMPLE.MUSICSERVICE.MUSICSERVICE");
		System.out.println("can be called");
		stopService(intent);
	}

	private void serviceStart() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(MainActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_TEST);
		System.out.println("can be called");
		startService(intent);
	}

	private void showList() {
		// TODO 自动生成的方法存根
		drawerLayout.openDrawer(Gravity.RIGHT);	
	}

	private void Next() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(MainActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY_NEXT);
		startService(intent);
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
		Intent intent=new Intent(MainActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_CHANGE_CIRCULATE_MODE);
		startService(intent);
	}

	private void Play() {
		// TODO 自动生成的方法存根
		switch (MusicService.MUSICPLAYER_STATE) {
		case MusicService.MUSICPLAYER_STOP:
			Intent intent=new Intent(MainActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_PLAY_CURR_MUSIC);
			startService(intent);
			play_img.setImageResource(R.drawable.pausered);
			break;
		case MusicService.MUSICPLAYER_PLAYING:
			Intent intent1=new Intent(MainActivity.this,MusicService.class);
			intent1.setAction(MusicService.ACTION_PAUSE_PLAY);
			startService(intent1);
			play_img.setImageResource(R.drawable.playred);
			break;
		case MusicService.MUSICPLAYER_PAUSED:
			Intent intent2=new Intent(MainActivity.this,MusicService.class);
			intent2.setAction(MusicService.ACTION_START_PLAY);
			startService(intent2);
			play_img.setImageResource(R.drawable.pausered);
			break;

		default:
			break;
		}
	
	}

	private void toOnlineMusic() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(MainActivity.this,OnlineMusicActivity.class);
		startActivity(intent);
	}

	private void toLocalMusic() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(MainActivity.this,LocalMusicActivity.class);
		startActivity(intent);
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
	
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		System.out.println("恢复到前台,重新载入播放列表数据和");
		switch (MUSICPLAYER_STATE) {
		case MusicService.MUSICPLAYER_STOP:
			play_img.setImageResource(R.drawable.playred);
			break;
		case MusicService.MUSICPLAYER_PAUSED:
			play_img.setImageResource(R.drawable.playred);
			break;
		case MusicService.MUSICPLAYER_PLAYING:
			play_img.setImageResource(R.drawable.pausered);
			break;
		default:
			break;
		}
		LoadPlayListData();
		
		super.onResume();
	}
}
