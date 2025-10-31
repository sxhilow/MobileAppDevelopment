package com.example.assignmentq4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.assignmentq4.dao.Product;

import java.util.List;

public class DashboardFragment extends Fragment {
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dbHelper = new DBHelper(getContext());

        TextView totalSales = view.findViewById(R.id.total_sales);
        TextView totalProfit = view.findViewById(R.id.total_profit);
        TextView inventoryValue = view.findViewById(R.id.inventory_value);

        totalSales.setText("Total Sales: $" + dbHelper.getTotalSales());
        totalProfit.setText("Total Profit: $" + dbHelper.getTotalProfit());
        inventoryValue.setText("Inventory Value: $" + dbHelper.getInventoryValue());

        // Low stock alert
        List<Product> lowStock = dbHelper.getLowStockProducts();
        if (!lowStock.isEmpty()) {
            StringBuilder alertMsg = new StringBuilder("Low Stock Items:\n");
            for (Product p : lowStock) {
                alertMsg.append(p.getName()).append(" - Stock: ").append(p.getStock()).append("\n");
            }
            new AlertDialog.Builder(getContext())
                    .setTitle("Low Stock Alert")
                    .setMessage(alertMsg.toString())
                    .setPositiveButton("OK", null)
                    .show();
        }

        return view;
    }
}