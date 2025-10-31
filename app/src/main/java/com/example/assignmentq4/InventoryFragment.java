package com.example.assignmentq4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentq4.dao.Product;

import java.util.List;

public class InventoryFragment extends Fragment {
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        dbHelper = new DBHelper(getContext());

        recyclerView = view.findViewById(R.id.recycler_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadProducts();

        Button addButton = view.findViewById(R.id.add_product);
        addButton.setOnClickListener(v -> showAddEditDialog(null));

        return view;
    }

    private void loadProducts() {
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();

        products = dbHelper.getAllProducts();
        adapter = new ProductAdapter(products, this::showAddEditDialog, this::confirmDelete);
        recyclerView.setAdapter(adapter);

        progress.dismiss();
    }

    private void showAddEditDialog(Product product) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product, null);
        EditText nameEt = dialogView.findViewById(R.id.et_name);
        EditText priceEt = dialogView.findViewById(R.id.et_price);
        EditText costEt = dialogView.findViewById(R.id.et_cost);
        EditText stockEt = dialogView.findViewById(R.id.et_stock);
        Spinner categorySpinner = dialogView.findViewById(R.id.spinner_category);

        String[] categories = {"Electronics", "Clothing", "Food", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        categorySpinner.setAdapter(spinnerAdapter);

        if (product != null) {
            nameEt.setText(product.getName());
            priceEt.setText(String.valueOf(product.getPrice()));
            costEt.setText(String.valueOf(product.cost));
            stockEt.setText(String.valueOf(product.stock));
            categorySpinner.setSelection(spinnerAdapter.getPosition(product.category));
        }

        new AlertDialog.Builder(getContext())
                .setTitle(product == null ? "Add Product" : "Edit Product")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameEt.getText().toString();
                    String priceStr = priceEt.getText().toString();
                    String costStr = costEt.getText().toString();
                    String stockStr = stockEt.getText().toString();
                    String category = categorySpinner.getSelectedItem().toString();

                    if (name.isEmpty() || priceStr.isEmpty() || costStr.isEmpty() || stockStr.isEmpty()) {
                        Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double price = Double.parseDouble(priceStr);
                    double cost = Double.parseDouble(costStr);
                    int stock = Integer.parseInt(stockStr);

                    if (product == null) {
                        dbHelper.addProduct(name, price, cost, stock, category);
                    } else {
                        dbHelper.updateProduct(product.id, name, price, cost, stock, category);
                    }
                    loadProducts();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(Product product) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteProduct(product.id);
                    loadProducts();
                })
                .setNegativeButton("No", null)
                .show();
    }
}