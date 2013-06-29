package edu.fsu.cs.alathrop.mobile_homework4;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

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
	public void onCreate() {
		super.onCreate();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		if(extras !=null)
			uri_audio = (Uri) extras.get("uri");
		
		media_player = new MediaPlayer().create(this, uri_audio);
		
		IntentFilter playFilter = new IntentFilter("ed.fsu.cs.alathrop.playbackservice.play");
		playFilter.addAction("ed.fsu.cs.alathrop.playbackservice.play");
		registerReceiver(playReceiver, playFilter);
		
		IntentFilter resumeFilter = new IntentFilter("ed.fsu.cs.alathrop.playbackservice.resume");
		resumeFilter.addAction("ed.fsu.cs.alathrop.playbackservice.resume");
		registerReceiver(resumeReceiver, resumeFilter);
		
		IntentFilter pauseFilter = new IntentFilter("ed.fsu.cs.alathrop.playbackservice.pause");
		pauseFilter.addAction("ed.fsu.cs.alathrop.playbackservice.pause");
		registerReceiver(pauseReceiver, pauseFilter);
		
		IntentFilter stopFilter = new IntentFilter("ed.fsu.cs.alathrop.playbackservice.stop");
		stopFilter.addAction("ed.fsu.cs.alathrop.playbackservice.stop");
		registerReceiver(stopReceiver, stopFilter);
		
		return Service.START_STICKY;
	}
	
	private BroadcastReceiver playReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			play();
			
		}
	};
	
	private BroadcastReceiver resumeReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			resume();
			
		}
	};
	
	private BroadcastReceiver pauseReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			pause();
			
		}
	};
	
	private BroadcastReceiver stopReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			stop();
			
		}
	};
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		media_player.pause();
		media_player.seekTo(0);

		stopSelf();
	}

	public void play() {
		try {
			if (media_player != null) {
				media_player.start();
			} else {
				media_player = new MediaPlayer().create(this, uri_audio);
				media_player.start();
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		media_player.start();
	}

	public void pause() {
		if (media_player != null) {
			media_player.pause();
		}
	}

	public void stop() {
		if (media_player != null) {
			media_player.pause();
			media_player.seekTo(0);
		}
	}
}
//