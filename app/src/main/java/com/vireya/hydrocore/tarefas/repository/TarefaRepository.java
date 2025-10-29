package com.vireya.hydrocore.tarefas.repository;

import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.dao.TarefaDao;
import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.util.List;

public class TarefaRepository {

    private final TarefaDao tarefaDao;
    private final TarefasApi tarefasApi;

    public TarefaRepository(TarefaDao tarefaDao, TarefasApi tarefasApi) {
        this.tarefaDao = tarefaDao;
        this.tarefasApi = tarefasApi;
    }



    public List<Tarefa> getAllTarefas() {
        return tarefaDao.getAll();
    }

    public TarefaDao getTarefaDao() {
        return tarefaDao;
    }
}
