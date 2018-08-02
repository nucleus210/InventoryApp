package com.example.nucle.inventoryapp.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.R;
import com.example.nucle.inventoryapp.model.MyViewModel;
import com.example.nucle.inventoryapp.sqldatabase.Inventory;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnInventoryListener} interface
 * to handle interaction events.
 * Use the {@link InventoryFragment}
 */
public class InventoryFragment extends Fragment {
    private static final String TAG = "InventoryFragment: ";
    private TableLayout mTableLayout;
    private List<Inventory> mInventoryList;

    // Required empty public constructor
    public InventoryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Live Data View Model and set observer to listen for data change

        MyViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(MyViewModel.class);
        model.getInventoryLiveData().observe(this, item -> {
            if (item != null) {
                try {
                    mInventoryList.clear();
                } catch (NullPointerException w) {
                    Log.d(TAG, "NuLL", w);
                }
                mInventoryList = item;
                mTableLayout.removeAllViewsInLayout();
                renderTable();
            } else {
                Toast.makeText(getActivity(),
                        R.string.search_table_query, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        mTableLayout = view.findViewById(R.id.table_main);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // render table from list
        renderTable();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void renderTable() {
        // Initialize Table layout
        mTableLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.table_main);
        // Check entries in database
        int rowInTable = 0;
        try {
            rowInTable = mInventoryList.size();
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL", w);
        }
        // Initialize new table row
        @SuppressLint("InflateParams") View tableColumnNames = LayoutInflater.from(getActivity())
                .inflate(R.layout.table_column_names, null, false);
        mTableLayout.addView(tableColumnNames);
        // Loop to populate data from data base to table layout
        for (int i = 0; i < rowInTable; i++) {
            // Initialize new table row
            @SuppressLint("InflateParams") View tableRow = LayoutInflater.from(getActivity())
                    .inflate(R.layout.table_rows, null, false);
            // find views
            TextView mProductName = tableRow.findViewById(R.id.product_name_txt);
            TextView mProductPrice = tableRow.findViewById(R.id.product_price_txt);
            TextView mProductQuantity = tableRow.findViewById(R.id.product_quantity_txt);
            TextView mProductSuppName = tableRow.findViewById(R.id.product_supplier_name_txt);
            TextView mProductSuppContact = tableRow.findViewById(R.id.product_supplier_contact_txt);
            TextView mProductDiscount = tableRow.findViewById(R.id.product_discount_txt);
            // set new values to views
            mProductName.setText(String.valueOf(mInventoryList.get(i).getProductName()));
            mProductPrice.setText(String.format("$ " + "%.2f",
                    mInventoryList.get(i).getProductPrice()));
            mProductQuantity.setText(String.valueOf(mInventoryList.get(i).getQuantity()));
            mProductSuppName.setText(String.valueOf(mInventoryList.get(i).getSupplierName()));
            mProductSuppContact.setText(String.valueOf(mInventoryList.get(i).getSupplierContact()));
            mProductDiscount.setText(String.format("%.2f" + " %%",
                    mInventoryList.get(i).getDiscount()));
            // add views to table layout
            mTableLayout.addView(tableRow);
        }
        if(rowInTable == 0) {
            TextView mTextDataMsg = new TextView(getActivity());
            mTextDataMsg.setText("No data" + "\n" + "Only on first run type something in search and remove search phrase to refresh screen");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnInventoryListener { }
}
