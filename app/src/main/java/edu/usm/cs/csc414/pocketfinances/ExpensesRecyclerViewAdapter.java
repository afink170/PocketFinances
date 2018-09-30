package edu.usm.cs.csc414.pocketfinances;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpensesRecyclerViewAdapter extends RecyclerView.Adapter<ExpensesRecyclerViewAdapter.RecyclerViewHolder> {

    private List<Expense> expenseList;
    private RecyclerView recyclerView;
    private View.OnClickListener onClickListener;

    public ExpensesRecyclerViewAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenses_recyclerview_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.nameTextView.setText(expense.getTitle());
        holder.dateTextView.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.US).toString());
        holder.dateTextView.setText(String.format(Locale.US, "%2d/%2d/%4d", expense.getDate().get(Calendar.MONTH)+1,
                expense.getDate().get(Calendar.DAY_OF_MONTH), expense.getDate().get(Calendar.YEAR)));
        holder.categoryTextView.setText(expense.getCategory().getText());
        holder.amountTextView.setText(String.format(Locale.US, "$%.2f", expense.getAmount()));
        holder.itemView.setTag(expense);

        if (expense.getDepositOrDeduct() == Expense.DEPOSIT) {
            holder.amountTextView.setTextColor(recyclerView.getResources().getColor(R.color.colorGreen));
        }
        else {
            holder.amountTextView.setTextColor(recyclerView.getResources().getColor(R.color.colorRed));
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void addItems(List<Expense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView categoryTextView;
        private TextView amountTextView;

        RecyclerViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.expenses_recyclerview_textview_expensename);
            dateTextView = (TextView) view.findViewById(R.id.expenses_recyclerview_textview_expensedate);
            categoryTextView = (TextView) view.findViewById(R.id.expenses_recyclerview_textview_expensecategory);
            amountTextView = (TextView) view.findViewById(R.id.expenses_recyclerview_textview_expenseamount);
        }
    }
}
