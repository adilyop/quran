package com.sonyericsson.tutorial.list3;

import java.util.Arrays;

import android.text.Html;
import android.app.AlertDialog;
import android.text.InputType;
import android.text.Html.ImageGetter;
import pack.coran.TextViewWithImages;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class result extends Activity {
	String nameState; 
	AlertDialog levelDialog ;
	int imageNumber = 1;
	int finAyat = 0;
	int hours = 0, minutes = 0, second = 0, millisec = 0;
	Utilities utils = new Utilities();
	private Handler mHandler = new Handler();;
	String souratName, souratLength;
	Db db;
	MediaController mediaController;
	private int currentPosition = 0;
	private boolean playable = true;
	int length = 0;
	int count = 0;
	private MediaPlayer mp, mp1;
	boolean firstTime = true, stop = true, isPlaying = false;
	ImageView imageViewPlay, imageViewStop, imageViewPause, imageViewRewind,
			imageViewForward, imageViewSeek;
	TextView TextViewsouratName;
	TextView TextViewDuration, TextViewPos;
	TextViewWithImages TextViewayats;
	ImageSpan num_1;
	SeekBar seekbartime;
	DigitalClock Durer;
	Intent in4;
	int timeRepeat = 3;
	float textsize = 10;
	String textType = "BOLD";
	public static int back = 0;
	public static String toAyat = "1";
	public static String fromAyat = "1";
	public static int souratID = 1;
	int intToAyat = Integer.valueOf(toAyat);
	int intFromAyat = Integer.valueOf(fromAyat);
	public static int[] songs = {};

	private int getDuration(int[] song) {
		int Duration = 0;
		for (int i = 0; i < song.length; i++) {
			mp1 = MediaPlayer.create(result.this, song[i]);
			Duration = Duration + mp1.getDuration();
		}
		return Duration;
	}
	private void playSong(int song) {
		Toast.makeText(result.this,"currentPosition  "+currentPosition, 100).show();
		mp = MediaPlayer.create(result.this, song);
		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				finAyat = finAyat + mp.getDuration();
				nextSong();
			}
		});
	}
	private void nextSong() {
		if (++currentPosition >= songs.length) {
			if (timeRepeat == 0) {
				Toast.makeText(result.this, "عدد تكرار الصوت = 0   ", 100)
						.show();
			} else {
				timeRepeat--;
				playSong(songs[0]);
				finAyat = 0;
				currentPosition = 0;
			}
		} else {
			if (currentPosition < 0)
				currentPosition = 0;
			// Play next song
			playSong(songs[currentPosition]);
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		num_1 = new ImageSpan(result.this, R.drawable.play);
		seekbartime = (SeekBar) (findViewById(R.id.SeekBar1));
		TextViewsouratName = (TextView) findViewById(R.id.textViewSouratName);
		TextViewayats = (TextViewWithImages) findViewById(R.id.textViewAyat);

		db = new Db(result.this);
		db.open();
		Cursor c2 = db.getConfiguration(1);
		c2.moveToNext();
		timeRepeat = c2.getInt(4);
		textsize = c2.getInt(2);
		textType = c2.getString(3);
		Cursor c1 = db.getSourat(souratID);
		c1.moveToNext();
		souratName = c1.getString(1);
		souratLength = c1.getString(2);
		TextViewsouratName.setText(" " + souratName + "");

		Cursor c = db.getAyatsBySouratID(souratID, intFromAyat, intToAyat);
		String[] ayats = new String[c.getCount()];
		int j = 0;
		String StringAyats = "";
		while (c.moveToNext()) {
			int numAyat = Integer.parseInt(fromAyat) + j;
			ayats[j] = String.valueOf(c.getString(0));
			StringAyats = StringAyats + "" + ayats[j] + " [img src=num_ayat_"
					+ numAyat + "/] ";
			songs = Arrays.copyOf(songs, songs.length + 1);
			songs[songs.length - 1] = displayVersets.ayats[c.getInt(1) - 1];
			if (displayVersets.ayats[c.getInt(1) - 1] < 999999) {
				playable = false;
			}
			j++;
		}
		db.close();
		// Toast.makeText(result.this,"StringAyats  "+StringAyats, 100).show();
		// text style size
		TextViewayats.setText(StringAyats);
		TextViewayats.setTextSize(20);
		TextViewayats.setTextSize(textsize);
		int type = Typeface.BOLD;
		if (textType == "BOLD")
			type = Typeface.BOLD;
		else if (textType == "NORMAL")
			type = Typeface.NORMAL;
		else
			type = Typeface.ITALIC;
		TextViewayats.setTypeface(null, type);
		imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
		imageViewPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				imageViewPlay.setVisibility(ImageView.GONE);
				imageViewPause.setVisibility(ImageView.VISIBLE);
				if (playable) {
					if (!isPlaying) {
						isPlaying = true;
						stop = false;
						TextViewPos = (TextView) findViewById(R.id.textViewPos);
						TextViewDuration = (TextView) findViewById(R.id.textViewDuration);
						millisec = getDuration(songs);
						seekbartime.setMax(millisec);
						TextViewDuration.setText(utils
								.milliSecondsToTimer(millisec));

						updateProgressBar();
						if (firstTime) {
							playSong(songs[0]);
						} else {
							mp.seekTo(length);
							mp.start();
						}
					} else {
						// Toast.makeText(result.this,"media is playing" ,100).show();
					}
				} else
					Toast.makeText(result.this, "ملف صوتي غير موجود", 100)
							.show();
			}
		});
		imageViewForward = (ImageView) findViewById(R.id.imageViewForward);
		imageViewForward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MediaPlayer mpTemp = MediaPlayer.create(result.this, songs[currentPosition]);
				finAyat = finAyat + mpTemp.getDuration();
				if (playable) {
					if (!isPlaying) {
						if ((songs.length - 1) == currentPosition) {
							currentPosition = 0;
						} else {
							currentPosition ++;
						}
					} else {
						mp.stop();
						nextSong();
					}
				} else
					Toast.makeText(result.this, "ملف صوتي غير موجود", 100)
							.show();
			}
		});
		imageViewRewind = (ImageView) findViewById(R.id.ImageViewRewind);
		imageViewRewind.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (playable) {
					if (!isPlaying) {
						if ( 0 == currentPosition) {
							currentPosition = 0;
						} else {
							currentPosition --;
						}
					} else {
						if ( 1 >= currentPosition) {
							mp.stop();
							mp.seekTo(0);
							playSong(songs[0]);
						} else {
							mp.stop();
							if (currentPosition == 0)
								currentPosition = 0;
							else {
								currentPosition-=2;
								MediaPlayer mpTemp = MediaPlayer.create(result.this, songs[currentPosition]);
								finAyat = finAyat - mpTemp.getDuration();
							}
							nextSong();
						}
					}
				} else
					Toast.makeText(result.this, "ملف صوتي غير موجود", 100)
							.show();
			}
		});
		imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
		imageViewStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				imageViewPlay.setVisibility(ImageView.VISIBLE);
				imageViewPause.setVisibility(ImageView.GONE);
				if (stop) {
				} else {
					mHandler.removeCallbacks(mUpdateTimeTask);
					TextViewPos.setText(utils.milliSecondsToTimer(0));
					seekbartime.setProgress(0);
					finAyat = 0;
					seekbartime.setProgress(0);
					stop = true;
					isPlaying = false;
					firstTime = true;
					currentPosition = 0;
					mp.stop();
				}
			}
		});
		imageViewPause = (ImageView) findViewById(R.id.imageViewPause);
		imageViewPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				imageViewPlay.setVisibility(ImageView.VISIBLE);
				imageViewPause.setVisibility(ImageView.GONE);
				mHandler.removeCallbacks(mUpdateTimeTask);
				if (stop) {
				} else {
					mp.pause();
					firstTime = false;
					isPlaying = false;
					length = mp.getCurrentPosition();
				}
			}
		});
		imageViewSeek = (ImageView) findViewById(R.id.imageViewSeek);
		imageViewSeek.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

    			AlertDialog.Builder builder1 = new AlertDialog.Builder(result.this);
    			builder1.setTitle("إضافة إلى قائمة المفضلة");
                
                final EditText input = new EditText(result.this);
                builder1.setView(input);
                builder1.setNegativeButton(" إلغاء ", null);
                builder1.setMessage(" أضف إسم جديد :");
                builder1.setPositiveButton(" حفظ ", new DialogInterface.OnClickListener() {
                   
                    public void onClick(DialogInterface dialog, int which) {
                    	int unique =0;
            			if (((String.valueOf(input.getText())).length() == 0)){
            				Toast.makeText(result.this,"حقل نص فارغ ... ",100 ).show();
            			} else {
            		        db=new Db(result.this);
            		        db.open();   
            		        Cursor c1 = db.getAllStates();    
            		    	int j=0;
            		        String[] states = new String[c1.getCount()];
            		    	while(c1.moveToNext())
            		        {states[j] = String.valueOf(c1.getString(0));
            		    		j++;}
            				int ltngth = states.length;
            				
            				for (int k=0; k<ltngth; k++){
            					String strOldState = String.valueOf(states[k]);
            					String strNewState = String.valueOf(input.getText());
            					if (strOldState.equals(strNewState ))
            						unique = 1;
            				}
            				
            			if (unique != 0) {
            				Toast.makeText(result.this,"يرجى إدخال اسم جديد" ,100 ).show();
            			} else {
						 db = new Db(result.this);
							db.open();
							int fromAyatInt = Integer.parseInt(fromAyat);
							int toAyatInt = Integer.parseInt(toAyat);
							String nameState = String.valueOf(input.getText()); 
							db.addSaveState(souratID, fromAyatInt, toAyatInt, nameState, null);
							Toast.makeText(result.this,"حفظ ... ",100 ).show();
							db.close();
            			}
            			}
                    }});
                levelDialog = builder1.create();
                levelDialog.show();
			}
		});
	};
	
	private ImageGetter imgGetter = new ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			if (imageNumber == 1) {
				drawable = getResources().getDrawable(R.drawable.num_ayat_103);
				++imageNumber;
			} else
				drawable = getResources().getDrawable(R.drawable.num_ayat_103);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = finAyat + mp.getCurrentPosition();
			TextViewPos.setText(utils.milliSecondsToTimer(currentDuration));
			long progress = currentDuration;
			seekbartime.setProgress((int) progress);
			mHandler.postDelayed(this, 100);
		}
	};

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		if (!firstTime)
			mp.stop();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (!firstTime)
			mp.stop();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!firstTime)
			mp.stop();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mp != null)
				mp.stop();
			mHandler.removeCallbacks(mUpdateTimeTask);
			if (back == 0) {
				Intent in = new Intent(result.this, displayVersets.class);
				startActivity(in);
			} else {
				Intent in = new Intent(result.this, TestActivity.class);
				startActivity(in);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BREAK) {
			Toast.makeText(result.this, "KEYCODE_BREAK ", 100).show();
			if (mp != null)
				mp.stop();
			mHandler.removeCallbacks(mUpdateTimeTask);
		}

		return super.onKeyDown(keyCode, event);
	}

}
