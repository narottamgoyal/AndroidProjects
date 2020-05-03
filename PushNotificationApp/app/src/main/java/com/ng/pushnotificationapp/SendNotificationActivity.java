package com.ng.pushnotificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SendNotificationActivity extends AppCompatActivity {

    private TextView toTextView;
    private EditText titleText, bodyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        User user = (User) getIntent().getSerializableExtra("user");
        toTextView = findViewById(R.id.toEmailId);

        toTextView.setText("To: " + user.email.toString());

        titleText = findViewById(R.id.notificationTitleId);
        bodyText = findViewById(R.id.notificationBodyId);

        findViewById(R.id.sendNotificationButton).setOnClickListener(v -> {
            sendNotification(user);
        });
    }

    private void sendNotification(User user) {

        String title = titleText.getText().toString().trim();
        String body = bodyText.getText().toString().trim();

        if (title.isEmpty()) {
            titleText.setError("title is empty");
            return;
        }

        if (body.isEmpty()) {
            bodyText.setError("title is empty");
            return;
        }
    }
}
