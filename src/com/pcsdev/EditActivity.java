package com.pcsdev;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.pcs.BaiduPCSActionInfo.PCSSimplefiedResponse;
import com.baidu.pcs.BaiduPCSStatusListener;
import com.pcsdev.tools.Doc;

/*
 * Author: ganxun(ganxun@baidu.com)
 * Time:   2012.7.10
 * 
 */

@SuppressWarnings("unused")
public class EditActivity extends Activity {
    /** Called when the activity is first created. */
	
	private TextView title = null;	
	private EditText content = null;	
	private ImageButton editBack = null;
	private ImageButton save = null;
	
	private String output_content = null;
		
	private int save_Flag = 0;
	
	Doc editNote = new Doc(); 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Log.v("PCSDEV  EditorActivity-->>", "EditorActivity showing");
        
        title = (TextView)findViewById(R.id.edit_title);
        content = (EditText)findViewById(R.id.edit_content);        
        editBack = (ImageButton)findViewById(R.id.btneditback);
        save = (ImageButton)findViewById(R.id.btneditsave);
        
        PCSInfo.statu = 1;
        
        PCSInfo.uiThreadHandler = new Handler(); 
        
        title.setText(PCSInfo.fileTitle);
        
        download();
    	
        //content.setText(PCSInfo.fileContent);
 
        editBack.setOnClickListener(new Button.OnClickListener(){
        	
        	public void onClick(View v){
        		
        		editNote.back(EditActivity.this);       
        	}
        });
        
        save.setOnClickListener(new Button.OnClickListener(){
        	
        	public void onClick(View v){
        		
        		PCSInfo.fileContent = content.getText().toString();
        		Log.v("PCSDEV  EditorActivity-->>", PCSInfo.fileContent);
        		
        		editNote.save(EditActivity.this);
        	}
        });       
    }
    
    
    
    public void download(){
    	
    	if(null != PCSInfo.client.accessToken()){

    		Thread workThread = new Thread(new Runnable(){
				public void run() {

//		    		BaiduPCSAPI api = new BaiduPCSAPI();
//		    		api.setAccessToken(PCSInfo.access_token);
		    		
		    		//Get the download file storage path on cloud
		    		PCSInfo.sourceFile = PCSInfo.mbRootPath + PCSInfo.fileTitle;//+".txt";
		    		Log.v("PCSDEV  EditorActivity-->>", PCSInfo.sourceFile);
		    		
		    		//Set the download file storage path
		    		PCSInfo.target = getApplicationContext().getFilesDir()+"/"+PCSInfo.fileTitle;//+".txt";
		    		Log.v("PCSDEV  EditorActivity-->>", PCSInfo.target);
		    		
		    		//Call PCS downloadFile API
		    		final PCSSimplefiedResponse downloadResponse = PCSInfo.client.downloadFile(PCSInfo.sourceFile, PCSInfo.target,  new BaiduPCSStatusListener(){

						@Override
						public void onProgress(long bytes, long total) {
							// TODO Auto-generated method stub
								
						}		    			
		    		});
		    		
		    		PCSInfo.uiThreadHandler.post(new Runnable(){
		    			public void run(){
		    				
		    				if(downloadResponse.errorCode == 0){
			    				try{
			    					//The local store download files
				    				File file = new File(PCSInfo.target);			    				
				    				FileInputStream inStream = new FileInputStream(file);
				    				
				    				int length = inStream.available();
				    				
				    				byte [] buffer = new byte[length];
				    				
				    				inStream.read(buffer);
				    				
				    				PCSInfo.fileContent = EncodingUtils.getString(buffer, "UTF-8");
				    				content.setText(PCSInfo.fileContent);
				    				Log.v("PCSDEV  EditorActivity-->>", PCSInfo.fileContent);
				    				
				    				inStream.close();
				    				
			    				}catch (Exception e) {
									// TODO: handle exception
			    					
			    					Toast.makeText(getApplicationContext(), "读取文件失败！", Toast.LENGTH_SHORT).show();
								}
		    				}else{
		    					
		    					Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT).show();
		    				}	
		    			}
		    		});	
				}
			});
			 
    		workThread.start();
    	}
    }
    
    
    
     
    // Back to the show content activity
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
	    menu.add(0, PCSInfo.ITEM0, 0,"退出");
	    menu.add(0, PCSInfo.ITEM1, 0, "关于我们");
	    
	    return true;
	}  
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		
		 switch (item.getItemId()) {
		     case PCSInfo.ITEM0:
		    	 editNote.exit(EditActivity.this);
		         break;
		     case PCSInfo.ITEM1:		    	 
		    	 Toast.makeText(getApplicationContext(), "自由开发者，呵呵！", Toast.LENGTH_SHORT).show();
		         break;
		 }
		 
		return true;
	}

}
