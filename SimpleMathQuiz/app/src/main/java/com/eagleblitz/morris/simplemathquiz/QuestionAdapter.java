package com.eagleblitz.morris.simplemathquiz;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mcmor on 2016-08-17.
 */
public class QuestionAdapter extends BaseAdapter {

    List<GameStatistics.QuestionStatistics> qStatList;
    LayoutInflater inflater;

    public QuestionAdapter(Context context, List<GameStatistics.QuestionStatistics> list) {
        this.qStatList = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return qStatList.size();
    }

    @Override
    public Object getItem(int position) {
        return qStatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_answer_item, null);
            holder = new Holder();

            holder.listItem = (RelativeLayout) convertView.findViewById(R.id.listItem);
            holder.numberTextView = (TextView) convertView.findViewById(R.id.numberTextView);
            holder.questionTextView = (TextView) convertView.findViewById(R.id.questionTextView);
            holder.inputTextView = (TextView) convertView.findViewById(R.id.inputTextView);
            holder.answerTextView = (TextView) convertView.findViewById(R.id.answerTextView);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.markImageView = (ImageView) convertView.findViewById(R.id.markImageView);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        GameStatistics.QuestionStatistics qStat = qStatList.get(position);

        if(qStat.isCorrect()) {
            holder.listItem.setBackgroundColor(Color.rgb(192, 255, 192));
            holder.inputTextView.setTextColor(Color.GREEN);
            holder.markImageView.setImageResource(R.drawable.check);
        } else {
            holder.listItem.setBackgroundColor(Color.rgb(255, 192, 192));
            holder.inputTextView.setTextColor(Color.RED);
            holder.markImageView.setImageResource(R.drawable.tick);
        }
        holder.numberTextView.setText(String.format("#%d)", qStat.getQuestionNum()));
        holder.questionTextView.setText(qStat.getQuestion());
        holder.inputTextView.setText(String.valueOf(qStat.getInput()));
        holder.answerTextView.setText(String.valueOf(qStat.getAnswer()));
        holder.timeTextView.setText(String.format("%.3f seconds", qStat.getTime()));

        return convertView;
    }

    class Holder {
        RelativeLayout listItem;
        TextView numberTextView;
        TextView questionTextView;
        TextView inputTextView;
        TextView answerTextView;
        TextView timeTextView;
        ImageView markImageView;
    }
}
