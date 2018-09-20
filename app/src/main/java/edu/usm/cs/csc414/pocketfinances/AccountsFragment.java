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
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountsFragment extends Fragment {

    private static final String TAG = "AccountsFragment";

    // Declare Typeface for custom font
    Typeface FONT_WALKWAY;

    // Declare UI elements
    TextView titleText;
    ImageButton editAccountsBtn, addAccountBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Attempting to create HomeFragment");
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        titleText = view.findViewById(R.id.fragment_accounts_title_textview);


        try {
            // Initialize custom font from resource
            FONT_WALKWAY = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_walkway_black));

            // Set the font for UI elements to custom font
            titleText.setTypeface(FONT_WALKWAY);
        }
        catch (Exception e) {
            Log.e(TAG, "Unable to set font of application text.", e);
        }
        Log.d(TAG, "Font of UI text successfully set.");


        return view;
    }
}
