package edu.usm.cs.csc414.pocketfinances;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MainActivity activity;
    private FrameLayout fragmentHolder;
    private BottomNavigationView bottomNavView;
    private AdView adView;
    private ImageView background;

    private HomeFragment homeFragment;
    private AccountsFragment accountsFragment;
    private ExpensesFragment expensesFragment;
    private RecurringExpensesFragment recurringExpensesFragment;
    private BudgetFragment budgetFragment;



    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activity = rule.getActivity();

        fragmentHolder = activity.findViewById(R.id.activity_main_framelayout);
        bottomNavView = activity.findViewById(R.id.bottom_nav_view);
        adView = activity.findViewById(R.id.adView);
        background = activity.findViewById(R.id.activity_main_background);

        // Initialize fragments
        homeFragment = new HomeFragment();
        accountsFragment = new AccountsFragment();
        expensesFragment = new ExpensesFragment();
        recurringExpensesFragment = new RecurringExpensesFragment();
        budgetFragment = new BudgetFragment();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(fragmentHolder);
        assertNotNull(bottomNavView);
        assertNotNull(adView);
        assertNotNull(background);

        assertNotNull(homeFragment);
        assertNotNull(accountsFragment);
        assertNotNull(expensesFragment);
        assertNotNull(recurringExpensesFragment);
        assertNotNull(budgetFragment);
    }

    @Test
    public void testHomeFragment() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentHolder.getId(), homeFragment, null)
                .commit();

        getInstrumentation().waitForIdleSync();

        assertNotNull(homeFragment.getView());


        TextView textView1 = homeFragment.getView().findViewById(R.id.fragment_home_textview1);
        TextView textView2 = homeFragment.getView().findViewById(R.id.fragment_home_textview2);
        TextView textView3 = homeFragment.getView().findViewById(R.id.fragment_home_textview3);
        TextView titleTextView = homeFragment.getView().findViewById(R.id.fragment_home_title_textview);
        TextView balanceTextView = homeFragment.getView().findViewById(R.id.fragment_home_balance_textview);
        TextView budgetTextView = homeFragment.getView().findViewById(R.id.fragment_home_budget_textview);
        TextView btnCaptionTextView = homeFragment.getView().findViewById(R.id.fragment_home_addexpense_caption);
        ImageButton addExpenseBtn = homeFragment.getView().findViewById(R.id.fragment_home_addexpense_btn);
        ImageButton settingsBtn = homeFragment.getView().findViewById(R.id.fragment_home_settings_btn);
        RecyclerView notificationsRecyclerView = homeFragment.getView().findViewById(R.id.fragment_home_notifications_recyclerview);

        assertNotNull(textView1);
        assertNotNull(textView2);
        assertNotNull(textView3);
        assertNotNull(titleTextView);
        assertNotNull(balanceTextView);
        assertNotNull(budgetTextView);
        assertNotNull(btnCaptionTextView);
        assertNotNull(addExpenseBtn);
        assertNotNull(settingsBtn);
        assertNotNull(notificationsRecyclerView);
    }


    @Test
    public void testAccountsFragment() {

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentHolder.getId(), accountsFragment, null)
                .commit();

        getInstrumentation().waitForIdleSync();

        assertNotNull(accountsFragment.getView());


        TextView titleText = accountsFragment.getView().findViewById(R.id.fragment_accounts_title_textview);
        ImageButton addAccountBtn = accountsFragment.getView().findViewById(R.id.fragment_accounts_add_btn);
        RecyclerView recyclerView = accountsFragment.getView().findViewById(R.id.fragment_accounts_recyclerview);

        assertNotNull(titleText);
        assertNotNull(addAccountBtn);
        assertNotNull(recyclerView);
    }


    @Test
    public void testExpensesFragment() {

        Bundle bundle = new Bundle();
        bundle.putInt("activeAccountId", -1);

        expensesFragment.setArguments(bundle);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentHolder.getId(), expensesFragment, null)
                .commit();

        getInstrumentation().waitForIdleSync();

        assertNotNull(expensesFragment.getView());


        TextView titleTextView = expensesFragment.getView().findViewById(R.id.fragment_expenses_title_textview);
        TextView parentAccountBalanceTextView = expensesFragment.getView().findViewById(R.id.fragment_expenses_accountbalance_textview);;
        ImageButton addExpenseBtn = expensesFragment.getView().findViewById(R.id.fragment_expenses_add_btn);
        ImageButton backBtn = expensesFragment.getView().findViewById(R.id.fragment_expenses_back_btn);
        RecyclerView recyclerView = expensesFragment.getView().findViewById(R.id.fragment_expenses_recyclerview);

        assertNotNull(titleTextView);
        assertNotNull(parentAccountBalanceTextView);
        assertNotNull(addExpenseBtn);
        assertNotNull(backBtn);
        assertNotNull(recyclerView);
    }


    @Test
    public void testRecurringExpensesFragment() {

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentHolder.getId(), recurringExpensesFragment, null)
                .commit();

        getInstrumentation().waitForIdleSync();

        assertNotNull(recurringExpensesFragment.getView());


        ImageButton addRecurringExpenseBtn = recurringExpensesFragment.getView().findViewById(R.id.fragment_recurring_expenses_add_btn);
        RecyclerView recyclerView = recurringExpensesFragment.getView().findViewById(R.id.fragment_recurring_expenses_recyclerview);

        assertNotNull(addRecurringExpenseBtn);
        assertNotNull(recyclerView);
    }


    @Test
    public void testBudgetFragment() {

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentHolder.getId(), budgetFragment, null)
                .commit();

        getInstrumentation().waitForIdleSync();

        assertNotNull(budgetFragment.getView());


        // test for null UI elements here.

    }
}