package com.example.assignmentq4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.assignmentq4.dao.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SalesFragment extends Fragment {
    private DBHelper dbHelper;
    private Spinner productSpinner;
    private EditText quantityEt;
    private TextView dateTv;
    private Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        dbHelper = new DBHelper(getContext());

        productSpinner = view.findViewById(R.id.spinner_product);
        quantityEt = view.findViewById(R.id.et_quantity);
        dateTv = view.findViewById(R.id.tv_date);
        Button recordButton = view.findViewById(R.id.record_sale);

        loadProducts();

        dateTv.setOnClickListener(v -> showDatePicker());
        updateDateLabel();

        recordButton.setOnClickListener(v -> recordSale());

        return view;
    }

    private void loadProducts() {
        List<Product> products = dbHelper.getAllProducts();
        String[] productNames = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            productNames[i] = products.get(i).name + " (ID: " + products.get(i).id + ")";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, productNames);
        productSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        new DatePickerDialog(getContext(), (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateTv.setText(sdf.format(calendar.getTime()));
    }

    private void recordSale() {
        if (productSpinner.getSelectedItem() == null) {
            Toast.makeText(getContext(), "No products", Toast.LENGTH_SHORT).show();
            return;
        }
        String selected = productSpinner.getSelectedItem().toString();
        int productId = Integer.parseInt(selected.split("ID: ")[1].replace(")", ""));
        String quantityStr = quantityEt.getText().toString();
        if (quantityStr.isEmpty()) {
            Toast.makeText(getContext(), "Enter quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = Integer.parseInt(quantityStr);
        String date = dateTv.getText().toString();

        Product product = dbHelper.getProductById(productId);
        if (product.stock < quantity) {
            Toast.makeText(getContext(), "Not enough stock", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = product.price * quantity;

        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Recording...");
        progress.show();

        dbHelper.addSale(productId, quantity, date, totalPrice);
        progress.dismiss();
        Toast.makeText(getContext(), "Sale recorded", Toast.LENGTH_SHORT).show();
        quantityEt.setText("");
    }
}