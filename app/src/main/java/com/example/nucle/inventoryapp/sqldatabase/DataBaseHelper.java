package com.example.nucle.inventoryapp.sqldatabase;

// Created by Kiril Kamenov on 28.07.2018

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nucle.inventoryapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Import Static variables from Inventory Contractor
import static com.example.nucle.inventoryapp.sqldatabase.InventoryContract.InventoryEntry;

/**
 * Create new class DataBaseHelper extend SQLiteOpenHelper to
 * manage database creation and version management.
 */

public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String TAG = "DataBaseHelper";
    private SQLiteDatabase mDataBase;

    //destination path (location) of our database on device
    private static String DB_PATH;

    // Database name
    private static final String DB_NAME = "inventoryDb.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +
                    InventoryEntry.INVENTORY_TABLE_NAME + " (" +
                    InventoryEntry.INVENTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    InventoryEntry.INVENTORY_IMAGE + " URI," +
                    InventoryEntry.INVENTORY_PRODUCT_NAME + " TEXT," +
                    InventoryEntry.INVENTORY_QUANTITY + " INTEGER," +
                    InventoryEntry.INVENTORY_PRODUCT_PRICE + " DECIMAL," +
                    InventoryEntry.INVENTORY_SUPPLIER_NAME + " TEXT," +
                    InventoryEntry.INVENTORY_SUPPLIER_CONTACT + " VARCHAR," +
                    InventoryEntry.INVENTORY_DISCOUNT + " DECIMAL," +
                    InventoryEntry.INVENTORY_NOTES + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + InventoryEntry.INVENTORY_TABLE_NAME;

    /**
     * Data Base Helper
     */
    @SuppressLint("SdCardPath")
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        DB_PATH = context.getApplicationInfo().dataDir + context.getString(R.string.database_dir);
        Log.d(TAG, "Constructor");
    }

    /**
     * Method is used to oped database.
     */
    @SuppressWarnings("unused")
    public void openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath,
                null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * Method is used to close database.
     */
    @Override
    public synchronized void close() {
        if (mDataBase != null) mDataBase.close();
        super.close();
        Log.d(TAG, "Database closed");
    }

    /**
     * Start onCreate method database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d(TAG, "DataBase created.");
    }

    /**
     * Method is used to update database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Insert new row inventory to database
     *
     * @param image           Url of image new entry
     * @param productName     Product name new entry
     * @param productPrice    Product price new entry
     * @param quantity        Product quantity new entry
     * @param supplierName    Supplier name new entry
     * @param supplierContact entry new entry
     * @param discount        Product discount new entry
     * @param desc            Product description new entry
     */
    public void insertData(String image,
                           String productName,
                           float  productPrice,
                           int    quantity,
                           String supplierName,
                           String supplierContact,
                           float  discount,
                           String desc) {

        SQLiteDatabase dBase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //insert new row in table
        values.put(InventoryEntry.INVENTORY_IMAGE, image);
        values.put(InventoryEntry.INVENTORY_PRODUCT_NAME, productName);
        values.put(InventoryEntry.INVENTORY_PRODUCT_PRICE, productPrice);
        values.put(InventoryEntry.INVENTORY_QUANTITY, quantity);
        values.put(InventoryEntry.INVENTORY_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.INVENTORY_SUPPLIER_CONTACT, supplierContact);
        values.put(InventoryEntry.INVENTORY_DISCOUNT, discount);
        values.put(InventoryEntry.INVENTORY_NOTES, desc);
        // Inserting Row into database
        dBase.insert(InventoryEntry.INVENTORY_TABLE_NAME, null, values);
        //close database
        dBase.close();
    }

    /**
     * Return all rows data from database.
     */
    public Cursor getAllRows() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + InventoryEntry.INVENTORY_TABLE_NAME;
        SQLiteDatabase dBase = this.getReadableDatabase();
        return dBase.rawQuery(selectQuery, null);
    }

    /**
     * Update row inventory to database
     *
     * @param id              _ID entry
     * @param image           Url of image entry new value
     * @param productName     Product name entry new value
     * @param productPrice    Product price entry new value
     * @param quantity        Product quantity entry new value
     * @param supplierName    Supplier name entry new value
     * @param supplierContact entry new value
     * @param discount        Product discount entry new value
     * @param desc            Product description entry new value
     */
    public void updateData(float  id,
                           String image,
                           String productName,
                           float  productPrice,
                           int    quantity,
                           String supplierName,
                           String supplierContact,
                           float  discount,
                           String desc) {
        // get readable database
        SQLiteDatabase dBase = this.getWritableDatabase();
        // get contentValues
        ContentValues values = new ContentValues();
        //update rowId in table
        values.put(InventoryEntry.INVENTORY_IMAGE, image);
        values.put(InventoryEntry.INVENTORY_PRODUCT_NAME, productName);
        values.put(InventoryEntry.INVENTORY_PRODUCT_PRICE, productPrice);
        values.put(InventoryEntry.INVENTORY_QUANTITY, quantity);
        values.put(InventoryEntry.INVENTORY_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.INVENTORY_SUPPLIER_CONTACT, supplierContact);
        values.put(InventoryEntry.INVENTORY_DISCOUNT, discount);
        values.put(InventoryEntry.INVENTORY_NOTES, desc);
        // updating row
        dBase.update(InventoryEntry.INVENTORY_TABLE_NAME,
                values,
                BaseColumns._ID + "=?",
                new String[]{String.valueOf(id)});
        //close database
        dBase.close();
    }

    /**
     * Return all rows data from database.
     *
     * @param searchString Search by product name
     */
    public Cursor searchInData(String searchString) {
        // open Readable Database
        SQLiteDatabase dBase = this.getReadableDatabase();

        String selectQuery = "SELECT  rowid as " +
                BaseColumns._ID + "," +
                InventoryEntry.INVENTORY_IMAGE + "," +
                InventoryEntry.INVENTORY_PRODUCT_NAME + "," +
                InventoryEntry.INVENTORY_PRODUCT_PRICE + "," +
                InventoryEntry.INVENTORY_QUANTITY + "," +
                InventoryEntry.INVENTORY_SUPPLIER_NAME + "," +
                InventoryEntry.INVENTORY_SUPPLIER_CONTACT + "," +
                InventoryEntry.INVENTORY_DISCOUNT + "," +
                InventoryEntry.INVENTORY_NOTES +
                " FROM " + InventoryEntry.INVENTORY_TABLE_NAME +
                " WHERE " + InventoryEntry.INVENTORY_PRODUCT_NAME +
                "  LIKE  '%" + searchString + "%' ";

        return dBase.rawQuery(selectQuery, null);
    }

    /**
     * Return dynamic rows data from database. Data with rows _id is passed to this method.
     * Then data is converted to be used in SQL statement.
     *
     * @param inventoryId Query database for specified criteria "_id"
     */
    public Inventory getInventoryById(long inventoryId) {
        // get readable database
        SQLiteDatabase dBase = this.getReadableDatabase();

        // Cursor cursor = dBase.rawQuery(select, null);
        Cursor cursor = dBase.query(InventoryEntry.INVENTORY_TABLE_NAME,
                new String[]{BaseColumns._ID,
                        InventoryEntry.INVENTORY_IMAGE,
                        InventoryEntry.INVENTORY_PRODUCT_NAME,
                        InventoryEntry.INVENTORY_PRODUCT_PRICE,
                        InventoryEntry.INVENTORY_QUANTITY,
                        InventoryEntry.INVENTORY_SUPPLIER_NAME,
                        InventoryEntry.INVENTORY_SUPPLIER_CONTACT,
                        InventoryEntry.INVENTORY_SUPPLIER_CONTACT,
                        InventoryEntry.INVENTORY_DISCOUNT,
                        InventoryEntry.INVENTORY_NOTES},
                BaseColumns._ID + "=?",
                new String[]{String.valueOf(inventoryId)},
                null,
                null,
                null,
                null);

        // looping through all rows and adding to list
        if (cursor != null)
            cursor.moveToFirst();
        Inventory inventory = new Inventory(
                cursor.getInt(Objects.requireNonNull(cursor).getColumnIndex(BaseColumns._ID)),
                cursor.getString(cursor.getColumnIndex(InventoryEntry.INVENTORY_IMAGE)),
                cursor.getString(cursor.getColumnIndex(InventoryEntry.INVENTORY_PRODUCT_NAME)),
                cursor.getFloat(cursor.getColumnIndex(InventoryEntry.INVENTORY_PRODUCT_PRICE)),
                cursor.getInt(cursor.getColumnIndex(InventoryEntry.INVENTORY_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(InventoryEntry.INVENTORY_SUPPLIER_NAME)),
                cursor.getString(cursor.getColumnIndex(InventoryEntry.INVENTORY_SUPPLIER_CONTACT)),
                cursor.getFloat(cursor.getColumnIndex(InventoryEntry.INVENTORY_DISCOUNT)),
                cursor.getString(cursor.getColumnIndex(InventoryEntry.INVENTORY_NOTES)));

        cursor.close();          // close cursor
        dBase.close();           // close database
        return inventory;        // return Inventory Object
    }

    /**
     * Update database row data by its _id.
     *
     * @param id    _ID of the entry to update.
     * @param value Update quantity value
     */
    public void updateValues(int id, int value) {
        List<Inventory> mList = new ArrayList<>();
        mList.add(getInventoryById(id));
        // get writable database
        SQLiteDatabase dBase = this.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.INVENTORY_IMAGE, mList.get(0).getImage());
        values.put(InventoryEntry.INVENTORY_PRODUCT_NAME, mList.get(0).getProductName());
        values.put(InventoryEntry.INVENTORY_PRODUCT_PRICE, mList.get(0).getProductPrice());
        values.put(InventoryEntry.INVENTORY_QUANTITY, value);
        values.put(InventoryEntry.INVENTORY_SUPPLIER_NAME, mList.get(0).getSupplierName());
        values.put(InventoryEntry.INVENTORY_SUPPLIER_CONTACT, mList.get(0).getSupplierContact());
        values.put(InventoryEntry.INVENTORY_DISCOUNT, mList.get(0).getDiscount());
        values.put(InventoryEntry.INVENTORY_NOTES, mList.get(0).getProductDescription());

        // Which row to update, based on the title
        dBase.update(InventoryEntry.INVENTORY_TABLE_NAME,
                values,
                BaseColumns._ID + "=?",
                new String[]{String.valueOf(id)});
        //close database
        dBase.close();
    }

    /**
     * Deletes one entry identified by its _id.
     *
     * @param id _ID of the entry to delete.
     * @return The number of rows deleted. Since we are deleting by id, this should be 0 or 1.
     */
    public int delete(long id) {
        int deleted = 0;
        // get readable database
        SQLiteDatabase dBase = this.getReadableDatabase();

        try {
            deleted = dBase.delete(InventoryEntry.INVENTORY_TABLE_NAME,
                    BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d(TAG, "DELETE EXCEPTION! " + e);
        }
        return deleted;
    }
}











