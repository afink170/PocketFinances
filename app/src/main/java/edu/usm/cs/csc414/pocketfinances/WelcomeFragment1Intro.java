package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment1Intro extends Fragment {

    private static final String TAG = "WelcomeFragment1";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Attempting to create WelcomeFragment1.");
        View view = inflater.inflate(R.layout.fragment_welcome_1_intro, container, false);




        return view;
    }
}
