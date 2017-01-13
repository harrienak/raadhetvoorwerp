package com.geckoapps.raadhetvoorwerp.classes;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geckoapps.raadhetvoorwerp.activity.ShopActivity;

import java.util.Timer;
import java.util.TimerTask;

public class BovenBalk {
	private SharedPreferences settings;
	private Context context;
	private Button back, money;
	private TextView title;
	private Typeface typeface;

	public BovenBalk(SharedPreferences settings, Context context, Button back,
			Button money, TextView textView) {
		super();
		typeface = Typeface.createFromAsset(context.getAssets(),
				"font/BRLNSDB.TTF");
		this.settings = settings;
		this.context = context;
		this.back = back;
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		this.money = money;
		this.money.setTypeface(typeface);
		money.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openShop();
			}
		});
		this.title = textView;
		this.title.setTypeface(typeface);
		setTitle();
		setCoins();
	}

	public void setTitle(){
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				title.setText("Level " +  settings.getInt("currentLevel", 1) );
			}
		});
	}

	public void setCoins(){
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				if(!settings.getBoolean("oneindig", false)){
					money.setText("" +  settings.getInt("coins", 0) );
				}
				else{
					money.setText("Oneindig");
				}
			}
		});
	}
		public void setCoins(int d) {
			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					if(!settings.getBoolean("oneindig", false)){
						money.setText("" + numberOfCoins);
					}
					else{
						money.setText("Oneindig");
					}
				}
			});
		}

		int numberOfCoins = 0;
		public void addCoins(int i) {
			numberOfCoins = settings.getInt("coins", 0);
			Editor editor = settings.edit();
			editor.putInt("coins", (settings.getInt("coins", 0) + i));
			editor.commit();
			if (i < 0)
				updateCoinsDisplayMin();
			else
				updateCoinsDisplay();
		}

		private void updateCoinsDisplayMin() {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (numberOfCoins > settings.getInt("coins", 0)) {
						numberOfCoins--;
						setCoins(numberOfCoins);
					}
				}
			}, 200, 100);// Update text every second
		}

		private void updateCoinsDisplay() {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (numberOfCoins < settings.getInt("coins", 0)) {
						numberOfCoins++;
						setCoins(numberOfCoins);
					}
				}
			}, 200, 100);// Update text every second
		}
	
	public void openShop(){
		Intent i = new Intent(context, ShopActivity.class);
		context.startActivity(i);
	}
	
	private void close(){
		((Activity) context).finish();
	}
	
	public void cancelBack() {
		back.setVisibility(Button.INVISIBLE);
	}
	public void setCoinsNotClickable() {
		money.setClickable(false);
		
	}
	
	public void showToastMsg(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public int getMaxLevel(){
			return 2525;

	}
	

}









