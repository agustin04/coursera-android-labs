package com.example.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private static final String TAG = "DailySelfie";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	public static final File SELFIE_DIR = 
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/DailySelfie");
	
	private SelfieAdapter mSelfieAdapter;

	private File mLastPictureFile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView listView = getListView();
		
		mSelfieAdapter = new SelfieAdapter(this);
		listView.setAdapter(mSelfieAdapter);
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
		if (id == R.id.action_settings) {
			return true;
		}
		
		if(id == R.id.action_camera){
			
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
