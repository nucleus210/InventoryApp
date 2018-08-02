package com.example.nucle.inventoryapp.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nucle.inventoryapp.sqldatabase.BaseColumns;
import com.example.nucle.inventoryapp.sqldatabase.DataBaseHelper;
import com.example.nucle.inventoryapp.sqldatabase.Inventory;
import com.example.nucle.inventoryapp.sqldatabase.InventoryContract;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inventory>> selected = new MutableLiveData<>();
    private final SqlLiveData data;
    private String messages;
    private int pos;

    public MyViewModel(@NonNull Application application) {
        super(application);
        data = new SqlLiveData(application);
    }

    public LiveData<List<Inventory>> getInventoryLiveData() { return selected; }

    public void setInventoryLiveData(String msg) {
        messages = msg;
        selected.postValue(data.loadData());
        Log.d("tag","return query result");
    }
    public void setInventoryAllData() {
        selected.postValue(data.loadAllRows());
        Log.d("tag","return query result");
    }

    public void setPos(long msg) { pos = (int) msg; }

    public int getPos() { return pos; }

    // create new inner class extent LiveData and pass Context in constructor
    class SqlLiveData extends LiveData<List<Inventory>> {
        private static final String TAG = "ViewModel: ";
        private final Context mContext;
        private final DataBaseHelper mDataBase;

        SqlLiveData(Context context) {
            this.mContext = context;
            mDataBase = new DataBaseHelper(mContext);
        }

        @Override
        protected void onActive() { }

        @Override
        protected void onInactive() { }

        /**
         * Method is used to query Database. Cursor is return from {@link DataBaseHelper}
         * In second handle user input search request string
         * @return All rows data in table
         */
        private List<Inventory>  loadAllRows() {
            List<Inventory> inventoryList = new ArrayList<>();
            // Search for the word in the database.
            Cursor cursor = mDataBase.getAllRows();
            if (cursor != null && cursor.getCount() > 0 ) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Inventory inventory = new Inventory();

                        inventory.setID(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
                        inventory.setImage(cursor.getString(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_IMAGE)));
                        inventory.setProductName(cursor.getString(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_PRODUCT_NAME)));
                        inventory.setProductPrice(cursor.getFloat(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_PRODUCT_PRICE)));
                        inventory.setQuantity(cursor.getInt(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_QUANTITY)));
                        inventory.setSupplierName(cursor.getString(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_SUPPLIER_NAME)));
                        inventory.setSupplierContact(cursor.getString(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_SUPPLIER_CONTACT)));
                        inventory.setDiscount(cursor.getFloat(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_DISCOUNT)));
                        inventory.setProductDescription(cursor.getString(
                                cursor.getColumnIndex(InventoryContract
                                        .InventoryEntry.INVENTORY_NOTES)));

                        // Adding object to Inventory List
                        inventoryList.add(inventory);
                    } while (cursor.moveToNext());

                    cursor.close();         // close cursor
                    mDataBase.close();           // close database
                    Log.d(TAG,"return all data");
                }
            }
            return inventoryList;
        }
        /**
         * Method is used to query Database. Cursor is return from {@link DataBaseHelper}
         * In second handle user input search request string
         * @return Live data list objects
         */
        private List<Inventory>  loadData() {
            List<Inventory> inventoryList = new ArrayList<>();
            // Search for the word in the database.
            String msg = messages;

            Cursor cursor = mDataBase.searchInData(msg);
            if (cursor != null && cursor.getCount() > 0 && msg != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Inventory inventory = new Inventory();
                        inventory.setID(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
                        inventory.setImage(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_IMAGE)));
                        inventory.setProductName(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_PRODUCT_NAME)));
                        inventory.setProductName(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_PRODUCT_NAME)));
                        inventory.setProductPrice(cursor.getFloat(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_PRODUCT_PRICE)));
                        inventory.setQuantity(cursor.getInt(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_QUANTITY)));
                        inventory.setSupplierName(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_SUPPLIER_NAME)));
                        inventory.setSupplierContact(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_SUPPLIER_CONTACT)));
                        inventory.setDiscount(cursor.getFloat(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_DISCOUNT)));
                        inventory.setProductDescription(cursor.getString(
                                cursor.getColumnIndex(InventoryContract.InventoryEntry
                                        .INVENTORY_NOTES)));

                        // Adding object to Inventory List
                        inventoryList.add(inventory);

                    } while (cursor.moveToNext());

                    cursor.close();         // close cursor
                    mDataBase.close();           // close database
                    Log.d(TAG,"return search query result");
                }
            }
            return inventoryList;
        }
    }
}

