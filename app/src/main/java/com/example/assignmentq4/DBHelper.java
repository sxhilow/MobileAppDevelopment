package com.example.assignmentq4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignmentq4.dao.Product;
import com.example.assignmentq4.dao.Sale;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "inventory.db";
    private static final int DB_VERSION = 1;

    // Products table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_STOCK = "stock";
    public static final String COL_CATEGORY = "category";
    public static final String COL_COST = "cost"; // Added for profit calc

    // Sales table
    public static final String TABLE_SALES = "sales";
    public static final String COL_SALE_ID = "_id";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_DATE = "date";
    public static final String COL_TOTAL_PRICE = "total_price";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProducts = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_COST + " REAL, " +
                COL_STOCK + " INTEGER, " +
                COL_CATEGORY + " TEXT)";
        db.execSQL(createProducts);

        String createSales = "CREATE TABLE " + TABLE_SALES + " (" +
                COL_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_ID + " INTEGER, " +
                COL_QUANTITY + " INTEGER, " +
                COL_DATE + " TEXT, " +
                COL_TOTAL_PRICE + " REAL)";
        db.execSQL(createSales);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        onCreate(db);
    }

    // CRUD for Products
    public long addProduct(String name, double price, double cost, int stock, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_COST, cost);
        values.put(COL_STOCK, stock);
        values.put(COL_CATEGORY, category);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                products.add(new Product(id, name, price, cost, stock, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public int updateProduct(int id, String name, double price, double cost, int stock, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_COST, cost);
        values.put(COL_STOCK, stock);
        values.put(COL_CATEGORY, category);
        return db.update(TABLE_PRODUCTS, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Sales operations
    public long addSale(int productId, int quantity, String date, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_ID, productId);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_DATE, date);
        values.put(COL_TOTAL_PRICE, totalPrice);
        long saleId = db.insert(TABLE_SALES, null, values);

        // Update stock
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{COL_STOCK}, COL_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor.moveToFirst()) {
            int currentStock = cursor.getInt(0);
            int newStock = currentStock - quantity;
            ContentValues stockValues = new ContentValues();
            stockValues.put(COL_STOCK, newStock);
            db.update(TABLE_PRODUCTS, stockValues, COL_ID + "=?", new String[]{String.valueOf(productId)});
        }
        cursor.close();
        return saleId;
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SALES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SALE_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_PRICE));
                sales.add(new Sale(id, productId, quantity, date, total));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sales;
    }

    // For low stock alert: Get products with stock < 5
    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COL_STOCK + " < 5", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Similar to getAllProducts
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                lowStock.add(new Product(id, name, price, cost, stock, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lowStock;
    }

    // Analytics methods
    public double getTotalSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_TOTAL_PRICE + ") FROM " + TABLE_SALES, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public double getTotalProfit() {
        // Profit = sum((price - cost) * quantity) for sales
        double profit = 0;
        List<Sale> sales = getAllSales();
        for (Sale sale : sales) {
            Product product = getProductById(sale.productId);
            if (product != null) {
                profit += (product.price - product.cost) * sale.quantity;
            }
        }
        return profit;
    }

    public double getInventoryValue() {
        double value = 0;
        List<Product> products = getAllProducts();
        for (Product p : products) {
            value += p.price * p.stock;
        }
        return value;
    }

    // Helper to get product by id
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
            double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
            cursor.close();
            return new Product(id, name, price, cost, stock, category);
        }
        cursor.close();
        return null;
    }

    // Get sales by date range for reports (daily/weekly)
    public List<Sale> getSalesByDate(String startDate, String endDate) {
        List<Sale> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SALES, null, COL_DATE + " BETWEEN ? AND ?", new String[]{startDate, endDate}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SALE_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_PRICE));
                sales.add(new Sale(id, productId, quantity, date, total));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sales;
    }
}