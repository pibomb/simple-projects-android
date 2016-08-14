package com.eagleblitz.morris.simplemathquiz;

import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    TextView questionView;
    TextView inputView;
    TextView scoreView;
    ProgressBar progressBar;
    GridLayout numpadLayout;
    GridLayout miniMenuLayout;
    Button resetButton;
    Button exitButton;

    MathQuestion mathQuestion;
    Numpad numpad;

    Toast toast;
    Vibrator vibrator;

    int correct = 0;
    int answered = 0;
    int total = 25;

    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionView = (TextView) findViewById(R.id.questionView);
        inputView = (TextView) findViewById(R.id.inputView);
        scoreView = (TextView) findViewById(R.id.scoreView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        numpadLayout = (GridLayout) findViewById(R.id.numpadLayout);
        miniMenuLayout = (GridLayout) findViewById(R.id.miniMenuLayout);

        resetButton = (Button) findViewById(R.id.resetButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        total = getIntent().getIntExtra("total", 25);

        initNumpad();

        start();
    }

    private void start() {
        progressBar.setVisibility(View.VISIBLE);
        scoreView.setVisibility(View.VISIBLE);
        inputView.setText("________");
        numpadLayout.setVisibility(View.VISIBLE);
        updateScore();

        startQuestion();
        startTime = System.currentTimeMillis();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void reset(View view) {
        vibrator.vibrate(25);

        correct = 0;
        answered = 0;

        miniMenuLayout.setVisibility(View.INVISIBLE);

        start();
    }

    public void exit(View view) {
        vibrator.vibrate(25);
        finish();
    }

    private void startQuestion() {
        mathQuestion = new MathQuestion();

        questionView.setText(mathQuestion.toString());
    }

    private void checkAnswer() {
        String text;
        if(mathQuestion.getAnswer() == numpad.getInput()) {
            correct++;
            text = "Correct!";
        } else {
            text = "Incorrect!";
            vibrator.vibrate(250);
        }
        answered++;

        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        updateScore();
    }

    private void updateScore() {
        progressBar.setProgress(correct * 100 / total);
        progressBar.setSecondaryProgress(answered * 100 / total);

        scoreView.setText(String.format("Score: %d/%d", correct, total));
    }

    private void gameOver() {
        long totalTime = System.currentTimeMillis() - startTime;

        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this, String.format("Completed in %.3f seconds.", (double)totalTime/1000), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        scoreView.setVisibility(View.INVISIBLE);
        questionView.setText("FINISHED");
        inputView.setText(String.format("Score: %d/%d", correct, total));
        numpadLayout.setVisibility(View.INVISIBLE);
        miniMenuLayout.setVisibility(View.VISIBLE);
    }

    class Numpad {
        Button[] keys = new Button[10];
        Button backspace;
        Button enter;

        int value = 0;

        Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();

        static final int BACKSPACE = -1;
        static final int ENTER = 10;

        void addListener() {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(25);
                    int id = v.getId();
                    int item = idMap.get(id);
                    if(0 <= item && item <= 9) {
                        value = value * 10 + item;
                        value = Math.min(value, 9999);
                        inputView.setText(String.valueOf(value));
                    } else if(item == BACKSPACE) {
                        value /= 10;
                        if(value == 0) {
                            inputView.setText("________");
                        } else {
                            inputView.setText(String.valueOf(value));
                        }
                    } else if(item == ENTER && !inputView.getText().equals("________")) {
                        checkAnswer();
                        value = 0;
                        inputView.setText("________");
                        if(answered < total) {
                            startQuestion();
                        } else {
                            gameOver();
                        }
                    }
                }
            };

            for(int i = 0; i < keys.length; i++)
                keys[i].setOnClickListener(listener);

            backspace.setOnClickListener(listener);
            enter.setOnClickListener(listener);
        }

        int getInput() {
            return value;
        }
    }


    private void initNumpad() {
        numpad = new Numpad();

        Resources r = getResources();
        String name = getPackageName();

        for(int i = 0; i < numpad.keys.length; i++) {
            int id = r.getIdentifier(String.format("btn_%d", i), "id", name);
            numpad.keys[i] = (Button) findViewById(id);
            numpad.idMap.put(id, i);
        }

        numpad.backspace = (Button) findViewById(R.id.btn_bksp);
        numpad.enter = (Button) findViewById(R.id.btn_enter);

        numpad.idMap.put(R.id.btn_bksp, Numpad.BACKSPACE);
        numpad.idMap.put(R.id.btn_enter, Numpad.ENTER);

        numpad.addListener();
    }
}
