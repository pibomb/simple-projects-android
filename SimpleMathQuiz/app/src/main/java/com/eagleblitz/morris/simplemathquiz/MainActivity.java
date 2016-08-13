package com.eagleblitz.morris.simplemathquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button quickButton, standardButton, extendedButton, marathonButton;
    Map<Button, Integer> questions = new HashMap<Button, Integer>();

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quickButton = (Button) findViewById(R.id.btn_quick);
        standardButton = (Button) findViewById(R.id.btn_standard);
        extendedButton = (Button) findViewById(R.id.btn_extended);
        marathonButton = (Button) findViewById(R.id.btn_marathon);

        questions.put(quickButton, 10);
        questions.put(standardButton, 25);
        questions.put(extendedButton, 50);
        questions.put(marathonButton, 100);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void play(int q) {
        Intent intent = new Intent();
        intent.setClass(this, QuizActivity.class);
        intent.putExtra("total", q);
        startActivity(intent);
    }

    public void mainMenuSelection(View view) {
        if(view instanceof Button) {
            vibrator.vibrate(25);
            Integer q = questions.get(view);

            if(q != null)
                play(q);
        }
    }
}
