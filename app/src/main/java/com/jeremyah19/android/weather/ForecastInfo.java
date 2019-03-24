package com.jeremyah19.android.weather;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

// Class representation of JSON forecast data
public class ForecastInfo implements Serializable{
    public static final String TAG = "ForecastInfo";

    public static final int CURRENTLY = 0;

    private static final int SIZE_MINUTELY_DATA = 61;
    private static final int SIZE_HOURLY_DATA = 49;
    private static final int SIZE_DAILY_DATA = 8;

    private static ForecastInfo mForecastInfo;

    private boolean mHasMinutelyData;

    private double  mOffset;

    private String mTimezone;

    private Currently mCurrently;
    private Minutely mMinutely;
    private Hourly mHourly;
    private Daily mDaily;

    public static ForecastInfo getInstance() {
        if(mForecastInfo == null) {
            mForecastInfo = new ForecastInfo();
        }
        return mForecastInfo;
    }

    public boolean hasMinutelyData() {
        return mHasMinutelyData;
    }

    public void setHasMinutelyData(boolean hasMinutelyData) {
        mHasMinutelyData = hasMinutelyData;
    }

    public double getOffset() {
        return mOffset;
    }

    public void setOffset(double offset) {
        mOffset = offset;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public Currently getCurrently() {
        return mCurrently;
    }

    public Minutely getMinutely() {
        return mMinutely;
    }

    public Hourly getHourly() {
        return mHourly;
    }

    public Daily getDaily() {
        return mDaily;
    }

    private ForecastInfo() {
        mCurrently = new Currently();
        mMinutely = new Minutely();
        mHourly = new Hourly();
        mDaily = new Daily();
    }

    public class Currently implements Serializable{
        private double mPrecipIntensity;
        private double mPrecipProbability;
        private double mTemperature;
        private double mApparentTemperature;
        private double mHumidity;
        private double mDewPoint;
        private double mPressure;
        private double mWindSpeed;
        private double mCloudCover;
        private double mVisibility;

        private int mWindBearing;

        private String mSummary;
        private String mIcon;
        private String mPrecipType;

        private Date mTime;

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public double getPrecipIntensity() {
            return mPrecipIntensity;
        }

        public void setPrecipIntensity(double precipIntensity) {
            mPrecipIntensity = precipIntensity;
        }

        public double getPrecipProbability() {
            return mPrecipProbability;
        }

        public void setPrecipProbability(double precipProbability) {
            mPrecipProbability = precipProbability;
        }

        public double getTemperature() {
            return mTemperature;
        }

        public void setTemperature(double temperature) {
            mTemperature = temperature;
        }

        public double getApparentTemperature() {
            return mApparentTemperature;
        }

        public void setApparentTemperature(double apparentTemperature) {
            mApparentTemperature = apparentTemperature;
        }

        public double getHumidity() {
            return mHumidity;
        }

        public void setHumidity(double humidity) {
            mHumidity = humidity;
        }

        public double getDewPoint() {
            return mDewPoint;
        }

        public void setDewPoint(double dewPoint) {
            mDewPoint = dewPoint;
        }

        public double getPressure() {
            return mPressure;
        }

        public void setPressure(double pressure) {
            mPressure = pressure * 0.02953;
        }

        public double getWindSpeed() {
            return mWindSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            mWindSpeed = windSpeed;
        }

        public double getCloudCover() {
            return mCloudCover;
        }

        public void setCloudCover(double cloudCover) {
            mCloudCover = cloudCover;
        }

        public double getVisibility() {
            return mVisibility;
        }

        public void setVisibility(double visibility) {
            mVisibility = visibility;
        }

        public int getWindBearing() {
            return mWindBearing;
        }

        public void setWindBearing(int windBearing) {
            mWindBearing = windBearing;
        }

        public String getPrecipType() {
            return mPrecipType;
        }

        public void setPrecipType(String precipType) {
            mPrecipType = precipType;
        }

        public Date getTime() {
            return mTime;
        }

        public void setTime(Date time) {
            mTime = time;
        }
    }

    public class Minutely implements Serializable{
        private String mSummary;
        private String mIcon;

        private ArrayList<Data> mData;

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public ArrayList<Data> getData() {
            return mData;
        }

        public void setData(ArrayList<Data> data) {
            mData = data;
        }

        private Minutely() {
            mData = new ArrayList<>();
            for(int i = 0; i < SIZE_MINUTELY_DATA; i++) {
                mData.add(i, new Data());
            }
        }

        public class Data implements Serializable{
            private double mPrecipIntensity;
            private double mPrecipProbability;

            private Date mTime;

            public double getPrecipIntensity() {
                return mPrecipIntensity;
            }

            public void setPrecipIntensity(double precipIntensity) {
                mPrecipIntensity = precipIntensity;
            }

            public double getPrecipProbability() {
                return mPrecipProbability;
            }

            public void setPrecipProbability(double precipProbability) {
                mPrecipProbability = precipProbability;
            }

            public Date getTime() {
                return mTime;
            }

            public void setTime(Date time) {
                mTime = time;
            }
        }
    }

    public class Hourly implements Serializable{
        private String mSummary;
        private String mIcon;

        private ArrayList<Data> mData;

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public ArrayList<Data> getData() {
            return mData;
        }

        public void setData(ArrayList<Data> data) {
            mData = data;
        }

        private Hourly() {
            mData = new ArrayList<>();
            for(int i = 0; i < SIZE_HOURLY_DATA; i++) {
                mData.add(i, new Data());
            }
        }

        public class Data implements Serializable{
            private double mPrecipIntensity;
            private double mPrecipProbability;
            private double mPrecipAccumulation;
            private double mTemperature;
            private double mApparentTemperature;
            private double mHumidity;
            private double mDewPoint;
            private double mPressure;
            private double mWindSpeed;
            private double mCloudCover;
            private double mVisibility;

            private int mWindBearing;

            private String mSummary;
            private String mIcon;
            private String mPrecipType;

            private Date mTime;

            public double getPrecipIntensity() {
                return mPrecipIntensity;
            }

            public void setPrecipIntensity(double precipIntensity) {
                mPrecipIntensity = precipIntensity;
            }

            public double getPrecipProbability() {
                return mPrecipProbability;
            }

            public void setPrecipProbability(double precipProbability) {
                mPrecipProbability = precipProbability;
            }

            public double getPrecipAccumulation() {
                return mPrecipAccumulation;
            }

            public void setPrecipAccumulation(double precipAccumulation) {
                mPrecipAccumulation = precipAccumulation;
            }

            public double getTemperature() {
                return mTemperature;
            }

            public void setTemperature(double temperature) {
                mTemperature = temperature;
            }

            public double getApparentTemperature() {
                return mApparentTemperature;
            }

            public void setApparentTemperature(double apparentTemperature) {
                mApparentTemperature = apparentTemperature;
            }

            public double getHumidity() {
                return mHumidity;
            }

            public void setHumidity(double humidity) {
                mHumidity = humidity;
            }

            public double getDewPoint() {
                return mDewPoint;
            }

            public void setDewPoint(double dewPoint) {
                mDewPoint = dewPoint;
            }

            public double getPressure() {
                return mPressure;
            }

            public void setPressure(double pressure) {
                mPressure = pressure * 0.02953;
            }

            public double getWindSpeed() {
                return mWindSpeed;
            }

            public void setWindSpeed(double windSpeed) {
                mWindSpeed = windSpeed;
            }

            public double getCloudCover() {
                return mCloudCover;
            }

            public void setCloudCover(double cloudCover) {
                mCloudCover = cloudCover;
            }

            public double getVisibility() {
                return mVisibility;
            }

            public void setVisibility(double visibility) {
                mVisibility = visibility;
            }

            public int getWindBearing() {
                return mWindBearing;
            }

            public void setWindBearing(int windBearing) {
                mWindBearing = windBearing;
            }

            public String getSummary() {
                return mSummary;
            }

            public void setSummary(String summary) {
                mSummary = summary;
            }

            public String getIcon() {
                return mIcon;
            }

            public void setIcon(String icon) {
                mIcon = icon;
            }

            public String getPrecipType() {
                return mPrecipType;
            }

            public void setPrecipType(String precipType) {
                mPrecipType = precipType;
            }

            public Date getTime() {
                return mTime;
            }

            public void setTime(Date time) {
                mTime = time;
            }
        }
    }

    public class Daily implements Serializable{
        private String mSummary;
        private String mIcon;

        private ArrayList<Data> mData;

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public ArrayList<Data> getData() {
            return mData;
        }

        public void setData(ArrayList<Data> data) {
            mData = data;
        }

        private Daily() {
            mData = new ArrayList<>();
            for(int i = 0; i < SIZE_DAILY_DATA; i++) {
                mData.add(i, new Data());
            }
        }

        public class Data implements Serializable{
            private double mPrecipIntensity;
            private double mPrecipProbability;
            private double mPrecipAccumulation;
            private double mTemperatureMin;
            private double mTemperatureMax;
            private double mApparentTemperatureMin;
            private double mApparentTemperatureMax;
            private double mHumidity;
            private double mDewPoint;
            private double mPressure;
            private double mWindSpeed;
            private double mCloudCover;
            private double mVisibility;

            private int mWindBearing;

            private String mSummary;
            private String mIcon;
            private String mPrecipType;

            private Date mTime;

            public double getPrecipIntensity() {
                return mPrecipIntensity;
            }

            public void setPrecipIntensity(double precipIntensity) {
                mPrecipIntensity = precipIntensity;
            }

            public double getPrecipProbability() {
                return mPrecipProbability;
            }

            public void setPrecipProbability(double precipProbability) {
                mPrecipProbability = precipProbability;
            }

            public double getPrecipAccumulation() {
                return mPrecipAccumulation;
            }

            public void setPrecipAccumulation(double precipAccumulation) {
                mPrecipAccumulation = precipAccumulation;
            }

            public double getTemperatureMin() {
                return mTemperatureMin;
            }

            public void setTemperatureMin(double temperatureMin) {
                mTemperatureMin = temperatureMin;
            }

            public double getTemperatureMax() {
                return mTemperatureMax;
            }

            public void setTemperatureMax(double temperatureMax) {
                mTemperatureMax = temperatureMax;
            }

            public double getApparentTemperatureMin() {
                return mApparentTemperatureMin;
            }

            public void setApparentTemperatureMin(double apparentTemperatureMin) {
                mApparentTemperatureMin = apparentTemperatureMin;
            }

            public double getApparentTemperatureMax() {
                return mApparentTemperatureMax;
            }

            public void setApparentTemperatureMax(double apparentTemperatureMax) {
                mApparentTemperatureMax = apparentTemperatureMax;
            }

            public double getHumidity() {
                return mHumidity;
            }

            public void setHumidity(double humidity) {
                mHumidity = humidity;
            }

            public double getDewPoint() {
                return mDewPoint;
            }

            public void setDewPoint(double dewPoint) {
                mDewPoint = dewPoint;
            }

            public double getPressure() {
                return mPressure;
            }

            public void setPressure(double pressure) {
                mPressure = pressure * 0.02953;
            }

            public double getWindSpeed() {
                return mWindSpeed;
            }

            public void setWindSpeed(double windSpeed) {
                mWindSpeed = windSpeed;
            }

            public double getCloudCover() {
                return mCloudCover;
            }

            public void setCloudCover(double cloudCover) {
                mCloudCover = cloudCover;
            }

            public double getVisibility() {
                return mVisibility;
            }

            public void setVisibility(double visibility) {
                mVisibility = visibility;
            }

            public int getWindBearing() {
                return mWindBearing;
            }

            public void setWindBearing(int windBearing) {
                mWindBearing = windBearing;
            }

            public String getSummary() {
                return mSummary;
            }

            public void setSummary(String summary) {
                mSummary = summary;
            }

            public String getIcon() {
                return mIcon;
            }

            public void setIcon(String icon) {
                mIcon = icon;
            }

            public String getPrecipType() {
                return mPrecipType;
            }

            public void setPrecipType(String precipType) {
                mPrecipType = precipType;
            }

            public Date getTime() {
                return mTime;
            }

            public void setTime(Date time) {
                mTime = time;
            }
        }
    }
}
