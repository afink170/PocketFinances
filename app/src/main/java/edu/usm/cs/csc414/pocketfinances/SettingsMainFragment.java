package edu.usm.cs.csc414.pocketfinances;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SettingsMainFragment extends Fragment {

    CustomSharedPreferences sharedPrefs;
    List<BankAccount> bankAccounts;

    Spinner backgroundSpinner, defaultAccountSpinner;
    TextView aboutTextView, licensingTextView;
    ImageButton backBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);

        backBtn = view.findViewById(R.id.fragment_settings_main_back_btn);
        aboutTextView = view.findViewById(R.id.fragment_settings_main_about_textview);
        licensingTextView = view.findViewById(R.id.fragment_settings_main_licensing_textview);
        backgroundSpinner = view.findViewById(R.id.fragment_settings_main_background_spinner);
        defaultAccountSpinner = view.findViewById(R.id.fragment_settings_main_defaultaccount_spinner);

        sharedPrefs = new CustomSharedPreferences(getContext());

        observeAccounts();
        setSpinnerAdapters();
        setListeners();
        setInitialStates();

        return view;
    }

    private void setInitialStates() {

        // Set initial state of each setting item.
        // For example, set the default selected item for the background spinner here.
        // TODO : Set backgroundSpinner initial state


        // Set default selected item for defaultAccountSpinner
        if (sharedPrefs.getDefaultIsAllAccounts()
                || sharedPrefs.getDefaultAccountId() == -1
                || bankAccounts == null
                || bankAccounts.isEmpty()) {

            // Set selected item to "None".
            defaultAccountSpinner.setSelection(0);
        }
        else {
            for (int i = 1; i <= bankAccounts.size(); i++) {
                if (bankAccounts.get(i-1).getAccountId() == sharedPrefs.getDefaultAccountId()) {
                    // Set the selected item to the default account stored in shared preferences.
                    defaultAccountSpinner.setSelection(i+1);
                    break;
                }
            }
        }
    }


    private void setSpinnerAdapters() {
        // TODO : Set spinner adapter for background spinner here
    }

    private void setListeners() {
        backgroundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Handle selection here
                // i refers to the array index of the background that was selected.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing here
            }
        });

        defaultAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0 && bankAccounts != null && !bankAccounts.isEmpty()) {
                    sharedPrefs.setDefaultAccountId(bankAccounts.get(i-1).getAccountId());
                    Timber.d("Default account picked: %s", bankAccounts.get(i-1).getAccountName());
                }
                else {
                    sharedPrefs.setDefaultIsAllAccounts(true);
                    Timber.d("Default account set to none.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing here.
            }
        });

        backBtn.setOnClickListener(view -> {
            Intent back_home = new Intent(view.getContext(), MainActivity.class);
            startActivity(back_home);
            getActivity().finish();
        });

        aboutTextView.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
            fragmentTransaction
                    .replace(R.id.activity_settings_fragmentholder, new SettingsAboutFragment())
                    .addToBackStack("settingsMain")
                    .commit();
        });

        licensingTextView.setOnClickListener(view -> {
            // Handle click
            // TODO : Set listener for licensing textview
        });
    }


    private void observeAccounts() {
        try {
            BankAccountsViewModel accountsViewModel = new BankAccountsViewModel(getActivity().getApplication());
            accountsViewModel.getBankAccountsList().observe(getActivity(), bankAccountsList -> {
                bankAccounts = bankAccountsList;

                ArrayList<String> bankAccountArrayList = new ArrayList<>();

                bankAccountArrayList.add("None");

                if (bankAccounts != null) {
                    for (BankAccount account : bankAccounts) {
                        bankAccountArrayList.add(account.getAccountName());
                    }
                }

                ArrayAdapter<String> bankAccountArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, bankAccountArrayList);
                defaultAccountSpinner.setAdapter(bankAccountArrayAdapter);

                if (!sharedPrefs.getDefaultIsAllAccounts()) {
                    for (int i = 1; i <= bankAccounts.size(); i++) {
                        if (sharedPrefs.getDefaultAccountId() == bankAccounts.get(i-1).getAccountId()) {
                            defaultAccountSpinner.setSelection(i);
                        }
                    }
                }
            });
        }
        catch (Exception e) {
            Timber.e(e, "Failed to observe bank accounts!");
        }
    }
}
