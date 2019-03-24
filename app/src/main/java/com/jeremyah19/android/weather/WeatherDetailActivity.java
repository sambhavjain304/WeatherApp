package com.jeremyah19.android.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class WeatherDetailActivity extends SingleFragmentActivity {
    public static final String TAG = "WeatherDetailActivity";

    private static final int CURRENTLY = 0;
    private static final int HOURLY = 1;
    private static final int DAILY = 2;

    private static final String EXTRA_CITY = "com.jeremyah19.android.weather.city";
    private static final String EXTRA_DATA = "com.jeremyah19.android.weather.data";
    private static final String EXTRA_TIME_PERIOD = "com.jeremyah19.android.weather.timePeriod";

    private String mCity;

    public static Intent newIntent(Context packageContext, ForecastInfo.Currently currently, String city) {
        Intent intent = new Intent(packageContext, WeatherDetailActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        intent.putExtra(EXTRA_DATA, currently);
        intent.putExtra(EXTRA_TIME_PERIOD, CURRENTLY);

        return intent;
    }

    public static Intent newIntent(Context packageContext, ForecastInfo.Hourly.Data data, String city) {
        Intent intent = new Intent(packageContext, WeatherDetailActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        intent.putExtra(EXTRA_DATA, data);
        intent.putExtra(EXTRA_TIME_PERIOD, HOURLY);

        return intent;
    }

    public static Intent newIntent(Context packageContext, ForecastInfo.Daily.Data data, String city) {
        Intent intent = new Intent(packageContext, WeatherDetailActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        intent.putExtra(EXTRA_DATA, data);
        intent.putExtra(EXTRA_TIME_PERIOD, DAILY);

        return intent;
    }

    @Override
    protected Fragment createFragment() {

        mCity = getIntent().getStringExtra(EXTRA_CITY);

        switch(getIntent().getIntExtra(EXTRA_TIME_PERIOD, -1)) {
            case CURRENTLY:
                ForecastInfo.Currently currently = (ForecastInfo.Currently) getIntent().getSerializableExtra(EXTRA_DATA);
                return CurrentWeatherDetailFragment.newInstance(currently);
            case HOURLY:
                ForecastInfo.Hourly.Data hourlydata = (ForecastInfo.Hourly.Data) getIntent().getSerializableExtra(EXTRA_DATA);
                return HourlyWeatherDetailFragment.newInstance(hourlydata);
            case DAILY:
                ForecastInfo.Daily.Data dailydata = (ForecastInfo.Daily.Data) getIntent().getSerializableExtra(EXTRA_DATA);
                return DailyWeatherDetailFragment.newInstance(dailydata);
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mCity);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }
}
