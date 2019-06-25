package com.presldn.pouch.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Pouch.class, Transaction.class}, version = 3)
public abstract class PouchRoomDatabase extends RoomDatabase {

    private static PouchRoomDatabase INSTANCE;

    public abstract PouchDAO pouchDAO();
    public abstract TransactionDAO transactionDAO();

    public static PouchRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PouchRoomDatabase.class) {
                if (INSTANCE == null) {
                    //Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PouchRoomDatabase.class, "pouch_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
