package com.rokk3rlabs.rockkerlabsbrandssearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.rokk3rlabs.rockkerlabsbrandssearch.database.AppDatabaseTable;


public class MainActivity extends AppCompatActivity {
    protected AppDatabaseTable db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new AppDatabaseTable(this);
        db.mDatabaseOpenHelper.getReadableDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v("MOW", "Searching " + query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("MOW", "onQueryTextSubmit " + query);
                doSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("MOW", "onQueryTextChange " + newText);
                return false;
            }
        });
        return true;
    }

    private void doSearchQuery(String query) {
        Cursor mCursor=db.getWord(query);

        Log.v("MOW","Cursor data "+mCursor.getCount());
        if (null == mCursor) {
			    /*
			     * Insert code here to handle the error. Be sure not to use the cursor! You may want to
			     * call android.util.Log.e() to log this error.
			     *
			     */
            // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {

			    /*
			     * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
			     * an error. You may want to offer the user the option to insert a new row, or re-type the
			     * search term.
			     */

        } else {
            // Insert code here to do something with the results
            //mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                /*for(int i=0; i<mCursor.getColumnNames().length;i++){
                    Log.v("MOW",mCursor.getColumnNames()[i]);
                }*/

                //String brand=mCursor.getString(mCursor.getColumnIndex("resultado"));
                String brand=mCursor.getString(mCursor.getColumnIndex(AppDatabaseTable.COL_BRAND_NAME));
                String clothing=mCursor.getString(mCursor.getColumnIndex(AppDatabaseTable.COL_CLOTHING_NAME));
                Log.v("MOW","BRAND FOUND "+brand+ " "+clothing);

            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
