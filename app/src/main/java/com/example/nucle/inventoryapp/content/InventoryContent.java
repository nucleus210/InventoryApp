package com.example.nucle.inventoryapp.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.nucle.inventoryapp.Utils;
import com.example.nucle.inventoryapp.sqldatabase.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */

public class InventoryContent {
    private static final String TAG = "InventoryContent";
    private final List<Inventory> mInventoryList;
    private int mCount;
    private final Context mContext;

    /**
     * An array of sample (Inventory) items.
     */
    public static final List<InventoryItem> ITEMS = new ArrayList<>();

    public InventoryContent(Context context, List<Inventory> inventoryList) {
        clearItem();
        this.mContext = context;
        mInventoryList = inventoryList;

        try {
            // check size before update data
            mCount = mInventoryList.size();
        } catch (NullPointerException w) {
            Log.w(TAG, "NuLL", w);
            // Add some sample items.
        }
        for (int i = 0; i <= mCount - 1; i++) {
            addItem(createDummyItem(i));
        }

        try {
            Log.d(TAG, "New Data ->" + mInventoryList.size());
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL", w);
        }
    }

    private void clearItem() {
        if (mInventoryList != null) {
            mInventoryList.clear();
        }
        ITEMS.clear();
    }

    private static void addItem(InventoryItem item) {
        ITEMS.add(item);
    }

    private InventoryItem createDummyItem(int position) {
        return new InventoryItem(
                makeProductId(position),
                makeImage(position),
                makeProductName(position),
                makeProductPrice(position),
                makeProductQuantity(position),
                makeProductDiscount(position));
    }
    private long makeProductId(int position) {
        return mInventoryList.get(position).getID();
    }

    private Bitmap makeImage(int position) {
        String bitmapUri = "file:///" + mInventoryList.get(position).getImage();
        Bitmap bitmap = null;
        try {
            bitmap = Utils.decodeUri(mContext, Uri.parse(bitmapUri));
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL", w);
        }
        return bitmap;
    }

    private String makeProductName(int position) {
        return mInventoryList.get(position).getProductName();
    }

    @SuppressLint("DefaultLocale")
    private float makeProductPrice(int position) {
        return mInventoryList.get(position).getProductPrice();
    }

    private int makeProductQuantity(int position) {
        return mInventoryList.get(position).getQuantity();
    }

    @SuppressLint("DefaultLocale")
    private float makeProductDiscount(int position) {
        return mInventoryList.get(position).getDiscount();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class InventoryItem {
        public final long   mId;
        public final Bitmap mBitmap;
        public final String mProductName;
        public final float  mProductPrice;
        public final int    mProductQuantity;
        public final float  mProductDiscount;

        InventoryItem(long   id,
                      Bitmap bitmap,
                      String productName,
                      float  productPrice,
                      int    productQuantity,
                      float  productDiscount) {

            this.mId = id;
            this.mBitmap = bitmap;
            this.mProductName = productName;
            this.mProductPrice = productPrice;
            this.mProductQuantity = productQuantity;
            this.mProductDiscount = productDiscount;
        }
    }
}
