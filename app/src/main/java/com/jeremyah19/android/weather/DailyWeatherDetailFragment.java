package com.jeremyah19.android.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DailyWeatherDetailFragment extends Fragment {

    public static final String TAG = "DWDetailFragment";

    public static DailyWeatherDetailFragment newInstance(ForecastInfo.Daily.Data data) {
        Bundle args = new Bundle();

        args.putSerializable(ForecastApiUtils.TIME, data.getTime());
        args.putString(ForecastApiUtils.SUMMARY, data.getSummary());
        args.putString(ForecastApiUtils.ICON, data.getIcon());
        args.putString(ForecastApiUtils.PRECIP_TYPE, data.getPrecipType());
        args.putDouble(ForecastApiUtils.TEMPERATURE_MIN, data.getTemperatureMin());
        args.putDouble(ForecastApiUtils.TEMPERATURE_MAX, data.getTemperatureMax());
        args.putDouble(ForecastApiUtils.APPARENT_TEMPERATURE_MIN, data.getApparentTemperatureMin());
        args.putDouble(ForecastApiUtils.APPARENT_TEMPERATURE_MAX, data.getApparentTemperatureMax());
        args.putDouble(ForecastApiUtils.HUMIDITY, data.getHumidity());
        args.putDouble(ForecastApiUtils.WIND_SPEED, data.getWindSpeed());
        args.putInt(ForecastApiUtils.WIND_BEARING, data.getWindBearing());
        args.putDouble(ForecastApiUtils.VISIBILITY, data.getVisibility());
        args.putDouble(ForecastApiUtils.DEW_POINT, data.getDewPoint());
        args.putDouble(ForecastApiUtils.CLOUD_COVER, data.getCloudCover());
        args.putDouble(ForecastApiUtils.PRESSURE, data.getPressure());
        args.putDouble(ForecastApiUtils.PRECIP_PROB, data.getPrecipProbability());
        args.putDouble(ForecastApiUtils.PRECIP_INTENSITY, data.getPrecipIntensity());
        args.putDouble(ForecastApiUtils.PRECIP_ACCUMULATION, data.getPrecipAccumulation());

        DailyWeatherDetailFragment fragment = new DailyWeatherDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) getArguments().getSerializable(ForecastApiUtils.TIME));
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setSubtitle(getString(
                            R.string.date_long,
                            ForecastApiUtils.getDayOfWeekLongString(calendar.get(Calendar.DAY_OF_WEEK)),
                            ForecastApiUtils.getMonthString(calendar.get(Calendar.MONTH)),
                            calendar.get(Calendar.DATE))
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather_detail, container, false);

        String precipType = getArguments().getString(ForecastApiUtils.PRECIP_TYPE);

        TextView summaryTextView = (TextView) v.findViewById(R.id.details_summary_text_view);
        final String summary = getArguments().getString(ForecastApiUtils.SUMMARY);
        summaryTextView.setText(summary);
        summaryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast summaryToast = Toast.makeText(getActivity(), summary, Toast.LENGTH_LONG);
                summaryToast.setGravity(Gravity.TOP, 0, 250);
                summaryToast.show();
            }
        });

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

        TextView temperatureTextView = (TextView) v.findViewById(R.id.details_temperature_text_view);
        temperatureTextView.setText((getString(
                R.string.temperature_hi_low_2,
                ForecastApiUtils.getValueString(ForecastApiUtils.TEMPERATURE_MAX, getArguments()),
                ForecastApiUtils.getValueString(ForecastApiUtils.TEMPERATURE_MIN, getArguments())
        )));

        TextView apparentTemperatureTextView = (TextView) v.findViewById(R.id.details_apparent_temperature_text_view);
        apparentTemperatureTextView.setText((getString(
                R.string.apparent_temperature_hi_low,
                ForecastApiUtils.getValueString(ForecastApiUtils.APPARENT_TEMPERATURE_MAX, getArguments()),
                ForecastApiUtils.getValueString(ForecastApiUtils.APPARENT_TEMPERATURE_MIN, getArguments())
        )));

        TextView humidityTextView = (TextView) v.findViewById(R.id.details_humidity_text_view);
        humidityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.HUMIDITY, getArguments()));

        TextView precipProbTextView = (TextView) v.findViewById(R.id.details_precip_probabability_text_view);
        precipProbTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_PROB, getArguments()));

        String precipAccumulation = ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_ACCUMULATION, getArguments());
        TextView precipIntensityTextView = (TextView) v.findViewById(R.id.details_precip_intensity_text_view);
        if(precipAccumulation != null && precipAccumulation.equals(ForecastApiUtils.NOT_PROVIDED)) {
            precipIntensityTitleTextView.setText(getString(R.string.title_precipitation_intensity, ForecastApiUtils.getPrecipTypeTitle(precipType)));
            precipIntensityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_INTENSITY, getArguments()));
        } else {
            precipIntensityTitleTextView.setText(getString(R.string.title_precipitation_accumulation, ForecastApiUtils.getPrecipTypeTitle(precipType)));
            precipIntensityTextView.setText(ForecastApiUtils.getValueString(ForecastApiUtils.PRECIP_ACCUMULATION, getArguments()));
        }

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
