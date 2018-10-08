package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment5Done extends Fragment {


    private static final String TAG = "WelcomeFragment5";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_5_done, container, false);

        try {
            String lastFragment = getArguments().getString("prevPage");

            if (lastFragment == "passwordConfirm") {
                ((WelcomeActivity)getActivity()).setNextButtonToFinish();
            }
        } catch(Exception e) {
            Log.e(TAG, "Failed to get last fragment.");
        }





        return view;
    }
}
