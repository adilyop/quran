package com.sonyericsson.tutorial.list3;

import java.util.Arrays;

import com.sun.webkit.BackForwardList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class displayVersets extends Activity{
	public static int[] ayats= constante.ayats_constante;
	Db db;
	Spinner spn, spnFrom, spnTo;
	Button display, check;
	EditText FromAyat;
	EditText ToAyat;
	TextView textviewFrom, textviewto;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayversets);
	spn = (Spinner) findViewById(R.id.spinner1);
	spnFrom = (Spinner) findViewById(R.id.Spinner01);
	spnTo = (Spinner) findViewById(R.id.Spinner02);
	FromAyat = (EditText) findViewById(R.id.editTextFromAyat);
	textviewFrom = (TextView) findViewById(R.id.textView1checkayatfrom);
	textviewto = (TextView) findViewById(R.id.textViewcheckayatTo);
	db=new Db(displayVersets.this);
    db.open();
   
    Cursor c = db.getAllSourats();
    //c.moveToFirst();
	int i=0;
	String[] sourat = new String[c.getCount()];
	while(c.moveToNext())
    {sourat[i] = String.valueOf(c.getString(0));
		i++;}

    //String souratLength = db.getSouratLengthByID(index+1);
	db.close();
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(displayVersets.this,android.R.layout.simple_spinner_dropdown_item, sourat);
    spn.setAdapter(spinnerArrayAdapter); 
    display =  (Button) findViewById(R.id.buttonDisplay);
    display.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			if (spnFrom.getSelectedItemPosition() == 0
					|| spnTo.getSelectedItemPosition() == 0) {
				Toast.makeText(displayVersets.this,"المرجو إختيار الاية",100 ).show();
			} else {
			db=new Db(displayVersets.this);
		    db.open();	   
		    String c = db.getSouratLengthByID(result.souratID);
		    int nombre_ayats = Integer.valueOf(c); 
			db.close();
			result.songs = Arrays.copyOf(result.songs, 0);
			result.souratID = (int) spn.getSelectedItemId() + 1;
			result.fromAyat = String.valueOf(spnFrom.getSelectedItem());
			result.toAyat = String.valueOf(spnTo.getSelectedItem());
			
		    if (nombre_ayats < Integer.valueOf(result.toAyat) 
		    		|| Integer.valueOf(result.fromAyat) < 1
		    		|| Integer.valueOf(result.fromAyat) > Integer.valueOf(result.toAyat)){
		    	{Toast.makeText(displayVersets.this,"المرجو إختيار الآية من 1 إلى " + nombre_ayats 
		    			,100 ).show();}
		    } else {
		    	result.back = 0;
			Intent in = new Intent(displayVersets.this,result.class);
			startActivity(in);
		    }
			}
		}
		});
    check =  (Button) findViewById(R.id.check);
    check.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			if (spnFrom.getSelectedItemPosition() == 0
					|| spnTo.getSelectedItemPosition() == 0 ) {
				Toast.makeText(displayVersets.this,"المرجو إختيار الاية",100 ).show();
			} else {
				db = new Db(displayVersets.this);
				db.open();
				Cursor c = db.getAyatBySouratID(spn.getSelectedItemPosition()+1, Integer.valueOf(String.valueOf(spnFrom.getSelectedItem())));
				Cursor c1 = db.getAyatBySouratID(spn.getSelectedItemPosition()+1, Integer.valueOf(String.valueOf(spnTo.getSelectedItem())));
				c.moveToNext();c1.moveToNext();
				textviewFrom.setText("من الاية : "+c.getString(0));
				textviewto.setText("الى الاية : "+c1.getString(0));
				db.close();
			}
			}
    	
		});
    spn.setOnItemSelectedListener(new OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	int souratID =(int) spn.getSelectedItemId() + 1;
			db=new Db(displayVersets.this);
	        db.open();   
		    String c0 = db.getSouratLengthByID(souratID);
		    int nombre_ayats = Integer.valueOf(c0); 
			db.close();
			String[] ayatsfrom = new String[nombre_ayats + 1];
			String[] ayatsto = new String[nombre_ayats + 1];
			ayatsfrom[0] = "من الاية";
			ayatsto[0] = "الى الاية"; 
			for (int j = 0; j < nombre_ayats ; j++) {
				//ayatsfrom[j+1] = "dfv";
				//ayatsto[j+1] = "dfv";
				ayatsfrom[j+1] = String.valueOf(j+1);
				ayatsto[j+1] = String.valueOf(j+1);
			}
			ArrayAdapter spinnerArrayAdapterfrom = new ArrayAdapter(displayVersets.this,android.R.layout.simple_spinner_dropdown_item, ayatsfrom);
			ArrayAdapter spinnerArrayAdapterto = new ArrayAdapter(displayVersets.this,android.R.layout.simple_spinner_dropdown_item, ayatsto);
		    spnFrom.setAdapter(spinnerArrayAdapterfrom);
		    spnTo.setAdapter(spinnerArrayAdapterto); 
		    //spnTo.setSelection(nombre_ayats);
	        /*String To = db.getSouratLengthByID(souratID);*/
	        //FromAyat.setText("1");
	        //ToAyat.setText(To);
	        db.close();
        }

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

    });
}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_BACK) {
		    	Intent in = new Intent(displayVersets.this,QuranActivity.class);
		    	startActivity(in);
		        return true;
		    } else if (keyCode == KeyEvent.KEYCODE_BREAK){
		    	
		    }

		    return super.onKeyDown(keyCode, event);
		}
}
