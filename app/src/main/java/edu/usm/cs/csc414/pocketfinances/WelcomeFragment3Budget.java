package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment3Budget extends Fragment {

    private static final String TAG = "WelcomeFragment3";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Attempting to create WelcomeFragment3.");
        View view = inflater.inflate(R.layout.fragment_welcome_3_budget, container, false);




        return view;
    }
}
