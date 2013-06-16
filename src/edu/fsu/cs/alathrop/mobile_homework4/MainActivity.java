package edu.fsu.cs.alathrop.mobile_homework4;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    public final static String URL_MP3 = "https://dl.dropbox.com/s/6e6pn43916nl10i/Bob_Marley-Jammin.mp3?dl=1";
    public final static String TAG = "MainActivity";
    public Uri uri_audio;
    private long enqueue;
    static DownloadManager download_Manager;
    private MediaPlayer media_player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_Download:
			Log.i(TAG, "Menu Download Clicked");
			
			download_Manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

	        Request request1 = new Request(Uri.parse(MainActivity.URL_MP3));
	        download_Manager.enqueue(request1);

	        class MyDownloadReceiver extends BroadcastReceiver {

	            @Override
	            public void onReceive(Context context, Intent intent) {

	                Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
	                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
	                uri_audio = download_Manager.getUriForDownloadedFile(id);
	                
	      	      	//get album cover
	    	        ImageView iv = (ImageView) findViewById(R.id.iv_CoverArt);
	    	        //line taken from http://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
	    	        iv.setImageDrawable(getResources().getDrawable(R.drawable.bm_pic));
	    			//end get album cover
	    	        
	            }
	        }

	        registerReceiver(new MyDownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			
			return true;
			
		case R.id.menu_Exit:
			Log.i(TAG, "Exit Clicked");

	    	if(media_player != null) {
	    	media_player.pause();
	    	media_player.seekTo(0);
	    	}
	    	
			finish();
			
			return true;
			
		case R.id.action_settings:
			Log.i(TAG, "Action Settings Clicked");
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	//return true;	
	}	
	
	   public void Play(View v){
	    	try{
	    		if(media_player != null) 
	    		{
	    			media_player.start();
	    		}       
	    		else{
	    		media_player = new MediaPlayer().create(this, uri_audio);
	    		media_player.start();
	    		}
	    	}catch(Exception e){
	    		
	    	}
	    }
	    
	    public void Resume(View v){
	    	media_player.start();
	    }
	    
	    public void Pause(View v){
	    	if(media_player != null) {
	    	media_player.pause();
	    	}
	    }
	    
	    
	    public void Stop(View v){
	    	if(media_player != null) {
	    	media_player.pause();
	    	media_player.seekTo(0);
	    	}
	    }	
	
}
