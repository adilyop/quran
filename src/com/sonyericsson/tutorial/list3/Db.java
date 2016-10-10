package com.sonyericsson.tutorial.list3;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Db {
    private static String DB_DIR = "/data/data/com.sonyericsson.tutorial.list3/databases/";
    private static String DB_NAME = "koran.sqlite";
    private static String DB_PATH = DB_DIR + DB_NAME;
    
	
	private DataBaseHelper mDbHelper;
	private SQLiteDatabase db;
	private Context context;
	

	public Db(Context context) {
		this.context=context;
		mDbHelper=new DataBaseHelper(this.context);
	}
	
	class DataBaseHelper extends SQLiteOpenHelper
	{
	    private boolean createDatabase = false;
	    @SuppressWarnings("unused")
		private boolean upgradeDatabase = false;
		Context context;
		
		
		public DataBaseHelper(Context context) {
			super(context, DB_NAME, null, 1);
			this.context=context;
		}
		
		
		
				
		public void initializeDataBase() {

	        getWritableDatabase();

	        if (createDatabase) {
	            try {
	                copyDataBase();
	            } catch (IOException e) {
	                throw new Error("Error copying database");
	            }
	        } 

	    }   
	    private void copyDataBase() throws IOException {
	        InputStream input = context.getAssets().open(DB_NAME);
	        OutputStream output = new FileOutputStream(DB_PATH);
	        
	            byte[] buffer = new byte[1024];
	            int length;

	            try {
	                while ((length = input.read(buffer)) > 0) {
	                    output.write(buffer, 0, length);
	                }
	            }
	            
	            finally {
	                try {
	                    if (output != null) {
	                        try {
	                            output.flush();
	                        } finally {
	                            output.close();
	                        }
	                }
	                } finally {
	                    if (input != null) {
	                        input.close();
	                    }
	                }
	            }
	        
	        getWritableDatabase().close();
	    }
	    public void onCreate(SQLiteDatabase db) {
	        
	        createDatabase = true;    
	    }
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        
	        upgradeDatabase = true;

	    }
	   
	    
	    public void onOpen(SQLiteDatabase db) {
	        super.onOpen(db);
	    }
	}
	
	
	public Db open()
	{
		mDbHelper.initializeDataBase();
		if(db==null)
			db=mDbHelper.getWritableDatabase();
		return this;
	}
	public void close()
	{
		db.close();
	}
	
	public Cursor getSourat(int index){		
		Cursor c=db.query("sourats", new String[]{"* "}, "_id="+index, null, null, null, null);
		return c;
	}
	public Cursor getAllSourats(){		
		Cursor c=db.query("sourats", new String[]{"sourat"}, null, null, null, null, null);
		return c;
	}

	public Cursor getAllStates(){		
		Cursor c=db.query("save_state", new String[]{"name"}, null, null, null, null, null);
		return c;
	}
	public String getSouratLengthByID(int index){		
		Cursor c=db.query("sourats", new String[]{"nombre_ayats"}, "_id="+index, null, null, null, null);
		c.moveToFirst();
		String sourat=String.valueOf(c.getString(0));
		return sourat;
	}
	
	public Cursor getAyatsBySouratID(int souratID, int fromAyat, int ToAyat){
		Cursor c=db.query("ayats", new String[]{"meaning","_id"}, "chapter="+souratID +" and verse between "+fromAyat +" and "+ToAyat, null, null, null, null);
		return c;
	}

	public Cursor getAyatBySouratID(int souratID, int ayat){
		Cursor c=db.query("ayats", new String[]{"meaning","_id"}, "chapter="+souratID +" and verse like '"+ayat +"'" , null, null, null, null);
		return c;
	}
	public Cursor getInfoSourat(String sourat){		
		Cursor c=db.query("sourats", new String[]{"nombre_ayats"}, "sourat like "+"'%"+sourat+"%'", null, null, null, null);
		return c;
	}
	public Cursor getSouratIDFromSouratName(String sourat){		
		Cursor c=db.query("sourats", new String[]{"_id"}, "sourat like "+"'%"+sourat+"%'", null, null, null, null);
		return c;
	}
	public Cursor getConfiguration(int a){		
		Cursor c=db.query("configurations", new String[]{"* "}, "_id ="+a, null, null, null, null);
		return c;
	}
	public Cursor getSaveState(){		
		Cursor c=db.query("save_state", new String[]{"* "}, null, null, null, null, null);
		return c;
	}
	public Cursor getSaveStateByName(String name){		
		Cursor c=db.query("save_state", new String[]{"* "}, "name ='"+name+"'", null, null, null, null);
		return c;
	}
	public Cursor findAyatByWord(String word){		
		Cursor c=db.query("ayats", new String[]{"* "}, "meaning like "+"'%"+word+"%' and CONTINENT like 'Europe'", null, null, null, null);
		return c;
	}
	
	public void deleteSaveState(String state){		
		db.execSQL("DELETE from save_state where  name= '" + state + "';");
	}
	
	public void addSaveState(int sourat_id, int from,int to, String name, String date){		
		db.execSQL("INSERT INTO save_state(`sourat_id`,`from`,`to`,`name`,`date`) VALUES (" +sourat_id + "," +from + "," +to + ",'" +name + "',NULL);");
	}

	public void updateSaveState(int sourat_id, int from,int to, String name, String date){		
		db.execSQL("UPDATE `save_state` SET `to`=" +to + ", 'from'=" +from + ", 'sourat_id'=" +sourat_id + ", 'date'=" +date + "  WHERE name='" + name + "';");
	}
	
	public void updateMyConfig(int config, String value){		
		db.execSQL("UPDATE `configurations` SET `config"+config+"`='" +value + "'  WHERE _id=1;");
	}
	public Cursor findCapOceanie(String a){		
		Cursor c=db.query("datapays", new String[]{"CAPITALE "}, "CAPITALE like "+"'%"+a+"%' and CONTINENT like 'Océanie'", null, null, null, null);
		return c;
	}
	
	public int getId1(String a){		
		Cursor c=db.query("datapays", new String[]{"_id"}, "NOM like "+"'%"+a+"%'", null, null, null, null);
		c.moveToFirst();
		if (c.getCount() == 0){return 0;}
			int id=Integer.parseInt(c.getString(0));
			return id;
			
	}
	
	public int getId2(String a){		
		Cursor c=db.query("datapays", new String[]{"_id"}, "CAPITALE like "+"'%"+a+"%'", null, null, null, null);
		c.moveToFirst();
		if (c.getCount() == 0){return 0;}
		int id=Integer.parseInt(c.getString(0));
		return id;
		
	}
	/////////////////
	public String getScore(int a){		
		Cursor c=db.query("datascore", new String[]{"score"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		
		String id=String.valueOf(c.getString(0));
		return id;

	}
	
	public String getSurface(int a){		
		Cursor c=db.query("datapays", new String[]{"SURFACE"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		
		String id=String.valueOf(c.getString(0));
		return id;

	}
	
	
	public String getPop(int a){		
		Cursor c=db.query("datapays", new String[]{"POPULATION"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		String id=String.valueOf(c.getString(0));
		return id;
	}
	public String getLan(int a){		
		Cursor c=db.query("datapays", new String[]{"LANGUE"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		String id=String.valueOf(c.getString(0));
		return id;
	}
	public String getCont(int a){		
		Cursor c=db.query("datapays", new String[]{"CONTINENT"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		
		String id=String.valueOf(c.getString(0));
		return id;
	}
	public String getMonn(int a){		
		Cursor c=db.query("datapays", new String[]{"MONNAIE"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		String id=String.valueOf(c.getString(0));
		return id;

	}
	public String getscor1(int a){		
		Cursor c=db.query("datascore", new String[]{"score"}, "_id= "+a, null, null, null, null);
		c.moveToFirst();
		String id=String.valueOf(c.getString(0));
		return id;
	}
	public void changer(int id,int val){
		if (id == 1){	
		db.execSQL("UPDATE datascore SET score= "+val+" WHERE _id= 1;");
		}
		if (id == 2){	
			db.execSQL("UPDATE datascore SET score= "+val+" WHERE _id= 2;");
			}
	}
}