package com.pcsdev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSClient;
//import com.baidu.pcs.BaiduPCSActionInfo;
//import com.baidu.pcs.BaiduPCSClient;

public class MainActivity extends Activity {

	private final String mbApiKey = "NgCOXiUlTzo0wiFXlIILUEO8";//请替换申请客户端应用时获取的Api Key串
	private final String mbRootPath =  "/PCSDIR"; //用户测试的根目录
	
	private Button login;
	private Button getQuota;
	private String accesstoken = null;
	private Handler mbUiThreadHandler = null;
	
	private Button LogInBtn;
	BaiduOAuth baiduOauth;
	//PCSAction loginAction = new PCSAction();
	
	private Button SMenuBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
		
	mbUiThreadHandler = new Handler();
	
	login = (Button) this.findViewById(R.id.login);	
	login.setOnClickListener(new OnClickListener() {
	        @Override
	public void onClick(View v) {
	BaiduOAuth oauthClient = new BaiduOAuth();
	oauthClient.startOAuth(MainActivity.this, mbApiKey, new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener() {
	                @Override
	public void onException(String msg) {
	Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
	                }
	                @Override
	public void onComplete(BaiduOAuthResponse response) {
	if(null != response){
	accesstoken = response.getAccessToken();
	Toast.makeText(getApplicationContext(), "Token: " + accesstoken + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
	                    }
	                }
	                @Override
	public void onCancel() {
	Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
	                }
	            });
	        }
	    });

	getQuota = (Button) this.findViewById(R.id.getquota);
	getQuota.setOnClickListener(new Button.OnClickListener(){
	public void onClick(View v) {
		test_getQuota();
	            } 
	    });
	
	LogInBtn = (Button) this.findViewById(R.id.LogInBtn);
	LogInBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			login(MainActivity.this);
		}
	});
	
	SMenuBtn = (Button)this.findViewById(R.id.SlidingMenuBtn);
	SMenuBtn.setOnClickListener(new OnClickListener() {
		
		String title = "Sliding Menu Test Page";
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Intent intent = new Intent();
//			intent.setClass(MainActivity.this, SlidingMenuTest.class);
//			MainActivity.this.startActivity(intent);
		}
	});
	
	//login(MainActivity.this);
	}

	private void test_getQuota(){

	if(null != accesstoken){
		    Thread workThread = new Thread(new Runnable(){
		public void run() {
			BaiduPCSClient api = new BaiduPCSClient();
			api.setAccessToken(accesstoken);
	final BaiduPCSActionInfo.PCSQuotaResponse info = api.quota();

		mbUiThreadHandler.post(new Runnable(){
	public void run(){
					if(null != info){
		if(0 == info.status.errorCode){
		Toast.makeText(getApplicationContext(), "Quota :" + info.total + "  used: " + info.used, Toast.LENGTH_SHORT).show();
	                            }
		else{
	Toast.makeText(getApplicationContext(), "Quota failed: " + info.status.errorCode + "  " + info.status.message, Toast.LENGTH_SHORT).show();
	                            }
	                        }
	                    }
	                });
	            }
	        }); 

		workThread.start();
	   }
	}
	
	public void login(final Context context){
    	Log.v("pcsdev", "LogIning");
    	if(null != PCSInfo.client && null != PCSInfo.client.accessToken()){
    		Log.v("pcsdev", "access_token is not null");
			Intent intent = new Intent();    				    						    				
			intent.setClass(context, ContentActivity.class); 				
			context.startActivity(intent); 
    	}else{
    		
    		baiduOauth = new BaiduOAuth();
    		Log.v("pcsdev", "access_token is null");
        	try {
        		//Start OAUTH dialog
        		baiduOauth.startOAuth(context, PCSInfo.app_key, new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener(){

        			//Login successful 
//        			public void onComplete(Bundle values) {
//        				//Get access_token
//        				PCSDemoInfo.access_token = values.getString("access_token");
//        				Log.v("pcsdev", PCSDemoInfo.access_token);
//        				Intent intent = new Intent();    				    						    				
//        				intent.setClass(context, LogInActivity.class); 				
//        				context.startActivity(intent);    				
//        			}

        			// TODO: the error code need be redefined
        			@SuppressWarnings("unused")
    				public void onError(int error) {   				
        				Toast.makeText(context, R.string.fail, Toast.LENGTH_SHORT).show();
        			}

        			public void onCancel() {   				
        				Toast.makeText(context, R.string.back, Toast.LENGTH_SHORT).show();
        			}

        			public void onException(String arg0) {
        				Toast.makeText(context, arg0, Toast.LENGTH_SHORT).show();
        			}

					@Override
					public void onComplete(BaiduOAuthResponse response) {
						// TODO Auto-generated method stub
						//PCSInfo.access_token = response.getAccessToken();
						PCSInfo.client = new BaiduPCSClient(response.getAccessToken());
        				Log.v("pcsdev", PCSInfo.client.accessToken());
        				
        				Intent intent = new Intent();    				    						    				
        				intent.setClass(context, ContentActivity.class); 				
        				context.startActivity(intent); 
					}
        		});
        	} catch (Exception e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
    		
    	}    	
    }
}
