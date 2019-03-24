package com.jeremyah19.android.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodingInfo {
    public static final String TAG = "GeocodingInfo";

    private static GeocodingInfo mGeocodingInfo;

    private double mLatitude;
    private double mLongitude;

    private AddressComponents mAddressComponents;

    public static GeocodingInfo getInstance() {

        if(mGeocodingInfo == null) {
            mGeocodingInfo = new GeocodingInfo();
        }
        return mGeocodingInfo;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getNeighborhood() {
        return mAddressComponents.mNeighborhood;
    }

    public String getSublocality() {
        return mAddressComponents.mSublocality;
    }

    public String getLocality() {
        return mAddressComponents.mLocality;
    }

    public String getAdministrativeArea() {
        return mAddressComponents.mAdministrativeArea;
    }

    public String getCountry() {
        return mAddressComponents.mCountry;
    }

    public void setAddressComponents(JSONArray array) {
        mAddressComponents.mNeighborhood = null;
        mAddressComponents.mSublocality = null;

        try{
            for(int i = 0; i < array.length(); i++) {
                JSONObject addressObject = array.getJSONObject(i);
                String shortName = addressObject.getString("long_name");
                switch(addressObject.getJSONArray("types").getString(0)) {
                    case "neighborhood":
                        mAddressComponents.mNeighborhood = shortName;
                        break;
                    case "sublocality_level_1":
                        mAddressComponents.mSublocality = shortName;
                        break;
                    case "locality":
                        mAddressComponents.mLocality = shortName;
                    case "administrative_area_level_1":
                        mAddressComponents.mAdministrativeArea = shortName;
                        break;
                    case "country":
                        mAddressComponents.mCountry = shortName;
                }
            }
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }

    public String getCityName() {
        if(getNeighborhood() != null) {
            return getNeighborhood();
        } else if(getSublocality() != null) {
            return getSublocality();
        } else {
            return getLocality();
        }
    }

    private GeocodingInfo() {
        mAddressComponents = new AddressComponents();
    }

    private class AddressComponents {
        String mNeighborhood;
        String mLocality;
        String mSublocality;
        String mAdministrativeArea;
        String mCountry;
    }
}
