package com.vireya.hydrocore.tarefas;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarefa {
    //variáveis
    private String titulo;
    private String descricao;
    private Date prazo;

    //Construtor
    public Tarefa(String titulo, String descricao, Date prazo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prazo = prazo;
    }

    //Getters e Setters
    public String getTitulo(){return this.titulo;}
    public String getDescricao(){return this.descricao;}
    public String getPrazo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(prazo);
    }
    public void setTitulo(String titulo){this.titulo = titulo;}
    public void setDescricao(String descricao){this.descricao = descricao;}
    public void setPrazo(Date prazo){this.prazo = prazo;}

    @Override
    public String toString() {
        return "Tarefa{" + "titulo='" + titulo + '\'' + ", descricao='" + descricao + '\'' + ", prazo=" + prazo + '}';
    }
}
