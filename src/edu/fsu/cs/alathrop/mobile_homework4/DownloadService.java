package edu.fsu.cs.alathrop.mobile_homework4;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;



public class DownloadService extends IntentService {
	
    public final static String URL_MP3 = "https://dl.dropbox.com/s/6e6pn43916nl10i/Bob_Marley-Jammin.mp3?dl=1";
    public Uri uri_audio;
    private long enqueue;
    static DownloadManager download_Manager;
	
	public DownloadService() {
		super("DownloadService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		download_Manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        Request request1 = new Request(Uri.parse(DownloadService.URL_MP3));
        download_Manager.enqueue(request1);

        class MyDownloadReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                uri_audio = download_Manager.getUriForDownloadedFile(id);
                

            }
        }

        registerReceiver(new MyDownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
        Intent i = new Intent("edu.fsu.cs.alathrop.mobile_homework4.UPDATE_READY");
        
        //getApplicationContext().uri_audio = uri_audio;
        i.setData(uri_audio);
        PendingIntent pIntent = (PendingIntent) intent.getParcelableExtra("pending");

        try{
        	pIntent.send(getApplicationContext(), 1, intent);
        }catch(CanceledException e){
        	e.printStackTrace();
        }
		
	}
}
//