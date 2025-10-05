package com.vireya.hydrocore.tarefas.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.vireya.hydrocore.tarefas.dao.TarefaDao;
import com.vireya.hydrocore.tarefas.model.Tarefa;

@Database(entities = {Tarefa.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class}) //

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract TarefaDao getTarefaDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "tarefas_db"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
