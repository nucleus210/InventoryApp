package com.example.nucle.inventoryapp.sqldatabase;

import android.provider.BaseColumns;

public final class InventoryContract implements BaseColumns {

    public static abstract class InventoryEntry implements BaseColumns {

        // Inventory table name
        static final String INVENTORY_TABLE_NAME = "inventory";

        // Inventory Table Columns names
        public final static String INVENTORY_ID = BaseColumns._ID;
        public static final String INVENTORY_IMAGE = "image";
        public static final String INVENTORY_PRODUCT_NAME = "productName";
        public static final String INVENTORY_QUANTITY = "quantity";
        public static final String INVENTORY_PRODUCT_PRICE = "productPrice";
        public static final String INVENTORY_SUPPLIER_NAME = "supplierName";
        public static final String INVENTORY_SUPPLIER_CONTACT = "supplierContact";
        public static final String INVENTORY_DISCOUNT = "discount";
        public static final String INVENTORY_NOTES = "notes";
    }
}