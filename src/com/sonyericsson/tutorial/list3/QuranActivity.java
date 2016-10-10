package com.sonyericsson.tutorial.list3;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class QuranActivity extends Activity {
	ImageView imageConf, imageList, imageSelect;
	FrameLayout frameConf, frameList, frameSelect, frameInfo;
	Db db;
	Intent in4;
    public void onCreate(Bundle savedInstanceState) {
        try{
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        //imageConf = (ImageView) findViewById(R.id.imageView1) ;
        //imageList = (ImageView) findViewById(R.id.imageView3) ;
        //imageSelect = (ImageView) findViewById(R.id.imageView2) ;
        frameConf = (FrameLayout) findViewById(R.id.FrameLayoutConfig);
        frameList = (FrameLayout) findViewById(R.id.frameLayoutList);
        frameSelect = (FrameLayout) findViewById(R.id.FrameLayoutRead);
        frameInfo = (FrameLayout) findViewById(R.id.FrameLayoutInfo);
        //imageSelect.setOnClickListener(new OnClickListener() {
        frameSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			Intent in = new Intent(QuranActivity.this,displayVersets.class);
			startActivity(in);
			}
			});
        frameInfo.setOnClickListener(
        		new OnClickListener() {
			public void onClick(View v) {
				// boite de dialogue
				AlertDialog.Builder boite;
                boite = new AlertDialog.Builder(QuranActivity.this);
               
                boite.setTitle("A propos : ");
                boite.setIcon(R.drawable.list2);
                    boite.setMessage("ElAlam : \n" +
                    		"C'est une application développée dans le cadre du Méditel Apps Challenge 2013\n" +
                    		"par : GUEZZI Adil\n" +
                    		"e-mail : guezziadil@gmail.com\n\n" +
                    		"Qui offre un accès facile et offline aux :\n" +
                    		"- informations sur chaque pays\n" +
                    		"- recherche par nom ou capitale\n" +
                    		"- recherche sur carte\n" +
                    		"- teste de connaissances\n");
                boite.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   
                    public void onClick(DialogInterface dialog, int which) {
                   
                    }
                    }
                );
                boite.show();
				
			}});
        //imageConf.setOnClickListener(new OnClickListener() {
        frameConf.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			Intent in = new Intent(QuranActivity.this,configuration.class);
			startActivity(in);
			}
			});
        
        //imageList.setOnClickListener(new OnClickListener() {
        frameList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			Intent in = new Intent(QuranActivity.this,TestActivity.class);
			startActivity(in);
			}
			});
        }
        catch(Exception ex){
        }
        
       }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
	    	intent.addCategory(Intent.CATEGORY_HOME);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(intent);
	        return true;
	    } else if (keyCode == KeyEvent.KEYCODE_BREAK){
	    	
	    }

	    return super.onKeyDown(keyCode, event);
	}
}