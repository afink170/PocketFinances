package edu.usm.cs.csc414.pocketfinances;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecurringExpensesItemViewHolder extends RecyclerView.ViewHolder {

    public TextView expenseName, expenseDate, expenseAmount, expenseId;



    public RecurringExpensesItemViewHolder(View itemView) {
        super(itemView);
        expenseName = (TextView)itemView.findViewById(R.id.recurring_expenses_recyclerview_textview_expensename);
        expenseDate = (TextView)itemView.findViewById(R.id.recurring_expenses_recyclerview_textview_expensedate);
        expenseAmount = (TextView)itemView.findViewById(R.id.recurring_expenses_recyclerview_textview_expenseamount);
        expenseId = (TextView)itemView.findViewById(R.id.recurring_expenses_recyclerview_textview_expenseid);
    }
}
