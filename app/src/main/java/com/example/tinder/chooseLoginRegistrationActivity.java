package com.example.tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseLoginRegistrationActivity extends AppCompatActivity {


    private Button mLogin,mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration2);

    mLogin = (Button) findViewById(R.id.Login);
    mRegister = (Button) findViewById(R.id.register);

    mLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent= new Intent(chooseLoginRegistrationActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    });

    mRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent= new Intent(chooseLoginRegistrationActivity.this,RegistrationActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    });
    }
}