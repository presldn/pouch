package com.presldn.pouch.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.presldn.pouch.application.PouchApplication;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.database.PouchRepository;
import com.presldn.pouch.database.Profile;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.database.TransactionRepository;
import com.presldn.pouch.database.TransactionType;

import java.util.List;
import java.util.Objects;

public class PouchViewModel extends AndroidViewModel {

    private PouchRepository pouchRepository;
    private TransactionRepository transactionRepository;

    private Profile profile;

    private LiveData<Pouch> pouch;

    private LiveData<List<Transaction>> transactions;

    public PouchViewModel(@NonNull Application application) {
        super(application);

        PouchApplication pouchApplication = PouchApplication.getInstance();
        this.profile = pouchApplication.getProfile();

        this.pouchRepository = new PouchRepository(application);
        this.transactionRepository = new TransactionRepository(application);
    }

    public LiveData<Pouch> getPouch(String name) {
        this.transactions = transactionRepository.getPouchesTransactions(name);
        this.pouch = pouchRepository.getPouch(name);
        return this.pouch;
    }

    public LiveData<List<Transaction>> getTransactions() {
        return this.transactions;
    }

    public void insertTransaction(Transaction transaction) {

        if (pouch.getValue() == null) {
            return;
        }

        int transactionAmount = transaction.getAmount();

        switch (TransactionType.valueOf(transaction.getTransactionType())) {
            case WITHDRAW:
                profile.remove(transactionAmount);
                pouch.getValue().removeBalance(transactionAmount);
                break;
            case DEPOSIT:
                profile.add(transactionAmount);
                pouch.getValue().addBalance(transactionAmount);
                break;
        }

        updatePouch(pouch.getValue());
        this.transactionRepository.insert(transaction);
    }

    public void deleteTransaction(Transaction transaction) {

        if (pouch.getValue() == null) {
            return;
        }

        int transactionAmount = transaction.getAmount();

        switch (TransactionType.valueOf(transaction.getTransactionType())) {
            case WITHDRAW:
                profile.add(transactionAmount);
                pouch.getValue().addBalance(transactionAmount);
                break;

            case DEPOSIT:
                profile.remove(transactionAmount);
                pouch.getValue().removeBalance(transactionAmount);
                break;
        }

        updatePouch(pouch.getValue());
        this.transactionRepository.delete(transaction);
    }

    public void updatePouch(Pouch pouch) {
        this.pouchRepository.update(pouch);
    }

    public void deletePouch() {
        int pouchBalance = Objects.requireNonNull(this.pouch.getValue()).getBalance();

        profile.remove(pouchBalance);

        pouchRepository.delete(this.pouch.getValue());
        this.pouch = null;

    }

    public LiveData<Pouch> getPouch() {
        return pouch;
    }

    public void setGoal(int goal) {
        if (pouch.getValue() != null) {
            this.pouch.getValue().setGoal(goal);
        }
    }

    public void setName(String name) {
        if (pouch.getValue() != null) {
            this.pouch.getValue().setName(name);
        }
    }

}
