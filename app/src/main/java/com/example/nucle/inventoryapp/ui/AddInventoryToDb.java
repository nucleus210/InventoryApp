package com.example.nucle.inventoryapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.R;
import com.example.nucle.inventoryapp.Utils;
import com.example.nucle.inventoryapp.sqldatabase.DataBaseHelper;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddToDbFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AddInventoryToDb} factory method to
 * create an instance of this fragment.
 */
public class AddInventoryToDb extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddInventoryToDb";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView mAddImgView;
    private EditText mAddProductName;
    private EditText mAddProductPrice;
    private EditText mAddProductQuantity;
    private EditText mAddProductSuppName;
    private EditText mAddProductSuppContact;
    private EditText mAddProductDiscount;
    private EditText mAddProductNote;
    private DataBaseHelper mDataBase;
    private Bitmap thumbnail;
    private OnAddToDbFragmentListener mListener;

    // Required empty public constructor
    public AddInventoryToDb() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBase = new DataBaseHelper(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_inventory, container, false);

        // Get views objects
        mAddImgView = view.findViewById(R.id.product_image_view);
        mAddProductName = view.findViewById(R.id.product_name_edit_txt);
        mAddProductPrice = view.findViewById(R.id.product_price_edit_txt);
        mAddProductQuantity = view.findViewById(R.id.product_quantity_edit_txt);
        mAddProductSuppName = view.findViewById(R.id.product_supp_name_edit_txt);
        mAddProductSuppContact = view.findViewById(R.id.product_supp_contact_edit_txt);
        mAddProductDiscount = view.findViewById(R.id.product_discount_edit_txt);
        mAddProductNote = view.findViewById(R.id.product_notes_edit_txt);
        Button mAddToDataDbBt = view.findViewById(R.id.add_db_btn);
        Button mAddBackBt = view.findViewById(R.id.add_back_btn);
        Button mImageBtn = view.findViewById(R.id.set_image_button);
        mAddToDataDbBt.setOnClickListener(this);
        mAddBackBt.setOnClickListener(this);
        mImageBtn.setOnClickListener(this);

        view.setOnKeyListener((v, keyCode, event) -> {
            Log.i(TAG, "keyCode: " + keyCode);

            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                Log.i(TAG, "onKey Back listener");
                Objects.requireNonNull(getFragmentManager()).popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            }
            return false;
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddToDbFragmentListener) {
            mListener = (OnAddToDbFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddToDbFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_db_btn:
                // Check user input data
                // -- > if an image is selected
                // -->  correct answer with selected user option

                if (thumbnail == null) {
                    Toast.makeText(getActivity(), R.string.error_image, Toast.LENGTH_LONG).show();
                } else if (mAddProductName.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_name, Toast.LENGTH_LONG).show();
                } else if (mAddProductPrice.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_price, Toast.LENGTH_LONG).show();
                } else if (mAddProductQuantity.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_quantity, Toast.LENGTH_LONG).show();
                } else if (mAddProductSuppName.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_supplier_name, Toast.LENGTH_LONG).show();
                } else if (mAddProductSuppContact.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_supplier_contact, Toast.LENGTH_LONG).show();
                } else if (mAddProductDiscount.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_discount, Toast.LENGTH_LONG).show();
                } else if (mAddProductNote.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_add_product_note, Toast.LENGTH_LONG).show();
                } else {
                    saveToDb();
                }
                break;

            case R.id.add_back_btn:
                mListener.onAddToDbFragmentListener();
                break;

            case R.id.set_image_button:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                break;
        }
    }

    /**
     * Method is used to get user input data from views
     */
    private void saveToDb() {
        String fileName = Utils.fileNameGenerator();
        Uri fileUri = null;
        try {
            fileUri = Utils.saveBitMap(Objects.requireNonNull(getActivity()), thumbnail, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imagePath = String.valueOf(fileUri);
        String mProductName = mAddProductName.getText().toString();
        float mProductPrice = Float.parseFloat(mAddProductPrice.getText().toString());
        int mProductQuantity = Integer.parseInt(mAddProductQuantity.getText().toString());
        String mProductSuppName = mAddProductSuppName.getText().toString();
        String mProductSuppContact = mAddProductSuppContact.getText().toString();
        float mProductDiscount = Float.parseFloat(mAddProductDiscount.getText().toString());
        String mProductNote = mAddProductNote.getText().toString();

        mDataBase.insertData(
                imagePath,
                mProductName,
                mProductPrice,
                mProductQuantity,
                mProductSuppName,
                mProductSuppContact,
                mProductDiscount,
                mProductNote);

        Toast.makeText(getActivity(), R.string.add_stored_new_record, Toast.LENGTH_LONG).show();
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
                            thumbnail = Utils.decodeUri(getActivity(), selectedImage);
                            mAddImgView.setImageBitmap(thumbnail);
                        } catch (NullPointerException w) {
                            Log.d("TAG", "NuLL", w);
                        }
                    }
                }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnAddToDbFragmentListener {
        void onAddToDbFragmentListener();
    }
}
