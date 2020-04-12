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
import android.widget.Toast;

import ng.example.common.AppConstant;
import ng.example.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity {

    private boolean isServiceBound = false;
    Intent musicServiceIntent;
    Button musicFunctionButton;
    ServiceConnection serviceConnection;
    private MusicPlayerService musicPlayerService;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        basicSettings();
    }

    private void basicSettings() {
        Button startServiceButton = findViewById(R.id.startServiceButton);
        startServiceButton.setOnClickListener(v -> onStartServiceBtnClicked(v));

        Button stopServiceButton = findViewById(R.id.stopServiceButton);
        stopServiceButton.setOnClickListener(v -> onStopBtnClicked(v));

        Button boundServiceButton = findViewById(R.id.boundServiceButton);
        boundServiceButton.setOnClickListener(v -> onBoundServiceBtnClicked(v));

        Button unBoundServiceButton = findViewById(R.id.unBoundServiceButton);
        unBoundServiceButton.setOnClickListener(v -> onUnBoundServiceBtnClicked());

        musicFunctionButton = findViewById(R.id.musicFunctionButton);
        musicFunctionButton.setOnClickListener(v -> musicFunctionBtnClicked());
    }

    private void musicFunctionBtnClicked() {
        if (isServiceBound) {
            if (musicPlayerService.isPlaying()) {
                musicPlayerService.pause();
                musicFunctionButton.setText(R.string.app_play_music);
            } else {
                // startService(intent);
                musicPlayerService.play();
                musicFunctionButton.setText(R.string.app_pause_music);
            }
        } else Toast.makeText(this, "Service is not bounded", Toast.LENGTH_LONG).show();
    }

    private void onUnBoundServiceBtnClicked() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            musicFunctionButton.setText(R.string.app_play_music);
            isServiceBound = false;
        }
    }

    private void onBoundServiceBtnClicked(View v) {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MusicPlayerService.MyServiceBinder myServiceBinder = (MusicPlayerService.MyServiceBinder) iBinder;
                    musicPlayerService = myServiceBinder.getService();
                    isServiceBound = true;
                    if (musicPlayerService.isPlaying()) {
                        musicFunctionButton.setText(R.string.app_pause_music);
                    }
                    Log.d(TAG, "from onBound method,on Service Connected");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                    Log.d(TAG, "from onBound method,on Service Disconnected");
                }
            };
        }
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onStopBtnClicked(View v) {
        stopService(musicServiceIntent);
    }

    public void onStartServiceBtnClicked(View view) {
        startService(musicServiceIntent);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String statusMessage = intent.getStringExtra(AppConstant.MUSIC_SERVICE_STATUS_KEY);
            switch (statusMessage) {
                case AppConstant.MUSIC_SERVICE_STATUS_COMPLETE: {
                    musicFunctionButton.setText(R.string.app_play_music);
                    break;
                }
            }
            Log.d(TAG, "onReceive: Thread name: " + Thread.currentThread().getName());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        musicServiceIntent = new Intent(MainActivity.this, MusicPlayerService.class);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mReceiver, new IntentFilter(AppConstant.LOCAL_BROADCAST_MANAGER_COMMUNICATION_KEY));
    }

    @Override
    protected void onStop() {
        super.onStop();
        onUnBoundServiceBtnClicked();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mReceiver);
    }
}
