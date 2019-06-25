package com.presldn.pouch.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.presldn.pouch.database.PouchRepository;

public class PouchConfigViewModel extends AndroidViewModel {

    private PouchRepository pouchRepository;

    public PouchConfigViewModel(@NonNull Application application) {
        super(application);
        this.pouchRepository = new PouchRepository(application);
    }

    public boolean nameExists(String name) {
        return pouchRepository.nameExists(name);
    }
}
