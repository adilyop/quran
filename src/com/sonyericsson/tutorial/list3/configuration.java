package com.sonyericsson.tutorial.list3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;



public class configuration extends Activity{
	AlertDialog levelDialog ;
	CheckBox check1,check2;
	Db db;
	String taille;
	String typeText;
	String timeRepeate; 
	Spinner spn;
	private ListView lv1;
	private String lv_arr[]={"  حجم الخط  ","  نوع الخط ", "  عدد تكرار الصوت  ","  تحديث قائمة المفضلة  ","  عرض الإعدادات  ", "  إختيار المقرئ  "};
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.configuration,
                null, false);
        lv1=(ListView)findViewById(R.id.listView1);
        lv1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , lv_arr));
        lv1.setTextFilterEnabled(true);
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		if (position == 0){
        			final CharSequence[] items = {" كبير "," متوسط "," صغير "};
        			AlertDialog.Builder builder = new AlertDialog.Builder(configuration.this);
                    builder.setTitle("إختر حجم الخط");
                    builder.setPositiveButton(" إلغاء ", null);
                    builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item)
                        {
                            case 0:
                    	        Toast.makeText(configuration.this,"ss "+item,100 ).show();
                    	        taille = "22";
                                     break;
                            case 1:
                            	taille = "18";
                                    break;
                            case 2:
                                taille = "15";    
                            	break;
                        }

                        db=new Db(configuration.this);
                        db.open();
                        db.updateMyConfig(1,taille);
                    	db.close();
                        levelDialog.dismiss();    
                        }
                    });

                    levelDialog = builder.create();
                    levelDialog.show();
        		} 
        		else if (position == 1){
        			final CharSequence[] items = {" Normal "," Etalique "," Gras "};
        			AlertDialog.Builder builder = new AlertDialog.Builder(configuration.this);
                    builder.setTitle("إختيار نوع الخط");
                    builder.setPositiveButton(" إلغاء ", null);
                    builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item)
                        {
                            case 0:
                    	        Toast.makeText(configuration.this,"ss "+item,100 ).show();
                                    typeText = "NORMAL";
                                     break;
                            case 1:
                            	typeText = "ITALIC";
                                    
                                    break;
                            case 2:
                                   typeText = "BOLD";
                                    break;
                            
                        }
                        db=new Db(configuration.this);
                        db.open();
                        db.updateMyConfig(2,typeText);
                    	db.close();
                        levelDialog.dismiss();   

                        }
                    });
                    levelDialog = builder.create();
                    levelDialog.show();
        		} else if (position == 2){
        			AlertDialog.Builder builder = new AlertDialog.Builder(configuration.this);
                    builder.setTitle("عدد تكرار الصوت");
                    
                    final EditText input = new EditText(configuration.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setNegativeButton(" إلغاء ", null);
                    builder.setPositiveButton(" حفظ ", new OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							timeRepeate = String.valueOf(input.getText());

							if (timeRepeate.trim().length() == 0){
								Toast.makeText(configuration.this,"حقل نص فارغ ... ",100 ).show();
							
							} else {
							db=new Db(configuration.this);
		                    db.open();
		                    db.updateMyConfig(3,timeRepeate);
		                	db.close();
							Toast.makeText(configuration.this,"حفظ ... ",100 ).show();                  	        

						}
						}
					});
                    levelDialog = builder.create();
                    levelDialog.show();
        		}  else if (position == 3){
        			Intent in2 = new Intent(configuration.this,updateState.class);
        			startActivity(in2);
        		} else if (position == 4){
        			AlertDialog.Builder builder = new AlertDialog.Builder(configuration.this);
                    builder.setTitle("عرض الإعدادات");
        			db=new Db(configuration.this);
                    db.open();
                    Cursor c = db.getConfiguration(1);
                    c.moveToNext();
                    String temType = c.getString(3);
                    int tempTaille2 = c.getInt(2);
                    String tempTaille = "";
                    if (tempTaille2 == 23)
                    	tempTaille = " صغير ";
                    else if (tempTaille2 == 25)
                    	tempTaille = " متوسط ";
                    else 
                    	tempTaille = " كبير ";
                    String tempRepeate = c.getString(4);
                	db.close();
                	builder.setPositiveButton(" إلغاء ", null);
                	builder.setMessage("نوع الخط : "+temType
                			+"\nحجم الخط : "+tempTaille
                			+"\nعدد تكرار الصوت : "+tempRepeate);
                    levelDialog = builder.create();
                    levelDialog.show();
        		} else if (position == 5){
        			final CharSequence[] items = {" إبراهيم الأخضر "};
        			AlertDialog.Builder builder = new AlertDialog.Builder(configuration.this);
                    builder.setTitle("اختيار المقرئ");
                    builder.setPositiveButton(" إلغاء ", null);
                    builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        levelDialog.dismiss();    
                        }
                    });

                    levelDialog = builder.create();
                    levelDialog.show();
        		}
        	}
        	});
	
	}
}
