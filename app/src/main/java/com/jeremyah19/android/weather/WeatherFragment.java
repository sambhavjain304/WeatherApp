package com.jeremyah19.android.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WeatherFragment extends Fragment {

    public static final String TAG = "WeatherFragment";

    private static final String ARG_FORECAST_INFO = "forecastInfo";
    private static final String ARG_CITY = "city";

    private String mCity;

    private ForecastInfo mForecastInfo;

    public static WeatherFragment newInstance(ForecastInfo info, String city) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORECAST_INFO, info);
        args.putString(ARG_CITY, city);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mForecastInfo = (ForecastInfo) getArguments().getSerializable(ARG_FORECAST_INFO);
        mCity = getArguments().getString(ARG_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        CardView currentWeatherCardView = (CardView) v.findViewById(R.id.current_weather_card_view);
        CurrentWeatherView currentWeatherView =
                (CurrentWeatherView) v.findViewById(R.id.current_weather_view);

        if(mForecastInfo == null) {
            currentWeatherCardView.setVisibility(View.INVISIBLE);
        } else {
            currentWeatherView.setIconDrawable(
                    ForecastApiUtils.getIconDrawable(
                            getActivity(),
                            mForecastInfo.getCurrently().getIcon()
            ));

            currentWeatherView.setSummaryText(mForecastInfo.getCurrently().getSummary());
            currentWeatherView.setTemperatureText(getString(
                    R.string.temperature,
                    Math.round(mForecastInfo.getCurrently().getTemperature())));

            currentWeatherView.setApparentTemperatureText(getString(
                    R.string.apparent_temperature,
                    Math.round(mForecastInfo.getCurrently().getApparentTemperature())));

            currentWeatherView.setHumidityText(getString(
                    R.string.humidity,
                    Math.round(mForecastInfo.getCurrently().getHumidity() * 100)));
        }

        Button currentWeatherMoreDetailsButton = currentWeatherView.getMoreDetailsButton();
        currentWeatherMoreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WeatherDetailActivity.newIntent(
                        getActivity(),
                        mForecastInfo.getCurrently(),
                        mCity
                );
                startActivity(intent);
            }
        });

        return v;

    }
}
