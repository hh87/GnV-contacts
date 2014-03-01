package com.xiang.mycontacts;

import java.util.ArrayList;
import java.util.HashMap;

import com.xiang.mycontacts.TitlePopup.OnItemOnClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;  

public class MainActivity extends Activity {
		//定义标题栏上的按钮
		private ImageButton titleBtn;
		
		//定义标题栏弹窗按钮
		private TitlePopup titlePopup;
		private TitlePopup itemPopup;
		final	int	voiceCode0 = 0;//for menu
		final	int	voiceCode1 = 1;//for item
		private GestureOverlayView mDrawGestureView;
		private GestureLibrary mLibrary;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			initView();
			
		}
		
		/**
		 * 初始化组件
		 */
		private void initView(){
			//实例化标题栏按钮并设置监听
			titleBtn = (ImageButton) findViewById(R.id.title_btn);
			titleBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					titlePopup.show(v);
				}
			});
			//实例化标题栏弹窗
			titlePopup = new TitlePopup(this.getApplicationContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			itemPopup = new TitlePopup(this.getApplicationContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			//init listView
			ListView lv = (ListView)findViewById(R.id.PhoneList);
			///test data
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
	        for(int i=0;i<10;i++)  
	        {  
	            HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("ItemTitle", "Level "+i);  
	            map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");  
	            listItem.add(map);  
	        }  
	        //生成适配器的Item和动态数组对应的元素  
	        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	            R.layout.lv_item,//ListItem的XML实现  
	            //动态数组与ImageItem对应的子项          
	            new String[] {"ItemTitle", "ItemText"},   
	            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	            new int[] {R.id.ItemTitle,R.id.ItemText}  
	        );  
	         
	        //添加并且显示  
	        lv.setAdapter(listItemAdapter);  
			
			//end of test data
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					itemPopup.show(arg1,true);
					
				}
			});
			//给标题栏弹窗添加子类
			titlePopup.addAction(new ActionItem(this, "Gesture",1));
			titlePopup.addAction(new ActionItem(this, "Voice",2));
			
			itemPopup.addAction(new ActionItem(this, "Choose An Action",0));
			itemPopup.addAction(new ActionItem(this, "Gesture",1));
			itemPopup.addAction(new ActionItem(this, "Voice",2));
			
			mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
	        if (!mLibrary.load()) {
	        	finish();
	        }
	        
			titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					switch(item.id){
					case 1:
						LayoutInflater factory = LayoutInflater.from(MainActivity.this);
	                    final View view = factory.inflate(R.layout.gesture_input, null);
	                    new AlertDialog.Builder(MainActivity.this)
                        .setView(view).create().show();  
	                    Log.i("button", "Gesture Mod!");
	                    GestureOverlayView gov = (GestureOverlayView)view.findViewById(R.id.gesture);  
	      	          
	        	        gov.setFadeOffset(2000);
	        	        //绑定监听器  
	                    gov.addOnGesturePerformedListener(new OnGesturePerformedListener(){

							@Override
							public void onGesturePerformed(
									GestureOverlayView overlay, Gesture gesture) {
								// TODO Auto-generated method stub
								ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

								// We want at least one prediction
								if (predictions.size() > 0) {
									Prediction prediction = predictions.get(0);
									// We want at least some confidence in the result
									if (prediction.score > 1.0) {
										// Show the spell
										showMessage(prediction.name);
										if(prediction.name.compareTo("n")==0){

										}
									}
								}
							}
	        	        	
	        	        });  
	                    gov.addOnGesturingListener(new OnGesturingListener() {
							
							@Override
							public void onGesturingStarted(GestureOverlayView overlay) {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void onGesturingEnded(GestureOverlayView overlay) {
								// TODO Auto-generated method stub
							}
						});  
	                    
						break;
					case 2:
	                    //voice service
	                    try{  
	                        //通过Intent传递语音识别的模式，开启语音  
	                        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
	                        //语言模式和自由模式的语音识别  
	                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
	                        //提示语音开始  
	                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");  
	                        //开始语音识别  
	                        startActivityForResult(intent, voiceCode0);  
	                        }catch (Exception e) {  
	                            // TODO: handle exception  
	                            e.printStackTrace();  
	                            Toast.makeText(getApplicationContext(), "找不到语音设备", 1).show();  
	                        }  
	                    
						Log.i("button", "Voice Mod!");
						break;
					default:
						Log.i("button", "none!");
						break;
					}
						
					
				}
			});
			
			itemPopup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					switch(item.id){
					case 1:
						Log.i("button", "Gesture Mod!");
						break;
					case 2:try{  
                        //通过Intent传递语音识别的模式，开启语音  
                        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
                        //语言模式和自由模式的语音识别  
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
                        //提示语音开始  
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");  
                        //开始语音识别  
                        startActivityForResult(intent, voiceCode1);  
                        }catch (Exception e) {  
                            // TODO: handle exception  
                            e.printStackTrace();  
                            Toast.makeText(getApplicationContext(), "找不到语音设备", 1).show();  
                        }  
						Log.i("button", "Voice Mod!");
						break;
					default:
						Log.i("button", "none!");
						break;
					}
						
					
				}
			});
		}
		
		@Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        // TODO Auto-generated method stub  
	        //回调获取从谷歌得到的数据   
	        if(requestCode==voiceCode0 && resultCode==RESULT_OK){  
	            //取得语音的字符  
	            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);  
	            
	            if(results.get(0).toLowerCase().compareTo("new")==0){
	            	
	            }
	            else{
	            	
	            }
	        }
	        else if(requestCode==voiceCode1 && resultCode==RESULT_OK){
	        	ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        	if(results.get(0).toLowerCase().compareTo("edit")==0){
	        		
	        	}
	        	else if(results.get(0).toLowerCase().compareTo("delete")==0){
	        		
	        	}
	        	else{
	        		
	        	}
	        }
	        super.onActivityResult(requestCode, resultCode, data);  
	    }  
		 
	      
	    private void showMessage(String s)  
	    {  
	        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();  
	    }  
	    
		
}
