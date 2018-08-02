package com.example.nucle.inventoryapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.sqldatabase.DataBaseHelper;
import com.example.nucle.inventoryapp.ui.InventoryItemFragment.OnListFragmentInteractionListener;
import com.example.nucle.inventoryapp.R;
import com.example.nucle.inventoryapp.content.InventoryContent.InventoryItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link InventoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyInventoryItemRecyclerViewAdapter extends
        RecyclerView.Adapter<MyInventoryItemRecyclerViewAdapter.ViewHolder> {

    private final List<InventoryItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int mOrderValue;
    private int mOrderSummary;
    private final Context mContext;

    public MyInventoryItemRecyclerViewAdapter(Context context, List<InventoryItem> items,
                                              OnListFragmentInteractionListener listener) {
        this.mContext = context;
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventory_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mImageView.setImageBitmap(mValues.get(position).mBitmap);
        holder.mProductName.setText(mValues.get(position).mProductName);
        holder.mProductPrice.setText(String.format(mContext.getString(R.string.list_view_price_txt)
                + "%.2f", mValues.get(position).mProductPrice));
        holder.mProductQuantity.setText(String.valueOf(mContext.getString(R.string.list_view_quantity_txt)
                + mValues.get(position).mProductQuantity));
        holder.mProductDiscount.setText(String.format(mContext.getString(R.string.list_view_discount_txt)
                + "%.2f", mValues.get(position).mProductDiscount));

        holder.mView.setOnClickListener((View v) -> {
            if (null != mListener) {
                long pos = mValues.get(position).mId;
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(pos, null);
            }
        });

        holder.mDecrementButton.setOnClickListener(view -> {
            if (mOrderValue > 0) {
                int productQuantity = mValues.get(position).mProductQuantity;
                mOrderValue -= 1;
                holder.mOrderTextView.setText(String.valueOf(mOrderValue));
                mOrderSummary = (((int) mValues.get(position).mProductPrice)) * mOrderValue;
                holder.mOrderTxtSummary.setText(String.valueOf(mOrderSummary));
                productQuantity = productQuantity - mOrderValue;
                holder.mProductQuantity.setText((String.valueOf(mContext
                        .getString(R.string.list_view_quantity_txt)
                        + productQuantity)));
            } else {
                Toast.makeText(mContext,
                        R.string.error_decrement_quantity, Toast.LENGTH_LONG).show();
            }
        });

        holder.mIncrementButton.setOnClickListener(view -> {
            int productQuantity = mValues.get(position).mProductQuantity;

            if (productQuantity > mOrderValue) {
                mOrderValue += 1;
                holder.mOrderTextView.setText(String.valueOf(mOrderValue));
                mOrderSummary = (((int) mValues.get(position).mProductPrice)) * mOrderValue;
                holder.mOrderTxtSummary.setText(String.valueOf(mOrderSummary));
                productQuantity = productQuantity - mOrderValue;
                holder.mProductQuantity.setText((String.valueOf(mContext
                        .getString(R.string.list_view_quantity_txt)
                        + productQuantity)));
            } else {
                Toast.makeText(mContext,
                        R.string.error_increment_quantity, Toast.LENGTH_LONG).show();
            }
        });

        holder.mSellButton.setOnClickListener(view -> {
            if (mOrderValue != 0) {
                DataBaseHelper mDataBase = new DataBaseHelper(mContext);
                int productQuantity = mValues.get(position).mProductQuantity;
                productQuantity = productQuantity - mOrderValue;
                long pos = mValues.get(position).mId;
                // update database
                mDataBase.updateValues((int) pos, productQuantity);
                holder.mProductQuantity.setText((String.valueOf(mContext
                        .getString(R.string.list_view_quantity_txt)
                        + productQuantity)));
                //update views
                holder.mOrderTextView.setText(String.valueOf(0));
                holder.mOrderTxtSummary.setText(String.valueOf(0));
                Toast.makeText(mContext,
                        R.string.sell_success, Toast.LENGTH_LONG).show();
                mListener.onListFragmentInteraction(position, "update");
                mOrderValue = 0;
            } else {
                Toast.makeText(mContext,
                        R.string.order_bt_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mProductName;
        final TextView mProductPrice;
        final TextView mProductQuantity;
        final TextView mProductDiscount;
        final ImageButton mDecrementButton;
        final ImageButton mIncrementButton;
        final TextView mOrderTextView;
        final TextView mOrderTxtSummary;
        final ImageButton mSellButton;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.list_image);
            mProductName = view.findViewById(R.id.list_product_name);
            mProductPrice = view.findViewById(R.id.list_product_price);
            mProductQuantity = view.findViewById(R.id.list_product_quantity);
            mProductDiscount = view.findViewById(R.id.list_product_discount);
            mDecrementButton = view.findViewById(R.id.decrement_order_bt);
            mIncrementButton = view.findViewById(R.id.increment_order_bt);
            mOrderTextView = view.findViewById(R.id.order_value);
            mOrderTxtSummary = view.findViewById(R.id.order_summary);
            mSellButton = view.findViewById(R.id.sale_bt);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductQuantity.getText() + "'";
        }
    }
}
