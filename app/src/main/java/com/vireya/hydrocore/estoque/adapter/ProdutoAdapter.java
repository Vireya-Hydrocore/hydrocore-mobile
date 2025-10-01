package com.vireya.hydrocore.estoque.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.estoque.model.Produto;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProductViewHolder> {

    private List<Produto> productList;

    public ProdutoAdapter(List<Produto> productList) {
        this.productList = productList;
    }

    public void updateList(List<Produto> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Produto produto = productList.get(position);


        holder.name.setText(produto.getNome());
        holder.quantity.setText(produto.getQuantidade() + "L");

        switch (produto.getStatus()) {
            case "Suficiente":
                holder.statusBar.setBackgroundColor(0xFF00796B);
                break;
            case "Próximo ao fim":
                holder.statusBar.setBackgroundColor(0xFFFBC02D);
                break;
            case "Insuficiente":
                holder.statusBar.setBackgroundColor(0xFFD32F2F);
                break;
            default:
                holder.statusBar.setBackgroundColor(0xFF9E9E9E);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity;
        View statusBar;
        Button button;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nomeProduto);
            quantity = itemView.findViewById(R.id.textQuantidade);
            statusBar = itemView.findViewById(R.id.statusBar);
            button = itemView.findViewById(R.id.button);

        }
    }
}
