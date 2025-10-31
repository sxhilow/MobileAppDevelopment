package com.example.assignmentq4;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.assignmentq4.dao.Sale;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportsFragment extends Fragment {
    private DBHelper dbHelper;
    private TextView reportTv;
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        dbHelper = new DBHelper(getContext());

        reportTv = view.findViewById(R.id.report_text);
        barChart = view.findViewById(R.id.bar_chart);
        Button dailyButton = view.findViewById(R.id.btn_daily);
        Button weeklyButton = view.findViewById(R.id.btn_weekly);
        Button exportButton = view.findViewById(R.id.btn_export);

        dailyButton.setOnClickListener(v -> generateReport("daily"));
        weeklyButton.setOnClickListener(v -> generateReport("weekly"));
        exportButton.setOnClickListener(v -> exportToCSV());

        return view;
    }

    private void generateReport(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String endDate = sdf.format(cal.getTime());
        String startDate;
        if (type.equals("daily")) {
            startDate = endDate;
        } else { // weekly
            cal.add(Calendar.DAY_OF_YEAR, -7);
            startDate = sdf.format(cal.getTime());
        }

        List<Sale> sales = dbHelper.getSalesByDate(startDate, endDate);
        double total = 0;
        for (Sale s : sales) {
            total += s.totalPrice;
        }

        reportTv.setText(type + " Sales Total: $" + total);

        // bar chart
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) total)); // one bar
        BarDataSet dataSet = new BarDataSet(entries, type + " Sales");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    private void exportToCSV() {
        List<Sale> sales = dbHelper.getAllSales();
        File csvFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sales.csv");
        try {
            FileWriter writer = new FileWriter(csvFile);
            writer.append("ID,Product ID,Quantity,Date,Total\n");
            for (Sale s : sales) {
                writer.append(s.id + "," + s.productId + "," + s.quantity + "," + s.date + "," + s.totalPrice + "\n");
            }
            writer.flush();
            writer.close();
            Toast.makeText(getContext(), "Exported to " + csvFile.getPath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error exporting", Toast.LENGTH_SHORT).show();
        }
    }
}