package com.android.demo.weather;

import com.silencecork.imagesearch.ImageSearch;
import com.silencecork.imagesearch.ImageSearch.OnImageSearchCompleteListener;

import zh.wang.android.utils.YahooWeather4a.ConditionDefinition;
import zh.wang.android.utils.YahooWeather4a.WeatherInfo;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherInfoListener;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity implements YahooWeatherInfoListener, OnImageSearchCompleteListener {
	
	private Bitmap mCurrentBitmap;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View view = findViewById(R.id.refresh);
        view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getBackgrond();
			}
		});
        getWeather();
        getBackgrond();
    }

	@Override
    public void onDestroy() {
    	super.onDestroy();
    	if (mCurrentBitmap != null) {
    		mCurrentBitmap.recycle();
    	}
    }
    
    private void getWeather() {
    	YahooWeatherUtils.getInstance().queryYahooWeather(this, "Taipei", this);
    }
    
    private void getBackgrond() {
    	ImageSearch.getInstance().search(this, "Taipei", "Taipei,101", this);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_refresh) {
			getWeather();
		}
		return true;
	}
	
    private int getItemIcon(String condition) {
    	condition = condition.toLowerCase();
		if (condition.contains("sunny")) {
			return R.drawable.sun;
		} else if (condition.contains("snow")) { 
			return R.drawable.snow;
		} else if (condition.contains("hail")) { 
			return R.drawable.hail;
		} else if (condition.contains("dust")) { 
			return R.drawable.dust;
		} else if (condition.contains("rain")) {
			return R.drawable.rain;
		} else if (condition.contains("thunder")) {
			return R.drawable.thunder;
		} else if (condition.contains("storm")) {
			return R.drawable.storm;
		} else {
			return R.drawable.cloudy;
		}
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		Log.i("", "weather " + weatherInfo);
		String date = weatherInfo.getForecast1Date();
		int tempature = weatherInfo.getCurrentTempC();
		
		TextView text = (TextView) findViewById(R.id.weather_text);
		text.setText(date + "\n" + tempature + "\u2103");
		
		int code = weatherInfo.getCurrentCode();
		String condition = new ConditionDefinition().getConditionByCode(code);
		int icon = getItemIcon(condition);
		
		ImageView image = (ImageView) findViewById(R.id.weather_icon);
		image.setImageResource(icon);
	}

	@Override
	public void onComplete(Bitmap b) {
		if (b == null) {
			return;
		}
		ImageView image = (ImageView) findViewById(R.id.location_image);
		image.setImageBitmap(b);
		mCurrentBitmap = b;
	}

	@Override
	public void onError() {
		
	}
}