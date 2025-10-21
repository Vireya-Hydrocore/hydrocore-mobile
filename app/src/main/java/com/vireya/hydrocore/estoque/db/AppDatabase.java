package com.vireya.hydrocore.estoque.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vireya.hydrocore.estoque.dao.ProdutoDao;
import com.vireya.hydrocore.estoque.model.Produto;

@Database(entities = {Produto.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ProdutoDao produtoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "estoque.db"
                    )
                    // 🔹 Importante para evitar o erro de schema enquanto você desenvolve
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
