package com.example.nucle.inventoryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.sqldatabase.DataBaseHelper;
import com.example.nucle.inventoryapp.sqldatabase.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class InventoryDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Details Activity";
    private static final int PICK_IMAGE_REQUEST = 2;
    private ImageView mProductImage;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private EditText mProductDiscount;
    private EditText mProductSupplierName;
    private EditText mProductSupplierContact;
    private TextView mProductDescription;
    private List<Inventory> mInventory;
    private boolean isEditable = false;
    private boolean isImgPick = false;
    private String mText = "";
    private int mOrderValue;
    private long mItem;
    private Bitmap thumbnail;
    private DataBaseHelper mDatabase;
    private FloatingActionButton mFloatActionBtn;
    private ImageButton mQuantityIncBtn;
    private ImageButton mQuantityDecrementBtn;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        mItem = getIntent().getLongExtra("ID", 0);
        mInventory = new ArrayList<>();
        mDatabase = new DataBaseHelper(this);
        // hide virtual keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // get views references
        mInventory.add(mDatabase.getInventoryById(mItem));
        mProductImage = findViewById(R.id.product_image_detail);
        mProductName = findViewById(R.id.product_name_detail);
        mProductPrice = findViewById(R.id.product_price_detail);
        mProductQuantity = findViewById(R.id.product_quantity_detail);
        mProductSupplierName = findViewById(R.id.supplier_name_detail);
        mProductSupplierContact = findViewById(R.id.supplier_contact_detail);
        mProductDiscount = findViewById(R.id.product_discount_detail);
        mProductDescription = findViewById(R.id.product_description_detail);
        // get buttons references
        ImageButton mOrderBtn = findViewById(R.id.order_button);
        ImageButton mAddRecordBtn = findViewById(R.id.add_button);
        ImageButton mDeleteRecordBtn = findViewById(R.id.delete_button);
        mQuantityDecrementBtn = findViewById(R.id.decrement_order_bt_detail);
        mQuantityIncBtn = findViewById(R.id.increment_order_bt_detail);
        mFloatActionBtn = findViewById(R.id.float_btn);
        ImageButton mEditButton = findViewById(R.id.edit_btn);

        // set on Click Listeners
        mOrderBtn.setOnClickListener(this);
        mEditButton.setOnClickListener(this);
        mAddRecordBtn.setOnClickListener(this);
        mFloatActionBtn.setOnClickListener(this);
        mQuantityIncBtn.setOnClickListener(this);
        mQuantityIncBtn.setEnabled(false);
        mQuantityDecrementBtn.setOnClickListener(this);
        mQuantityDecrementBtn.setEnabled(false);
        mProductDescription.setOnClickListener(this);
        mDeleteRecordBtn.setOnClickListener(this);
        mProductImage.setOnClickListener(this);
        mProductImage.setEnabled(false);

        // render UI
        renderDetails();
    }

    /**
     * Render UI
     **/
    @SuppressLint("DefaultLocale")
    private void renderDetails() {
        try {
            mProductImage.setImageURI(Uri.parse(mInventory.get(0).getImage()));
            mProductName.setText(String.valueOf(mInventory.get(0).getProductName()));
            mProductPrice.setText(String.format("%.2f", mInventory.get(0).getProductPrice()));
            mProductQuantity.setText(String.valueOf(mInventory.get(0).getQuantity()));
            mProductSupplierName.setText(String.valueOf(mInventory.get(0).getSupplierName()));
            mProductSupplierContact.setText(String.valueOf(mInventory.get(0).getSupplierContact()));
            mProductDiscount.setText(String.format("%.2f", mInventory.get(0).getDiscount()));
            mProductDescription.setText(mInventory.get(0).getProductDescription());
            mOrderValue = mInventory.get(0).getQuantity();
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL", w);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isImgPick=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close database
        mDatabase.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mInventory.clear();
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL, w");
        }
        finish();
    }

    /**
     * Activity for Result Method is used to handle image pick intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        try {
                            thumbnail = Utils.decodeUri(this, selectedImage);
                            mProductImage.setImageBitmap(thumbnail);
                            isImgPick = true;
                            updateData(mItem);
                        } catch (NullPointerException w) {
                            Log.d("TAG", "NuLL", w);
                        }
                    }
                }
        }
    }

    /**
     * Method is used to intent phone call
     *
     * @param phoneNumber Supplier contact number
     */
    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Onclick Listener
     *
     * @param v View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                // intent add to database activity
                Intent addIntent = new Intent(
                        InventoryDetailActivity.this, InventoryAddActivity.class);
                startActivity(addIntent);
                break;

            case R.id.delete_button:
                AlertDialog.Builder deleteRow = new AlertDialog.Builder(this,
                        R.style.AlertDialogCustom);

                deleteRow.setTitle(R.string.delete);
                // Set up the input
                final TextView message = new TextView(this);
                message.setTextColor(getResources().getColor(R.color.colorText));
                // Specify the input type
                message.setText(R.string.delete_entry);
                deleteRow.setView(message);

                // Set up the buttons
                deleteRow.setPositiveButton(R.string.delete_btn, (dialog, which) -> {
                    DataBaseHelper mDataBase = new DataBaseHelper(this);
                    int delRec = mDataBase.delete(mItem);
                    Toast.makeText(this, R.string.deleted_record + delRec,
                            Toast.LENGTH_LONG).show();
                    finish();
                });

                deleteRow.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
                deleteRow.show();
                break;

            case R.id.order_button:
                // intent phone call
                dialPhoneNumber(String.valueOf(mInventory.get(0).getSupplierContact()));
                break;

            case R.id.decrement_order_bt_detail:
                // Decrement quantity
                if (mOrderValue > 0) {
                    mOrderValue -= 1;
                    mProductQuantity.setText(String.valueOf(mOrderValue));
                    updateData(mItem);
                } else {
                    Toast.makeText(getBaseContext(),
                            R.string.error_decrement_quantity, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.increment_order_bt_detail:
                // Increment quantity
                if (mOrderValue < 500) {
                    mOrderValue += 1;
                    mProductQuantity.setText(String.valueOf(mOrderValue));
                    updateData(mItem);
                } else {
                    Toast.makeText(getBaseContext(),
                            R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.product_description_detail:
                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        R.style.AlertDialogCustom);
                builder.setTitle(R.string.desc_title_dialog);

                // Set up the input
                final EditText input = new EditText(this);
                input.setTextColor(getResources().getColor(R.color.colorText));
                // Specify the input type
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton(R.string.update, (dialog, which) -> {
                    mText = input.getText().toString();
                    mProductDescription.setText(mText);
                    updateData(mItem);
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
                builder.show();
                break;

            case R.id.float_btn:
                onEdit();
                break;

            case R.id.edit_btn:
                onEdit();
                break;

            case R.id.product_image_detail:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                break;
        }
    }

    /**
     * Set booleans to Enable and Disable Button and EditText View.
     */
    private void onEdit() {
        if (!isEditable) {
            mQuantityDecrementBtn.setEnabled(true);
            mQuantityIncBtn.setEnabled(true);
            mProductImage.setEnabled(true);
            mProductName.setEnabled(true);
            mProductPrice.setEnabled(true);
            mProductQuantity.setEnabled(true);
            mProductSupplierName.setEnabled(true);
            mProductSupplierContact.setEnabled(true);
            mProductDiscount.setEnabled(true);
            mProductDescription.setEnabled(true);
            isEditable = true;
            mFloatActionBtn.setImageResource(R.drawable.ic_update_white_24dp);
        } else {
            mQuantityDecrementBtn.setEnabled(false);
            mQuantityIncBtn.setEnabled(false);
            mProductImage.setEnabled(false);
            mProductName.setEnabled(false);
            mProductPrice.setEnabled(false);
            mProductQuantity.setEnabled(false);
            mProductSupplierName.setEnabled(false);
            mProductSupplierContact.setEnabled(false);
            mProductDiscount.setEnabled(false);
            mProductDescription.setEnabled(false);
            isEditable = false;
            mFloatActionBtn.setImageResource(R.drawable.ic_edit_white_24dp);
            updateData(mItem);
        }
    }

    /**
     * Method is used to update data and UI
     *
     * @param item ID entry to be updated
     */
    private void updateData(long item) {
        String imagePath = null;
        float  productPrice = 0;
        int    productQuantity = 0;
        float  productDiscount = 0;

        if (isImgPick) {
            String fileName = Utils.fileNameGenerator();
            Uri fileUri = null;
            try {
                fileUri = Utils.saveBitMap(Objects.requireNonNull(this), thumbnail, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileUri != null) {
                imagePath = String.valueOf(fileUri);
            }
        } else {
            imagePath = String.valueOf(Uri.parse(mInventory.get(0).getImage()));
        }

        String productName = mProductName.getText().toString();

        try {
            productPrice = Float.parseFloat(mProductPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "No data", e);
        }
        try {
            productQuantity = Integer.parseInt(mProductQuantity.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "No data", e);
        }
        String productSuppName = mProductSupplierName.getText().toString();
        String productSuppContact = mProductSupplierContact.getText().toString();
        try {
            productDiscount = Float.parseFloat(mProductDiscount.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "No data", e);
        }
        String productNote = mProductDescription.getText().toString();
        // check data
        if (Objects.requireNonNull(imagePath).equals("")) {
            Toast.makeText(getBaseContext(),
                    R.string.error_image, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productName.equals("")) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_name, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productPrice == 0) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_price, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productQuantity == 0) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_quantity, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productSuppName.equals("")) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_supplier_name, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productSuppContact.equals("")) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_supplier_contact, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productDiscount == 0) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_discount, Toast.LENGTH_SHORT).show();
            onEdit();
        } else if (productNote.equals("")) {
            Toast.makeText(getBaseContext(),
                    R.string.error_add_product_note, Toast.LENGTH_SHORT).show();
            onEdit();
        } else {

            mDatabase.updateData(
                    item,
                    imagePath,
                    productName,
                    productPrice,
                    productQuantity,
                    productSuppName,
                    productSuppContact,
                    productDiscount,
                    productNote);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_inventory_detail);
            Toast.makeText(getBaseContext(), "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_inventory_detail);
            Toast.makeText(getBaseContext(), "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mItem = savedInstanceState.getLong("item");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("item", mItem);
        super.onSaveInstanceState(outState);
    }
}
