package edu.usm.cs.csc414.pocketfinances;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecurringExpensesSectionViewHolder extends RecyclerView.ViewHolder {

    public TextView sectionName;


    public RecurringExpensesSectionViewHolder(View itemView) {
        super(itemView);
        sectionName = (TextView)itemView.findViewById(R.id.recurring_expenses_recyclerview_textview_sectionname);
    }
}
