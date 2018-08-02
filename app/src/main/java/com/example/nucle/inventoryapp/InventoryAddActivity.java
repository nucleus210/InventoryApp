package com.example.nucle.inventoryapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.nucle.inventoryapp.ui.AddInventoryToDb;

public class InventoryAddActivity extends InventoryMainActivity implements
        AddInventoryToDb.OnAddToDbFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        // hide virtual keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Initialize Tool bar
        Toolbar toolbar = findViewById(R.id.toolbar_add_activity);
        setSupportActionBar(toolbar);

        // inflate fragment
        if (findViewById(R.id.details_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            AddInventoryToDb placesListFragment = new AddInventoryToDb();
            placesListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_fragment_container,
                            placesListFragment,
                            "ADD_TO_DB_FRAGMENT")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // terminate activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_back:
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAddToDbFragmentListener() {
        finish();
    }
}
