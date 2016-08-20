package com.eagleblitz.morris.simplemathquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsDialog extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private OnFragmentInteractionListener mListener;

    private GameStatistics stats;

    TextView numCorrectTextView;
    TextView numQuestionsTextView;
    TextView percentScoreTextView;
    TextView letterGradeTextView;
    TextView totalTimeTextView;
    TextView avgTimeTextView;

    public StatisticsDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stats
     * @return A new instance of fragment StatisticsDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsDialog newInstance(GameStatistics stats) {
        StatisticsDialog fragment = new StatisticsDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, stats);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null) {
            this.stats = getArguments().getParcelable(ARG_PARAM1);
        }

        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_statistics_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView)
                .setTitle("Quiz Statistics")
                .setPositiveButton("OK", null);

        numCorrectTextView = (TextView) contentView.findViewById(R.id.numCorrectTextView);
        numQuestionsTextView = (TextView) contentView.findViewById(R.id.numQuestionsTextView);
        percentScoreTextView = (TextView) contentView.findViewById(R.id.percentScoreTextView);
        letterGradeTextView = (TextView) contentView.findViewById(R.id.letterGradeTextView);
        totalTimeTextView = (TextView) contentView.findViewById(R.id.totalTimeTextView);
        avgTimeTextView = (TextView) contentView.findViewById(R.id.avgTimeTextView);

        numCorrectTextView.setText(String.valueOf(stats.correct));
        numQuestionsTextView.setText(String.valueOf(stats.total));
        percentScoreTextView.setText(String.valueOf(stats.getPercentScore()) + "%");
        letterGradeTextView.setText(stats.getLetterGrade());
        totalTimeTextView.setText(String.format(Locale.getDefault(), "%.2f s", stats.getTotalTime()));
        avgTimeTextView.setText(String.format(Locale.getDefault(), "%.2f s", stats.getAvgTime()));

        return builder.create();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(stats);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(GameStatistics stats);
    }
}
