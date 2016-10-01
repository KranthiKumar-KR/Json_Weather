package com.example.kranthi.json_weather;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView welcome;
    TextView output;
    TextView longitudetv, latitudetv, temp, min_temp, max_temp, humiditytv, descriptiontv, pressuretv;
    ImageView imageView2;
    EditText input;
    Button search;
    String message = "";


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "please enter valid city name", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject ourData = jsonObject.getJSONObject("main");
                double tempDoub = Double.parseDouble(ourData.getString("temp"));
                double humidityDoub = Double.parseDouble(ourData.getString("humidity"));
                double tempMin = Double.parseDouble(ourData.getString("temp_min"));
                double tempMax = Double.parseDouble(ourData.getString("temp_max"));
                int pressure = Integer.parseInt(ourData.getString("pressure"));
                int humidity = (int) (humidityDoub);
                int temp_min_int = (int) (tempMin - 273);
                int temp_max_int = (int) (tempMax - 273);
                int tempInt = (int) (tempDoub - 273);
                String name = jsonObject.getString("name");
                output.setText(name);
                temp.setText("Temperature: " + tempInt + "c");
                max_temp.setText("Maximum Temperature: " + temp_max_int + "c");
                min_temp.setText("Minimum Temperature: " + temp_min_int + "c");
                pressuretv.setText("Pressure: " + pressure);
                humiditytv.setText("Humidity: " + humidity);

                JSONObject locData = jsonObject.getJSONObject("coord");
                String latitude = locData.getString("lat");
                String longitude = locData.getString("lon");
                longitudetv.setText("longitude: " + longitude);
                latitudetv.setText("Latitude: " + latitude);

                String weatherData = jsonObject.getString("weather");
                JSONArray array = new JSONArray(weatherData);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObjectPart = array.getJSONObject(i);
                    String main = jsonObjectPart.getString("main");
                    String description = jsonObjectPart.getString("description");

                    if (main != "" && description != "") {

                        if (main.toString().equals("Clouds")){
                            imageView2.setImageResource(R.drawable.cloudyicon);
                        }
                        if (main.toString().equals("Haze")){
                            imageView2.setImageResource(R.drawable.hazyicon);
                        }
                        if (main.toString().equals("Clear")){
                            imageView2.setImageResource(R.drawable.bck);
                        }
                        if (main.toString().equals("Rain")){
                            imageView2.setImageResource(R.drawable.rainicon);
                        }
                        if (main.toString().equals("Thunderstorm")){
                            imageView2.setImageResource(R.drawable.thunderlightningstormicon);
                        }
                        if (main.toString().equals("Mist")){
                            imageView2.setImageResource(R.drawable.mist);
                        }
                        welcome.setText(main);
                        //welcome.setAllCaps(true);
                        descriptiontv.setText(description);
                        //descriptiontv.setAllCaps(true);

                    } else {
                        Toast.makeText(getApplicationContext(), "please enter valid city name", Toast.LENGTH_LONG).show();

                    }
                }


            } catch (Exception e) {
            }

        }


    }

    public void getWeather(View view) {
        InputMethodManager img = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        img.hideSoftInputFromWindow(input.getWindowToken(), 0);

        try {

            String encodedCityName = "dallas";
            encodedCityName = URLEncoder.encode(input.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();


            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&APPID=ce3e552eda85cdd5e85c882f6420b392").get();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "please enter valid city name", Toast.LENGTH_LONG).show();
        }
        /////***************** not working dont try again *****************************
        //search.setVisibility(View.INVISIBLE);
        ///input.setVisibility(View.INVISIBLE);*/

    }
   /* public void showWeather(View view) {
        InputMethodManager img = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        img.hideSoftInputFromWindow(input.getWindowToken(), 0);

        try {

            String currentlat = "";
            String currentlong = "";
            currentlat = URLEncoder.encode(input.getText().toString(), "UTF-8");
            currentlong = URLEncoder.encode(input.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();


            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&APPID=ce3e552eda85cdd5e85c882f6420b392").get();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "please enter valid city name", Toast.LENGTH_LONG).show();
        }
        ////*//***************** not working dont try again *****************************
        //search.setVisibility(View.INVISIBLE);
        ///input.setVisibility(View.INVISIBLE);*//*

    }
   */ @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.result);
        input = (EditText) findViewById(R.id.input);
        welcome = (TextView) findViewById(R.id.welcome);
        latitudetv = (TextView) findViewById(R.id.latitude);
imageView2 = (ImageView) findViewById(R.id.imageView2);
        longitudetv = (TextView) findViewById(R.id.longitude);
        temp = (TextView) findViewById(R.id.temp);
        min_temp = (TextView) findViewById(R.id.mintemp);
        max_temp = (TextView) findViewById(R.id.maxtemp);
        humiditytv = (TextView) findViewById(R.id.humiditytv);
        pressuretv = (TextView) findViewById(R.id.pressure);
        descriptiontv = (TextView) findViewById(R.id.description);

    }
}
