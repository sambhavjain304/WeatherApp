package com.jeremyah19.android.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentWeatherDetailFragment extends Fragment {
    public static final String TAG = "CWDetailFragment";

    public static CurrentWeatherDetailFragment newInstance(ForecastInfo.Currently currently) {
        Bundle args = new Bundle();

        args.putString(ForecastApiUtils.SUMMARY, currently.getSummary());
        args.putString(ForecastApiUtils.ICON, currently.getIcon());
        args.putString(ForecastApiUtils.PRECIP_TYPE, currently.getPrecipType());
        args.putDouble(ForecastApiUtils.TEMPERATURE, currently.getTemperature());
        args.putDouble(ForecastApiUtils.APPARENT_TEMPERATURE, currently.getApparentTemperature());
        args.putDouble(ForecastApiUtils.HUMIDITY, currently.getHumidity());
        args.putDouble(ForecastApiUtils.WIND_SPEED, currently.getWindSpeed());
        args.putInt(ForecastApiUtils.WIND_BEARING, currently.getWindBearing());
        args.putDouble(ForecastApiUtils.VISIBILITY, currently.getVisibility());
        args.putDouble(ForecastApiUtils.DEW_POINT, currently.getDewPoint());
        args.putDouble(ForecastApiUtils.CLOUD_COVER, currently.getCloudCover());
        args.putDouble(ForecastApiUtils.PRESSURE, currently.getPressure());
        args.putDouble(ForecastApiUtils.PRECIP_PROB, currently.getPrecipProbability());
        args.putDouble(ForecastApiUtils.PRECIP_INTENSITY, currently.getPrecipIntensity());

        CurrentWeatherDetailFragment fragment = new CurrentWeatherDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(getString(R.string.title_current_conditions));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather_detail, container, false);

        String precipType = getArguments().getString(ForecastApiUtils.PRECIP_TYPE);

        TextView summaryTextView = (TextView) v.findViewById(R.id.details_summary_text_view);
        summaryTextView.setText(getArguments().getString(ForecastApiUtils.SUMMARY));

        ImageView iconImageView = (ImageView) v.findViewById(R.id.details_icon_image_view);
        iconImageView.setImageDrawable(ForecastApiUtils.getIconDrawable(getActivity(), getArguments().getString(ForecastApiUtils.ICON)));

        TextView windBearingTextView = (TextView) v.findViewById(R.id.details_wind_bearing_text_view);
        if(getArguments().getDouble(ForecastApiUtils.WIND_SPEED) == 0) {
            windBearingTextView.setText(getString(R.string.not_applicable));
        } else {
            windBearingTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.WIND_BEARING, getArguments()));
        }

        TextView precipProbTitleTextView = (TextView) v.findViewById(R.id.details_precip_probabability_title_text_view);
        precipProbTitleTextView.setText(getString(R.string.title_precipitation_probability, ForecastApiUtils.getPrecipTypeTitle(precipType)));

        TextView precipIntensityTitleTextView = (TextView) v.findViewById(R.id.details_precip_intensity_title_text_view);
        precipIntensityTitleTextView.setText(getString(R.string.title_precipitation_intensity, ForecastApiUtils.getPrecipTypeTitle(precipType)));

        TextView temperatureTextView = (TextView) v.findViewById(R.id.details_temperature_text_view);
        temperatureTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.TEMPERATURE, getArguments()));

        TextView apparentTemperatureTextView = (TextView) v.findViewById(R.id.details_apparent_temperature_text_view);
        apparentTemperatureTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.APPARENT_TEMPERATURE, getArguments()));

        TextView humidityTextView = (TextView) v.findViewById(R.id.details_humidity_text_view);
        humidityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.HUMIDITY, getArguments()));

        TextView precipProbTextView = (TextView) v.findViewById(R.id.details_precip_probabability_text_view);
        precipProbTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_PROB, getArguments()));

        TextView precipIntensityTextView = (TextView) v.findViewById(R.id.details_precip_intensity_text_view);
        precipIntensityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_INTENSITY, getArguments()));
        if(getArguments().getDouble(ForecastApiUtils.PRECIP_PROB) == 0) {
            precipIntensityTextView.setVisibility(View.GONE);
            precipIntensityTitleTextView.setVisibility(View.GONE);
        }

        TextView windSpeedTextView = (TextView) v.findViewById(R.id.details_wind_speed_text_view);
        windSpeedTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.WIND_SPEED, getArguments()));

        TextView visibilityTextView = (TextView) v.findViewById(R.id.details_visibility_text_view);
        visibilityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.VISIBILITY, getArguments()));

        TextView dewPointTextView = (TextView) v.findViewById(R.id.details_dew_point_text_view);
        dewPointTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.DEW_POINT, getArguments()));

        TextView cloudCoverTextView = (TextView) v.findViewById(R.id.details_cloud_cover_text_view);
        cloudCoverTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.CLOUD_COVER, getArguments()));

        TextView pressureTextView = (TextView) v.findViewById(R.id.details_pressure_text_view);
        pressureTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRESSURE, getArguments()));

        return v;
    }

}
