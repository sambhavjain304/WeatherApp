package com.jeremyah19.android.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentWeatherView extends LinearLayout {

    private String mSummaryText;
    private String mTemperatureText;
    private String mApparentTemperatureText;
    private String mHumidityText;

    private Drawable mIcon;

    private TextView mSummaryTextView;
    private TextView mTemperatureTextView;
    private TextView mApparentTemperatureTextView;
    private TextView mHumidityTextView;

    private Button mMoreDetailsButton;

    ImageView mIconImageView;

    public CurrentWeatherView(Context context) {
        super(context);
        init(context, null);
    }

    public CurrentWeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurrentWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CurrentWeatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public String getSummaryText() {
        return mSummaryText;
    }

    public void setSummaryText(String summaryText) {
        mSummaryText = summaryText;
        mSummaryTextView.setText(mSummaryText);
    }

    public String getTemperatureText() {
        return mTemperatureText;
    }

    public void setTemperatureText(String temperatureText) {
        mTemperatureText = temperatureText;
        mTemperatureTextView.setText(mTemperatureText);
    }

    public String getApparentTemperatureText() {
        return mApparentTemperatureText;
    }

    public void setApparentTemperatureText(String apparentTemperatureText) {
        mApparentTemperatureText = apparentTemperatureText;
        mApparentTemperatureTextView.setText(mApparentTemperatureText);
    }

    public String getHumidityText() {
        return mHumidityText;
    }

    public void setHumidityText(String humidityText) {
        mHumidityText = humidityText;
        mHumidityTextView.setText(mHumidityText);
    }

    public void setIconDrawable(Drawable icon) {
        mIcon = icon;
        mIconImageView.setImageDrawable(mIcon);
    }

    public Button getMoreDetailsButton() {
        return mMoreDetailsButton;
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.card_weather, this);

        mSummaryTextView = (TextView) rootView.findViewById(R.id.currently_summary_text_view);
        mTemperatureTextView = (TextView) rootView.findViewById(R.id.currently_temperature_text_view);
        mApparentTemperatureTextView = (TextView) rootView.findViewById(R.id.currently_apparent_temperature_text_view);
        mHumidityTextView = (TextView) rootView.findViewById(R.id.currently_humidity_text_view);
        mIconImageView = (ImageView) rootView.findViewById(R.id.currently_icon_image_view);
        mMoreDetailsButton = (Button) rootView.findViewById(R.id.currently_more_details_button);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CurrentWeatherView, 0, 0);
        if (ta != null) {
            mSummaryText = ta.getString(R.styleable.CurrentWeatherView_summaryText);
            mTemperatureText = ta.getString(R.styleable.CurrentWeatherView_temperatureText);
            mApparentTemperatureText = ta.getString(R.styleable.CurrentWeatherView_apparentTemperatureText);
            mHumidityText = ta.getString(R.styleable.CurrentWeatherView_humidityText);
            mIcon = ta.getDrawable(R.styleable.CurrentWeatherView_iconSrc);
            ta.recycle();

            mSummaryTextView.setText(mSummaryText);
            mTemperatureTextView.setText(mTemperatureText);
            mApparentTemperatureTextView.setText(mApparentTemperatureText);
            mHumidityTextView.setText(mHumidityText);
            mIconImageView.setImageDrawable(mIcon);
        }
    }
}
