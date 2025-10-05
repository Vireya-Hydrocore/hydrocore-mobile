package com.vireya.hydrocore.tarefas.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.vireya.hydrocore.tarefas.model.Tarefa;
import java.util.List;

@Dao
public interface TarefaDao {

    @Query("SELECT * FROM tarefas")
    List<Tarefa> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Tarefa> tarefas);

    @Query("DELETE FROM tarefas")
    void deleteAll();
}
