package com.pcsdev.tools;

import java.io.FileOutputStream;

import com.pcsdev.ContentActivity;
import com.pcsdev.MainActivity;
import com.pcsdev.PCSInfo;
import com.pcsdev.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class Doc {
	
public void save(Context context) {
    	
    	try{
    		PCSInfo.sourceFile = context.getFilesDir()+"/"+PCSInfo.fileTitle;//+".txt";
       		
       	    String saveFile = PCSInfo.fileTitle;//+".txt";
       			        			 	 
       	    FileOutputStream outputStream= context.openFileOutput(saveFile, Context.MODE_PRIVATE);
       	 
       	    if(!PCSInfo.fileContent.equals("")){
       		    //save file
           	    outputStream.write(PCSInfo.fileContent.getBytes());
     					           	           	 
       	    }else{

	       		byte bytes = 0;
	       		outputStream.write(bytes);
       	    }
       	          	 
       	    outputStream.close();
       	    
       	    if(PCSInfo.statu == 0){
       	    	//Upload the file to cloud 
       	    	//upload(context);
       	    	
       	    }else{
       	    	//If it is edited file save, the first remove the clouds existing file, and then upload
       	    	if(PCSInfo.statu == 1){
       	    		
       	    		//delete(context);       	    		
       	    	}
       	    }
   	                 		       	 		    
       	  }catch (Exception e) {   
               Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();  
          }    	    		 
    }

	//Back to the content activity
    public void back(Context context){    	  		
    	Intent content = new Intent();
  	    content.setClass(context, ContentActivity.class);	
  	    context.startActivity(content);   		  	
    }
    
    //Finish the program
    public void  exit(final Context context){
    	
        AlertDialog.Builder exitAlert = new AlertDialog.Builder(context);
        exitAlert.setIcon(R.drawable.alert_dark).setTitle("��ʾ...").setMessage("��ȷ��Ҫ�뿪�ͻ�������");
        exitAlert.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
               
               public void onClick(DialogInterface dialog, int which) {
                    	PCSInfo.flag= 1;
                        Intent intent = new Intent(); 
                        intent.setClass(context, MainActivity.class);//��ת��login���棬���ݲ����˳�
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //ע�Ȿ�е�FLAG����,clear����Activity��¼
                        context.startActivity(intent);//ע�Ⱑ������ת��ҳ���н��м������˳�

                    }
                });
        
        exitAlert.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
             
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        
        exitAlert.show();
    }
}
