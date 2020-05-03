package com.ng.pushnotificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://push-notification-api-server.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.sendPushNotification(user.token, title, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(SendNotificationActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SendNotificationActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
