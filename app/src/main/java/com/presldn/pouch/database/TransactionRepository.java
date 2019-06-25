package com.presldn.pouch.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TransactionRepository {

    private TransactionDAO transactionDAO;

    private LiveData<List<Transaction>> transactions;

    public TransactionRepository(Application application) {
        PouchRoomDatabase db = PouchRoomDatabase.getDatabase(application);
        this.transactionDAO = db.transactionDAO();
    }

    public LiveData<List<Transaction>> getRecentTransactions() {
        this.transactions = transactionDAO.getRecentTransactions();
        return transactions;
    }

    public LiveData<List<Transaction>> getPouchesTransactions(String pouchName) {
        this.transactions = this.transactionDAO.getPouchesTransactions(pouchName);
        return transactions;
    }

    public void insert(Transaction transaction) {
        new TransactionAsyncTask(CRUDOperation.INSERT, transactionDAO).execute(transaction);
    }

    public void delete(Transaction transaction) {
        new TransactionAsyncTask(CRUDOperation.DELETE, transactionDAO).execute(transaction);
    }

    private static class TransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private CRUDOperation operation;
        private TransactionDAO asyncTaskDao;

        TransactionAsyncTask(CRUDOperation operation, TransactionDAO transactionDAO) {
            this.operation = operation;
            this.asyncTaskDao = transactionDAO;
        }

        @Override
        protected Void doInBackground(Transaction... params) {
            switch (operation) {
                case INSERT:
                    asyncTaskDao.insert(params[0]);
                    break;
                case DELETE:
                    asyncTaskDao.delete(params[0]);
                    break;
            }
            return null;
        }
    }
}
