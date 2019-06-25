package com.presldn.pouch.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.presldn.pouch.application.PouchApplication;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.database.PouchRepository;
import com.presldn.pouch.database.Profile;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.database.TransactionRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Profile> profile;
    private PouchRepository pouchRepository;
    private LiveData<List<Pouch>> pouches;

    private LiveData<Pouch> mostSavedPouch, almostCompletedPouch;
    private LiveData<List<Transaction>> recentTransactions;

    public MainViewModel(Application application) {
        super(application);

        PouchApplication pouchApplication = PouchApplication.getInstance();
        this.profile = new MutableLiveData<>();
        this.profile.setValue(pouchApplication.getProfile());

        this.pouchRepository = new PouchRepository(application);
        this.pouches = pouchRepository.getAllPouches();

        TransactionRepository transactionRepository = new TransactionRepository(application);
        this.recentTransactions = transactionRepository.getRecentTransactions();

        this.mostSavedPouch = pouchRepository.getMostSavedPouch();
        almostCompletedPouch = pouchRepository.getAlmostCompletedPouch();
    }

    public LiveData<List<Transaction>> getRecentTransactions() {
        return recentTransactions;
    }

    public LiveData<Profile> getProfile() {
        return this.profile;
    }

    public LiveData<Pouch> getMostSavedPouch() {
        return mostSavedPouch;
    }

    public LiveData<List<Pouch>> getPouches() {
        return this.pouches;
    }

    public void insertPouch(Pouch pouch) {
        this.pouchRepository.insert(pouch);
    }

    public LiveData<Pouch> getAlmostCompletedPouch() {
        return almostCompletedPouch;
    }
}
