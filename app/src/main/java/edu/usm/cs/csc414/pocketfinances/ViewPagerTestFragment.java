package edu.usm.cs.csc414.pocketfinances;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
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
import java.util.Locale;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import timber.log.Timber;

public class ViewPagerTestFragment extends Fragment {

    private static final float MIN_SCALE = 0.55f;
    private static final float MIN_ALPHA = 0.55f;


    private float[] values={25.3f,10.6f,44.32f,16.54f};
    private String[] labels={"Rent","Utility","Entertainment","Dining"};

    VerticalViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_viewpager, container, false);

        viewPager = view.findViewById(R.id.fragment_test_viewpager_layout);


        viewPager.setAdapter(new VerticalViewPagerAdapter(getFragmentManager()));

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
                view1.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view1.setAlpha(0);
            }
        });



        return view;
    }




    public class VerticalViewPagerAdapter extends FragmentPagerAdapter {

        public VerticalViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                    bundle.putStringArray("labels", labels);
                    bundle.putFloatArray("values", values);
                    PieChartFragment pieFragment = new PieChartFragment();
                    pieFragment.setArguments(bundle);
                    return pieFragment;
                case 1:
                    bundle.putStringArray("labels", labels);
                    bundle.putFloatArray("values", values);
                    BarChartFragment barFragment = new BarChartFragment();
                    barFragment.setArguments(bundle);
                    return barFragment;
                case 2:
                    String[] monthlySpendingLabels = {"Jan. '18", "Feb. '18", "Mar. '18", "Apr. '18", "May. '18", "Jun. '18"};
                    float[] monthlySpendingValues = {1042.10f, 801.99f, 1180.00f, 1102.85f, 1730.77f, 1512.33f};
                    bundle.putStringArray("labels", monthlySpendingLabels);
                    bundle.putFloatArray("values", monthlySpendingValues);
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

        PieChart pieChart;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test_viewpager_piechart, container, false);

            try {
                values = getArguments().getFloatArray("values");
                labels = getArguments().getStringArray("labels");
            } catch (Exception e) {
                Timber.e(e, "Failed to get data from parent fragment!");
            }


            pieChart = (PieChart) view.findViewById(R.id.piechart);

            pieChart.getDescription().setText("Spending Statistics");
            pieChart.setRotationEnabled(false);
            pieChart.setHoleRadius(25f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setHoleColor(getResources().getColor(R.color.colorTransparent));
            pieChart.setCenterText("Monthly\r\nSpending");
            pieChart.setCenterTextSize(14);
            pieChart.setCenterTextColor(getResources().getColor(R.color.colorWhite));
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
            Timber.d("addDataSet: ");

            ArrayList<PieEntry> entries = new ArrayList<>();
            //populating the data
            for(int i=0;i<values.length;i++){
                entries.add(new PieEntry(values[i], labels[i]));

            }

            PieDataSet pieDataSet=new PieDataSet(entries, "");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(12);

            pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            pieDataSet.setValueTextColor(getResources().getColor(R.color.colorDarkGrey));

            //create pie data object
            PieData pieData= new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(12);
            pieChart.setData(pieData);
            pieChart.invalidate();
        }

    }






    public static class BarChartFragment extends Fragment {

        private float[] values;
        private String[] labels;
        private BarChart barChart;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test_viewpager_barchart, container, false);


            try {
                values = getArguments().getFloatArray("values");
                labels = getArguments().getStringArray("labels");
            } catch (Exception e) {
                Timber.e(e, "Failed to get data from parent fragment!");
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
            barChart.setNoDataTextColor(getResources().getColor(R.color.colorWhite));

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new LabelFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(labels.length);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setDrawLimitLinesBehindData(false);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(getResources().getColor(R.color.colorWhite));
            xAxis.setLabelRotationAngle(-30f);

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setDrawZeroLine(true);
            yAxisLeft.setZeroLineColor(getResources().getColor(R.color.colorWhite));
            yAxisLeft.setAxisMaximum(100);
            yAxisLeft.setDrawGridLines(false);
            yAxisLeft.setGridColor(getResources().getColor(R.color.colorWhite));
            yAxisLeft.setTextColor(getResources().getColor(R.color.colorWhite));

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setGridColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setTextColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setAxisLineColor(getResources().getColor(R.color.colorTransparent));

            addDataSet();

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

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
            Timber.d("addDataSet: ");


            ArrayList<BarEntry> entriesList = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                entriesList.add(new BarEntry(i, values[i]));
            }



            BarDataSet barDataSet=new BarDataSet(entriesList,"");
            barDataSet.setValueTextSize(12);

            barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            barDataSet.setValueTextColor(getResources().getColor(R.color.colorWhite));
            barDataSet.setHighLightColor(getResources().getColor(R.color.colorWhite));


            //create bar data object
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);
            BarData barData = new BarData(dataSets);

            barData.setValueFormatter(new PercentFormatter());
            barData.setValueTextSize(12);
            barData.setBarWidth(0.9f);

            barChart.setData(barData);
            barChart.invalidate();
        }
    }










    public static class LineChartFragment extends Fragment {

        private float[] values;
        private String[] labels;
        private LineChart lineChart;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test_viewpager_linechart, container, false);


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
            lineChart.setNoDataTextColor(getResources().getColor(R.color.colorWhite));

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new LabelFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(labels.length);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(getResources().getColor(R.color.colorWhite));
            xAxis.setSpaceMin(.3f);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            xAxis.setLabelRotationAngle(-30f);
            xAxis.setTextColor(getResources().getColor(R.color.colorWhite));

            YAxis yAxisLeft = lineChart.getAxisLeft();
            yAxisLeft.setDrawZeroLine(true);
            yAxisLeft.setZeroLineColor(getResources().getColor(R.color.colorWhite));
            yAxisLeft.setAxisMinimum(0f);
            yAxisLeft.setDrawGridLines(false);
            yAxisLeft.setGridColor(getResources().getColor(R.color.colorWhite));
            yAxisLeft.setTextColor(getResources().getColor(R.color.colorWhite));

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setGridColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setTextColor(getResources().getColor(R.color.colorTransparent));
            yAxisRight.setAxisLineColor(getResources().getColor(R.color.colorTransparent));

            addDataSet();

            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

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
            Timber.d("addDataSet: ");


            ArrayList<Entry> entriesList = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                entriesList.add(new Entry(i, values[i]));
            }



            LineDataSet lineDataSet = new LineDataSet(entriesList,"");
            lineDataSet.setValueTextSize(12);

            lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            lineDataSet.setValueTextColor(getResources().getColor(R.color.colorWhite));
            lineDataSet.setHighLightColor(getResources().getColor(R.color.colorWhite));


            //create bar data object
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData lineData = new LineData(dataSets);

            lineData.setValueFormatter(new CurrencyFormatter());
            lineData.setValueTextSize(12);

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
            formatter = new DecimalFormat("#.00");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "$" + formatter.format(value);
        }
    }
}
