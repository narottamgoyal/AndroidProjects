package com.ng.pushnotificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private final String Node_User = "users";
    private List<User> userList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.userListProgressId);
        //progressBar.setVisibility(View.INVISIBLE);
        LoadUsers();

        //NotificationHelper.displayNotification(ProfileActivity.this, "this is title", "here is your message");

        FirebaseMessaging.getInstance().subscribeToTopic("MessagingTopicName");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        saveToken(token);
                    } else {
                    }
                });

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            IsUserLoggedIn();
        });
    }

    private void LoadUsers() {
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Node_User);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dbUser : dataSnapshot.getChildren()) {
                        User user = dbUser.getValue(User.class);
                        userList.add(user);
                    }

                    UserAdapter userAdapter = new UserAdapter(ProfileActivity.this, userList);
                    recyclerView.setAdapter(userAdapter);
                } else {
                    Toast.makeText(ProfileActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IsUserLoggedIn();
    }

    private void IsUserLoggedIn() {
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void saveToken(String token) {
        String email = firebaseAuth.getCurrentUser().getEmail();
        User user = new User(email, token);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Node_User);
        databaseReference.child(firebaseAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(task -> {
            Toast.makeText(ProfileActivity.this, "Token saved", Toast.LENGTH_SHORT).show();
        });
    }
}
