package com.vireya.hydrocore.ui.perfil.model;

public class Estatistica {
    private int tarefasParaHoje;
    private int tarefasFeitas;
    private int tarefasNaoRealizadas;
    private int tarefasTotais;

    public Estatistica(int tarefasParaHoje, int tarefasFeitas, int tarefasNaoRealizadas, int tarefasTotais) {
        this.tarefasParaHoje = tarefasParaHoje;
        this.tarefasFeitas = tarefasFeitas;
        this.tarefasNaoRealizadas = tarefasNaoRealizadas;
        this.tarefasTotais = tarefasTotais;
    }

    public int getTarefasParaHoje() {
        return tarefasParaHoje;
    }

    public int getTarefasFeitas() {
        return tarefasFeitas;
    }

    public int getTarefasNaoRealizadas() {
        return tarefasNaoRealizadas;
    }

    public int getTarefasTotais() {
        return tarefasTotais;
    }
}
