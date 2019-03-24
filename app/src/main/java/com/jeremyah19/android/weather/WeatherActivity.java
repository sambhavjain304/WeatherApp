package com.jeremyah19.android.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WeatherActivity extends AppCompatActivity {
    public static final String TAG = "WeatherActivity";

    private static final int REQUEST_PERMISSIONS = 1;

    private String mCity;

    private Location mLocation;

    private ForecastInfo mForecastInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mLocation == null) {
            processCurrentLocationForecast();
        }

        if(mForecastInfo == null) {
            setContentView(R.layout.activity_launch);
        } else {
            init();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GeocodingTask().execute(query.replace(" ", "+"));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    public void processCurrentLocationForecast() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int fineLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        );

        int coarseLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);

        }

        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (mLocation != null) {
            Log.d(TAG, "Location Latitude: " + mLocation.getLatitude());
            new ReverseGeocodingTask().execute(mLocation.getLatitude(), mLocation.getLongitude());
        }
    }

    private void init() {
        setContentView(R.layout.weather_activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mCity);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.toolbar_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_now));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_hourly));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_daily));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_view_pager);
        final WeatherPagerAdapter adapter =
                new WeatherPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public class WeatherPagerAdapter extends FragmentStatePagerAdapter {
        private int mNumberOfTabs;

        public WeatherPagerAdapter(FragmentManager fm, int numberOfTabs) {
            super(fm);
            mNumberOfTabs = numberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WeatherFragment.newInstance(mForecastInfo, mCity);
                case 1:
                    return HourlyWeatherListFragment.newInstance(mForecastInfo, mCity);
                case 2:
                    return DailyWeatherListFragment.newInstance(mForecastInfo, mCity);
            }
            return null;
        }

        @Override
        public int getCount() {
            return mNumberOfTabs;
        }
    }

    private class GeocodingTask extends AsyncTask<String, Void, GeocodingInfo> {
        @Override
        protected GeocodingInfo doInBackground(String... params) {
            return GeocodingUtils.getGeocodingInfo(params[0], 0, 0);
        }

        @Override
        protected void onPostExecute(GeocodingInfo geocodingInfo) {
            mCity = geocodingInfo.getCityName();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mCity);
            }

            new FetchWeatherTask().execute(
                    geocodingInfo.getLatitude(),
                    geocodingInfo.getLongitude()
            );
        }
    }

    private class ReverseGeocodingTask extends AsyncTask<Double, Void, GeocodingInfo> {
        @Override
        protected GeocodingInfo doInBackground(Double... params) {
            return GeocodingUtils.getGeocodingInfo(null, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(GeocodingInfo geocodingInfo) {
            mCity = geocodingInfo.getCityName();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mCity);
            }

            new FetchWeatherTask().execute(
                    geocodingInfo.getLatitude(),
                    geocodingInfo.getLongitude()
            );
        }
    }

    private class FetchWeatherTask extends AsyncTask<Double, Void, ForecastInfo> {
        @Override
        protected ForecastInfo doInBackground(Double... params) {
            return ForecastApiUtils.getForecastInfo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(ForecastInfo forecastInfo) {
            mForecastInfo = forecastInfo;
            init();
        }
    }
}
