package com.jeremyah19.android.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ForecastApiUtils {
    public static final String TAG = "ForecastApiUtils";

    public static final String SUMMARY = "summary";
    public static final String ICON = "icon";
    public static final String TIME = "time";
    public static final String TEMPERATURE = "temperature";
    public static final String TEMPERATURE_MIN = "temperatureMin";
    public static final String TEMPERATURE_MAX = "temperatureMax";
    public static final String APPARENT_TEMPERATURE = "apparentTemperature";
    public static final String APPARENT_TEMPERATURE_MIN = "apparentTemperatureMin";
    public static final String APPARENT_TEMPERATURE_MAX = "apparentTemperatureMax";
    public static final String PRECIP_PROB = "precipProbability";
    public static final String PRECIP_TYPE = "precipType";
    public static final String PRECIP_INTENSITY = "precipIntensity";
    public static final String PRECIP_ACCUMULATION = "precipAccumulation";
    public static final String HUMIDITY = "humidity";
    public static final String WIND_SPEED = "windSpeed";
    public static final String WIND_BEARING = "windBearing";
    public static final String CLOUD_COVER = "cloudCover";
    public static final String DEW_POINT = "dewPoint";
    public static final String PRESSURE = "pressure";
    public static final String VISIBILITY = "visibility";
    public static final String NOT_PROVIDED = "Not Provided";

    private static final String API_DOMAIN = "https://api.forecast.io/forecast";

    public static ForecastInfo getForecastInfo(double latitude, double longitude) {
        ForecastInfo forecastInfo = null;
        Uri uri = Uri.parse(API_DOMAIN)
                .buildUpon()
                .appendPath(ApiKeys.FORECAST_API_KEY)
                .appendPath(Double.toString(latitude) + "," + Double.toString(longitude))
                .build();
        Log.d(TAG, "Forecast JSON URL: " + uri.toString());

        try {
            JSONObject jsonObject = new JSONObject(
                    new String(GeocodingUtils.getUrlBytes(uri.toString())));
            Log.i(TAG, "Received JSON: " + jsonObject.toString());

            forecastInfo = ForecastInfo.getInstance();
            forecastInfo.setTimezone(jsonObject.getString("timezone"));
            forecastInfo.setOffset(jsonObject.getDouble("offset"));

            setCurrentForecast(forecastInfo, getNullCheckObject(jsonObject, "currently"));

            setMinutelyForecast(forecastInfo, getNullCheckObject(jsonObject, "minutely"));

            setHourlyForecast(forecastInfo, getNullCheckObject(jsonObject, "hourly"));

            setDailyForecast(forecastInfo, getNullCheckObject(jsonObject, "daily"));

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to connect", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return forecastInfo;
    }

    public static Drawable getIconDrawable(Context context, String icon) {
        if(Build.VERSION.SDK_INT >= 21) {
            return context.getResources().getDrawable(getIconId(icon), null);
        } else {
            return context.getResources().getDrawable(getIconId(icon));
        }
    }

    public static String getValueString(String type, Bundle args) {
        if(!type.equals(WIND_BEARING) && args.getDouble(type) == -1) {
            return NOT_PROVIDED;
        }

        switch(type) {
            case TEMPERATURE:
            case TEMPERATURE_MIN:
            case TEMPERATURE_MAX:
            case APPARENT_TEMPERATURE_MIN:
            case APPARENT_TEMPERATURE_MAX:
            case DEW_POINT:
                return Long.toString(Math.round(args.getDouble(type))).concat("º");
            case APPARENT_TEMPERATURE:
                return "Feels Like ".concat(Long.toString(Math.round(args.getDouble(type))).concat("º"));
            case CLOUD_COVER:
            case HUMIDITY:
            case PRECIP_PROB:
                return Long.toString(Math.round(args.getDouble(type) * 100)).concat("%");
            case WIND_SPEED:
                return Long.toString(Math.round(args.getDouble(type))).concat("mph");
            case VISIBILITY:
                return Double.toString(args.getDouble(type)).concat("mi");
            case PRECIP_INTENSITY:
                return new DecimalFormat("#.##").format(args.getDouble(type)).concat("in/hr");
            case PRECIP_ACCUMULATION:
                return new DecimalFormat("#.##").format(args.getDouble(type)).concat("in");
            case PRESSURE:
                return new DecimalFormat("#.##").format(args.getDouble(type)).concat("in-Hg");
            case WIND_BEARING:
                if(args.getInt(type) == -1) {
                    return "Not Provided";
                }
                return getWindBearingString((double)args.getInt(type));
        }
        return null;
    }

    public static String getPrecipTypeTitle(String precipType) {
        switch(precipType) {
            case "rain":
                return "RAIN";
            case "sleet":
                return "SLEET";
            case "snow":
                return "SNOW";
            case "hail":
                return "HAIL";
            case "NP":
                return "PRECIP.";
        }
        return null;
    }

    public static String getAmPmString(int amPm) {
        switch(amPm) {
            case Calendar.AM:
                return "AM";
            case Calendar.PM:
                return "PM";
        }
        return null;
    }

    public static String getTimeString(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance();
        if(calendar.get(Calendar.DATE) == currentCalendar.get(Calendar.DATE)) {
            return "Today";
        } else if(calendar.get(Calendar.DATE) == currentCalendar.get(Calendar.DATE) + 1) {
            return "Tomorrow";
        } else {
            return ForecastApiUtils.getDayOfWeekLongString(calendar.get(Calendar.DAY_OF_WEEK));
        }
    }

    public static String getDayOfWeekShortString(int dayOfWeek) {
        switch(dayOfWeek) {
            case Calendar.SUNDAY:
                return "SUN";
            case Calendar.MONDAY:
                return "MON";
            case Calendar.TUESDAY:
                return "TUE";
            case Calendar.WEDNESDAY:
                return "WED";
            case Calendar.THURSDAY:
                return "THU";
            case Calendar.FRIDAY:
                return "FRI";
            case Calendar.SATURDAY:
                return "SAT";
        }
        return null;
    }

    public static String getDayOfWeekLongString(int dayOfWeek) {
        switch(dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        return null;
    }

    public static String getMonthString(int month) {
        switch(month) {
            case Calendar.JANUARY:
                return "January";
            case Calendar.FEBRUARY:
                return "February";
            case Calendar.MARCH:
                return "March";
            case Calendar.APRIL:
                return "April";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "June";
            case Calendar.JULY:
                return "July";
            case Calendar.AUGUST:
                return "August";
            case Calendar.SEPTEMBER:
                return "September";
            case Calendar.OCTOBER:
                return "October";
            case Calendar.NOVEMBER:
                return "November";
            case Calendar.DECEMBER:
                return "December";
        }
        return null;
    }

    public static String getHourString(int hour) {
        if(hour == 0) {
            return "12";
        } else {
            return Integer.toString(hour);
        }
    }

    private static String getWindBearingString(double windBearing) {
        if (windBearing == -1) {
            return "Not Provided";
        } else if (11.25 <= windBearing && windBearing < 33.75) {
            return "NNE";
        } else if (33.75 <= windBearing && windBearing < 56.25) {
            return "NE";
        } else if (56.25 <= windBearing && windBearing < 78.75) {
            return "ENE";
        } else if (78.75 <= windBearing && windBearing < 101.25) {
            return "E";
        } else if (101.25 <= windBearing && windBearing < 123.75) {
            return "ESE";
        } else if (123.75 <= windBearing && windBearing < 146.25) {
            return "SE";
        } else if (146.25 <= windBearing && windBearing < 168.75) {
            return "SSE";
        } else if (168.75 <= windBearing && windBearing < 191.25) {
            return "S";
        } else if (191.25 <= windBearing && windBearing < 213.75) {
            return "SSW";
        } else if (213.75 <= windBearing && windBearing < 236.25) {
            return "SW";
        } else if (236.25 <= windBearing && windBearing < 258.75) {
            return "WSW";
        } else if (258.75 <= windBearing && windBearing < 281.25) {
            return "W";
        } else if (281.25 <= windBearing && windBearing < 303.75) {
            return "WNW";
        } else if (303.75 <= windBearing && windBearing < 326.25) {
            return "NW";
        } else if (326.25 <= windBearing && windBearing < 348.75) {
            return "NNW";
        } else {
            return "N";
        }
    }

    // Returns appropriate icon id based on input icon string
    private static int getIconId(String icon) {
        switch(icon) {
            case "clear-day":
                return R.drawable.ic_clear_day;
            case "clear-night":
                return R.drawable.ic_clear_night;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
            case "hail":
            case "sleet":
                return R.drawable.ic_snow;
            case "wind":
                return R.drawable.ic_wind;
            case "fog":
                return R.drawable.ic_fog;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "partly-cloudy-day":
                return R.drawable.ic_partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.ic_partly_cloudy_night;
            case "thunderstorm":
                return R.drawable.ic_thunderstorm;
            case "tornado":
                return R.drawable.ic_tornado;
        }
        return 0;
    }

    private static void setCurrentForecast(ForecastInfo info, JSONObject o) {
        ForecastInfo.Currently currently = info.getCurrently();

        currently.setSummary(getNullCheckString(o, "summary"));
        currently.setIcon(getNullCheckString(o, "icon"));
        currently.setTime(new Date(getNullCheckLong(o, "time") * 1000));
        currently.setPrecipIntensity(getNullCheckDouble(o, "precipIntensity"));
        currently.setPrecipProbability(getNullCheckDouble(o, "precipProbability"));
        if(getNullCheckDouble(o, "precipProbability") != 0) {
            currently.setPrecipType(getNullCheckString(o, "precipType"));
        } else {
            currently.setPrecipType("NP");
        }
        currently.setTemperature(getNullCheckDouble(o, "temperature"));
        currently.setApparentTemperature(getNullCheckDouble(o, "apparentTemperature"));
        currently.setHumidity(getNullCheckDouble(o, "humidity"));
        currently.setDewPoint(getNullCheckDouble(o, "dewPoint"));
        currently.setWindSpeed(getNullCheckDouble(o, "windSpeed"));
        currently.setCloudCover(getNullCheckDouble(o, "cloudCover"));
        currently.setPressure(getNullCheckDouble(o, "pressure"));
        currently.setVisibility(getNullCheckDouble(o, "visibility"));
        currently.setWindBearing(getNullCheckInt(o, "windBearing"));
    }

    private static void setMinutelyForecast(ForecastInfo info, JSONObject o) {
        ForecastInfo.Minutely minutely = info.getMinutely();

        minutely.setSummary(getNullCheckString(o, "summary"));
        minutely.setIcon(getNullCheckString(o, "icon"));

        JSONArray a = getNullCheckDataArray(o, "data");
        ArrayList<ForecastInfo.Minutely.Data> dataArrayList = new ArrayList<>();
        if(a != null) {
            for (int i = 0; i < a.length(); i++) {
                ForecastInfo.Minutely.Data data = minutely.getData().get(i);
                data.setTime(new Date(getNullCheckLong(getNullCheckObject(a, i), "time") * 1000));
                data.setPrecipProbability(getNullCheckDouble(getNullCheckObject(a, i), "precipProbability"));
                data.setPrecipIntensity(getNullCheckDouble(getNullCheckObject(a, i), "precipIntensity"));
                dataArrayList.add(i, data);
            }
        }

        minutely.setData(dataArrayList);
    }

    private static void setHourlyForecast(ForecastInfo info, JSONObject o) {
        ForecastInfo.Hourly hourly = info.getHourly();

        hourly.setSummary(getNullCheckString(o, "summary"));
        hourly.setIcon(getNullCheckString(o, "icon"));

        JSONArray a = getNullCheckDataArray(o, "data");
        ArrayList<ForecastInfo.Hourly.Data> dataArrayList = new ArrayList<>();
        if(a != null) {
            for (int i = 0; i < a.length(); i++) {
                ForecastInfo.Hourly.Data data = hourly.getData().get(i);
                data.setTime(new Date(getNullCheckLong(getNullCheckObject(a, i), "time") * 1000));
                data.setSummary(getNullCheckString(getNullCheckObject(a, i), "summary"));
                data.setIcon(getNullCheckString(getNullCheckObject(a, i), "icon"));
                data.setPrecipType(getNullCheckString(getNullCheckObject(a, i), "precipType"));
                data.setTemperature(getNullCheckDouble(getNullCheckObject(a, i), "temperature"));
                data.setApparentTemperature(getNullCheckDouble(getNullCheckObject(a, i), "apparentTemperature"));
                data.setPrecipProbability(getNullCheckDouble(getNullCheckObject(a, i), "precipProbability"));
                data.setPrecipIntensity(getNullCheckDouble(getNullCheckObject(a, i), "precipIntensity"));
                data.setPrecipAccumulation(getNullCheckDouble(getNullCheckObject(a, i), "precipAccumulation"));
                data.setHumidity(getNullCheckDouble(getNullCheckObject(a, i), "humidity"));
                data.setPressure(getNullCheckDouble(getNullCheckObject(a, i), "pressure"));
                data.setVisibility(getNullCheckDouble(getNullCheckObject(a, i), "visibility"));
                data.setDewPoint(getNullCheckDouble(getNullCheckObject(a, i), "dewPoint"));
                data.setCloudCover(getNullCheckDouble(getNullCheckObject(a, i), "cloudCover"));
                data.setWindSpeed(getNullCheckDouble(getNullCheckObject(a, i), "windSpeed"));
                data.setWindBearing(getNullCheckInt(getNullCheckObject(a, i), "windBearing"));
                dataArrayList.add(i, data);
            }
        }

        hourly.setData(dataArrayList);
    }

    private static void setDailyForecast(ForecastInfo info, JSONObject o) {
        ForecastInfo.Daily daily = info.getDaily();

        daily.setSummary(getNullCheckString(o, "summary"));
        daily.setIcon(getNullCheckString(o, "icon"));

        JSONArray a = getNullCheckDataArray(o, "data");
        ArrayList<ForecastInfo.Daily.Data> dataArrayList = new ArrayList<>();
        if(a != null) {
            for (int i = 0; i < a.length(); i++) {
                ForecastInfo.Daily.Data data = daily.getData().get(i);
                data.setTime(new Date(getNullCheckLong(getNullCheckObject(a, i), "time") * 1000));
                data.setSummary(getNullCheckString(getNullCheckObject(a, i), "summary"));
                data.setIcon(getNullCheckString(getNullCheckObject(a, i), "icon"));
                data.setPrecipType(getNullCheckString(getNullCheckObject(a, i), "precipType"));
                data.setTemperatureMin(getNullCheckDouble(getNullCheckObject(a, i), "temperatureMin"));
                data.setTemperatureMax(getNullCheckDouble(getNullCheckObject(a, i), "temperatureMax"));
                data.setApparentTemperatureMin(getNullCheckDouble(getNullCheckObject(a, i), "apparentTemperatureMin"));
                data.setApparentTemperatureMax(getNullCheckDouble(getNullCheckObject(a, i), "apparentTemperatureMax"));
                data.setPrecipProbability(getNullCheckDouble(getNullCheckObject(a, i), "precipProbability"));
                data.setPrecipIntensity(getNullCheckDouble(getNullCheckObject(a, i), "precipIntensity"));
                data.setPrecipAccumulation(getNullCheckDouble(getNullCheckObject(a, i), "precipAccumulation"));
                data.setHumidity(getNullCheckDouble(getNullCheckObject(a, i), "humidity"));
                data.setPressure(getNullCheckDouble(getNullCheckObject(a, i), "pressure"));
                data.setVisibility(getNullCheckDouble(getNullCheckObject(a, i), "visibility"));
                data.setDewPoint(getNullCheckDouble(getNullCheckObject(a, i), "dewPoint"));
                data.setCloudCover(getNullCheckDouble(getNullCheckObject(a, i), "cloudCover"));
                data.setWindSpeed(getNullCheckDouble(getNullCheckObject(a, i), "windSpeed"));
                data.setWindBearing(getNullCheckInt(getNullCheckObject(a, i), "windBearing"));
                dataArrayList.add(i, data);
            }
        }

        daily.setData(dataArrayList);
    }

    private static double getNullCheckDouble(JSONObject o, String value) {
        try {
            if(o.isNull(value)){
                return -1;
            } else {
                return o.getDouble(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return 0;
    }

    private static long getNullCheckLong(JSONObject o, String value) {
        try {
            if(o.isNull(value)){
                return -1;
            } else {
                return o.getLong(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return 0;
    }

    private static int getNullCheckInt(JSONObject o, String value) {
        try {
            if(o.isNull(value)){
                return -1;
            } else {
                return o.getInt(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return 0;
    }

    private static String getNullCheckString(JSONObject o, String value) {
        try {
            if(o.isNull(value)){
                return "NP";
            } else {
                return o.getString(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return null;
    }

    private static JSONArray getNullCheckDataArray(JSONObject o, String value) {
        try {
            if(o.isNull(value)){
                return null;
            } else {
                return o.getJSONArray(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return null;
    }

    private static JSONObject getNullCheckObject(JSONArray a, int index) {
        try {
            if(a.getJSONObject(index) == null){
                return null;
            } else {
                return a.getJSONObject(index);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return null;
    }

    private static JSONObject getNullCheckObject(JSONObject o, String value) {
        try {
            if(o.getJSONObject(value) == null){
                return null;
            } else {
                return o.getJSONObject(value);
            }
        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return null;
    }

}
