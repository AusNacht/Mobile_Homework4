package edu.fsu.cs.alathrop.mobile_homework4;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class PlaybackService extends Service {
	
    public Uri uri_audio;
    public MediaPlayer media_player;
    
	public PlaybackService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
@Override
public void onCreate(){
	super.onCreate();
	
	//uri_audio = x;
	media_player = new MediaPlayer().create(this, uri_audio);
	media_player.start();
}


  public int onStartCommand(Intent intent, int flags, int startId){
	  
	  return Service.START_STICKY;
  }
 

/*
 * onResume
 * media_player.start();
 */

/*
 * onPause
 * 	    	if(media_player != null) {
	    	media_player.pause();
 */


	
@Override
public void onDestroy(){
	super.onDestroy();
	
	media_player.pause();
	media_player.seekTo(0);
	
	 stopSelf();
}
}
