package com.pcsdev;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSActionInfo.PCSCommonFileInfo;

public class ContentActivity extends ListActivity{

	private ImageButton create = null;
	private ImageButton refresh = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("pcsdev", "Login Activity");
		setContentView(R.layout.contentshow);
		
		create = (ImageButton)findViewById(R.id.btncreate);
        refresh = (ImageButton)findViewById(R.id.btnrefresh);
      
        PCSInfo.uiThreadHandler = new Handler(); 
        
        PCSInfo.statu = 2;
        
        list(ContentActivity.this);
        
        create.setOnClickListener(new Button.OnClickListener(){
        	
        	public void onClick(View v){	
        		create();
        	}
        });
        
        refresh.setOnClickListener(new Button.OnClickListener(){
        	
        	public void onClick(View v){       		
        		refresh();
        	}
        });
	}

	private void list(final Context context) {
		// TODO Auto-generated method stub
    	
        if (null != PCSInfo.client.accessToken()){
        	Log.v("PCSDEV  List Dir-->>", PCSInfo.client.accessToken());
        	
    		Thread workThread = new Thread(new Runnable(){
    			
				public void run() {
					//The path to  file storage on the cloud
		    		String path = PCSInfo.mbRootPath;
		    		Log.v("PCSDEV  List Dir-->>", PCSInfo.mbRootPath);
		    		
		    		final BaiduPCSActionInfo.PCSListInfoResponse listResponse = PCSInfo.client.list(path, "name", "asc");
		    		if(null != listResponse.list) Log.v("PCSDEV  List Dir-->>", "list is not empty: " + listResponse.list.toString());
		    		
		    		PCSInfo.uiThreadHandler.post(new Runnable(){
		    			
		    			public void run(){		    				
		    			
		    				ArrayList<HashMap<String, String>> list =new ArrayList<HashMap<String,String>>();
		    				
		    				if(listResponse.list != null){
		    					if( ! listResponse.list.isEmpty()){
		    						
		    						Log.v("PCSDEV  List Dir-->>", "list is not empty, and start loop");
		    					   			    	            
			    	            for(Iterator<PCSCommonFileInfo> i = listResponse.list.iterator(); i.hasNext();){
			    	            	
			    	            	Log.v("PCSDEV  List Dir-->>", "now in loop");
			    	            	
			    	            	HashMap<String, String> map =new HashMap<String, String>();
			    	            				    	            	
			    	            	PCSCommonFileInfo info = i.next();
			    	            	Log.v("PCSDEV  List Dir-->>", info.path);
			    	            	//Get the file name 			    	            	
			    	         	    String path = info.path;			    	         	    
			    	         	    String fileName = path.substring(PCSInfo.mbRootPath.length());
			    	         	    Log.v("PCSDEV  List Dir-->>", fileName);
			    	         	    
			    	         	    //Get the last modified time
			    	         	    Date date = new Date(info.mTime*1000);
			    	         	    
			    	         	    //Modify the format of the time
			    	         	    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
			    	         	    String dateString = formatter.format(date);
		  			 			    			    	            	
			    	            	map.put("file_name", fileName);			    	            	
			    	            	map.put("time", dateString);
			    	            	
			    	            	//Add item to content list	
			    	            	list.add(map); 	            	
			    	            	PCSInfo.fileNameList.add(fileName);							    				    	             
			    	            }			    	               
			    	        }else{
			    	        	
			    	        	//Clear content list
		    					list.clear();
		    					Toast.makeText(context, "ï¿½ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½ï¿½ï¿½Îªï¿½Õ£ï¿½", Toast.LENGTH_SHORT).show();		    					
		    				}    
                                        }	
			    	         SimpleAdapter listAdapter =new SimpleAdapter(context, list, R.layout.content, new String[]{"file_name","time"}, new int[]{R.id.file_name,R.id.time});   
			    	        
			    	         //Set listview to display content
			    	         ((ListActivity)context).setListAdapter(listAdapter);
		    	         
			    	         Toast.makeText(context, R.string.refresh, Toast.LENGTH_SHORT).show();

		    			}
		    		});	
		    		
				}
			});
			 
    		workThread.start();

        } 
    
		
	}

	private void create() {
		// TODO Auto-generated method stub
		
	}

	private void refresh() {
		// TODO Auto-generated method stub
		list(ContentActivity.this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	super.onListItemClick(l, v, position, id);
    	
    	//Get filename form item
    	 PCSInfo.fileTitle = l.getAdapter().getItem(position).toString();
    	
    	 PCSInfo.fileTitle = PCSInfo.fileTitle.substring(PCSInfo.fileTitle.indexOf("=")+1, PCSInfo.fileTitle.lastIndexOf(","));
    	 
		//Select operation(edit/delete/cancel)
    	AlertDialog.Builder onListItemClickAlert = new AlertDialog.Builder(ContentActivity.this);
    	onListItemClickAlert.setTitle("²Ù×÷Ñ¡Ôñ£º");
    		
    	onListItemClickAlert.setPositiveButton("±à¼­", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				Intent edit_intent = new Intent();
			    edit_intent.setClass(getApplicationContext(),EditActivity.class);					
				ContentActivity.this.startActivity(edit_intent);
				Log.v("PCSDEV  List Dir-->>", "going to EditerActivity");
			}
		});
    	
    	onListItemClickAlert.setNeutralButton("É¾³ý", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				// TODO contentNote.delete(ContentActivity.this);		
			}
		});
    	
    	onListItemClickAlert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	onListItemClickAlert.show();  
    	 
    }

}
