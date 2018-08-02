package com.example.nucle.inventoryapp;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nucle.inventoryapp.model.MyViewModel;
import com.example.nucle.inventoryapp.sqldatabase.DataBaseHelper;
import com.example.nucle.inventoryapp.ui.InventoryFragment;
import com.example.nucle.inventoryapp.ui.InventoryItemFragment;
import com.example.nucle.inventoryapp.ui.WelcomeFragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class InventoryMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InventoryFragment.OnInventoryListener,
        InventoryItemFragment.OnListFragmentInteractionListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = "InventoryMainActivity";
    private ViewPager mViewPager;                   // View pager
    private MyViewModel model;                      // Live data View Model
    private TabLayout mTabLayout;                   // Tab layout reference
    // set tab Layout icons Array
    private final int[] tabIcons = {
            R.drawable.ic_info_outline_white_24dp,
            R.drawable.ic_dashboard_white_24dp,
            R.drawable.ic_list_white_24dp};

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_main);
        // Initialize View Model
        model = ViewModelProviders.of(this).get(MyViewModel.class);
        // Run First Time and upload demo song to Android device. This code run only in installation
        // process. After that
        SharedPreferences wmbPreference =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FirstRun", true);
        // check is first application run
        if (isFirstRun) {
            // Permission is granted
            // Start new AsyncTask to copy media files in background mode
            // without blocking first Run
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String mImageUri;
                    String filename;
                    Uri bitmapUri = null;
                    int mRowsCount = DemoResourceCollector.productNames.length;
                    // create loop to add demo data to database
                    for (int i = 0; i < mRowsCount; i++) {
                        // initialize new DataBase Helper Object
                        DataBaseHelper mDbHelper = new DataBaseHelper(getBaseContext());
                        Bitmap tmpBitmap;
                        // scale down demo images
                        tmpBitmap = Utils.decodeSampledBitmapFromResource(
                                getResources(),
                                DemoResourceCollector.productPhotos[i],
                                333,
                                222);
                        // Generate random name for image files
                        filename = Utils.initNameGenerator(i);

                        try {
                            // save images to internal storage directory
                            bitmapUri = Utils.saveBitMap(getBaseContext(), tmpBitmap, filename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // get bitmap uri path
                        mImageUri = String.valueOf(bitmapUri);

                        String productName = getString(DemoResourceCollector.productNames[i]);
                        float randomPrice = (float) Utils.generateRandomDouble(20f, 500f);
                        int randomQuantity = Utils.generateRandomInt();
                        String suppName = getString(DemoResourceCollector.supplierNames[i]);
                        String suppContact = getString(DemoResourceCollector.supplierContacts[i]);
                        float productDiscount = (float) Utils.generateRandomDouble(0, 50);
                        String productDesc = getString(DemoResourceCollector.productDescriptions[i]);
                        // insert new record to database
                        mDbHelper.insertData
                                (
                                        mImageUri,
                                        productName,
                                        randomPrice,
                                        randomQuantity,
                                        suppName,
                                        suppContact,
                                        productDiscount,
                                        productDesc
                                );

                        mDbHelper.close();
                        model.setInventoryAllData();
                    }
                    return null;
                }
            }.execute();
            // write to shared preferences. This will run only once.
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FirstRun", false);
            editor.apply();
        }

        // Initialize Tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize Navigation View
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        // Initialize View Pager and Tab layout
        mViewPager = findViewById(R.id.my_pager);
        mTabLayout = findViewById(R.id.my_tab);
        mTabLayout.setupWithViewPager(mViewPager);
        // Setup View Pager
        SetUpViewPager(mViewPager);
        // Setup Tab layout icons
        setupTabIcons();
        // Set View pager addOnPage Listener
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Method to handle intend from user search input
     * Switch to this approach
     *
     * @param msg User search intent submit text
     */

    @Override
    public boolean onQueryTextSubmit(String msg) {
        Log.d(TAG, "Search result: " + msg);
        // send Live data
        model.setInventoryLiveData(msg);
        return false;
    }

    /**
     * Method to handle intend from user search input
     *
     * @param msg User search text listener
     */

    @Override
    public boolean onQueryTextChange(String msg) {
        Log.d(TAG, "Search result: " + msg);
        // send Live data
        model.setInventoryLiveData(msg);
        return false;
    }

    /**
     * Setup View Pager. Add fragment to View pager. Set Title names.
     *
     * @param viewPager View pager View
     */
    private void SetUpViewPager(ViewPager viewPager) {
        InventoryPagerAdapter adapter = new InventoryPagerAdapter(getSupportFragmentManager());
        adapter.AddFragmentPage(new WelcomeFragment(), "Welcome");
        adapter.AddFragmentPage(new InventoryFragment(), "Table View");
        adapter.AddFragmentPage(new InventoryItemFragment(), "Inventory list");
        viewPager.setAdapter(adapter);
    }

    /**
     * New Class used to setup View Pager Adapter.
     */
    class InventoryPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        InventoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void AddFragmentPage(Fragment mFragment, String mTitle) {
            mFragments.add(mFragment);
            mTitleList.add(mTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    /**
     * Setup Tab layout icons
     */
    private void setupTabIcons() {
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setIcon(tabIcons[2]);
    }

    /**
     * Handle Back hardware Back Button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setInventoryAllData();
    }

    /**
     * Menu onOption Item Selected
     *
     * @param item menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getBaseContext(),
                    R.string.coming_soon, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create Options Menu
     *
     * @param menu Menu
     * @return boolean
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inventory_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // ToolBar search view
        SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        mSearchView.setIconified(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchClickListener(view -> {

        });
        return true;
    }

    /**
     * Handle Navigation menu Item selections
     *
     * @param item menu items
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.add_to_inventory:
                Intent addIntent = new Intent(
                        InventoryMainActivity.this,
                        InventoryAddActivity.class);
                startActivity(addIntent);
                break;

            case R.id.show_inventory:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.query_inventory:
                mViewPager.setCurrentItem(2);
                break;

            case R.id.nav_settings:
                Toast.makeText(getBaseContext(),
                        R.string.coming_soon, Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_share:
                Toast.makeText(getBaseContext(),
                        R.string.coming_soon, Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_send:
                Toast.makeText(getBaseContext(),
                        R.string.coming_soon, Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(long item, String update) {
        if (update == null) {
            Intent detailIntent = new Intent(
                    InventoryMainActivity.this, InventoryDetailActivity.class);
            detailIntent.putExtra("ID", item);
            startActivity(detailIntent);
        } else {
            model.setPos(item);
            model.setInventoryAllData();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
