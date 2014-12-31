package com.example.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private static final String TAG = "DailySelfie";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	public static final File SELFIE_DIR = 
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/DailySelfie");
	
	private SelfieAdapter mSelfieAdapter;

	private File mLastPictureFile; 
	
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView listView = getListView();
		
		mSelfieAdapter = new SelfieAdapter(this);
		listView.setAdapter(mSelfieAdapter);
		
		registerForContextMenu(listView);
		
		setAlarms();
	}
	
	public void setAlarms(){
		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		// Create an Intent to broadcast to the AlarmNotificationReceiver
				mNotificationReceiverIntent = new Intent(this,
						NotificationReceiver.class);
		
				// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				this, 0, mNotificationReceiverIntent, 0);
				
		// Set single alarm
		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + INITIAL_ALARM_DELAY, INITIAL_ALARM_DELAY,
				mNotificationReceiverPendingIntent);
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    //File storageDir = Environment.getExternalStoragePublicDirectory(
	     //       Environment.DIRECTORY_PICTURES+"/DailySelfie");
	    SELFIE_DIR.mkdirs();
	    
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        SELFIE_DIR      /* directory */
	    );

	    return image;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
				
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Log.i(TAG,"On Activity Result - all was good");
			mSelfieAdapter.add(mLastPictureFile);
	    }else if (resultCode == RESULT_CANCELED){
	    	Log.i(TAG,"On Activity Result - User cancelled");
	    	if(mLastPictureFile != null){
	    		Log.i(TAG,"On Activity Result - deleting picture");
	    		mLastPictureFile.delete();
	    	}
	    }
	}

	private void dispatchTakePictureIntent() {
		Log.i(TAG,"Taking picture");
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	        	Log.i(TAG,"Creating image file");
	            photoFile = createImageFile();
	            Log.i(TAG,"Image file:"+photoFile.toString());
	        } catch (IOException ex) {
	            // Error occurred while creating the File

	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	
	        	mLastPictureFile = photoFile;
	        	
	        	Log.i(TAG,"Starting camera intent");
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	            //startActivity(takePictureIntent);
	        }
	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if(id == R.id.action_camera){
			
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.context_delete:
			
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
}
