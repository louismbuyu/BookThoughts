package com.example.louisnelsonlevoride.bookthoughts.DBData;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    private static final String LOG_TAG = BookDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "books";
    private static BookDatabase sInstance;

    public static BookDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        BookDatabase.class, BookDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract BookDao BookDao();
}
