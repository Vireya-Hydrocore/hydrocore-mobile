package com.vireya.hydrocore.estoque.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vireya.hydrocore.estoque.model.Produto;

import java.util.List;

@Dao
public interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Produto> produto);

    @Query("SELECT * FROM produtos")
    List<Produto> getAll();

    @Query("DELETE FROM produtos")
    void deleteAll();
}
