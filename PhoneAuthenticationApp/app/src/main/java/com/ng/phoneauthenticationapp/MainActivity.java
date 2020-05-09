package com.ng.phoneauthenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String verificaionId;
    private EditText phoneEditTest;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private EditText codeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        phoneEditTest = findViewById(R.id.phonenumberId);
        progressBar = findViewById(R.id.progressBarId);
        progressBar.setVisibility(View.INVISIBLE);
        codeText = findViewById(R.id.verificationCodeId);

        findViewById(R.id.sendOtpButton).setOnClickListener(v -> {
            String phoneNo = phoneEditTest.getText().toString().trim();
            if (phoneNo.isEmpty() || phoneNo.length() < 10) {
                Toast.makeText(this, "phone no is incorrect", Toast.LENGTH_SHORT).show();
            } else {
                verifyPhoneNumber(phoneNo);
            }
        });

        findViewById(R.id.continueButtonId).setOnClickListener(v -> {
            verifyCode(codeText.getText().toString());
        });
    }

    private void verifyPhoneNumber(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBAck
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBAck =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificaionId = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        codeText.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificaionId, code);
        signInwithCredential(credential);
    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();

                             String em =  firebaseAuth.getCurrentUser().getEmail();

                        } else {
                            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
