package com.example.android.ud843_quakereport;

/**
 * Created by OWNER on 7/22/2016.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();


    private static final String QUERY_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";


    private QueryUtils() {
    }


    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Earthquake> earthquake = extractEarthquakes(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static ArrayList<Earthquake> extractEarthquakes(String JsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root = new JSONObject(JsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");

            for (int i = 0; i < featuresArray.length(); i++){
                JSONObject earthquakeJSON = featuresArray.getJSONObject(i);
                JSONObject propertiesJSON = earthquakeJSON.getJSONObject("properties");
                double magnitude = propertiesJSON.getDouble("mag");
                DecimalFormat formatter = new DecimalFormat("0.0");
                String magnitudeString = formatter.format(magnitude);
                String location = propertiesJSON.getString("place");
                String relativeLocation = "";
                String mainLocation = "";
                if (location.contains("km") && location.contains("of")){
                    int index = location.indexOf("of");
                    relativeLocation = location.substring(0, index + 2);
                    mainLocation = location.substring(index+3, location.length());
                }
                else {
                    relativeLocation = "Near the";
                    mainLocation = location;
                }
                long time = propertiesJSON.getLong("time");

                String url = propertiesJSON.getString("url");

                earthquakes.add(new Earthquake(magnitude, relativeLocation,mainLocation, time, url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
//    private static String LOCATION_SEPARATOR = " of ";
//    if (originalLocation.contains(LOCATION_SEPARATOR)) {
//        String[] parts = originalLocation.split(LOCATION_SEPARATOR);
//        locationOffset = parts[0] + LOCATION_SEPARATOR;
//        primaryLocation = parts[1];
//    } else {
//        locationOffset = getContext().getString(R.string.near_the);
//        primaryLocation = originalLocation;
//    }
}