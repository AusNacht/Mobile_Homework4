package edu.fsu.cs.alathrop.mobile_homework4;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String URL_MP3 = "https://dl.dropbox.com/s/6e6pn43916nl10i/Bob_Marley-Jammin.mp3?dl=1";
	public final static String TAG = "MainActivity";
	public Uri uri_audio;
	private long enqueue;
	static DownloadManager download_Manager;

	final int NOTIFICATION_ID2 = 2;
	NotificationManager mNotificationManager2;
	Notification notification2;
	Intent notificationIntent2;
	PendingIntent contentIntent2;

	Intent myIntent;
	Intent playIntent = new Intent();
	Intent pauseIntent = new Intent();
	Intent stopIntent = new Intent();
	Intent resumeIntent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView tv_artist = (TextView) findViewById(R.id.tv_ArtistName);
		TextView tv_song = (TextView) findViewById(R.id.tv_SongTitle);

		tv_artist.setText("Artist");
		tv_song.setText("Title");

		mNotificationManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notification2 = new Notification(R.drawable.ic_launcher, "Playing",
				System.currentTimeMillis());

		notificationIntent2 = this.getIntent();
		contentIntent2 = PendingIntent.getActivity(this, 0,
				notificationIntent2, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_Download:
			Log.i(TAG, "Menu Download Clicked");

			// /////////////////////////download started notification
			final int NOTIFICATION_ID = 1;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification(
					R.drawable.ic_launcher, "Download Started",
					System.currentTimeMillis());

			// Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent2, 0);

			notification.setLatestEventInfo(this, "Homework 4",
					"Download Started", contentIntent);

			notification.flags = Notification.FLAG_AUTO_CANCEL;

			mNotificationManager.notify(NOTIFICATION_ID, notification);
			// //////////////////////////// end notification

			download_Manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

			Request request1 = new Request(Uri.parse(MainActivity.URL_MP3));
			download_Manager.enqueue(request1);

			class MyDownloadReceiver extends BroadcastReceiver {

				@Override
				public void onReceive(Context context, Intent intent) {

					long id = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);
					uri_audio = download_Manager.getUriForDownloadedFile(id);

					// get album cover
					ImageView iv = (ImageView) findViewById(R.id.iv_CoverArt);
					// line taken from
					// http://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.bm_pic));
					// end get album cover

					String delims = "[/]+";
					String[] tokens = URL_MP3.split(delims);

					String artist = tokens[4];
					delims = "[-]+";
					tokens = artist.split(delims);
					artist = tokens[0];
					artist = artist.replaceAll("_", " ");

					String song = tokens[1];
					delims = "[.]+";
					tokens = song.split(delims);
					song = tokens[0];
					song = song.replaceAll("_", " ");

					TextView tv_artist = (TextView) findViewById(R.id.tv_ArtistName);
					TextView tv_song = (TextView) findViewById(R.id.tv_SongTitle);

					tv_artist.setText(artist);
					tv_song.setText(song);

					finNotify();
				}

			}

			registerReceiver(new MyDownloadReceiver(), new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));

			return true;

		case R.id.menu_Exit:
			Log.i(TAG, "Exit Clicked");

			notification2.setLatestEventInfo(this, "Homework 4", "Stop",
					contentIntent2);

			mNotificationManager2.cancelAll();

			// /////////////////////////////////////////////////////////////////////////////////////
			stopIntent.setAction("ed.fsu.cs.alathrop.playbackservice.stop");
			getApplicationContext().sendBroadcast(stopIntent);

			// ////////////////////////////////////////////////////////////////////////////////////

			finish();

			return true;

		case R.id.action_settings:
			Log.i(TAG, "Action Settings Clicked");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		// return true;
	}

	public void onDestroy() {
		super.onDestroy();
		stopService(myIntent);
	}
	
	private void sendMessageToService(int intvaluetosend){
		
	}

	public void Play(View v) {	
		playIntent.setAction("ed.fsu.cs.alathrop.playbackservice.play");
		getApplicationContext().sendBroadcast(playIntent);
		
				notification2.setLatestEventInfo(this, "Homework 4", "Playing",
						contentIntent2);

				notification2.flags = Notification.FLAG_NO_CLEAR;

				mNotificationManager2.notify(NOTIFICATION_ID2, notification2);
				// ////////////////notification which won't go away
	}

	public void Resume(View v) {
		resumeIntent.setAction("ed.fsu.cs.alathrop.playbackservice.resume");
		getApplicationContext().sendBroadcast(resumeIntent);
	}

	public void Pause(View v) {
		pauseIntent.setAction("ed.fsu.cs.alathrop.playbackservice.pause");
		getApplicationContext().sendBroadcast(pauseIntent);
		
			notification2.setLatestEventInfo(this, "Homework 4", "Pause",
					contentIntent2);

			mNotificationManager2.cancel(NOTIFICATION_ID2);

			// ////////////////clear permanent notification
	}

	public void Stop(View v) {
		stopIntent.setAction("ed.fsu.cs.alathrop.playbackservice.stop");
		getApplicationContext().sendBroadcast(stopIntent);

			notification2.setLatestEventInfo(this, "Homework 4", "Stop",
					contentIntent2);

			mNotificationManager2.cancel(NOTIFICATION_ID2);

		// ////////////////clear permanent notification
	}

	public void finNotify() {

		// ////////////////////////////////download complete notification
		final int NOTIFICATION_ID3 = 3;
		NotificationManager mNotificationManager3 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification3 = new Notification(R.drawable.ic_launcher,
				"Download Completed", System.currentTimeMillis());

		// Intent notificationIntent3 = new Intent(this, MainActivity.class);
		PendingIntent contentIntent3 = PendingIntent.getActivity(this, 0,
				notificationIntent2, 0);

		notification3.setLatestEventInfo(this, "Homework 4",
				"Download Completed", contentIntent3);

		notification3.flags = Notification.FLAG_AUTO_CANCEL;

		mNotificationManager3.notify(NOTIFICATION_ID3, notification3);
		// ///////////////// end notification
		
		myIntent = new Intent(this, PlaybackService.class);
		myIntent.putExtra("uri", uri_audio);
		startService(myIntent);
	}
}
//