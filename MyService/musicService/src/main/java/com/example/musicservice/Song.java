package com.example.musicservice;

public class Song {
	private String songName;
	private String singer;
	private String album;
	private String songurl;
	private String description;
	private int songnumber;
	private int mv_isexist;
	private String filePath;
	private String isAddedToPlayList;
	private boolean isAdded;
	private int isStoragedByUser;	//是否已被用户收藏
	private int isDownLoaded;		//是否已下载到本地
	private int isHaveMV;
	private int key;
	private String mvurl;
	private int localorweb;
	public Song(){
		
	};
	
	@Override
	public String toString() {
		return "Song [songName=" + songName + ", singer=" + singer + ", songurl=" + songurl + "]";
	}
	public Song(String songName, String singer, String songurl) {
		super();
		this.songName = songName;
		this.singer = singer;
		this.songurl = songurl;
	}
	
	public int getLocalorweb() {
		return localorweb;
	}
	public void setLocalorweb(int localorweb) {
		this.localorweb = localorweb;
	}
	public String getMvurl() {
		return mvurl;
	}
	public int getIsHaveMV() {
		return isHaveMV;
	}
	public void setIsHaveMV(int isHaveMV) {
		this.isHaveMV = isHaveMV;
	}
	public void setMvurl(String mvurl) {
		this.mvurl = mvurl;
	}
	public int getIsStoragedByUser() {
		return isStoragedByUser;
	}
	public void setIsStoragedByUser(int isStoragedByUser) {
		this.isStoragedByUser = isStoragedByUser;
	}
	public int getIsDownLoaded() {
		return isDownLoaded;
	}
	public void setIsDownLoaded(int isDownLoaded) {
		this.isDownLoaded = isDownLoaded;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public boolean isAdded() {
		return isAdded;
	}
	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMv_isexist() {
		return mv_isexist;
	}
	public void setMv_isexist(int mv_isexist) {
		this.mv_isexist = mv_isexist;
	}
	
	public int getSongnumber() {
		return songnumber;
	}
	public void setSongnumber(int songnumber) {
		this.songnumber = songnumber;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getSongurl() {
		return songurl;
	}
	public void setSongurl(String songurl) {
		this.songurl = songurl;
	}
	public String getIsAddedToPlayList() {
		return isAddedToPlayList;
	}
	public void setIsAddedToPlayList(String isAddedToPlayList) {
		this.isAddedToPlayList = isAddedToPlayList;
	}
	}
