package edu.usm.cs.csc414.pocketfinances;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Class defining the logic for the Home Fragment.
public class HomeFragment extends Fragment {
    private static String TAG = "HomeFragment";

    // Declare Typeface for custom font
    Typeface FONT_WALKWAY;

    // Declare UI elements
    TextView textView1, textView2, textView3, balanceTextView, budgetTextView;

    /*
    public HomeFragment() {

    }
    */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment from fragment_home XML
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            // Initialize UI elements
            textView1 = view.findViewById(R.id.fragment_home_textview1);
            textView2 = view.findViewById(R.id.fragment_home_textview2);
            textView3 = view.findViewById(R.id.fragment_home_textview3);
            balanceTextView = view.findViewById(R.id.fragment_home_balance_textview);
            budgetTextView = view.findViewById(R.id.fragment_home_budget_textview);
        }
        catch (Exception e) {
            Log.e(TAG, "Unable to initialize UI elements.", e);
        }


        try {
            // Initialize custom font from resource
            FONT_WALKWAY = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_walkway_black));

            // Set the font for UI elements to custom font
            textView1.setTypeface(FONT_WALKWAY);
            textView2.setTypeface(FONT_WALKWAY);
            textView3.setTypeface(FONT_WALKWAY);
            balanceTextView.setTypeface(FONT_WALKWAY);
            budgetTextView.setTypeface(FONT_WALKWAY);
        }
        catch (Exception e) {
            Log.e(TAG, "Unable to set font of application text.", e);
        }



        return view;
    }
}
