package com.eagleblitz.morris.simplemathquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mainMenuLayout;

    RelativeLayout playLayout;
    Button quickButton, standardButton, extendedButton, marathonButton;
    Map<Button, Integer> questions = new HashMap<Button, Integer>();

    TextView instructionsTextView;

    View currentView;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuLayout = (RelativeLayout) findViewById(R.id.mainMenuLayout);

        playLayout = (RelativeLayout) findViewById(R.id.playLayout);

        quickButton = (Button) findViewById(R.id.btn_quick);
        standardButton = (Button) findViewById(R.id.btn_standard);
        extendedButton = (Button) findViewById(R.id.btn_extended);
        marathonButton = (Button) findViewById(R.id.btn_marathon);

        instructionsTextView = (TextView) findViewById(R.id.instructionsTextView);

        questions.put(quickButton, 10);
        questions.put(standardButton, 25);
        questions.put(extendedButton, 50);
        questions.put(marathonButton, 100);

        currentView = mainMenuLayout;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void mainMenuPlay(View view) {
        vibrator.vibrate(10);
        Animations.fadeSwitch(this, mainMenuLayout, playLayout);
        currentView = playLayout;
    }

    public void mainMenuInstructions(View view) {
        vibrator.vibrate(10);
        Animations.fadeSwitch(this, mainMenuLayout, instructionsTextView);
        currentView = instructionsTextView;
    }

    public void gameSelection(View view) {
        if(view instanceof Button) {
            vibrator.vibrate(10);
            Integer q = questions.get(view);

            if(q != null)
                play(q);
        }
    }

    private void play(int q) {
        Animations.fadeSwitch(this, currentView, mainMenuLayout);
        currentView = mainMenuLayout;

        Intent intent = new Intent();
        intent.setClass(this, QuizActivity.class);
        intent.putExtra("total", q);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Animations.fadeSwitch(this, currentView, mainMenuLayout);
            currentView = mainMenuLayout;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
