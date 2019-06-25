package com.presldn.pouch.ui.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.database.TransactionType;
import com.presldn.pouch.viewmodels.PouchViewModel;
import com.presldn.pouch.ui.adapters.TXRecyclerViewAdapter;
import com.presldn.pouch.utils.Helper;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import faranjit.currency.edittext.CurrencyEditText;

public class PouchTransactionFragment extends Fragment {

    private PouchViewModel pouchViewModel;

    private RecyclerView mTxRecyclerView;
    private TextView emptyListTV;

    private TXRecyclerViewAdapter paymentRecyclerViewAdapter;

    public PouchTransactionFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pouch_tx, container, false);

        emptyListTV = rootView.findViewById(R.id.empty_list_tv);
        mTxRecyclerView = rootView.findViewById(R.id.tx_recycler_view);

        paymentRecyclerViewAdapter = new TXRecyclerViewAdapter(getContext());
        mTxRecyclerView.setAdapter(paymentRecyclerViewAdapter);

        pouchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PouchViewModel.class);
        pouchViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                paymentRecyclerViewAdapter.setTransactions(transactions);
                checkTransactionsSize(paymentRecyclerViewAdapter.getItemCount());
            }
        });

        FloatingActionButton mFab = rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showTransactionDialog();
            }
        });

        return rootView;
    }

    private void checkTransactionsSize(int itemCount) {
        if (itemCount > 0) {
            mTxRecyclerView.setVisibility(View.VISIBLE);
            emptyListTV.setVisibility(View.INVISIBLE);
        } else {
            emptyListTV.setVisibility(View.VISIBLE);
            mTxRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showTransactionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialogCustom);

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View paymentDialog = layoutInflater.inflate(R.layout.tx_dialog, null);

        final TextView amountInPouchLabel = paymentDialog.findViewById(R.id.pouch_totalSaved_tv_label);
        final TextView amountInPouchTV = paymentDialog.findViewById(R.id.pouch_totalSaved_tv);

        final Pouch pouch = pouchViewModel.getPouch().getValue();

        assert pouch != null;
        amountInPouchLabel.setText(getString(R.string.amount_saved_in_pouch, pouch.getName()));
        amountInPouchTV.setText(Helper.toMoneyFormat(pouch.getBalance()));

        final RadioGroup transactionTypesRG = paymentDialog.findViewById(R.id.radioGroup);
        transactionTypesRG.check(R.id.deposit_rb);
        final CurrencyEditText amountET = paymentDialog.findViewById(R.id.amount_et);

        transactionTypesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.deposit_rb :
                        amountInPouchLabel.setVisibility(View.GONE);
                        amountInPouchTV.setVisibility(View.GONE);
                        break;
                    case R.id.withdraw_rb :
                        amountInPouchLabel.setVisibility(View.VISIBLE);
                        amountInPouchTV.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        builder.setTitle(R.string.edit_payment);
        builder.setMessage(R.string.payment_dialog_message);
        builder.setView(paymentDialog);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int amount;
                        try {
                            amount = (int) (amountET.getCurrencyDouble() * 100);
                        } catch (ParseException | NumberFormatException e) {
                            Helper.toastMessage(getContext(), "Please enter a positive amount.");
                            return;
                        }
                        TransactionType transactionType;

                        switch (transactionTypesRG.getCheckedRadioButtonId()) {
                            case R.id.deposit_rb:
                                transactionType = TransactionType.DEPOSIT;
                                break;
                            case R.id.withdraw_rb:
                                transactionType = TransactionType.WITHDRAW;
                                break;
                            default:
                                Helper.toastMessage(getContext(), "Please select a transaction type.");
                                return;
                        }

                        Transaction transaction = new Transaction(pouch.getName(), amount, transactionType.name());
                        pouchViewModel.insertTransaction(transaction);
                        dialog.dismiss();

                        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content),
                                "Transaction added.", Snackbar.LENGTH_SHORT).setAction(R.string.alright, null);

                        // get snackbar view
                        View snackbarView = snackbar.getView();

                        // change snackbar text color
                        int snackbarTextId = android.support.design.R.id.snackbar_text;
                        TextView textView = snackbarView.findViewById(snackbarTextId);
                        textView.setTextColor(getResources().getColor(R.color.colorPrimary));

                        snackbar.show();
                    }
                });
            }
        });

        dialog.show();
    }
}
