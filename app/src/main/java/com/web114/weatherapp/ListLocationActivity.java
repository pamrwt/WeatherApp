package com.web114.weatherapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListLocationActivity extends ActionBarActivity {
    private static final String TAG = ListLocationActivity.class.getSimpleName();
    private DatabaseQuery query;
    private List<DatabaseLocationObject> allLocations;
    private LocationObject locationObject;
    private LocationMapObject locationMapObject;
    private RequestQueue queue;
    private List<LocationObject> allData;
    private LocationAdapter locationAdapter;
    private RecyclerView locationRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location);
        setTitle(Helper.LOCATION_LIST);
        queue = Volley.newRequestQueue(ListLocationActivity.this);
        allData = new ArrayList<LocationObject>();
        query = new DatabaseQuery(ListLocationActivity.this);
        allLocations = query.getStoredDataLocations();
     // android.support.v7.app.ActionBar actionBar=getSupportActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListLocationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        if(null != allLocations){
            for(int i = 0; i < allLocations.size(); i++){
                // make volley network call here
                System.out.println("Response printing " + allLocations.get(i).getLocation());
                requestJsonObject(allLocations.get(i));
            }
        }
        Toast.makeText(ListLocationActivity.this, "Count number of locations " + allLocations.size(), Toast.LENGTH_LONG).show();
        ImageButton addLocation = (ImageButton) findViewById(R.id.add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLocationIntent = new Intent(ListLocationActivity.this, AddLocationActivity.class);
                startActivity(addLocationIntent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListLocationActivity.this);
        locationRecyclerView = (RecyclerView) findViewById(R.id.location_list);
        locationRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void requestJsonObject(final DatabaseLocationObject paramValue){
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+paramValue.getLocation()+"&APPID=62f6de3f7c0803216a3a13bbe4ea9914&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                locationMapObject = gson.fromJson(response, LocationMapObject.class);
                if (null == locationMapObject) {
                    Toast.makeText(getApplicationContext(), "Nothing was returned", Toast.LENGTH_LONG).show();
                } else {
                    int rowId = paramValue.getId();
                    Long tempVal = Math.round(Math.floor(Double.parseDouble(locationMapObject.getMain().getTemp())));
                    String city = locationMapObject.getName() + ", " + locationMapObject.getSys().getCountry();
                    String weatherInfo = String.valueOf(tempVal) + "<sup>o</sup>, " + Helper.capitalizeFirstLetter(locationMapObject.getWeather().get(0).getDescription());
                    allData.add(new LocationObject(rowId, city, weatherInfo));
                    locationAdapter = new LocationAdapter(ListLocationActivity.this, allData);
                    locationRecyclerView.setAdapter(locationAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
          //  Intent intent=new Intent(ListLocationActivity.this,WeatherActivity.class);
           // startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}