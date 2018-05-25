package com.web114.weatherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

public class AddLocationActivity extends AppCompatActivity {
    private static final String TAG = AddLocationActivity.class.getSimpleName();
    private AutoCompleteTextView addLocation;
    private RequestQueue queue;
    private CustomArrayAdapter customAdapter;
    private static List<ListJsonObject> mData;
    private CustomSharedPreference mPreference;
    private DatabaseQuery databaseQuery;
    private DataSourceFromSharedPref dataSourceFromSharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setTitle(Helper.MANAGER_LOCATION);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mPreference = new CustomSharedPreference(AddLocationActivity.this);
        databaseQuery = new DatabaseQuery(AddLocationActivity.this);
        queue = Volley.newRequestQueue(this);
        addLocation = (AutoCompleteTextView) findViewById(R.id.new_location);
        Button goToLocationButton = (Button)findViewById(R.id.go_to_location_button);
        goToLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listLocationIntent = new Intent(AddLocationActivity.this, ListLocationActivity.class);
                startActivity(listLocationIntent);
            }
        });
        final Button addLocationButton = (Button)findViewById(R.id.add_location_button);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredLocation = addLocation.getText().toString();
                if (TextUtils.isEmpty(enteredLocation)) {
                    Toast.makeText(AddLocationActivity.this, Helper.LOCATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                    return;
                }
                // add this to the database
                int numOfLocationsStored = databaseQuery.countAllStoredLocations();
                Toast.makeText(AddLocationActivity.this, "Total count " + numOfLocationsStored, Toast.LENGTH_LONG).show();
                if(numOfLocationsStored <= 3){
                    databaseQuery.insertNewLocation(enteredLocation);
                }else{
                    Toast.makeText(AddLocationActivity.this, getString(R.string.stored_location), Toast.LENGTH_LONG).show();
                }
                Intent listLocationIntent = new Intent(AddLocationActivity.this, ListLocationActivity.class);
                startActivity(listLocationIntent);
            }
        });
    }
    private class DataSourceFromSharedPref extends AsyncTask<Void, Void, List<ListJsonObject>> {
        protected void onProgressUpdate() {
        }
        protected void onPostExecute(List<ListJsonObject> result) {
            if(null != result){
                mData = result;
                customAdapter = new CustomArrayAdapter(AddLocationActivity.this, R.layout.city_list, mData);
                addLocation.setAdapter(customAdapter);
                addLocation.setThreshold(1);
            }
        }
        @Override
        protected List<ListJsonObject> doInBackground(Void... voids) {
            return mergeStoredData();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(null == mData){
            dataSourceFromSharedPref = new DataSourceFromSharedPref();
            dataSourceFromSharedPref.execute();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != null){
            dataSourceFromSharedPref.cancel(true);
        }
    }
    private List<ListJsonObject> mergeStoredData(){
        List<ListJsonObject> firstObject = mPreference.getAllDataObject(Helper.STORED_DATA_FIRST);
        List<ListJsonObject> secondObject = mPreference.getAllDataObject(Helper.STORED_DATA_SECOND);
        return Lists.newArrayList(Iterables.concat(firstObject, secondObject));
    }
}