package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import timber.log.Timber;

public class BudgetFragment extends Fragment {

    private float[] yData={25.3f,10.6f,44.32f,16.54f};
    private String[] xData={"Expenses","Income","Subscription","recurringExp"};
    PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
       pieChart = (PieChart) view.findViewById(R.id.idPieChart);

        //pieChart.setDescription("BUDGET");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("BUDGET");
        pieChart.setCenterTextSize(10);
        pieChart.setUsePercentValues(true);

        pieChart.animateXY(1400, 1400);
        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {



            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Timber.d("onValueSelected: Value select from chart:");
                Timber.d("onValueSelected: %s", e.describeContents());
                Timber.d("onValueSelected: %s", h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for(int i = 0; i < yData.length; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });


        return view;

    }

    private void addDataSet() {
        Timber.d("addDataSet: ");

        ArrayList<PieEntry> yEntrys=new ArrayList<>();
        ArrayList<String> xEntrys=new ArrayList<>();
        //populating the data
        for(int i=0;i<yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));

        }
        for(int i=0;i<xData.length;i++){
            xEntrys.add(xData[i]);
        }//create the data set

        PieDataSet pieDataSet=new PieDataSet(yEntrys,"BUDGET TRACKING");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        //adding legend
        Legend legend= pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);

        //create pie data object
        PieData pieData= new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}


