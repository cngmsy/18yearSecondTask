package com.example.musicservice;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

public class SearchLocalMusicThread extends Thread {
	private Handler handler;
	private String songname;
	private String singer;
	private String songurl;
	private String filename;
	private SQLiteDatabase db;
	String[] ext = { ".mp3" };  
    File file = Environment.getExternalStorageDirectory();  
    private ArrayList<Song> songInfos=new ArrayList<Song>();
    private Cursor cursor;
	public SearchLocalMusicThread(Handler handler,SQLiteDatabase db){
		this.handler=handler;
		this.db=db;
	}
	
	public void run(){
		Log.i("SearchLocalMusicThread 31:", "线程已启动");
		//在run()方法里调用search方法去进行音乐文件的搜索
		search(file,ext);
		//搜索完毕，发送一个消息给LocalMusic
		sendMessage();
		
	}

	private void sendMessage() {
		// TODO �Զ����ɵķ������
		handler.sendEmptyMessage(LocalMusicActivity.SEARCH_FINISHED);
	}

	private void search(File file, String[] ext) {
		// TODO �Զ����ɵķ������
		        if (file != null) { 
		            if (file.isDirectory()) { 
		            	String searchingPath=file.getAbsolutePath();
		            	Message msg=new Message();
		            	msg.what=LocalMusicActivity.SEARCHING_MUSIC;
		            	msg.obj=searchingPath;
		            	handler.sendMessage(msg);
		                File[] listFile = file.listFiles();  
		                if (listFile != null) {  
		                    for (int i = 0; i < listFile.length; i++) {  
		                        search(listFile[i], ext);  
		                    }  
		                }  
		            } else {  
		                songurl = file.getAbsolutePath(); //文件路径为文件的绝对路径
		                filename=file.getName();
		                System.out.println("278filename:"+filename);
		                if (filename.length()>4) {
							songname=filename.substring(0, file.getName().length()-4);
							System.out.println(songname);
						}
		                int position=songname.indexOf("-");
		                if(position>0){
		                	singer=songname.substring(0, position-1);
		                }else{
		                	singer="未知艺术家";
		                }
		                
		                for (int i = 0; i < ext.length; i++) {  
		                    if (songurl.endsWith(ext[i])) {  
		                    	cursor=db.rawQuery("select * from localmusictb where songurl=? and songname=?",new String[]{songurl,songname} );//查询数据库里有没有这个路径
		                    	if(cursor.moveToNext()){														//索引能够移动说明有	
		                    		Log.i("已经添加", songurl);													//所以就不用添加了
		                    	}else{																			//没有的话就添加
		                    	Song song=new Song();
		                    	song.setFilePath(songurl);
		                    	song.setSongName(songname);
		                    	song.setSinger(singer);
		                    	songInfos.add(song);	
		                    	System.out.println("86"+songInfos.size());
		                    	db.execSQL("insert into localmusictb(songurl,songname,singer) values('"+songurl+"','"+songname+"','"+singer+"')");
		                        break;  
		                    	}
		                    }  
		                }  
		            }  
		        }  
		     
		    
		
	}

}
