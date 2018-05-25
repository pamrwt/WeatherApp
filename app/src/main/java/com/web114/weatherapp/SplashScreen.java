package com.web114.weatherapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.Window;
import android.view.WindowManager;

import com.google.common.collect.Lists;
import com.google.common.math.IntMath;

import java.io.InputStream;
import java.math.RoundingMode;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private CustomSharedPreference customSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        customSharedPreference = new CustomSharedPreference(SplashScreen.this);
        if(!customSharedPreference.getDataSourceIfPresent()){
            PrepareDataSource mDataSource = new PrepareDataSource();
            mDataSource.execute();
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(startActivityIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    private class PrepareDataSource extends AsyncTask<Void, Void, Void> {
        protected void onProgressUpdate() {
        }
        protected void onPostExecute() {
        }
        @Override
        protected Void doInBackground(Void... voids) {
            InputStream stream = ((CustomApplication)getApplication()).getJsonStream();
            List<ListJsonObject> storeSourceData = ((CustomApplication)getApplication()).readStream(stream);
            // store data in shared reference
            int partitionSize = IntMath.divide(storeSourceData.size(), 2, RoundingMode.UP);
            List<List<ListJsonObject>> partitions = Lists.partition(storeSourceData, partitionSize);
            List<ListJsonObject> firstListObject = partitions.get(0);
            List<ListJsonObject> secondListObject = partitions.get(1);
            customSharedPreference.setDataFromSharedPreferences(Helper.STORED_DATA_FIRST, firstListObject);
            customSharedPreference.setDataFromSharedPreferences(Helper.STORED_DATA_SECOND, secondListObject);
            customSharedPreference.setDataSourceIfPresent(true);
            return null;
        }
    }
}