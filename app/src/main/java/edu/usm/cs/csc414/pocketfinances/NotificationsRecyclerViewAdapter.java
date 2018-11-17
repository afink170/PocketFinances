package edu.usm.cs.csc414.pocketfinances;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.RecyclerViewHolder> {

    private List<Expense> expenseList;
    private RecyclerView recyclerView;
    private Context context;


    public NotificationsRecyclerViewAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }


    @Override
    public NotificationsRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_recyclerview_item, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    @Override
    public void onBindViewHolder(final NotificationsRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        String dayName = expense.getNextOccurrence().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

        holder.titleTextView.setText(expense.getTitle());
        holder.dueDateTextView.setText(String.format(context.getResources().getString(R.string.notification_duedate_builder), dayName,
                ExpenseTypeConverters.dateToString(expense.getNextOccurrence())));
        holder.itemView.setTag(expense);

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
        private TextView titleTextView;
        private TextView dueDateTextView;

        RecyclerViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.notifications_recyclerview_item_title);
            dueDateTextView = (TextView) view.findViewById(R.id.notifications_recyclerview_item_duedate);
        }
    }

}
