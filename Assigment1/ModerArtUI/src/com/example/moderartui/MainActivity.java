package com.example.moderartui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification.Action;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	
	public static final String TAG = "ModernArtUI";
	
	SeekBar seekBar;
	
	
	Map<Integer, SquareColor> squares = new HashMap<Integer, SquareColor>();
	
	int seekProgress = 0;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUpSquares();
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.i(TAG,"StopTracking");
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.i(TAG,"Start Tracking");
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changeLayoutColor(progressValue);
			}
		});
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
			
			new MyCustomDialog().show(getFragmentManager(), "Alert");
			
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setUpSquares(){
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		
		squares.put(R.id.blackLayout, new SquareColor(R.id.blackLayout, Color.BLACK , Color.MAGENTA));
		squares.put(R.id.redLayout, new SquareColor(R.id.redLayout, Color.RED , Color.GREEN)); //Color.argb(255, 102, 255, 0) 
		squares.put(R.id.blueLayout, new SquareColor(R.id.blueLayout, Color.BLUE ,getResources().getColor(R.color.aqua) ));//Color.argb(255, 52, 203, 173)
		squares.put(R.id.brownLayout, new SquareColor(R.id.brownLayout, getResources().getColor(R.color.brown), getResources().getColor(R.color.yellowish)));
		
		// Color.argb(255, 109, 62, 04)
		//Color.argb(255, 204, 208, 7)
	}
	
	public void changeLayoutColor(int progress){
		
		Iterator<SquareColor> it = squares.values().iterator();
		while(it.hasNext()){
			changeColor(progress, it.next());
		}
		
		
	}
	
	public void changeColor(int progress, SquareColor squareColor){
		int initialC = squareColor.getInitialColor();
		int finalC = squareColor.getFinalColor();
		
		int diffR = getRChannel(finalC) - getRChannel(initialC);
		int diffG = getGChannel(finalC) - getGChannel(initialC);
		int diffB = getBChannel(finalC) - getBChannel(initialC);
		
		int currentR = getRChannel(initialC) + (diffR * progress / 100);
		int currentG = getGChannel(initialC) + (diffG * progress / 100);
		int currentB = getBChannel(initialC) + (diffB * progress / 100);
		
		squareColor.setCurrentColor(Color.argb(255, currentR, currentG, currentB));
		
		setViewColor(squareColor.getId(), squareColor.getCurrentColor());
		
	}
	
	public void setViewColor(int viewId, int color){
		View v = findViewById(viewId);
		v.setBackgroundColor(color);
	}
	
	public int getRChannel(int color){
		int channelR = color ;
		channelR = (channelR >> 16) & 0x000000FF;
		return channelR;
	}
	
	public int getGChannel(int color){
		int channelG = color;
		channelG = (channelG >> 8) & 0x000000FF;	
		return channelG;
	}
	
	public int getBChannel(int color){
		int channelB = color & 0x000000FF;
		return channelB;
	}
	
	
	private class MyCustomDialog extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder
			.setView(getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null))
			.setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse("http://www.moma.org"));
					startActivity(i);
				}
			})
			.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			
			Dialog dialog = builder.create();
			
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onShow(DialogInterface dialog) {
					// TODO Auto-generated method stub
					
					final Drawable drawableP = getResources().getDrawable(R.drawable.my_button);
					final Drawable drawableN = getResources().getDrawable(R.drawable.my_button);
					Button positive = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
					Button negative = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
					if(positive != null && negative != null){
						//positive.setBackgroundColor(getActivity().getResources().getColor(R.color.aqua));
						//negative.setBackgroundColor(getActivity().getResources().getColor(R.color.aqua));
						if(Build.VERSION.SDK_INT >= 16){
							positive.setBackground(drawableP);
							negative.setBackground(drawableN);
						}else{
							positive.setBackgroundDrawable(drawableP);
							negative.setBackgroundDrawable(drawableN);
						}	
					}
						
					
					
					
					
					
					
				}
			});
			
			
			
			return dialog;
			
		}
	}
}
