package com.eagleblitz.morris.simplelogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText userText;
    EditText passText;
    TextView welcomeView;

    Button btn_login;

    ImageView lockView;

    boolean loggedIn = false;
    int attempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = (EditText) findViewById(R.id.username);
        passText = (EditText) findViewById(R.id.password);
        welcomeView = (TextView) findViewById(R.id.welcomeView);

        btn_login = (Button) findViewById(R.id.btn_login);

        lockView = (ImageView) findViewById(R.id.lockView);
    }

    public void login(View view) {
        if(loggedIn) {
            lockView.setImageResource(R.drawable.lock);
//            attempts = 5;
            userText.setVisibility(View.VISIBLE);
            passText.setVisibility(View.VISIBLE);
            welcomeView.setVisibility(View.INVISIBLE);
            btn_login.setText("LOGIN");
            loggedIn = false;
        } else {
            String username = userText.getText().toString();
            String password = passText.getText().toString();

            if (username.equals("admin") && password.equals("icecream")) {
                lockView.setImageResource(R.drawable.unlock);
                userText.setVisibility(View.INVISIBLE);
                passText.setVisibility(View.INVISIBLE);
                welcomeView.setVisibility(View.VISIBLE);
                btn_login.setText("LOGOUT");
                loggedIn = true;
            } else {
                attempts--;

            }
        }
    }
}
