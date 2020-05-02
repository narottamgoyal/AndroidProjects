package com.ng.pushnotificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// Youtube : https://www.youtube.com/watch?v=s7ph4x70m7w&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh&index=4
public class MainActivity extends AppCompatActivity {

    EditText userIdEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotificationHelper.Channel_Id,
                    NotificationHelper.Channel_Name, NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription(NotificationHelper.Channel_Des);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        TextView textView = findViewById(R.id.textViewToken);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        userIdEditText = findViewById(R.id.usernameid);
        passwordEditText = findViewById(R.id.passwordid);

        findViewById(R.id.loginButtonId).setOnClickListener(v -> {
            createUser();
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        textView.setText("Token: " + token);
                    } else {
                        textView.setText("Token error: " + task.getException().getMessage());
                    }
                });

        findViewById(R.id.showNotificationButton).setOnClickListener(v -> {
            NotificationHelper.displayNotification(this,
                    "My first notification working",
                    "My first notification message");
        });
    }

    private void createUser() {
        String userId = userIdEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (userId.isEmpty()) {
            userIdEditText.setError("userId is empty");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password is empty or length is less than 6 char");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(userId, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startProfileActivity();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            LoginUser(userId, password);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void LoginUser(String userId, String password) {
        firebaseAuth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startProfileActivity();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            startProfileActivity();
        }
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
