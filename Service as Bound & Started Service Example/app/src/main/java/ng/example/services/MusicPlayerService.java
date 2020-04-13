package ng.example.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ng.example.R;
import ng.example.common.AppConstant;

public class MusicPlayerService extends Service {

    public class MyServiceBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    Intent messageIntent;
    private static final String TAG = "MusicPlayerService";
    private final Binder mBinder = new MyServiceBinder();
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        mPlayer = MediaPlayer.create(this, R.raw.dont_stop_the_party_30sec);
        messageIntent = new Intent(AppConstant.LOCAL_BROADCAST_MANAGER_COMMUNICATION_KEY);

        // Service create
        mPlayer.start();

        mPlayer.setOnCompletionListener(mp -> {
            SendMessage(AppConstant.MUSIC_SERVICE_STATUS_KEY, AppConstant.MUSIC_SERVICE_STATUS_COMPLETE);
            stopSelf();
        });
    }

    private void SendMessage(String key, String message) {
        messageIntent.putExtra(key, message);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(messageIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void play() {
        mPlayer.start();
    }

    public void pause() {
        mPlayer.pause();
    }
}
