package com.vireya.hydrocore.estoque.model;

import java.util.List;

public class ProdutoResponse {
    private int id;
    private List<Produto> produtos;

    public int getId() { return id; }
    public List<Produto> getProdutos() { return produtos; }
}
