package com.example.musicservice;

import java.io.File;
import java.util.ArrayList;

import com.example.musicservice.MainActivity.TimeServiceReceiver;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
public class LocalMusicActivity extends Activity implements OnClickListener,OnSeekBarChangeListener{
	private DrawerLayout drawerLayout;
	private Button back_btn;
	private ImageView more_img;
	private LinearLayout search_progress_layout;
	private LinearLayout search_finished_layout;
	private TextView search_progress_tv;
	private TextView search_finished_tv;
	private LinearLayout playAll_layout;
	private SeekBar seekBar;
	private ImageView circulateMode_img;
	private ImageView play_img;
	private ImageView next_img;
	private ImageView list_img;
	private TextView totalTime_tv;
	private TextView currTime_tv;
	private TextView songname_tv;
	private TextView singer_tv;
	private TextView songcount_tv;
	private TextView clearPlaylist_tv;
	private SQLiteDatabase db;
	private String songUrl;
	private String currTime;
	private String totalTime;
	private String songname;
	private String singer;
	private int music_length;
	private int progress;
	private Song song;
	private ArrayList<Song> song_list=new ArrayList<Song>();
	private ArrayList<Song> play_list=new ArrayList<Song>();
	private MusicListAdapter musicListAdapter;	//音乐列表适配器
	private PlayListAdapter playListAdapter;	//播放列表适配器
	private ListView localMusicListview;
	private ListView playListView;
	public static final int SEARCH_FINISHED=1;
	public static final int SEARCHING_MUSIC=2;
	public static final int MUSIC_OPTIONS=0;
	public static final int MUSIC_PLAY=1;
	public int WHETHER_LOAD_DATA;
	private int MUSICPLAYER_STATE;
	public static final int HAS_NOT_LOADED_LOCALMUSIC=0;
	public static final int HAD_LOADED_LOCALMUSIC=1;
	private IntentFilter filter;
	private TimeServiceReceiver timeServiceReceiver;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_music_activity);
		initView();
		initEvent();
		initPlayBar();
		LoadPlayListData();
	}
	
	private void initPlayBar() {
		// TODO 自动生成的方法存根
		db= openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists musictb (_id integer primary key autoincrement,"
				+ "songurl text not null,songname text not null,singer text not null)");
		db.execSQL("create table if not exists playerbarinfotb (_id integer primary key autoincrement, "
				+ "songname text not null,singer text not null,songurl text not null, seekbarmax integer not null,seekbarprogress integer not null,currtime text not null,totaltime text not null)");
		db.execSQL("create table if not exists loadlocalmusiclistviewtb (_id integer primary key autoincrement,load integer not null)");
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
    	cursor.close();
    	db.close();
	}
	
	private void initView() {
		// TODO �Զ����ɵķ������
		drawerLayout=(DrawerLayout) findViewById(R.id.id_drawerlayout);
		back_btn=(Button) findViewById(R.id.id_back_btn);
		more_img=(ImageView) findViewById(R.id.id_more_img);
		search_progress_layout=(LinearLayout) findViewById(R.id.id_search_progress_layout);
		search_finished_layout=(LinearLayout) findViewById(R.id.id_search_finished_layout);
		search_progress_tv=(TextView) findViewById(R.id.id_search_progress_tv);
		search_finished_tv=(TextView) findViewById(R.id.id_search_finished_tv);
		playAll_layout=(LinearLayout) findViewById(R.id.id_playall_layout);
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
		filter=new IntentFilter();					
		filter.addAction(MusicService.ACTION_UPDATE_TIME);	
		timeServiceReceiver=new TimeServiceReceiver();		
		registerReceiver(timeServiceReceiver, filter);
		localMusicListview=(ListView) findViewById(R.id.id_listView);
		playListView=(ListView) findViewById(R.id.id_playlist_listview);
		songcount_tv=(TextView) findViewById(R.id.id_songcounts_tv);
		db= openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists loadlocalmusiclistviewtb (_id integer primary key autoincrement,load integer not null)");
		Cursor cursor=db.rawQuery("select * from loadlocalmusiclistviewtb",null);
		if(cursor.moveToNext()){		
			WHETHER_LOAD_DATA=cursor.getInt(cursor.getColumnIndex("_id"));
			System.out.println("是否已经载入"+WHETHER_LOAD_DATA);
			switch (WHETHER_LOAD_DATA) {
			case HAS_NOT_LOADED_LOCALMUSIC:
				
				break;
			case HAD_LOADED_LOCALMUSIC:
				System.out.println("已经搜索过本地音乐");
				Cursor c=db.rawQuery("select * from localmusictb", null);
				while(c.moveToNext()){
					Song song=new Song();
					song.setSongName(c.getString(c.getColumnIndex("songname")));
					song.setSinger(c.getString(c.getColumnIndex("singer")));
					song.setSongurl(c.getString(c.getColumnIndex("songurl")));
					song_list.add(song);
				}
				c.close();
				search_progress_layout.setVisibility(View.GONE);
            	//search_finished_layout.setVisibility(View.VISIBLE);
            	musicListAdapter=new MusicListAdapter(this,musicOptionsHandler);
        	 	musicListAdapter.setData(song_list);
        	 	localMusicListview.setAdapter(musicListAdapter);
        	 	songcount_tv.setText("（共"+song_list.size()+"首）");
				break;
			default:
				break;
			}
    	}
    	cursor.close();
    	db.close();
    	
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
		back_btn.setOnClickListener(this);
		more_img.setOnClickListener(this);
		playAll_layout.setOnClickListener(this);
		circulateMode_img.setOnClickListener(this);
		play_img.setOnClickListener(this);
		next_img.setOnClickListener(this);
		list_img.setOnClickListener(this);
		clearPlaylist_tv.setOnClickListener(this);
		search_finished_tv.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.id_back_btn:
			back();
			break;
		case R.id.id_playall_layout:
			if(song_list.size()==0){
				Toast.makeText(LocalMusicActivity.this, "尚无本地歌曲，请点右上角扫描", 2).show();
			}else{
			Intent intent=new Intent(LocalMusicActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_PLAY_ALL_LOCALMUSIC);
			startService(intent);
			addAllToPlaylist();
			}
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
		case R.id.id_more_img:
			more();
			break;
		case R.id.id_search_finished_tv:					//点击“搜索完成”按钮，按钮消失,并加载数据到listview中
			search_finished_layout.setVisibility(View.GONE);
			LoadData();
			break;
		default:
			break;
		}
	}
	private void LoadData() {
		// TODO 自动生成的方法存根
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		String sql="select * from localmusictb";
		Cursor cursor=db.rawQuery(sql, null);
		if (cursor!=null) {
	 		while (cursor.moveToNext()) {
	 			song=new Song();
	 			song.setKey(cursor.getInt(cursor.getColumnIndex("_id")));
	 			song.setSongurl(cursor.getString(cursor.getColumnIndex("songurl")));
	 			song.setSongName(cursor.getString(cursor.getColumnIndex("songname")));
	 			song.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
	 			Log.i("从SQLite获取歌曲", "songUrl:"+cursor.getString(cursor.getColumnIndex("songurl")));
	 			
				song_list.add(song);
			}
	 		cursor.close();
	 	}
	 	db.close();
	 	musicListAdapter=new MusicListAdapter(this,musicOptionsHandler);
	 	musicListAdapter.setData(song_list);
	 	localMusicListview.setAdapter(musicListAdapter);
	 	songcount_tv.setText("（共"+song_list.size()+"首）");
	 	
	 	hadLoadList();
	}
	
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
	/*
	 * 该方法可以向本地载入一个数据
	 */
	private void hadLoadList() {
		// TODO 自动生成的方法存根
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		//db.rawQuery("select * from loadlocalmusiclistviewtb", null);
		db.execSQL("insert into loadlocalmusiclistviewtb (load) values('"+HAD_LOADED_LOCALMUSIC+"')");
		//ContentValues values=new ContentValues();
		
		//db.insert("loadlocalmusiclistviewtb", "number", values);
		db.close();
		System.out.println("已保存载入记录，下次进入该页面将直接载入数据");
	}

	
	private void back(){
		
	}
	/*
	 * 将本地列表的所有歌曲都添加到播放列表中
	 */
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
	
	private void clearPlayList() {
		// TODO 自动生成的方法存根
		
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
		Intent intent=new Intent(LocalMusicActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_CHANGE_CIRCULATE_MODE);
		startService(intent);
	}

	private void Play() {
		// TODO 自动生成的方法存根
		switch (MusicService.MUSICPLAYER_STATE) {
		case MusicService.MUSICPLAYER_STOP:
			Intent intent=new Intent(LocalMusicActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_PLAY_CURR_MUSIC);
			startService(intent);
			play_img.setImageResource(R.drawable.pausered);
		case MusicService.MUSICPLAYER_PLAYING:
			Intent intent1=new Intent(LocalMusicActivity.this,MusicService.class);
			intent1.setAction(MusicService.ACTION_PAUSE_PLAY);
			startService(intent1);
			play_img.setImageResource(R.drawable.playred);
			break;
		case MusicService.MUSICPLAYER_PAUSED:
			Intent intent2=new Intent(LocalMusicActivity.this,MusicService.class);
			intent2.setAction(MusicService.ACTION_START_PLAY);
			startService(intent2);
			play_img.setImageResource(R.drawable.pausered);
			break;

		default:
			break;
		}
	}

	private void Next() {
		// TODO 自动生成的方法存根
		Intent intent=new Intent(LocalMusicActivity.this,MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY_NEXT);
		startService(intent);
	}

	private void showList() {
		// TODO 自动生成的方法存根
		drawerLayout.openDrawer(Gravity.RIGHT);	
	}

	private void more() {
		// TODO 自动生成的方法存根
		String []options={"搜索本地音乐","清空播放列表"};
		Builder builder=new AlertDialog.Builder(LocalMusicActivity.this);
		builder.setTitle("歌曲选项操作");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				switch (which) {
				case 0:
					searchLocalMusic();
					break;
				case 1:
					
					break;
				default:
					break;
				}
			}

		}).show();
	}
	
	private void searchLocalMusic() {
		// TODO 自动生成的方法存根
		db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
		new SearchLocalMusicThread(searchMusicHandler,db).start();
		search_progress_layout.setVisibility(View.VISIBLE);
	}
	
	Handler searchMusicHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {  
			 switch (msg.what) {  
			 		/*
			 	 	 *handler接收到发送过来搜索完成的消息后
			 	 	 *显示搜索进度的layout设置为不可见
			 	 	 *显示搜索完成的layout显示为可见 
			 		 */
	            case SEARCH_FINISHED:  
	            	search_progress_layout.setVisibility(View.GONE);
	            	search_finished_layout.setVisibility(View.VISIBLE);
	                break;  
	                
	                /*
	            	 * handler接收到发送过来的正在搜索的文件夹
	            	 * 将其显示在搜索进度文本框内
	            	 */
	            case SEARCHING_MUSIC:  
	            	String searchingPath=msg.obj.toString();
	            	search_progress_tv.setText(searchingPath);
	                break;  
	            default:  
	                break;  
	            }  
            }  
     
	};
	Handler musicOptionsHandler=new Handler(){
		public void handleMessage(android.os.Message msg) { 
			int position=msg.what;
			final Song song=song_list.get(position);
			switch (msg.arg1) {
			case MUSIC_OPTIONS:
		       
		        String songname=song.getSongName();
		        String singer=song.getSinger();
		        final String songpath=song.getFilePath();
		        final String[] optionlist=new String []{"收藏","评论","添加到播放列表","收藏歌手"+singer,"删除"};
		        Builder builder=new AlertDialog.Builder(LocalMusicActivity.this);
				builder.setTitle("歌曲："+songname);
				builder.setItems(optionlist, new DialogInterface.OnClickListener() {
									
					@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					switch (which) {
					case 0:
						//storage(song);
						System.out.println("收藏");
						break;
					case 1:
						System.out.println("评论");
						break;
					case 2:
						addToPlayList(song);					//添加至播放列表
						break;
					case 3:
						System.out.println("歌手");
						break;
					case 4:
						deleteSong(song);
						//通知Service更新Service里面的playlist
						
						
						break;
					default:
						break;
					}
				}
				});
					builder.create().show();	
				break;
			case MUSIC_PLAY:
				addToPlayList(song);
				break;
			default:
				break;
			}
			 
		}
	};
	
	
	
	private Handler playListHander = new Handler() {  
		public void handleMessage(android.os.Message msg) {  
	        int position =msg.what;
	        Song song=play_list.get(position);
	        deleteFromPlayList(song);        	                     
	        } 
	    };	
	    
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
	    
	    /*
		 * 将歌曲从播放列表删除
		 */
		private void deleteFromPlayList(Song song) {
			// TODO 自动生成的方法存根
			db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
			String songUrl=song.getSongurl();
			Intent intent=new Intent(LocalMusicActivity.this,MusicService.class);
			intent.setAction(MusicService.ACTION_DELETE_PLAYLIST_MUSIC);
			intent.putExtra("songUrl", songUrl);
			startService(intent);
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
	
		private void deleteSong(Song song) {
			// TODO 自动生成的方法存根
			String songUrl=song.getSongurl();
			String songname=song.getSongName();
			String singer=song.getSinger();
			db=openOrCreateDatabase("music.db", MODE_PRIVATE, null);
			Cursor cursor=db.rawQuery("select * from localmusictb where songurl=?",new String[]{songUrl} );//查询数据库里有没有这个路径
	    	if(cursor.moveToNext()){														//索引能够移动说明有	
	    		db.execSQL("delete from localmusictb where songurl=?",new String[]{songUrl});	//在播放列表数据库中删除歌曲信息
	    		System.out.println("删除本地音乐列表里的相关歌曲数据");
	    		song_list.remove(song);//在歌曲列表适配器的数据源中删除歌曲信息
	    		System.out.println("删除本地歌曲List中的数据");
	    		musicListAdapter.updateData(song_list);									//通知适配器更新
	    		songcount_tv.setText("（共"+song_list.size()+"首）");
	    		Cursor cursor2=db.rawQuery("select * from playlisttb where songurl=?", new String[]{songUrl} );
	    		if(cursor2.moveToNext()){
	    			db.execSQL("delete from playlisttb where songurl=?",new String[]{songUrl} );
	    			System.out.println("删除播放列表里的相关歌曲数据");
	    		}
	    		play_list.clear();
	    		LoadPlayListData();
	    		cursor2.close();
	    		File file=new File(songUrl);
	    		file.delete();		//删除音频文件
	    		System.out.println("删除音频文件");
	    		Intent intent=new Intent(LocalMusicActivity.this,MusicService.class); 	//通知Service更新
	    		intent.setAction(MusicService.ACTION_DELETE_LOCALMUSIC);
	    		startService(intent);
	    	}else{																			
	    		Log.e("error", "路径不存在！");													//没有的话就打印错误日志
	    	}
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
