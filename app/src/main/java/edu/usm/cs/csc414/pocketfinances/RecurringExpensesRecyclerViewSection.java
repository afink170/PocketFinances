package edu.usm.cs.csc414.pocketfinances;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class RecurringExpensesRecyclerViewSection extends StatelessSection {


    private static final String TAG = RecurringExpensesRecyclerViewSection.class.getSimpleName();

    private String title;
    private List<Expense> list;
    private Context context;
    private View.OnLongClickListener onLongClickListener;


    public RecurringExpensesRecyclerViewSection(String sectionName, List<Expense> list, Context context) {
        super(R.layout.recurring_expenses_recyclerview_sectionheader_item, R.layout.recurring_expenses_recyclerview_item);
        this.title = sectionName;
        this.list = list;
        this.context = context;
    }


    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    @Override
    public int getContentItemsTotal() {
        return list.size();
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new RecurringExpensesItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Expense expense = list.get(position);

        RecurringExpensesItemViewHolder itemViewHolder = (RecurringExpensesItemViewHolder)holder;

        itemViewHolder.expenseName.setText(expense.getTitle());
        itemViewHolder.expenseDate.setText(ExpenseTypeConverters.dateToString(expense.getDate()));
        itemViewHolder.expenseAmount.setText(ExpenseTypeConverters.amountToString(expense.getAmount()));
        itemViewHolder.expenseId.setText(String.valueOf(expense.getExpenseId()));

        if (expense.getDepositOrDeduct() == Expense.DEPOSIT) {
            itemViewHolder.expenseAmount.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
        else {
            itemViewHolder.expenseAmount.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        itemViewHolder.itemView.setOnLongClickListener(onLongClickListener);
    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new RecurringExpensesSectionViewHolder(view);
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        RecurringExpensesSectionViewHolder sectionViewHolder = (RecurringExpensesSectionViewHolder)holder;
        sectionViewHolder.sectionName.setText(title);
    }
}
