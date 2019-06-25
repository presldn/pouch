package com.presldn.pouch.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.databinding.RecentTxListBinding;

import java.util.List;

public class RecentTXRecyclerViewAdapter extends RecyclerView.Adapter<RecentTXRecyclerViewAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecentTxListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recent_tx_list, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final RecentTxListBinding recentTxListBinding;

        ViewHolder(RecentTxListBinding binding) {
            super(binding.getRoot());

            recentTxListBinding = binding;
        }

        public void bind(Transaction transaction) {
            recentTxListBinding.setTransaction(transaction);
            recentTxListBinding.executePendingBindings();
        }

    }
}
