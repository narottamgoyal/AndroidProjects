package ng.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ng.example.common.AppConstant;
import ng.example.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity {

    private Button mPlayButton;
    private boolean mBound = false;
    private MusicPlayerService mMusicPlayerService;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(v -> onBtnMusicClicked(v));
    }

    public void onBtnMusicClicked(View view) {
        if (mBound) {
            if (mMusicPlayerService.isPlaying()) {
                mMusicPlayerService.pause();
                mPlayButton.setText(AppConstant.MUSIC_SERVICE_ACTION_PLAY);
            } else {
                Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
                startService(intent);
                mMusicPlayerService.play();
                mPlayButton.setText(AppConstant.MUSIC_SERVICE_ACTION_PAUSE);
            }
        }
    }

    private ServiceConnection mServiceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MusicPlayerService.MyServiceBinder myServiceBinder = (MusicPlayerService.MyServiceBinder) iBinder;
            mMusicPlayerService = myServiceBinder.getService();
            mBound = true;
            Log.d(TAG, "onServiceConnected");

            if (mMusicPlayerService.isPlaying()) {
                mPlayButton.setText("Pause");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(AppConstant.MUSIC_SERVICE_STATUS_KEY);
            if (result == AppConstant.MUSIC_SERVICE_STATUS_DONE)
                mPlayButton.setText(AppConstant.MUSIC_SERVICE_ACTION_PLAY);
            Log.d(TAG, "onReceive: Thread name: " + Thread.currentThread().getName());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
        bindService(intent, mServiceCon, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mReceiver, new IntentFilter(AppConstant.MUSIC_SERVICE_STATUS_COMPLETE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceCon);
            mBound = false;
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mReceiver);
    }
}
