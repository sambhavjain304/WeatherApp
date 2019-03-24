package com.jeremyah19.android.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class DailyWeatherListFragment extends Fragment {
    public static final String TAG = "DWListFragment";

    private static final String ARG_FORECAST_INFO = "forecastInfo";
    private static final String ARG_CITY = "city";

    private String mCity;

    private ForecastInfo mForecastInfo;

    RecyclerView mRecyclerView;

    public static DailyWeatherListFragment newInstance(ForecastInfo info, String city) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORECAST_INFO, info);
        args.putString(ARG_CITY, city);

        DailyWeatherListFragment fragment = new DailyWeatherListFragment();
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

        View v = inflater.inflate(R.layout.fragment_daily_weather_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.daily_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new DailyWeatherAdapter(mForecastInfo.getDaily().getData()));

        return v;
    }

    private String getMonthString(int month) {
        return Integer.toString(month + 1);
    }

    private class DailyWeatherHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mDateTextView;
        private TextView mDayTextView;
        private TextView mSummaryTextView;
        private TextView mTemperatureTextView;

        private ImageView mIconImageView;

        private ForecastInfo.Daily.Data mDailyData;

        public DailyWeatherHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mDateTextView = (TextView) itemView.findViewById(R.id.daily_date_text_view);
            mDayTextView = (TextView) itemView.findViewById(R.id.daily_day_text_view);
            mSummaryTextView = (TextView) itemView.findViewById(R.id.daily_summary_text_view);
            mTemperatureTextView = (TextView) itemView.findViewById(R.id.daily_temperature_text_view);

            mIconImageView = (ImageView) itemView.findViewById(R.id.daily_icon_image_view);
        }

        public void bindDailyData(ForecastInfo.Daily.Data dailyData) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dailyData.getTime());

            mDailyData = dailyData;

            mDateTextView.setText(getString(
                    R.string.date,
                    getMonthString(calendar.get(Calendar.MONTH)),
                    Integer.toString(calendar.get(Calendar.DATE))));

            mDayTextView.setText((ForecastApiUtils.getDayOfWeekShortString(calendar.get(Calendar.DAY_OF_WEEK))));

            mSummaryTextView.setText(mDailyData.getSummary());

            mTemperatureTextView.setText(getString(
                    R.string.temperature_hi_low,
                    Math.round(mDailyData.getTemperatureMax()),
                    Math.round(mDailyData.getTemperatureMin())
                    ));

            mIconImageView.setImageDrawable(
                    ForecastApiUtils.getIconDrawable(getActivity(), dailyData.getIcon())
            );

        }

        @Override
        public void onClick(View v) {
            Intent intent = WeatherDetailActivity.newIntent(
                    getActivity(),
                    mDailyData,
                    mCity
            );
            startActivity(intent);
        }
    }

    private class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherHolder> {

        private ArrayList<ForecastInfo.Daily.Data> mDailyData;

        public DailyWeatherAdapter(ArrayList<ForecastInfo.Daily.Data> dailyData) {
            mDailyData = dailyData;
        }

        @Override
        public DailyWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.daily_weather_list_item, parent, false);

            return new DailyWeatherHolder(v);
        }

        @Override
        public void onBindViewHolder(DailyWeatherHolder holder, int position) {
            ForecastInfo.Daily.Data data = mDailyData.get(position);

            holder.bindDailyData(data);
        }

        @Override
        public int getItemCount() {
            return mDailyData.size();
        }
    }

}
