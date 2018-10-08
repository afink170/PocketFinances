package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment4Security extends Fragment {

    private static final String TAG = "WelcomeFragment4";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Attempting to create WelcomeFragment4.");
        View view = inflater.inflate(R.layout.fragment_welcome_4_security, container, false);

        WelcomeFragment4SecurityEnter enterPasswordFragment = new WelcomeFragment4SecurityEnter();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
        fragmentTransaction.replace(R.id.activity_welcome_framelayout, enterPasswordFragment).commit();


        return view;
    }
}
