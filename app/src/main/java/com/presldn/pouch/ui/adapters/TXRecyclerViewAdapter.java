package com.presldn.pouch.ui.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.presldn.pouch.R;
import com.presldn.pouch.database.TransactionType;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.viewmodels.PouchViewModel;
import com.presldn.pouch.utils.Helper;

import java.util.List;

public class TXRecyclerViewAdapter extends RecyclerView.Adapter<TXRecyclerViewAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private Context context;
    private final LayoutInflater inflater;


    public TXRecyclerViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tx_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);
        holder.mPaymentDate.setText(Helper.convertToDate(transaction.getDate()));

        TransactionType transactionType = TransactionType.valueOf(transaction.getTransactionType());
        switch (transactionType) {
            case WITHDRAW:
                holder.mPaymentAmount.setText(context.getString(R.string.negative_transaction, Helper.toMoneyFormat(transaction.getAmount())));
                holder.mPaymentAmount.setTextColor(context.getResources().getColor(R.color.colorWithdrawTransaction));
                break;
            case DEPOSIT:
                holder.mPaymentAmount.setText(context.getString(R.string.positive_transaction, Helper.toMoneyFormat(transaction.getAmount())));
                holder.mPaymentAmount.setTextColor(context.getResources().getColor(R.color.colorDepositTransaction));
                break;
        }

        holder.mDeleteTXBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                        .setTitle("Delete Transaction")
                        .setMessage("Are you sure you want to delete this transaction?")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete transaction
                                PouchViewModel model = ViewModelProviders.of((FragmentActivity) context).get(PouchViewModel.class);
                                model.deleteTransaction(transaction);
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mPaymentDate;
        final TextView mPaymentAmount;
        final ImageButton mDeleteTXBtn;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPaymentDate = view.findViewById(R.id.tx_date);
            mPaymentAmount = view.findViewById(R.id.tx_list_amount);
            mDeleteTXBtn = view.findViewById(R.id.delete_tx_btn);
        }

    }
}
