package com.presldn.pouch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.presldn.pouch.R;
import com.presldn.pouch.application.PouchApplication;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.database.TransactionType;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by presldn on 22/02/2018.
 */

public class Helper {

    public static String convertToDate(long timeInMillis) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date date = new Date(timeInMillis);
        return simpleDateFormat.format(date);
    }

    public static int getProgress(int goal, int balance) {

        if (!(goal > 0)) {
            return 0;
        }

        int progress = balance * 100 / goal;

        if (progress < 0) {
            return 0;
        } else if (progress >= 100) {
            return 100;
        } else {
            return progress;
        }
    }

    public static String getProgressPercentage(int goal, int balance) {
        int progress;
        if (goal > 0) {
            progress = balance * 100 / goal;
        } else {
            progress = 0;
        }

        return String.format("%s%%", String.valueOf(progress > 0 ? progress : 0));
    }

    public static String toPercentageFormat(int percentage) {
        return String.format("%s%%", String.valueOf(percentage));
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String toMoneyFormat(int amount) {

        double doubleAmount = (double) amount;
        doubleAmount /= 100;

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");

        return String.format("%s%s", PouchApplication.getInstance().getProfile().getCurrencySymbol(), decimalFormat.format(doubleAmount));
    }

    public static String toTransactionFormat(Transaction transaction) {
        PouchApplication pouchApplication = PouchApplication.getInstance();

        TransactionType transactionType = TransactionType.valueOf(transaction.getTransactionType());

        switch (transactionType) {
            case WITHDRAW:
                return pouchApplication.getString(R.string.negative_transaction, toMoneyFormat(transaction.getAmount()));
            case DEPOSIT:
                return pouchApplication.getString(R.string.positive_transaction, toMoneyFormat(transaction.getAmount()));
            default:
                return null;
        }

    }
}
