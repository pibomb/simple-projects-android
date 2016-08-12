package com.eagleblitz.morris.simplemathquiz;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView questionView;
    TextView inputView;
    TextView scoreView;
    ProgressBar progressBar;
    GridLayout numpadLayout;
    Button resetButton;

    MathQuestion mathQuestion;
    Numpad numpad;

    int correct = 0;
    int answered = 0;
    int total = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionView = (TextView) findViewById(R.id.questionView);
        inputView = (TextView) findViewById(R.id.inputView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        numpadLayout = (GridLayout) findViewById(R.id.numpadLayout);
        resetButton = (Button) findViewById(R.id.reset);

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
    }

    public void reset(View view) {
        correct = 0;
        answered = 0;

        resetButton.setVisibility(View.INVISIBLE);

        start();
    }

    private void startQuestion() {
        mathQuestion = new MathQuestion();

        questionView.setText(mathQuestion.toString());
    }

    private void checkAnswer() {
        String text = "Incorrect!";
        if(mathQuestion.getAnswer() == numpad.getInput()) {
            correct++;
            text = "Correct!";
        }
        answered++;

        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
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
        progressBar.setVisibility(View.INVISIBLE);
        scoreView.setVisibility(View.INVISIBLE);
        questionView.setText("GAME OVER");
        inputView.setText(String.format("Score: %d/%d", correct, total));
        numpadLayout.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.VISIBLE);
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
}
