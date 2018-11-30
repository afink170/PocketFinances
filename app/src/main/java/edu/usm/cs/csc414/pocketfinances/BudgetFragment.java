package edu.usm.cs.csc414.pocketfinances;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import timber.log.Timber;

public class BudgetFragment extends Fragment {

    private static final float MIN_SCALE = 0.55f;
    private static final float MIN_ALPHA = 0.55f;

    private static final int NUMBER_OF_MONTHS = 6;
    private static final int NUMBER_OF_CATEGORIES = 17;
    private static final float THRESHOLD = 0.5f;

    VerticalViewPager viewPager;
    ExpensesViewModel viewModel;

    List<Float> expenseListCategories_values;
    List<Float> expenseListTotal_values;

    List<String> expenseListCategories_labels;
    List<String> expenseListTotal_labels;

    int currentMonth;
    int currentYear;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        viewPager = view.findViewById(R.id.fragment_budget_viewpager_layout);

        Calendar currentTime = Calendar.getInstance();
        currentMonth = currentTime.get(Calendar.MONTH);
        currentYear = currentTime.get(Calendar.YEAR);

        expenseListCategories_values = new ArrayList<>();
        expenseListTotal_values = new ArrayList<>();
        expenseListCategories_labels = new ArrayList<>();
        expenseListTotal_labels = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            if (i < NUMBER_OF_MONTHS) {
                expenseListTotal_values.add(0.0f);
                expenseListTotal_labels.add("");
            }
            expenseListCategories_values.add(0.0f);
            expenseListCategories_labels.add(ExpenseCategory.getCategoryFromValue(i).getText());
        }

        int month = currentMonth;
        int year = currentYear;
        for (int i = NUMBER_OF_MONTHS - 1; i >= 0; i--) {

            if (month < 0) {
                year--;
                month += 12;
            }
            String label = getMonthName(month) + getYearName(year);
            expenseListTotal_labels.set(i, label);
            month--;
        }



        // Get ExpensesViewModel instance
        viewModel = new ExpensesViewModel(getActivity().getApplication());

        // Observe expenses list in database and populate arrays from it
        viewModel.getExpensesList().observe(this, expenseList -> {
            if (expenseList != null && !expenseList.isEmpty()) {

                Timber.d("Attempting to get expenses data from database and analyze data for graphing.");

                try {
                    // For all expenses in the expense table
                    for (Expense expense : expenseList) {
                        int expenseMonth = expense.getDate().get(Calendar.MONTH);
                        int expenseYear = expense.getDate().get(Calendar.YEAR);

                        Calendar startCheckData = Calendar.getInstance();
                        startCheckData.add(Calendar.MONTH, -1 * NUMBER_OF_MONTHS - 1);
                        // Check that expense is a deduction, isn't a parent recurring expense, and occurred in the last 7 months.
                        if (expense.getDepositOrDeduct() == Expense.DEDUCT
                                && (!expense.getIsRecurring() || !expense.getIsFirstOccurrence())
                                && expense.getDate().getTimeInMillis() > startCheckData.getTimeInMillis()) {

                            // Check that expense occurred this month
                            if (expenseMonth == currentMonth && expenseYear == currentYear) {
                                int index = expense.getCategory().getValue();
                                expenseListCategories_values.set(index, (float) expense.getAmount() + expenseListCategories_values.get(index));
                                //Timber.v("%s -> $%.2f", expense.getCategory().toString(), expense.getAmount());
                            }

                            // Add expense to the last six months' expense totals
                            for (int i = 0; i < NUMBER_OF_MONTHS; i++) {
                                if (expenseListTotal_labels.get(i).equals(getMonthName(expenseMonth) + getYearName(expenseYear))) {
                                    expenseListTotal_values.set(i, expenseListTotal_values.get(i) + ((float) expense.getAmount()));
                                }
                            }
                        }
                    }

                    // The total spending for this month
                    float total = expenseListTotal_values.get(expenseListTotal_values.size() - 1);

                    for (int i = 0; i < expenseListCategories_values.size(); i++) {
                        // This month's spending in each category
                        float amount = expenseListCategories_values.get(i);

                        // Set value to percentage it is of the total
                        expenseListCategories_values.set(i, (amount / total) * 100);

                        /*
                        Timber.d("{ Category: %s,  Amount: $%.2f,  Total: $%.2f, Percentage: %.2f }",
                                ExpenseCategory.getCategoryFromValue(i).getText(),
                                amount, total,
                                expenseListCategories_values.get(i));
                        */
                    }
                }
                catch (Exception e) {
                    Timber.e(e, "Failed to observe expenses and build analyze data for graphing.");
                }
            }
            else {
                Timber.d("No expenses found in table.  Could not build data from empty table.");
            }

            setVerticalViewPagerAdapter();

        });

        return view;
    }


    private String getMonthName(int monthNumber) {
        switch(monthNumber) {
            case 0:
                return "Jan,";
            case 1:
                return "Feb,";
            case 2:
                return "Mar,";
            case 3:
                return "Apr,";
            case 4:
                return "May,";
            case 5:
                return "Jun,";
            case 6:
                return "Jul,";
            case 7:
                return "Aug,";
            case 8:
                return "Sep,";
            case 9:
                return "Oct,";
            case 10:
                return "Nov,";
            case 11:
                return "Dec,";
        }
        return "";
    }

    private String getYearName(int year) {
        return String.valueOf(year);
    }

    private void setVerticalViewPagerAdapter() {
        viewPager.setAdapter(new VerticalViewPagerAdapter(getChildFragmentManager()));

        viewPager.setPageTransformer(true, (view1, position) -> {
            int pageWidth = view1.getWidth();
            int pageHeight = view1.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view1.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view1.setTranslationY(vertMargin - horzMargin / 2);
                } else {
                    view1.setTranslationY(-vertMargin + horzMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view1.setScaleX(scaleFactor);
                view1.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view1.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view1.setAlpha(0);
            }
        });
    }





    public class VerticalViewPagerAdapter extends FragmentStatePagerAdapter {

        public VerticalViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            Timber.d("New VerticalViewPager position: %d", position);

            Bundle bundle = new Bundle();

            String[] categoryLabels = new String[NUMBER_OF_CATEGORIES];
            String[] monthlyLabels = new String[NUMBER_OF_MONTHS];
            float[] categoryValues = new float[NUMBER_OF_CATEGORIES];
            float[] monthlyValues = new float[NUMBER_OF_MONTHS];

            categoryLabels = expenseListCategories_labels.toArray(categoryLabels);
            monthlyLabels = expenseListTotal_labels.toArray(monthlyLabels);

            for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
                categoryValues[i] = expenseListCategories_values.get(i);
                if (i < NUMBER_OF_MONTHS) monthlyValues[i] = expenseListTotal_values.get(i);
            }


            switch (position) {
                case 0:
                    bundle.putStringArray("labels", categoryLabels);
                    bundle.putFloatArray("values", categoryValues);
                    PieChartFragment pieFragment = new PieChartFragment();
                    pieFragment.setArguments(bundle);
                    return pieFragment;
                case 1:
                    bundle.putStringArray("labels", categoryLabels);
                    bundle.putFloatArray("values", categoryValues);
                    BarChartFragment barFragment = new BarChartFragment();
                    barFragment.setArguments(bundle);
                    return barFragment;
                case 2:
                    bundle.putStringArray("labels", monthlyLabels);
                    bundle.putFloatArray("values", monthlyValues);
                    LineChartFragment lineFragment = new LineChartFragment();
                    lineFragment.setArguments(bundle);
                    return lineFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "PieChart";
                case 1:
                    return "BarChart";
                case 3:
                    return "LineChart";
            }
            return null;
        }

    }







    public static class PieChartFragment extends Fragment {

        private float[] values;
        private String[] labels;

        private float[] tempValues;
        private String[] tempLabels;

        PieChart pieChart;
        TextView titleText;
        int textColor;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Timber.d("Creating PieChart fragment.");

            View view = inflater.inflate(R.layout.fragment_budget_viewpager_piechart, container, false);

            titleText = view.findViewById(R.id.textview);

            if (new CustomSharedPreferences(getContext()).getActivityTheme() == CustomSharedPreferences.THEME_DARK)
                textColor = getResources().getColor(R.color.colorWhite);
            else
                textColor = getResources().getColor(R.color.colorDarkGrey);

            titleText.setTextColor(textColor);

            try {
                values = getArguments().getFloatArray("values");
                labels = getArguments().getStringArray("labels");
            } catch (Exception e) {
                Timber.e(e, "Failed to get data from parent fragment!");
            }

            int counter = 0;

            tempValues = new float[values.length];
            tempLabels = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                if (values[i] > 0.0f) {
                    tempValues[counter] = values[i];
                    tempLabels[counter] = labels[i];
                    counter++;
                }
            }

            values = new float[counter];
            labels = new String[counter];

            for (int i = 0; i < counter; i++) {
                values[i] = tempValues[i];
                labels[i] = tempLabels[i];
            }


            pieChart = (PieChart) view.findViewById(R.id.piechart);

            pieChart.getDescription().setText("Spending Statistics");
            pieChart.setRotationEnabled(false);
            pieChart.setHoleRadius(10f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setHoleColor(getResources().getColor(R.color.colorTransparent));
            pieChart.setCenterText("");
            pieChart.setCenterTextSize(14);
            pieChart.setCenterTextColor(textColor);
            pieChart.setUsePercentValues(true);
            pieChart.getLegend().setEnabled(false);
            pieChart.setEntryLabelColor(getResources().getColor(R.color.colorDarkGrey));
            pieChart.animateXY(700, 700);

            addDataSet();

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Timber.d("onValueSelected: Value select from chart:");
                    Timber.d("onValueSelected: %s", e.describeContents());
                    Timber.d("onValueSelected: %s", h.toString());
                }

                @Override
                public void onNothingSelected() {

                }
            });


            return view;

        }

        private void addDataSet() {
            //Timber.d("addDataSet: ");

            ArrayList<PieEntry> entries = new ArrayList<>();
            //populating the data
            for(int i=0;i<values.length;i++){
                if (values[i] > THRESHOLD)
                    entries.add(new PieEntry(values[i], labels[i]));

            }

            PieDataSet pieDataSet=new PieDataSet(entries, "");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(12);

            pieDataSet.setColors(
                    mergeIntArrays(
                            ColorTemplate.VORDIPLOM_COLORS,
                            ColorTemplate.JOYFUL_COLORS,
                            ColorTemplate.PASTEL_COLORS));
            pieDataSet.setValueTextColor(getResources().getColor(R.color.colorDarkGrey));

            //create pie data object
            PieData pieData= new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(12);

            Timber.d("Populating and rendering PieChart.");
            pieChart.setData(pieData);
            pieChart.invalidate();
        }

    }






    public static class BarChartFragment extends Fragment {

        private float[] values;
        private String[] labels;

        private float[] tempValues;
        private String[] tempLabels;

        int textColor;
        TextView titleText;
        private BarChart barChart;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Timber.d("Creating LineChart fragment.");

            View view = inflater.inflate(R.layout.fragment_budget_viewpager_barchart, container, false);

            titleText = view.findViewById(R.id.textview);

            if (new CustomSharedPreferences(getContext()).getActivityTheme() == CustomSharedPreferences.THEME_DARK)
                textColor = getResources().getColor(R.color.colorWhite);
            else
                textColor = getResources().getColor(R.color.colorDarkGrey);

            titleText.setTextColor(textColor);


            try {
                values = getArguments().getFloatArray("values");
                labels = getArguments().getStringArray("labels");
            } catch (Exception e) {
                Timber.e(e, "Failed to get data from parent fragment!");
            }

            int counter = 0;

            tempValues = new float[values.length];
            tempLabels = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                if (values[i] > 0.0f) {
                    tempValues[counter] = values[i];
                    tempLabels[counter] = labels[i];
                    counter++;
                }
            }

            values = new float[counter];
            labels = new String[counter];

            for (int i = 0; i < counter; i++) {
                values[i] = tempValues[i];
                labels[i] = tempLabels[i];
            }


            barChart = view.findViewById(R.id.barchart);

            barChart.getDescription().setText("");
            barChart.setDrawValueAboveBar(true);
            barChart.setDrawBarShadow(false);
            barChart.setFitBars(true);
            barChart.setSaveEnabled(true);
            barChart.setScaleYEnabled(true);
            barChart.getLegend().setEnabled(false);
            barChart.animateXY(700, 700);
            barChart.setNoDataTextColor(textColor);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new LabelFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(labels.length);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setDrawLimitLinesBehindData(false);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(textColor);
            xAxis.setLabelRotationAngle(-30f);

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setDrawZeroLine(true);
            yAxisLeft.setZeroLineColor(textColor);
            yAxisLeft.setAxisMaximum(100);
            yAxisLeft.setDrawGridLines(false);
            yAxisLeft.setGridColor(textColor);
            yAxisLeft.setTextColor(textColor);

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setGridColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setTextColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setAxisLineColor(getResources().getColor(R.color.colorTransparent));

            addDataSet();

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    /*
                    Timber.d("onValueSelected: Value select from chart:");
                    Timber.d("onValueSelected: %s", e.describeContents());
                    Timber.d("onValueSelected: %s", h.toString());
                    */
                }

                @Override
                public void onNothingSelected() {

                }
            });

            return view;
        }


        private void addDataSet() {
            //Timber.d("addDataSet: ");


            ArrayList<BarEntry> entriesList = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                if (values[i] > THRESHOLD)
                    entriesList.add(new BarEntry(i, values[i]));
            }



            BarDataSet barDataSet=new BarDataSet(entriesList,"");
            barDataSet.setValueTextSize(12);

            barDataSet.setColors(
                    mergeIntArrays(
                        ColorTemplate.VORDIPLOM_COLORS,
                        ColorTemplate.JOYFUL_COLORS,
                        ColorTemplate.PASTEL_COLORS));
            barDataSet.setValueTextColor(textColor);
            barDataSet.setHighLightColor(textColor);


            //create bar data object
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);
            BarData barData = new BarData(dataSets);

            barData.setValueFormatter(new PercentFormatter());
            barData.setValueTextSize(12);
            barData.setBarWidth(0.9f);

            Timber.d("Populating and rendering BarChart.");
            barChart.setData(barData);
            barChart.invalidate();
        }
    }










    public static class LineChartFragment extends Fragment {

        private float[] values;
        private String[] labels;
        private LineChart lineChart;
        TextView titleText;

        int textColor;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Timber.d("Creating LineChart fragment.");

            View view = inflater.inflate(R.layout.fragment_budget_viewpager_linechart, container, false);

            titleText = view.findViewById(R.id.textview);

            if (new CustomSharedPreferences(getContext()).getActivityTheme() == CustomSharedPreferences.THEME_DARK)
                textColor = getResources().getColor(R.color.colorWhite);
            else
                textColor = getResources().getColor(R.color.colorDarkGrey);

            titleText.setTextColor(textColor);


            try {
                values = getArguments().getFloatArray("values");
                labels = getArguments().getStringArray("labels");
            } catch (Exception e) {
                Timber.e(e, "Failed to get data from parent fragment!");
            }


            lineChart = view.findViewById(R.id.lineChart);

            lineChart.getDescription().setText("");
            lineChart.setSaveEnabled(true);
            lineChart.getLegend().setEnabled(false);
            lineChart.animateXY(700, 700);
            lineChart.setNoDataTextColor(textColor);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new LabelFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(labels.length);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(textColor);
            xAxis.setSpaceMin(.3f);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            xAxis.setLabelRotationAngle(-30f);
            xAxis.setTextColor(textColor);

            YAxis yAxisLeft = lineChart.getAxisLeft();
            yAxisLeft.setDrawZeroLine(true);
            yAxisLeft.setZeroLineColor(textColor);
            yAxisLeft.setAxisMinimum(0f);
            yAxisLeft.setDrawGridLines(false);
            yAxisLeft.setGridColor(textColor);
            yAxisLeft.setTextColor(textColor);

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setGridColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setTextColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setAxisLineColor(getResources().getColor(R.color.colorTransparent));

            addDataSet();

            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    /*
                    Timber.d("onValueSelected: Value select from chart:");
                    Timber.d("onValueSelected: %s", e.describeContents());
                    Timber.d("onValueSelected: %s", h.toString());
                    */
                }

                @Override
                public void onNothingSelected() {

                }
            });

            return view;
        }


        private void addDataSet() {
            //Timber.d("addDataSet: ");


            ArrayList<Entry> entriesList = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                entriesList.add(new Entry(i, values[i]));
            }



            LineDataSet lineDataSet = new LineDataSet(entriesList,"");
            lineDataSet.setValueTextSize(12);

            //lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            lineDataSet.setValueTextColor(textColor);
            lineDataSet.setHighLightColor(textColor);


            //create bar data object
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData lineData = new LineData(dataSets);

            lineData.setValueFormatter(new CurrencyFormatter());
            lineData.setValueTextSize(12);

            Timber.d("Populating and rendering LineChart.");
            lineChart.setData(lineData);
            lineChart.invalidate();
        }
    }






    public static class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }


    public static class CurrencyFormatter implements IValueFormatter {

        private DecimalFormat formatter;

        public CurrencyFormatter() {
            formatter = new DecimalFormat("#0.00");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "$" + formatter.format(value);
        }
    }


    public static int[] mergeIntArrays(final int[] ...arrays ) {
        int size = 0;
        for ( int[] a: arrays )
            size += a.length;

        int[] res = new int[size];

        int destPos = 0;
        for ( int i = 0; i < arrays.length; i++ ) {
            if ( i > 0 ) destPos += arrays[i-1].length;
            int length = arrays[i].length;
            System.arraycopy(arrays[i], 0, res, destPos, length);
        }

        return res;
    }
}


