package com.example.louisnelsonlevoride.bookthoughts.DBData;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book ORDER BY created_at DESC")
    LiveData<List<Book>> loadAllBooks();

    @Insert
    void insertBook(Book book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(List<Book> books);

    @Delete
    void deleteBook(Book book);

    @Query("DELETE FROM book")
    void deleteAllBooks();

    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<Book> loadBookById(int id);
}
