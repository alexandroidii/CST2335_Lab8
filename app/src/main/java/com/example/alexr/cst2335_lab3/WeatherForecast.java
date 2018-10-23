package com.example.alexr.cst2335_lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "1234 WeatherForecast";
    ProgressBar progressBar;
    String minTemp;
    String maxTemp;
    String currentTemp;
    String iconName;
    URL iconUrl;
    File iconFile;


    Bitmap currentTempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = (ProgressBar) findViewById(R.id.weatherProgress);
        progressBar.setVisibility(View.VISIBLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]); //("http://www.google.com/");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                Log.i("XML parsing:", "" + xpp.getEventType());
                int type;
                while ((type = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress((new Integer[]{25})); //telling android to update the gui

                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress((new Integer[]{50})); //telling android to update the gui

                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress((new Integer[]{75})); //telling android to update the gui

                            Log.i("XML tempurature:", currentTemp + minTemp + maxTemp);

                        }
                        if (xpp.getName().equals("weather")) {
                            try {

                                iconName = xpp.getAttributeValue(null, "icon");
                                FileInputStream inputStream = null;
                                if (fileExistance(iconName + ".png")) {
                                    try {
                                        inputStream = new FileInputStream(iconFile);
                                        Log.i(ACTIVITY_NAME, "Weather Icon File Exists.  Opening locally.");
                                    } catch (FileNotFoundException fileNotFoundException) {
                                        fileNotFoundException.printStackTrace();
                                        Log.i(ACTIVITY_NAME, "Weather Icon File does not Exists.  Fetching file online.");

                                    }

                                    currentTempBitmap = BitmapFactory.decodeStream(inputStream);
                                } else {

                                    iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                    currentTempBitmap = HttpUtils.getImage(iconUrl);

                                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                    currentTempBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    publishProgress(100);


                                }
                            } catch (MalformedURLException malformedURLException) {

                            } finally {
                                urlConnection.disconnect();
                            }
                        }
                    }
                    xpp.next();
                }

            } catch (Exception me) {
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

            try {
                for (int i = 0; i < urls.length; i++) {
                    Thread.sleep(1000);
                    publishProgress((new Integer[]{25, 50, 75})); //telling android to update the gui

                }
            } catch (Exception e) {

            }

            return "Weather Loaded Successfully";
        }

        public boolean fileExistance(String fileName) {
            iconFile = getBaseContext().getFileStreamPath(fileName);
            return iconFile.exists();
        }

        @Override
        public void onProgressUpdate(Integer... progress) {
            Log.i(ACTIVITY_NAME, "" + progress[0]);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
        }

        public void onPostExecute(String work) {
            Log.i(ACTIVITY_NAME, work);
            TextView currentTempText = (TextView) findViewById(R.id.currentTempText);
            TextView minTempText = (TextView) findViewById(R.id.minTempText);
            TextView maxTempText = (TextView) findViewById(R.id.maxTempText);
            ImageView currentTempIcon = (ImageView) findViewById(R.id.currentTempIcon);

            currentTempText.setText(currentTemp);
            currentTempText.setVisibility(View.VISIBLE);

            minTempText.setText(minTemp);
            minTempText.setVisibility(View.VISIBLE);

            maxTempText.setText(maxTemp);
            maxTempText.setVisibility(View.VISIBLE);

            currentTempIcon.setImageBitmap(currentTempBitmap);

            progressBar.setVisibility(View.INVISIBLE);


        }
    }

    /* Source: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
     *  Author: Inzimam Tariq IT
     *  Date: 2016-05-12
     *  This enables the back button on the action bar to return to the start activity.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

