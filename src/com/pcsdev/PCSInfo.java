package com.pcsdev;

import java.util.ArrayList;

import com.baidu.pcs.BaiduPCSClient;

import android.os.Handler;
import android.view.Menu;


public class PCSInfo {
	
	//Judge whether the program is finish
	public static int flag = 0;
		
	//Save access_token
	//public static String access_token = null; 
	
	public static BaiduPCSClient client = null;
	
	//The file in the cloud of the storage of folders
	public final static String mbRootPath = "/apps/PCSDIR/";
	
	//UI thread
	public static Handler uiThreadHandler = null;
	
	//The file in the clouds of the storage of name
	public static ArrayList<String> fileNameList = new ArrayList<String>();

  
    /*
     * MbApiKey should be your app_key, please instead of "your app_key"
     */
	public final static String app_key = "NgCOXiUlTzo0wiFXlIILUEO8"; 
	
	public static String fileName = null;
	
	public static String fileTitle = null;
	
	//Judge whether the file name is empty or exits
	public static int fileFlag = 0;
	
	//The path to the file storage on cloud
	public static String sourceFile = null;
	
	//The path to the file storage on local
	public static String target = null;
	
	public static String fileContent = null;
	
	
	//ASctivity status :create£¨0£©¡¢edit£¨1£©¡¢content list£¨2£©
	public static int statu = 2;
	
	//Menu options
    public static final int ITEM0=Menu.FIRST;//System value
    public static final int ITEM1=Menu.FIRST+1;

}
