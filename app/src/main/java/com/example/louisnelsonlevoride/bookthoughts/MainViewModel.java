package com.example.louisnelsonlevoride.bookthoughts;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.louisnelsonlevoride.bookthoughts.DBData.BookDatabase;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Book>> books;
    private BookDatabase database;

    public MainViewModel(Application application) {
        super(application);
        database = BookDatabase.getInstance(this.getApplication());
        books = database.BookDao().loadAllBooks();
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

}
