package com.sc1prabin.neblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Button loginButton, createAcButton;
    private EditText emailTextField, passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginBtnId);
        createAcButton = findViewById(R.id.createAcBtnId);
        emailTextField = findViewById(R.id.emailEtId);
        passwordTextField = findViewById(R.id.passwordEtId);

        createAcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                finish();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }

            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailTextField.getText().toString()) && !TextUtils.isEmpty(passwordTextField.getText().toString())) {
                    String email = emailTextField.getText().toString();
                    String pwd = passwordTextField.getText().toString();

                    login(email, pwd);
                } else {
                    Toast.makeText(MainActivity.this, "Please fill above field", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void login(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, PostListActivity.class);
                    startActivity(i);
                    //startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
/*
email:prabin@sc1prabin.com
password:smellcode
 */