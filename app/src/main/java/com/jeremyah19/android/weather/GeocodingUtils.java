package com.jeremyah19.android.weather;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Takes address, and uses google maps api to get latitude, longitude, etc.
public class GeocodingUtils {
    public static final String TAG = "GeocodingUtils";

    private static final String API_DOMAIN = "https://maps.googleapis.com/maps/api/geocode/json";

    public static GeocodingInfo getGeocodingInfo(String address, double latitude, double longitude) {
        GeocodingInfo geocodingInfo = GeocodingInfo.getInstance();
        Uri uri;

        if(address != null) {
            uri = Uri.parse(API_DOMAIN)
                    .buildUpon()
                    .appendQueryParameter("address", address)
                    .appendQueryParameter("key", ApiKeys.GEOCODING_API_KEY)
                    .build();
        } else {
            uri = Uri.parse(API_DOMAIN)
                    .buildUpon()
                    .appendQueryParameter("latlng", latitude + "," + longitude)
                    .appendQueryParameter("key", ApiKeys.GEOCODING_API_KEY)
                    .build();
        }
        Log.d(TAG, "Geocoding JSON URL: " + uri.toString());

        try {
            JSONObject jsonObject = new JSONObject(new String(getUrlBytes(uri.toString())));
            Log.i(TAG, "Received JSON: " + jsonObject.toString());

            JSONObject resultsObject = jsonObject.getJSONArray("results").getJSONObject(0);

            JSONArray addressComponentsArray = resultsObject.getJSONArray("address_components");

            JSONObject locationObject = resultsObject
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            geocodingInfo.setAddressComponents(addressComponentsArray);
            geocodingInfo.setLatitude(locationObject.getDouble("lat"));
            geocodingInfo.setLongitude(locationObject.getDouble("lng"));

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to connect", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return geocodingInfo;
    }

    public static byte[] getUrlBytes(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + url);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

}
