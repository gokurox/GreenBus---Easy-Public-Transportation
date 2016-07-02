package com.example.gursimransingh.greenbus_evs_iiitd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Feedback;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.UserInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShareExperience_Fragment extends Fragment {
    @InjectView(R.id.input_feedbackBusNumber) EditText feedbackBusNumber;
    @InjectView (R.id.input_feedback) EditText edittext_feedback;
    RatingBar feedbackRating;
    Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Share Your Experience");
        ButterKnife.inject (getActivity());

        View v = inflater.inflate(R.layout.fragment_share_experience, container, false);
        feedbackBusNumber = (EditText) v.findViewById(R.id.input_feedbackBusNumber);
        edittext_feedback = (EditText) v.findViewById(R.id.input_feedback);
        feedbackRating = (RatingBar) v.findViewById(R.id.feedbackRating);
        submitButton = (Button) v.findViewById(R.id.btn_submitfeedback);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClick();
            }
        });
        return v;
    }

    public void onSubmitButtonClick () {
        String bus_number = feedbackBusNumber.getText().toString();
        String feedback = edittext_feedback.getText().toString();
        Float rating = feedbackRating.getRating();

        feedbackBusNumber.setError(null);
        edittext_feedback.setError(null);

        boolean valid = true;
        if (bus_number.isEmpty())
        {
            feedbackBusNumber.setError ("Enter a Bus Number");
            valid = false;
        }
        if (feedback.isEmpty())
        {
            edittext_feedback.setError ("Enter Feedback");
            valid = false;
        }
        if (rating == 0)
        {
            valid = false;
            edittext_feedback.setError("Please rate");
        }

        if (valid)
        {
            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
            UserInfo ui = db.ULI_getOnlineUser(getActivity());

            Feedback fbk = new Feedback (bus_number, feedback, rating, ui.email);
            db.insert_Feedback (fbk);

            Toast.makeText(getActivity(), "Thanks for leaving a Feedback", Toast.LENGTH_LONG).show();

            feedbackBusNumber.setText(null);
            edittext_feedback.setText(null);
        }
    }
}
