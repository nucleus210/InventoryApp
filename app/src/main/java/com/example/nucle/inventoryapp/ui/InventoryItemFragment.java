package com.example.nucle.inventoryapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.R;
import com.example.nucle.inventoryapp.adapters.MyInventoryItemRecyclerViewAdapter;
import com.example.nucle.inventoryapp.content.InventoryContent;
import com.example.nucle.inventoryapp.model.MyViewModel;
import com.example.nucle.inventoryapp.sqldatabase.Inventory;

import java.util.List;
import java.util.Objects;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class InventoryItemFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "InventoryItemFragment: ";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private List<Inventory> mInventoryList;
    private RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InventoryItemFragment() {
    }

    @SuppressWarnings("unused")
    public static InventoryItemFragment newInstance(int columnCount) {
        InventoryItemFragment fragment = new InventoryItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Live Data View Model and set observer to listen for data change
        MyViewModel model = ViewModelProviders.of(Objects
                .requireNonNull(getActivity())).get(MyViewModel.class);
        model.getInventoryLiveData().observe(this, item -> {
            if (item != null) {
                try {
                    mInventoryList.clear();
                } catch (NullPointerException w) {
                    Log.d(TAG, "NuLL", w);
                }
                int mPos = model.getPos();
                recyclerView.setAdapter(null);
                mInventoryList = item;
                TextView defaultText = getActivity().findViewById(R.id.default_txt);
                defaultText.setVisibility(View.GONE);
                setInventoryContent();
                recyclerView.setAdapter(new MyInventoryItemRecyclerViewAdapter
                        (mContext, InventoryContent.ITEMS, mListener));
                if (mPos > 0) {
                    recyclerView.scrollToPosition(mPos);
                    model.setPos(0);
                }
                if (mInventoryList != null) {
                    if (mInventoryList.size() == 0) {
                       defaultText.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Toast.makeText(getActivity(),
                        R.string.search_table_query, Toast.LENGTH_LONG).show();
            }
        });

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventoryitem_list,
                container, false);
        mContext = getActivity();

        // initialize new Content object to populate recycle adapter
        setInventoryContent();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyInventoryItemRecyclerViewAdapter
                    (mContext, InventoryContent.ITEMS, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long pos, String update);
    }
    /**
     * Initialize {@link InventoryContent}  Inventory data content holder
     */
    private void setInventoryContent() {
        new InventoryContent(mContext, mInventoryList);
    }
}
