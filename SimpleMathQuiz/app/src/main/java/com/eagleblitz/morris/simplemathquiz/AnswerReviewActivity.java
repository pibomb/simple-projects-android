package com.eagleblitz.morris.simplemathquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class AnswerReviewActivity extends AppCompatActivity {

    GameStatistics stats;
    ListView answerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_review);

        answerListView = (ListView) findViewById(R.id.answerListView);
        stats = getIntent().getParcelableExtra("stats");

        setupList();
    }

    private void setupList() {
        QuestionAdapter adapter = new QuestionAdapter(this, stats.getQuestionStats());
        answerListView.setAdapter(adapter);
    }

    public void exit(View view) {
        finish();
    }
}
