package com.web114.weatherapp;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pavlospt.CircleView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG =MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    TextView cityCountry,Tempmax,tempmin,pressure,sunrise,sunset,icon,direction;
    private TextView currentDate;
    private ImageView weatherImage,image_desription,weather_image;
    private CircleView circleTitle;
    CharSequence[] valuestemp = {" C"," F"};
    CharSequence[] valueswind = {" Km/hour"," Mph"};
    CharSequence[] valuesvisibility = {" Km/hour"," Mph"};
    CharSequence[] valuespressure = {" HPa"," Mb","mmHg","In"};
    private TextView windResult;
    private TextView humidityResult,text_description,text_temp,text_time;
    private RequestQueue queue;
    private LocationMapObject locationMapObject;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private CustomSharedPreference sharedPreference;
    private String isLocationSaved;
    private DatabaseQuery query;
    private String apiUrl;
    TextView temp,wind,visibility,presure, latitude,langitude;
    private FiveDaysForecast fiveDaysForecast;
    AlertDialog alertDialog1;
    static Long tempVal;
    String windSpeed;
    Double Lat;
    String cardinalDirection = null;
    Double Lang;
    //private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 10000000;  /* 10 secs */
    private long FASTEST_INTERVAL = 200000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation();
        queue = Volley.newRequestQueue(this);
        query = new DatabaseQuery(MainActivity.this);
        sharedPreference = new CustomSharedPreference(MainActivity.this);
        isLocationSaved = sharedPreference.getLocationInPreference();
        cityCountry = (TextView) findViewById(R.id.city_country);
        currentDate = (TextView) findViewById(R.id.current_date);
        weatherImage = (ImageView) findViewById(R.id.weather_icon);
        text_time=(TextView)findViewById(R.id.text_time);
        weather_image= (ImageView) findViewById(R.id.acording);
        // circleTitle = (CircleView) findViewById(R.id.weather_result);
        windResult = (TextView) findViewById(R.id.wind_result);
        humidityResult = (TextView) findViewById(R.id.humidity_result);
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        temp= (TextView) findViewById(R.id.temp);
        wind= (TextView) findViewById(R.id.wind);
        visibility= (TextView) findViewById(R.id.visiblity);
        pressure= (TextView) findViewById(R.id.pressure);
image_desription= (ImageView) findViewById(R.id.imagedrc);
        text_description= (TextView) findViewById(R.id.textdrc);
text_temp=(TextView)findViewById(R.id.texttemp);
        direction=(TextView)findViewById(R.id.direction);
        // Tempmax = (TextView) findViewById(R.id.temp_max);
        //  tempmin = (TextView) findViewById(R.id.temp_min);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        pressure = (TextView) findViewById(R.id.pressure);
        visibility = (TextView) findViewById(R.id.visible);

        //  icon = (TextView) findViewById(R.id.icon);

        location();




        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.weather_daily_list);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void location(){
        if (isLocationSaved.equals("")) {
            SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);
            String la = prefs.getString("Lat", null);
            String lang = prefs.getString("Lang", null);
            Toast.makeText(this, "mkkk"+lang, Toast.LENGTH_SHORT).show();
// double lati=Double.valueOf(la);
//            double langi=Double.valueOf(lang);
         //   Toast.makeText(this, ""+lati, Toast.LENGTH_SHORT).show();
            // make API call with longitude and latitude


            apiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=28.7041&lon=77.1025&APPID="+Helper.API_KEY+"&units=metric";

            //   Toast.makeText(this, "d"+Lat, Toast.LENGTH_SHORT).show();
            Timer timer = new Timer ();
            TimerTask hourlyTask = new TimerTask () {
                @Override
                public void run () { makeJsonObject(apiUrl);
                }
            };

// schedule the task to run starting now and then every hour...
            timer.schedule (hourlyTask, 0l, 1000*1*60);


        } else {
            // make API call with city name
            String storedCityName = sharedPreference.getLocationInPreference();
            //String storedCityName = "Enugu";
            System.out.println("Stored city " + storedCityName);
            String[] city = storedCityName.split(",");
            if (!TextUtils.isEmpty(city[0])) {
                System.out.println("Stored city " + city[0]);
                final String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city[0] + "&APPID="+Helper.API_KEY+"&units=metric";
                Timer timer = new Timer ();
                TimerTask hourlyTask = new TimerTask () {
                    @Override
                    public void run () {  makeJsonObject(url);
                    }
                };

// schedule the task to run starting now and then every hour...
                timer.schedule (hourlyTask, 0l, 1000*1*60);
            }
        }
    }

    private void makeJsonObject(final String apiUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                locationMapObject = gson.fromJson(response, LocationMapObject.class);
                if (null == locationMapObject) {
                    Toast.makeText(getApplicationContext(), "Nothing was returned", Toast.LENGTH_LONG).show();
                } else {
                    String date = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    text_time.setText(date);
                    Toast.makeText(getApplicationContext(), "Response Good", Toast.LENGTH_LONG).show();
                    String city = locationMapObject.getName() + ", " + locationMapObject.getSys().getCountry();
                    String todayDate = getTodayDateInStringFormat();
                    tempVal = Math.round(Math.floor(Double.parseDouble(locationMapObject.getMain().getTemp())));
                    String weatherTemp = String.valueOf(tempVal) + "°C";
                    String weatherDescription = Helper.capitalizeFirstLetter(locationMapObject.getWeather().get(0).getDescription());
                    windSpeed = locationMapObject.getWind().getSpeed();
                    long Sunrise = locationMapObject.getSys().getSunrise();
                    long Sunset = locationMapObject.getSys().getSunset();
                    String Windspeed = locationMapObject.getWind().getSpeed();
                    long Visibility= locationMapObject.getVisibility();
                    double Pressure = locationMapObject.getMain().getPressure();
                    int dir=locationMapObject.getWind().getDeg();
                    String Icon =  Helper.capitalizeFirstLetter(locationMapObject.getWeather().get(0).getIcon());
                    long temp_max=  Math.round(Math.floor(Double.parseDouble(locationMapObject.getMain().getTemp_max())));
                    String Temp_max = String.valueOf(temp_max) + "°";
                    long temp_min= Math.round(Math.floor(Double.parseDouble(locationMapObject.getMain().getTemp_min())));
                    String Temp_min= String.valueOf(temp_min) + "°";
                    convertDegreeToCardinalDirection(dir);
                    Toast.makeText(MainActivity.this, ""+cardinalDirection, Toast.LENGTH_SHORT).show();
                    direction.setText(cardinalDirection);

                    // String Set = String.valueOf(Sunset) ;
                    Date dateObject = new Date(Sunrise*1000);
                    Date dateObject1 = new Date(Sunset*1000);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                    String Rise = timeFormat.format(dateObject);
                    String Set = timeFormat.format(dateObject1) ;
                    if(Calendar.getInstance().getTime().after(dateObject)){
                    }

                    //   String Temp_max = String.valueOf(temp_max) + "°";
                    String humidityValue = locationMapObject.getMain().getHumudity();
                    //save location in database
                    if (apiUrl.contains("lat")) {
                        query.insertNewLocation(locationMapObject.getName());
                    }
                    // populate View data
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ");
                    String currentDateandTime = sdf.format(currentTime );
                    cityCountry.setText(Html.fromHtml(city));
                    currentDate.setText(Html.fromHtml(todayDate));
//                    Tempmax.setText(Html.fromHtml(Temp_max).toString());
                    //                  tempmin.setText(Html.fromHtml(Temp_min).toString());
                    sunrise.setText(Html.fromHtml(Rise)+"AM");
                    sunset.setText(Html.fromHtml(Set)+"PM");
                    pressure.setText(Html.fromHtml(String.valueOf(Pressure)+"hPa"));
//                    icon.setText(Html.fromHtml(Icon));
                    visibility.setText(Html.fromHtml(String.valueOf(Visibility)+"hPa"));
                    // currentDate.setText(Html.fromHtml(todayDate));
                    // currentDate.setText(Html.fromHtml(todayDate));
                 //   circleTitle.setTitleText(Html.fromHtml(weatherTemp).toString());
                  //  circleTitle.setSubtitleText(Html.fromHtml(weatherDescription).toString());
                    text_description.setText(Html.fromHtml(weatherDescription).toString());
                    text_temp.setText(Html.fromHtml(weatherTemp).toString());
                    switch (Html.fromHtml(weatherDescription).toString()){
                        case "Haze":
                            image_desription.setImageResource(R.drawable.sunny);
                            break;
                        case"clear sky":
                            image_desription.setImageResource(R.drawable.sunny);
                            break;
                        case"few clouds":
                            image_desription.setImageResource(R.drawable.sun);
                            break;
                        case "broken clouds":
                            image_desription.setImageResource(R.drawable.sun);
                            break;
                        case"light rain":
                            image_desription.setImageResource(R.drawable.rain);
                            break;
                        case"scattered clouds":
                            image_desription.setImageResource(R.drawable.sun);
                            break;
                        default:
                            image_desription.setImageResource(R.drawable.sunny);
                            break;
                    }
                    windResult.setText(Html.fromHtml(windSpeed) + " km/h");
                    humidityResult.setText(Html.fromHtml(humidityValue) + " %");
                    SharedPreferences.Editor editor = getSharedPreferences("context", MODE_PRIVATE).edit();
                    editor.putString("city",(Html.fromHtml(city)).toString() );
                    editor.putString("weatherTemp", Html.fromHtml(weatherTemp).toString());
                    editor.putString("weatherDescription", Html.fromHtml(weatherDescription).toString());
                    editor.putString("Time",currentDateandTime);
                    editor.apply();
                    fiveDaysApiJsonObjectCall(locationMapObject.getName());
                    ShowNotificationService.startActionShow(getApplicationContext());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //make api   call

                    if (locationManager != null) {

                        SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);
                        String la = prefs.getString("Lat", null);
                        String lang = prefs.getString("Lang", null);
                        Toast.makeText(this, "mkkk"+lang, Toast.LENGTH_SHORT).show();
                        double lati=Double.valueOf(la);
                        double langi=Double.valueOf(lang);

                        apiUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+langi+"&APPID="+Helper.API_KEY+"&units=metric";
                        makeJsonObject(apiUrl);
                    } else {
                        SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);
                        String la = prefs.getString("Lat", null);
                        String lang = prefs.getString("Lang", null);
                        Toast.makeText(this, "mkkk"+lang, Toast.LENGTH_SHORT).show();
                        double lati=Double.valueOf(la);
                        double langi=Double.valueOf(lang);
                        apiUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+langi+"&APPID="+Helper.API_KEY+"&units=metric";
                        makeJsonObject(apiUrl);
                    }
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.permission_notice), Toast.LENGTH_LONG).show();
            }
        }
    }

    //  @Override
    //  public void onLocationChanged(Location location) {
    //     this.location = location;
    // }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private String getTodayDateInStringFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
    }

    private void fiveDaysApiJsonObjectCall(String city) {
        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID="+Helper.API_KEY+"&units=metric";
        final List<WeatherObject> daysOfTheWeek = new ArrayList<WeatherObject>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response 5 days" + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Forecast forecast = gson.fromJson(response, Forecast.class);
                if (null == forecast) {
                    Toast.makeText(getApplicationContext(), "Nothing was returned", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Response Good", Toast.LENGTH_LONG).show();
                    int[] everyday = new int[]{0, 0, 0, 0, 0, 0, 0};
                    List<FiveWeathers> weatherInfo = forecast.getList();
                    for (int i = 0; i < weatherInfo.size(); i++) {
                        String time = weatherInfo.get(i).getDt_txt();
                        String shortDay = String.format(convertTimeToDay(time), "dd/MM");
                        String temp = weatherInfo.get(i).getMain().getTemp();
                        String tempMin = weatherInfo.get(i).getMain().getTemp_min();
                        String temp_max = weatherInfo.get(i).getMain().getTemp_max();
                        String weather_des=weatherInfo.get(i).getWeather().get(0).getDescription();
                        String timeS = weatherInfo.get(i).getDt_txt();
String timeStamp=String.format(convertTimeToDate(timeS));

                            //   Toast.makeText(WeatherActivity.this, ""+shortDay, Toast.LENGTH_SHORT).show();
                           SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
                       try{     String timeStamp2;
                            Date startDate;


                          startDate = df.parse(timeStamp);
                           Toast.makeText(MainActivity.this, ""+startDate, Toast.LENGTH_SHORT).show();
                       //    timeStamp2   = df.format(startDate);
                            //   System.out.println(newDateString);

                         }catch (ParseException  e){}
                        if (convertTimeToDay(time).equals("Mon") && everyday[0] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[0] = 1;
                        }
                        if (convertTimeToDay(time).equals("Tue") && everyday[1] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[1] = 1;
                        }
                        if (convertTimeToDay(time).equals("Wed") && everyday[2] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[2] = 1;
                        }
                        if (convertTimeToDay(time).equals("Thu") && everyday[3] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[3] = 1;
                        }
                        if (convertTimeToDay(time).equals("Fri") && everyday[4] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[4] = 1;
                        }
                        if (convertTimeToDay(time).equals("Sat") && everyday[5] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[5] = 1;
                        }
                        if (convertTimeToDay(time).equals("Sun") && everyday[6] < 1) {
                            daysOfTheWeek.add(new WeatherObject(shortDay, R.drawable.small_weather_icon, temp, tempMin,weather_des,temp_max,timeStamp));
                            everyday[6] = 1;
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, daysOfTheWeek);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
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

    private String convertTimeToDay(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSSS", Locale.getDefault());
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String days = "";
        String dateformate="";
        try {
            Date date1 = format1.parse(time);
        //    Toast.makeText(this, ""+date1, Toast.LENGTH_SHORT).show();
            Date date = format.parse(time);
            System.out.println("Our time " + date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();
            days = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

            System.out.println("Our time " + days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
    private String convertTimeToDate(String time) {
        String out = "";
        try{  SimpleDateFormat inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());


            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); //Also tried GMT+00:00

            Date date = inputFormat.parse(time);

            outputFormat.setTimeZone(TimeZone.getDefault());
            out = outputFormat.format(date);}catch (Exception e){}



        return out;}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {

            Intent addLocationIntent = new Intent(MainActivity.this, AddLocationActivity.class);
            startActivity(addLocationIntent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            final TextView tv= (TextView) findViewById(R.id.textview);
            tv.setText("°C");
            CreateAlertDialogWithRadioButtonGroup();
        } else if (id == R.id.nav_slideshow) {
            CreateAlertDialogWithRadioButtonGroup1();

        } else if (id == R.id.nav_manage) {
            CreateAlertDialogWithRadioButtonGroup2();

        } else if (id == R.id.nav_share) {
            CreateAlertDialogWithRadioButtonGroup4();

        } else if (id == R.id.nav_wind) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            // intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CreateAlertDialogWithRadioButtonGroup(){
        final TextView tv= (TextView) findViewById(R.id.textview);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(valuestemp, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        temp.setText("°C");
                        String weatherTemp = String.valueOf(tempVal) + "°C";
                        circleTitle.setTitleText(Html.fromHtml(weatherTemp).toString());

                        break;
                    case 1:

                        temp.setText("°F");
                        //   Toast.makeText(WeatherActivity.this, ""+tempVal, Toast.LENGTH_SHORT).show();
                        Long f=tempVal*9/5+100;
                        String fahranit= String.valueOf(f) + "°F";
                        circleTitle.setTitleText(Html.fromHtml(fahranit).toString());
                        break;


                }
                alertDialog1.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void CreateAlertDialogWithRadioButtonGroup1(){
        final TextView tv= (TextView) findViewById(R.id.textview);
        tv.setText("mph");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Wind");

        builder.setSingleChoiceItems(valueswind, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        wind.setText("Km/hr");
                        windResult.setText(Html.fromHtml(windSpeed) + " km/hr");
                        break;
                    case 1:
                        wind.setText("mph");
                        windResult.setText(Html.fromHtml(windSpeed) + "mph");
                     //   double wind=Long.parseLong(windSpeed)/1.6;
                      //  String w=Double.toString(wind);
                        //   Toast.makeText(WeatherActivity.this, ""+tempVal, Toast.LENGTH_SHORT).show();
                      //  windResult.setText(Html.fromHtml(w) + "mph");
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void CreateAlertDialogWithRadioButtonGroup2(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Visibility");

        builder.setSingleChoiceItems(valuesvisibility, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                TextView tv= (TextView) findViewById(R.id.textview);
                switch(item)
                {
                    case 0:
                        visibility.setText("km/h");
                        windResult.setText(Html.fromHtml(windSpeed) + "km/h");
                        break;
                    case 1:
                        visibility.setText("mph");
                        double wind=Long.parseLong(windSpeed)/1.6;
                        String w=Double.toString(wind);
                        //   Toast.makeText(WeatherActivity.this, ""+tempVal, Toast.LENGTH_SHORT).show();
                        windResult.setText(Html.fromHtml(w) + "mph");
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void CreateAlertDialogWithRadioButtonGroup4(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Pressure");

        builder.setSingleChoiceItems(valuespressure, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:pressure.setText("km/hr");
                        windResult.setText(Html.fromHtml(windSpeed) + " km/h");
                        break;
                    case 1:
                        pressure.setText("mph");
                        double wind=Long.parseLong(windSpeed)/1.6;
                        String w=Double.toString(wind);
                        //   Toast.makeText(WeatherActivity.this, ""+tempVal, Toast.LENGTH_SHORT).show();
                        windResult.setText(Html.fromHtml(w) + "mph");
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        //  mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        Lat=  location.getLatitude();
        Toast.makeText(this, ""+Lat, Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor = getSharedPreferences("location", MODE_PRIVATE).edit();
        editor.putString("Lat", Double.toString(location.getLatitude()));
        editor.putString("Lang", Double.toString(location.getLongitude()));
        editor.apply();
        Toast.makeText(this, ""+Lat, Toast.LENGTH_SHORT).show();
        Lang= location.getLongitude();
        Lat=location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public String convertDegreeToCardinalDirection(int directionInDegrees){

        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "N";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "NNE";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "NE";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "ENE";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "E";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "ESE";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "SE";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "SSE";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "S";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "SSW";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "SW";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "WSW";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "W";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "WNW";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "NW";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }

}