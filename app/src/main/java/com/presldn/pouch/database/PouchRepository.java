package com.presldn.pouch.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PouchRepository {

    private PouchDAO pouchDAO;
    private LiveData<List<Pouch>> pouches;

    public PouchRepository(Application application) {
        PouchRoomDatabase db = PouchRoomDatabase.getDatabase(application);
        this.pouchDAO = db.pouchDAO();
        this.pouches = pouchDAO.getAllPouches();
    }

    public LiveData<List<Pouch>> getAllPouches() {
        return pouches;
    }

    public LiveData<Pouch> getPouch(String name) {
        return pouchDAO.getPouch(name);
    }

    public boolean nameExists(String name) {
        try {
            return new NameExistsAsyncTask(pouchDAO).execute(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insert(Pouch pouch) {
        new PouchAsyncTask(CRUDOperation.INSERT, pouchDAO).execute(pouch);
    }

    public void update(Pouch pouch) {
        new PouchAsyncTask(CRUDOperation.UPDATE, pouchDAO).execute(pouch);
    }

    public LiveData<Pouch> getMostSavedPouch() {
        return pouchDAO.getMostSaved();
    }

    public LiveData<Pouch> getAlmostCompletedPouch() { return pouchDAO.getAlmostCompletedPouch(); }

    public void delete(Pouch pouch) {
        new PouchAsyncTask(CRUDOperation.DELETE, pouchDAO).execute(pouch);
    }

    private static class NameExistsAsyncTask extends AsyncTask<String, Void, Boolean> {

        private PouchDAO pouchDAO;

        NameExistsAsyncTask(PouchDAO pouchDAO) {
            this.pouchDAO = pouchDAO;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return pouchDAO.getNameCount(strings[0]) > 0;
        }

    }

    private static class PouchAsyncTask extends AsyncTask<Pouch, Void, Void> {

        private CRUDOperation operation;
        private PouchDAO asyncTaskDao;

        PouchAsyncTask(CRUDOperation operation, PouchDAO pouchDAO) {
            this.operation = operation;
            this.asyncTaskDao = pouchDAO;
        }

        @Override
        protected Void doInBackground(Pouch... params) {
            switch (operation) {
                case INSERT:
                    asyncTaskDao.insert(params[0]);
                    break;
                case UPDATE:
                    asyncTaskDao.update(params[0]);
                    break;
                case DELETE:
                    asyncTaskDao.delete(params[0]);
                    break;
            }
            return null;
        }
    }
}
