package com.xiang.mycontacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;  

public class MainActivity extends Activity {
		private ImageButton titleBtn;
		private TitlePopup titlePopup;
		private TitlePopup itemPopup;
		final	int	voiceCode0 = 0;//for menu
		final	int	voiceCode1 = 1;//for item
		private DBServices db;
		private long selectItem;
		private List<contact> contactsList;
		private GestureLibrary mLibrary;
		ListView lv;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			//初始化数据服务
			db = DBServices.getInstance(getApplicationContext());
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
			lv = (ListView)findViewById(R.id.PhoneList);
			
			refreshlistview();
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
	        
	        //title menu
			titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					switch(item.id){
					case 1:
						LayoutInflater factory = LayoutInflater.from(MainActivity.this);
	                    final View view = factory.inflate(R.layout.gesture_input, null);
	                    final AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setView(view).create();
	                    ad.show();
	                    Log.i("button", "Gesture Mod!");
	                    GestureOverlayView gov = (GestureOverlayView)view.findViewById(R.id.gesture);  
	      	          
	        	        gov.setFadeOffset(1000);
	                    gov.addOnGesturePerformedListener(new OnGesturePerformedListener(){

							@Override
							public void onGesturePerformed(
									GestureOverlayView overlay, Gesture gesture) {
								ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
								// We want at least one prediction
								if (predictions.size() > 0) {
									Prediction prediction = predictions.get(0);
									// We want at least some confidence in the result
									if (prediction.score > 1.0) {
										// Show the spell
										//showMessage(prediction.name);
										if(prediction.name.compareTo("n")==0){
											contactorNew();
											ad.dismiss();
										}
										else{
											showMessage(getResources().getString(R.string.Unrecognizegesture));
										}
									}
								}
							}
	        	        	
	        	        });
						break;
					case 2:
	                    //voice service
	                    try{  
	                        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
	                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
	                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, 
	                        		getResources().getString(R.string.startspeaking));
	                        startActivityForResult(intent, voiceCode0);  
	                        }catch (Exception e) {  
	                            e.printStackTrace();  
	                            showMessage(getResources().getString(R.string.novoicedev));  
	                        }  

	                    Log.i("button", "Voice Mod!");
						break;
					default:
						Log.i("button", "none!");
						break;
					}
						
					
				}
			});
			
			//item menu
			itemPopup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					switch(item.id){
					case 1:
						LayoutInflater factory = LayoutInflater.from(MainActivity.this);
	                    final View view = factory.inflate(R.layout.gesture_input, null);
	                    final AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setView(view).create();
	                    ad.show();
	                    Log.i("button", "Gesture Mod!");
	                    GestureOverlayView gov = (GestureOverlayView)view.findViewById(R.id.gesture);  
	        	        gov.setFadeOffset(1000);
	                    gov.addOnGesturePerformedListener(new OnGesturePerformedListener(){

							@Override
							public void onGesturePerformed(
									GestureOverlayView overlay, Gesture gesture) {
								//加载raw中预存的gesture（用com.android.gesture.builder.apk生成）
								ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

								// We want at least one prediction
								if (predictions.size() > 0) {
									Prediction prediction = predictions.get(0);
									// We want at least some confidence in the result
									if (prediction.score > 1.0) {
										// Show the spell
										//showMessage(prediction.name);
										if(prediction.name.compareTo("e")==0){
											contactorEdit();
											ad.dismiss();
										}
										else if(prediction.name.compareTo("d")==0){
											contactorDel();
											ad.dismiss();
										}
										else{
											showMessage(getResources().getString(R.string.Unrecognizegesture));
										}
									}
								}
							}
	        	        });
						Log.i("button", "Gesture Mod!");
						break;
					case 2:try{  
                        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); 
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        		getResources().getString(R.string.startspeaking));  
                        startActivityForResult(intent, voiceCode1);  
                        }catch (Exception e) {
                            e.printStackTrace();  
                            showMessage(getResources().getString(R.string.novoicedev));  
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
		
		/**
		 * 语音查询的回调函数
		 */
		@Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if(requestCode==voiceCode0 && resultCode==RESULT_OK){
	            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);  
	            
	            if(results.get(0).toLowerCase(Locale.getDefault()).compareTo("new")==0){
	            	contactorNew();
	            }
	        }
	        else if(requestCode==voiceCode1 && resultCode==RESULT_OK){
	        	ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        	if(results.get(0).toLowerCase(Locale.getDefault()).compareTo("edit")==0){
	        		contactorEdit();
				}
	        	else if(results.get(0).toLowerCase(Locale.getDefault()).compareTo("delete")==0){
	        		contactorDel();
	        	}
	        }
	        super.onActivityResult(requestCode, resultCode, data);  
	    }  
		 
	      
	    private void showMessage(String s)  
	    {  
	        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();  
	    }  
	    
	    /**
	     * 新建联系人
	     */
	    private void contactorNew(){
	    	LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View view = factory.inflate(R.layout.add_and_edit, null);
            
            new AlertDialog.Builder(MainActivity.this)
            .setView(view).setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					contact newcontacter = new contact();
					
					newcontacter.setName(((EditText)view.findViewById(R.id.name)).getText().toString());
					newcontacter.setPhonenumber(((EditText)view.findViewById(R.id.phonenumber)).getText().toString());
					db.addContacter(newcontacter);
					refreshlistview();
				}
			})
			.setNegativeButton(getResources().getString(R.string.cancel), null)
			.create().show();
			((TextView)view.findViewById(R.id.add_and_edit_title)).setText(getResources().getString(R.string.newcontact));
	    }
	    
	    /**
	     * 编辑联系人
	     */
	    private void contactorEdit(){
	    	LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View view = factory.inflate(R.layout.add_and_edit, null);
            ((EditText)view.findViewById(R.id.name)).setText(((contact)contactsList.get((int)selectItem)).getName());
            ((EditText)view.findViewById(R.id.phonenumber)).setText(((contact)contactsList.get((int)selectItem)).getPhonenumber());
            new AlertDialog.Builder(MainActivity.this)
            .setView(view).setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					contact newcontacter = new contact();
					
					newcontacter.setName(((EditText)view.findViewById(R.id.name)).getText().toString());
					newcontacter.setId(((contact)contactsList.get((int)selectItem)).getId());
					newcontacter.setPhonenumber(((EditText)view.findViewById(R.id.phonenumber)).getText().toString());
					db.editContacter(newcontacter);
					refreshlistview();
				}
			})
			.setNegativeButton(getResources().getString(R.string.cancel), null)
			.create().show();
			((TextView)view.findViewById(R.id.add_and_edit_title)).setText(getResources().getString(R.string.editcontact));
	    }
	    /**
	     * 删除联系人
	     */
	    private void contactorDel(){
	    	new AlertDialog.Builder(MainActivity.this)
	    	.setTitle(getResources().getString(R.string.deleteconfirm))
            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					db.delContacter((contact)contactsList.get((int)selectItem));
					refreshlistview();
				}
			})
			.setNegativeButton(getResources().getString(R.string.cancel), null)
			.create().show();
	    }
	    
	    /**
	     * 每次修改联系人后，更新列表
	     */
	    private void refreshlistview(){
	    	lv.setAdapter(null);  
	    	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>(); 
	    	contactsList = db.getcontacterList();
			if(contactsList!=null){
		        for(contact i:contactsList)
		        {
		        	if(i!=null){
			            HashMap<String, Object> map = new HashMap<String, Object>();  
			            map.put("ItemTitle", i.getName());  
			            map.put("ItemText",i.getPhonenumber());  
			            listItem.add(map);  
		        	}
		        }  
			}
			else{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ItemTitle", getResources().getString(R.string.nocontacts));
	            map.put("ItemText","");
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
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					selectItem = arg3;
					itemPopup.show(arg1,arg3);
				}
			});
	    }
}
