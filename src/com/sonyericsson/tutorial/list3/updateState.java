package com.sonyericsson.tutorial.list3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class updateState extends Activity {
	Spinner spnState, spnUpTo, spnUpFrom;
	Button buttonUpdate, buttonRemove;
	EditText editTextName, EditTextTO,EditTextFrom;
	public static int position = 0;
	Db db;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatestate);
        buttonUpdate = (Button) findViewById(R.id.buttonupdate);
        buttonRemove = (Button) findViewById(R.id.buttonremove);
        spnState =  (Spinner) findViewById(R.id.spinnerState2);
        spnUpTo =  (Spinner) findViewById(R.id.SpinnerUp01);
        spnUpFrom =  (Spinner) findViewById(R.id.SpinnerUp02);
        editTextName = (EditText) findViewById(R.id.edittextNameSourat);
        EditTextTO = (EditText) findViewById(R.id.edittextayatto);
        EditTextFrom = (EditText) findViewById(R.id.edittextayatfrom);
        db=new Db(updateState.this);
        db.open();
       
        Cursor c = db.getAllStates();
        //c.moveToFirst();
    	int i=0;
    	String[] states = new String[c.getCount()];
    	while(c.moveToNext())
        {states[i] = String.valueOf(c.getString(0));
    		i++;}
    	ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(updateState.this,android.R.layout.simple_spinner_dropdown_item, states);
        spnState.setAdapter(spinnerArrayAdapter); 
        spnState.setSelection(position);
        /*//String souratLength = db.getSouratLengthByID(index+1);*/
    	db.close();
        spnState.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	String nameState =String.valueOf(spnState.getSelectedItem());
            	db=new Db(updateState.this);
    	        db.open();
    	        Cursor c = db.getSaveStateByName(nameState);
    	        c.moveToNext();
    			int sourat_id = c.getInt(1);
    			String length = db.getSouratLengthByID(sourat_id);
    			int nombre_ayats = Integer.valueOf(length); 
    			Integer[] ayats = new Integer[nombre_ayats];
    			for (int j = 0; j < ayats.length; j++) {
    				ayats[j] = j + 1;
    			}
    			ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(updateState.this,android.R.layout.simple_spinner_dropdown_item, ayats);
    		    spnUpFrom.setAdapter(spinnerArrayAdapter);
    		    spnUpTo.setAdapter(spinnerArrayAdapter);
    			String souratName;
    			Cursor c1 = db.getSourat(sourat_id);
    			c1.moveToNext();
    			souratName = c1.getString(1);
    	        editTextName.setText(souratName); 
    	        Toast.makeText(updateState.this,c.getString(2)+" = c.getString(2) "+c.getString(3),100 ).show();
    		    
    	        spnUpFrom.setSelection(Integer.parseInt(c.getString(2)) - 1 );
    		    spnUpTo.setSelection(Integer.parseInt(c.getString(3)) - 1 );
    	        //EditTextTO.setText(c.getString(3));
    	        //EditTextFrom.setText(c.getString(2));
    	        db.close();
            }

    		public void onNothingSelected(AdapterView<?> arg0) {
    			// TODO Auto-generated method stub
    			
    		}

        });
        buttonRemove.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			db = new Db(updateState.this);
    			db.open();
    			String StateName = String.valueOf(spnState.getSelectedItem());
    			db.deleteSaveState(StateName);
				Toast.makeText(updateState.this,"حفظ ... ",100 ).show();       
		        Cursor c = db.getAllStates();
		        //c.moveToFirst();
		    	int i=0;
		    	String[] states = new String[c.getCount()];
		    	while(c.moveToNext())
		        {states[i] = String.valueOf(c.getString(0));
		    		i++;}
		    	ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(updateState.this,android.R.layout.simple_spinner_dropdown_item, states);
		        spnState.setAdapter(spinnerArrayAdapter); 
		        /*//String souratLength = db.getSouratLengthByID(index+1);*/
		    	db.close();
    			}
    		});
        buttonUpdate.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			String souratName =  String.valueOf(editTextName.getText());
    			db = new Db(updateState.this);
    			db.open();
    			Cursor c = db.getSouratIDFromSouratName(souratName);
    			c.moveToNext();
    			int sourat_id = c.getInt(0);
    			String length = db.getSouratLengthByID(sourat_id);
    			int nombre_ayats = Integer.valueOf(length); 
    			String StateName = String.valueOf(spnState.getSelectedItem());			
    			int from = Integer.valueOf(String.valueOf(spnUpFrom.getSelectedItem()));
    			int to = Integer.valueOf(String.valueOf(spnUpTo.getSelectedItem()));
    			if (nombre_ayats < to 
    		    		|| from < 1
    		    		|| from > to){
    		    	{Toast.makeText(updateState.this,"المرجو إختيار الآية من 1 إلى " + nombre_ayats 
    		    			,100 ).show();}
    		    } else {
    			db.updateSaveState(sourat_id, from, to, StateName, "10/44/2016");
				Toast.makeText(updateState.this,"حفظ ... ",100 ).show();    
    		    }
    			db.close();      
    			}
    		});
	}
}