package com.vireya.hydrocore.tarefas;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Tarefa {
    @SerializedName("idTarefa")
    private int idTarefa;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("dataCriacao")
    private Date dataCriacao;

    @SerializedName("dataConclusao")
    private Date dataConclusao;

    @SerializedName("status")
    private String status;

    @SerializedName("idFuncionario")
    private int idFuncionario;

    @SerializedName("nivel")
    private String nivel;

    public Tarefa(int idTarefa, String descricao, Date dataCriacao, Date dataConclusao, String status, int idFuncionario, String nivel) {
        this.idTarefa = idTarefa;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.idFuncionario = idFuncionario;
        this.nivel = nivel;
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

    public int getIdTarefa() { return idTarefa; }
    public void setIdTarefa(int idTarefa) { this.idTarefa = idTarefa; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }

    public Date getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(Date dataConclusao) { this.dataConclusao = dataConclusao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    @Override
    public String toString() {
        return "Tarefa{" +
                "idTarefa=" + idTarefa +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataConclusao=" + dataConclusao +
                ", status='" + status + '\'' +
                ", idFuncionario=" + idFuncionario +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}
