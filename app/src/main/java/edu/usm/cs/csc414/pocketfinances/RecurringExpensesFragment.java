package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RecurringExpensesFragment extends Fragment {



    private static final String TAG = "RecurringExpensesFrag";

    // Declare Typeface for custom font
    //Typeface FONT_WALKWAY;

    // Declare UI elements
    ImageButton addRecurringExpenseBtn;
    RecyclerView recyclerView;
    //RecurringExpensesRecyclerViewAdapter recyclerViewAdapter;
    SectionedRecyclerViewAdapter sectionAdapter;
    ExpensesViewModel viewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Attempting to create RecurringExpensesFragment.");

        View view = inflater.inflate(R.layout.fragment_recurring_expenses, container, false);

        // Initialize UI elements
        addRecurringExpenseBtn = view.findViewById(R.id.fragment_recurring_expenses_add_btn);

        // Initialize the RecyclerView for holding the list of accounts
        recyclerView = view.findViewById(R.id.fragment_recurring_expenses_recyclerview);


        // Initialize the ViewModel
        viewModel = ViewModelProviders.of(this,
                new ExpensesViewModelFactory(this.getActivity().getApplication(),
                        true, null, null)).get(ExpensesViewModel.class);


        // Set the LayoutManager to be the LinearLayoutManager
        // This makes the list display linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sectionAdapter = new SectionedRecyclerViewAdapter();

        recyclerView.setAdapter(sectionAdapter);


        addRecurringExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NewExpenseDialog newExpenseDialog = new NewExpenseDialog(getActivity(), true);
                newExpenseDialog.show();
            }
        });


        // Set the ViewModel to observe the live Expense data list
        //      so that the UI will update whenever the data or config changes
        viewModel.getExpensesList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenseList) {
                Log.d(TAG, "Recurring expenses received.  Populating RecyclerView.");

                sectionAdapter.removeAllSections();

                // There are 8 different recurrence rates, but the first one is "Once," or no recurrence.
                // So we will start at 1, and iterate through looking for expense matching the recurrence rate.
                // Basically, we are sorting them into sections by recurrence rate
                for (int i = 1; i < 8; i++) {

                    RecurrenceRate rate = RecurrenceRate.getRateFromValue(i);

                    ArrayList<Expense> expenses = new ArrayList<>();

                    for (Expense expense : expenseList) {
                        //Log.v(TAG, expense.toString());
                        if (expense.getRecurrenceRate().getValue().equals(rate.getValue()))
                            expenses.add(expense);
                    }

                    if (expenses.isEmpty()) continue;

                    //Log.v(TAG, "Section: " + rate.getText() + "   Expenses: \r\n" + expenses);
                    RecurringExpensesRecyclerViewSection section = new RecurringExpensesRecyclerViewSection(rate.getText(),expenses, getContext());

                    section.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            //int itemPosition = recyclerView.getChildLayoutPosition(view);

                            try {
                                TextView expenseIdTextView = view.findViewById(R.id.recurring_expenses_recyclerview_textview_expenseid);

                                int expenseId = Integer.valueOf(expenseIdTextView.getText().toString());

                                Expense clickedExpense = null;
                                for (Expense expense : viewModel.getExpensesList().getValue()) {
                                    if (expense.getExpenseId() == expenseId) {
                                        clickedExpense = expense;
                                        break;
                                    }
                                }

                                if (clickedExpense != null) {
                                    EditDeleteExpenseDialog editDeleteExpenseDialog = new EditDeleteExpenseDialog(getActivity(), viewModel, clickedExpense);
                                    editDeleteExpenseDialog.show();
                                }
                                else
                                    throw new Exception("clickedExpense is null!");
                            } catch(Exception e) {
                                String message = "ERROR: Failed to find position of clicked expense!";
                                Log.e(TAG, message, e);
                                //showToastMessage(message);
                            }

                            return true;
                        }
                    });

                    sectionAdapter.addSection(section);
                    recyclerView.setAdapter(sectionAdapter);
                }
            }
        });

        return view;
    }
}
