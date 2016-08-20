package com.eagleblitz.morris.simplemathquiz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
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

public class QuizActivity extends AppCompatActivity implements StatisticsDialog.OnFragmentInteractionListener {

    TextView questionView;
    TextView inputView;
    TextView scoreView;
    ProgressBar progressBar;
    GridLayout numpadLayout;
    GridLayout miniMenuLayout;
    Button resetButton;
    Button exitButton;
    Button statsButton;
    Button ansButton;

    MathQuestion mathQuestion;
    Numpad numpad;

    GameStatistics stats;

    Toast toast;
    Vibrator vibrator;

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
        statsButton = (Button) findViewById(R.id.statsButton);
        ansButton = (Button) findViewById(R.id.ansButton);

        int total = getIntent().getIntExtra("total", 25);
        stats = new GameStatistics(total);

        initNumpad();

        start();
    }

    private void start() {
        progressBar.setVisibility(View.VISIBLE);
        scoreView.setVisibility(View.VISIBLE);
        inputView.setText("________");
        numpadLayout.setVisibility(View.VISIBLE);
        updateScore();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startQuestion();
        stats.timeAnswered[0] = System.currentTimeMillis();
    }

    public void reset(View view) {
        vibrator.vibrate(10);

        int total = stats.total;
        stats = new GameStatistics(total);

        miniMenuLayout.setVisibility(View.INVISIBLE);

        start();
    }

    public void exit(View view) {
        vibrator.vibrate(10);
        finish();
    }

    public void viewStats(View view) {
        vibrator.vibrate(10);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        StatisticsDialog dialog = StatisticsDialog.newInstance(stats);

        dialog.show(ft, "StatisticsDialog");
    }

    public void showAnswers(View view) {
        vibrator.vibrate(10);
        Intent intent = new Intent();
        intent.setClass(this, AnswerReviewActivity.class);
        intent.putExtra("stats", stats);
        startActivity(intent);
    }

    private void startQuestion() {
        mathQuestion = new MathQuestion();

        questionView.setText(mathQuestion.toString());
    }

    private void checkAnswer() {
        stats.answered++;
        stats.timeAnswered[stats.answered] = System.currentTimeMillis();

        String text;

        int ans = mathQuestion.getAnswer();
        int in = numpad.getInput();

        stats.addQuestion(mathQuestion, in);

        if(ans == in) {
            MediaPlayer.create(QuizActivity.this, R.raw.correct_answer).start();
            stats.correct++;
            text = "Correct!";
        } else {
            MediaPlayer.create(QuizActivity.this, R.raw.incorrect_answer).start();
            text = "Incorrect!";
            vibrator.vibrate(250);
        }

        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        updateScore();
    }

    private void updateScore() {
        progressBar.setProgress(stats.correct * 100 / stats.total);
        progressBar.setSecondaryProgress(stats.answered * 100 / stats.total);

        scoreView.setText(String.format("Score: %d/%d", stats.correct, stats.total));
    }

    private void gameOver() {
        long totalTime = System.currentTimeMillis() - stats.timeAnswered[0];

        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this, String.format("Completed in %.3f seconds.", (double)totalTime/1000), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        scoreView.setVisibility(View.INVISIBLE);
        questionView.setText("FINISHED");
        inputView.setText(String.format("Score: %d/%d", stats.correct, stats.total));
        numpadLayout.setVisibility(View.INVISIBLE);
        miniMenuLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(GameStatistics stats) {

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
                        if(stats.answered < stats.total) {
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
