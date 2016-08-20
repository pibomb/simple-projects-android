package com.eagleblitz.morris.simplemathquiz;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcmor on 2016-08-14.
 */
public class GameStatistics implements Parcelable {
    int correct;
    int answered;
    int total;
    long[] timeAnswered;

    List<MathQuestion> mathQuestions = new ArrayList<>();
    List<Integer> inputs = new ArrayList<>();

    public GameStatistics(int total) {
        this.total = total;
        timeAnswered = new long[total+1];
    }

    public void addQuestion(MathQuestion mq, int in) {
        mathQuestions.add(mq);
        inputs.add(in);
    }

    /**
     *
     * @param question the index of the MathQuestion, starting from 0
     * @return time taken to answer the MathQuestion at index question
     */
    public double getTimeUsed(int question) {
        return (timeAnswered[question + 1] - timeAnswered[question]) / 1000.0;
    }

    public int getPercentScore() {
        return correct * 100 / total;
    }

    public String getLetterGrade() {
        int score = getPercentScore();

        String grade;

        if(score >= 95)
            grade = "A+";
        else if(score >= 87)
            grade = "A";
        else if(score >= 80)
            grade = "A-";
        else if(score >= 77)
            grade = "B+";
        else if(score >= 73)
            grade = "B";
        else if(score >= 70)
            grade = "B-";
        else if(score >= 67)
            grade = "C+";
        else if(score >= 63)
            grade = "C";
        else if(score >= 60)
            grade = "C-";
        else if(score >= 57)
            grade = "D+";
        else if(score >= 53)
            grade = "D";
        else if(score >= 50)
            grade = "D-";
        else
            grade = "F";

        return grade;
    }

    public double getTotalTime() {
        return (double)(timeAnswered[total] - timeAnswered[0]) / 1000;
    }

    public double getAvgTime() {
        return getTotalTime() / total;
    }

    public class QuestionStatistics {
        int questionNum;
        String question;
        int input;
        int answer;
        boolean isCorrect;
        double time;

        QuestionStatistics(int qNum, String q, int in, int ans, double t) {
            this.questionNum = qNum;
            this.question = q;
            this.input = in;
            this.answer = ans;
            this.isCorrect = input == answer;

            this.time = t;
        }

        public int getQuestionNum() {
            return questionNum;
        }

        public String getQuestion() {
            return question;
        }

        public int getInput() {
            return input;
        }

        public int getAnswer() {
            return answer;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public double getTime() {
            return time;
        }
    }

    public List<QuestionStatistics> getQuestionStats() {
        List<QuestionStatistics> qStatList = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            MathQuestion mq = mathQuestions.get(i);
            qStatList.add(new QuestionStatistics(i+1, mq.toString(), inputs.get(i), mq.getAnswer(), getTimeUsed(i)));
        }

        return qStatList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.correct);
        dest.writeInt(this.answered);
        dest.writeInt(this.total);
        dest.writeLongArray(this.timeAnswered);
        dest.writeList(this.mathQuestions);
        dest.writeList(this.inputs);
    }

    protected GameStatistics(Parcel in) {
        this.correct = in.readInt();
        this.answered = in.readInt();
        this.total = in.readInt();
        this.timeAnswered = in.createLongArray();
        this.mathQuestions = new ArrayList<MathQuestion>();
        in.readList(this.mathQuestions, MathQuestion.class.getClassLoader());
        this.inputs = new ArrayList<Integer>();
        in.readList(this.inputs, Integer.class.getClassLoader());
    }

    public static final Creator<GameStatistics> CREATOR = new Creator<GameStatistics>() {
        @Override
        public GameStatistics createFromParcel(Parcel source) {
            return new GameStatistics(source);
        }

        @Override
        public GameStatistics[] newArray(int size) {
            return new GameStatistics[size];
        }
    };
}
