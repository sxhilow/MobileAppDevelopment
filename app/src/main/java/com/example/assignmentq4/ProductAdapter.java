package com.example.assignmentq4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentq4.dao.Product;

import java.util.List;
import java.util.function.Consumer;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private Consumer<Product> onEdit;
    private Consumer<Product> onDelete;

    public ProductAdapter(List<Product> products, Consumer<Product> onEdit, Consumer<Product> onDelete) {
        this.products = products;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTv.setText(product.getName());
        holder.priceTv.setText("$" + product.getPrice());
        holder.stockTv.setText(" Stock: " + product.getStock());

        holder.itemView.setOnClickListener(v -> onEdit.accept(product));
        holder.itemView.setOnLongClickListener(v -> {
            onDelete.accept(product);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, priceTv, stockTv;

        ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            priceTv = itemView.findViewById(R.id.tv_price);
            stockTv = itemView.findViewById(R.id.tv_stock);
        }
    }
}