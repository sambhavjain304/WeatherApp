package com.jeremyah19.android.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class HourlyWeatherListFragment extends Fragment {
    public static final String TAG = "HWListFragment";

    private static final String ARG_FORECAST_INFO = "forecastInfo";
    private static final String ARG_CITY = "city";

    private String mCity;

    private ForecastInfo mForecastInfo;

    RecyclerView mRecyclerView;

    public static HourlyWeatherListFragment newInstance(ForecastInfo info, String city) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORECAST_INFO, info);
        args.putString(ARG_CITY, city);

        HourlyWeatherListFragment fragment = new HourlyWeatherListFragment();
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

        View v = inflater.inflate(R.layout.fragment_hourly_weather_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.hourly_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new HourlyWeatherAdapter(mForecastInfo.getHourly().getData()));

        return v;
    }

    private class HourlyWeatherHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mHourTextView;
        private TextView mSummaryTextView;
        private TextView mTemperatureTextView;

        private ImageView mIconImageView;

        private ForecastInfo.Hourly.Data mHourlyData;

        public HourlyWeatherHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mHourTextView = (TextView) itemView.findViewById(R.id.hourly_hour_text_view);
            mSummaryTextView = (TextView) itemView.findViewById(R.id.hourly_summary_text_view);
            mTemperatureTextView = (TextView) itemView.findViewById(R.id.hourly_temperature_text_view);

            mIconImageView = (ImageView) itemView.findViewById(R.id.hourly_icon_image_view);
        }

        public void bindHourlyData(ForecastInfo.Hourly.Data hourlyData) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hourlyData.getTime());

            mHourlyData = hourlyData;

            mHourTextView.setText(getString(
                    R.string.hour,
                    ForecastApiUtils.getHourString(calendar.get(Calendar.HOUR)),
                    ForecastApiUtils.getAmPmString(calendar.get(Calendar.AM_PM))
            ));
            mSummaryTextView.setText(mHourlyData.getSummary());
            mTemperatureTextView.setText(getString(
                    R.string.temperature,
                    Math.round(hourlyData.getTemperature())
            ));

            mIconImageView.setImageDrawable(
                    ForecastApiUtils.getIconDrawable(getActivity(), hourlyData.getIcon())
            );

        }

        @Override
        public void onClick(View v) {
            Intent intent = WeatherDetailActivity.newIntent(
                    getActivity(),
                    mHourlyData,
                    mCity
            );
            startActivity(intent);
        }
    }

    private class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherHolder> {

        private ArrayList<ForecastInfo.Hourly.Data> mHourlyData;

        public HourlyWeatherAdapter(ArrayList<ForecastInfo.Hourly.Data> hourlyData) {
            mHourlyData = hourlyData;
        }

        @Override
        public HourlyWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.hourly_weather_list_item, parent, false);

            return new HourlyWeatherHolder(v);
        }

        @Override
        public void onBindViewHolder(HourlyWeatherHolder holder, int position) {
            ForecastInfo.Hourly.Data data = mHourlyData.get(position);

            holder.bindHourlyData(data);
        }

        @Override
        public int getItemCount() {
            return mHourlyData.size();
        }
    }

}
