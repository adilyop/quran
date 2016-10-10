
package com.sonyericsson.tutorial.list3;


import android.view.KeyEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test activity to display the list view
 */
public class TestActivity extends Activity {

	AlertDialog levelDialog ;
    /** Id for the toggle rotation menu item */
    private static final int TOGGLE_ROTATION_MENU_ITEM = 0;

    /** Id for the toggle lighting menu item */
    private static final int TOGGLE_LIGHTING_MENU_ITEM = 1;

    /** The list view */
    private MyListView mListView;

    /**
     * Small class that represents a contact
     */
    private static class Contact {

        /** Name of the contact */
        String mName;

        /** Phone number of the contact */
        String mNumber;

        /**
         * Constructor
         * 
         * @param name The name
         * @param number The number
         */
        public Contact(final String name, final String number) {
            mName = name;
            mNumber = number;
        }

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);

        final ArrayList<Contact> contacts = createContactList(20);
        final MyAdapter adapter = new MyAdapter(this, contacts);

        mListView = (MyListView)findViewById(R.id.my_list);
        mListView.setAdapter(adapter);

        mListView.setDynamics(new SimpleDynamics(0.9f, 0.6f));

        /*mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			public boolean onItemLongClick(final AdapterView<?> arg0,final View arg1,
					final int arg2, final long arg3) {
				Toast.makeText(TestActivity.this,"OnItemLongClickListener.. ",100 ).show();
				updateState.position = (int) arg3;
            	Intent in = new Intent(TestActivity.this,updateState.class);
    			startActivity(in);		
				return true;
			}
		});*/
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {
				Toast.makeText(TestActivity.this,"OnItemClickListener.. ",100 ).show();
            	final String name = contacts.get(position).mName;
                Db db;
                db=new Db(TestActivity.this);
                db.open();
                Cursor c = db.getSaveStateByName(name);
            	c.moveToNext();
    			result.songs = Arrays.copyOf(result.songs, 0);
    			result.souratID = c.getInt(1);
    			result.fromAyat = c.getString(2);
    			result.toAyat = c.getString(3);
            	db.close();
            	result.back = 1;
            	Intent in = new Intent(TestActivity.this,result.class);
    			startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(Menu.NONE, TOGGLE_ROTATION_MENU_ITEM, 0, "Toggle Rotation");
        menu.add(Menu.NONE, TOGGLE_LIGHTING_MENU_ITEM, 1, "Toggle Lighting");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case TOGGLE_ROTATION_MENU_ITEM:
                mListView.enableRotation(!mListView.isRotationEnabled());
                return true;

            case TOGGLE_LIGHTING_MENU_ITEM:
                mListView.enableLight(!mListView.isLightEnabled());
                return true;

            default:
                return false;
        }
    }

    /**
     * Creates a list of fake contacts
     * 
     * @param size How many contacts to create
     * @return A list of fake contacts
     */
    private ArrayList<Contact> createContactList(final int size) {
        final ArrayList<Contact> contacts = new ArrayList<Contact>();
       /* for (int i = 0; i < size; i++) {
            contacts.add(new Contact("Contact Number " + i, "+46(0)"
                    + (int)(1000000 + 9000000 * Math.random())));
        }*/
        //souratName+"("+from+"-"+to+")"
        Db db;
        db=new Db(TestActivity.this);
        db.open();
        Cursor c = db.getSaveState();
    	while(c.moveToNext())
        {
    		String stateName = c.getString(4);
        int souratID = c.getInt(1);
        int from = c.getInt(2);
        int to = c.getInt(3);
        Cursor c2 = db.getSourat(souratID);
        c2.moveToNext();
        String souratName = c2.getString(1);
    		contacts.add(new Contact(stateName, souratName+"("+from+"-"+to+")"));
    		}

        //String souratLength = db.getSouratLengthByID(index+1);
    	db.close();
        return contacts;
    }

    /**
     * Adapter class to use for the list
     */
    private static class MyAdapter extends ArrayAdapter<Contact> {

        /** Re-usable contact image drawable */
        private final Drawable contactImage;

        /**
         * Constructor
         * 
         * @param context The context
         * @param contacts The list of contacts
         */
        public MyAdapter(final Context context, final ArrayList<Contact> contacts) {
            super(context, 0, contacts);
            contactImage = context.getResources().getDrawable(R.drawable.button_play_red);
        }
        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            }
           // view
            //animate();
            //setAlpha((float) 0.5);
            //setAlpha((float) 0.5);
            //view.setAlpha((float) 0.5);
            final TextView name = (TextView)view.findViewById(R.id.contact_name);
            name.setText(getItem(position).mName);

            final TextView number = (TextView)view.findViewById(R.id.contact_number);
            number.setText(getItem(position).mNumber);

            final ImageView photo = (ImageView)view.findViewById(R.id.image_list);
            photo.setImageDrawable(contactImage);
            return view;
        }
    }

    /**
     * A very simple dynamics implementation with spring-like behavior
     */
    class SimpleDynamics extends Dynamics {

        /** The friction factor */
        private float mFrictionFactor;

        /** The snap to factor */
        private float mSnapToFactor;

        /**
         * Creates a SimpleDynamics object
         * 
         * @param frictionFactor The friction factor. Should be between 0 and 1.
         *            A higher number means a slower dissipating speed.
         * @param snapToFactor The snap to factor. Should be between 0 and 1. A
         *            higher number means a stronger snap.
         */
        public SimpleDynamics(final float frictionFactor, final float snapToFactor) {
            mFrictionFactor = frictionFactor;
            mSnapToFactor = snapToFactor;
        }

        @Override
        protected void onUpdate(final int dt) {
            // update the velocity based on how far we are from the snap point
            mVelocity += getDistanceToLimit() * mSnapToFactor;

            // then update the position based on the current velocity
            mPosition += mVelocity * dt / 1000;

            // and finally, apply some friction to slow it down
            mVelocity *= mFrictionFactor;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent in = new Intent(TestActivity.this,QuranActivity.class);
	    	startActivity(in);
	        return true;
	    } else if (keyCode == KeyEvent.KEYCODE_BREAK){
	    	
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
