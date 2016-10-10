package com.sonyericsson.tutorial.list3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class addState extends Activity {
	Button add;
	Db db;
	int unique=0;
	Spinner spnSourat, spnStates;
	EditText editname, editfrom, editto;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstate);
        editname = (EditText) findViewById(R.id.editName);
        editfrom = (EditText) findViewById(R.id.editayatfrom);
        editto = (EditText) findViewById(R.id.ayatto);
        add = (Button) findViewById(R.id.ButtonAdd);
        spnStates = (Spinner) findViewById(R.id.SpinnerState);
        spnSourat = (Spinner) findViewById(R.id.SpinnerSourat);
        db=new Db(addState.this);
        db.open();       
        Cursor c = db.getAllSourats();
        Cursor c1 = db.getAllStates();
    	int i=0;
    	String[] sourat = new String[c.getCount()];
    	while(c.moveToNext())
        {sourat[i] = String.valueOf(c.getString(0));
    		i++;}
    	int j=0;
    	String[] states = new String[c1.getCount()];
    	while(c1.moveToNext())
        {states[j] = String.valueOf(c1.getString(0));
    		j++;}
        //String souratLength = db.getSouratLengthByID(index+1);
    	db.close();
    	 @SuppressWarnings({ "rawtypes", "unchecked" })
    	ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(addState.this,android.R.layout.simple_spinner_dropdown_item, sourat);
    	 spnSourat.setAdapter(spinnerArrayAdapter); 
     	@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter spinnerArrayAdapter1 = new ArrayAdapter(addState.this,android.R.layout.simple_spinner_dropdown_item, states);
   	 spnStates.setAdapter(spinnerArrayAdapter1); 
   	add.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			unique =0;
			if (((String.valueOf(editfrom.getText())).length() == 0)
					|| ((String.valueOf(editto.getText())).length() == 0)
					|| (String.valueOf(editname.getText())).length() == 0){
				Toast.makeText(addState.this,"حقل نص فارغ ... ",100 ).show();
			} else {
				int ltngth = spnStates.getCount();
				
				for (int k=0; k<ltngth; k++){
					String strOldState = String.valueOf(spnStates.getItemAtPosition(k));
					String strNewState = String.valueOf(editname.getText());
					if (strOldState.equals(strNewState ))
						unique = 1;
				}
				
			if (unique != 0) {
				Toast.makeText(addState.this,"يرجى إدخال اسم جديد" ,100 ).show();
			} else {	
			String name = String.valueOf(editname.getText());
			int sourat_id = spnSourat.getSelectedItemPosition()+1;			
			int from = Integer.valueOf(String.valueOf(editfrom.getText()));
			int to = Integer.valueOf(String.valueOf(editto.getText()));
			
			db = new Db(addState.this);
			db.open();
			String c = db.getSouratLengthByID(sourat_id);
			int nombre_ayats = Integer.valueOf(c); 
			if (nombre_ayats < to
		    		|| from < 1
		    		|| from > to){
		    	{Toast.makeText(addState.this,"المرجو إختيار الآية من 1 إلى " + nombre_ayats 
		    			,100 ).show();}
		    } else {
			db.addSaveState(sourat_id, from, to, name, null);
			Toast.makeText(addState.this,"حفظ ... ",100 ).show(); 
		    }
			db.close();
			db = new Db(addState.this);
			db.open();
	        Cursor c1 = db.getAllStates();
	        int j=0;
	        String[] states = new String[c1.getCount()];
	    	while(c1.moveToNext())
	        {states[j] = String.valueOf(c1.getString(0));
	    		j++;}
			db.close();
			ArrayAdapter spinnerArrayAdapter1 = new ArrayAdapter(addState.this,android.R.layout.simple_spinner_dropdown_item, states);
		   	 spnStates.setAdapter(spinnerArrayAdapter1); 
			}
			}
		}
		});
   	
}
}