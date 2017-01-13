package com.geckoapps.raadhetvoorwerp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geckoapps.raadhetvoorwerp.R;
import com.geckoapps.raadhetvoorwerp.classes.BovenBalk;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TapjoyConnectNotifier;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;
import com.tapjoy.TapjoyViewNotifier;

import java.util.Hashtable;

public class PopUpActivity extends Activity implements TapjoyNotifier,
TapjoySpendPointsNotifier, TapjoyEarnedPointsNotifier, TapjoyViewNotifier{
	private BovenBalk bovenbalk;
	private SharedPreferences settings;
	private ImageView text;
	private Button button, button2, next;
	private String popup;
	/*private Tracker tracker;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);
		
		Hashtable<String,Object> connectFlags = new Hashtable<String,Object>();
		connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true"); 
		//If you are not using Tapjoy Managed currency, you would set your own user ID here. 
		//connectFlags.put(TapjoyConnectFlag.TJC_OPTION_USER_ID, "A_UNIQUE_USER_ID");
		TapjoyConnect.requestTapjoyConnect(getApplicationContext(),
		     "fd08bc5e-f1ca-49ea-8020-205323c52345",
		     "JUSrhNHlzQey2BeOeSv5",
		     connectFlags,
		     new TapjoyConnectNotifier() {
		          @Override public void connectSuccess() {
		               //The Connect call succeeded 
		               Log.i("tapjoy", "The Connect call succeeded ");
		          }
		          @Override public void connectFail() {
		               //The connect call failed 
		               Log.i("tapjoy", "The connect call failed");
		          }
		});
		
		settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		/*tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-57548145-3");*/
		setViews();
		
		popup = getIntent().getStringExtra("popup");
		
		sentAnalytics(popup);
		
		setTekstAndButton();
	}
	
	private void setTekstAndButton() {
		button2.setVisibility(Button.GONE);
		
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			    finish();
			}
		});
		
		if(popup.equals("rate")){
			text.setImageResource(R.drawable.tekst_rate);
			button.setBackgroundResource(R.drawable.rate_button);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					 /*tracker.send(new HitBuilders.EventBuilder()
			            .setCategory("Button")
			            .setAction("Rate")
			            .setLabel("level:")
			            .build());*/
					
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.geckoapps.raadhetvoorwerp"));
					startActivity(browserIntent);	
					finish();
				}
			});
		}
		else if(popup.equals("fb")){
			text.setImageResource(R.drawable.tekst_fblike);
			button.setBackgroundResource(R.drawable.likeus_button);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					 /*tracker.send(new HitBuilders.EventBuilder()
			            .setCategory("Button")
			            .setAction("Like")
			            .setLabel("level")
			            .build());*/
					bovenbalk.addCoins(75);
					Editor edit = settings.edit();
					edit.putBoolean("fblike", false);
					edit.commit();
					
					open();
				}
			});
		}
		else if(popup.equals("geengeld")){
			text.setImageResource(R.drawable.tekst_geenmunten);
			button.setBackgroundResource(R.drawable.shopfree_button);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
							openOfferWall();				
						    finish();
						}
			});
			button2.setVisibility(Button.VISIBLE);
			button2.setBackgroundResource(R.drawable.koopmunten_button);
			button2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
							bovenbalk.openShop();				
						    finish();
						}
			});
		}
		else if(popup.equals("drempel")){
			button.setVisibility(Button.GONE);
			text.setImageResource(R.drawable.tekst_drempel);
			next.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					if(settings.getBoolean("oneindig", false)){
						Editor edit = settings.edit();
						edit.putBoolean("drempel", false);
						edit.commit();
						finish();
					}
					else{
						if(settings.getInt("coins", 0) >= 400){
							 /*tracker.send(new HitBuilders.EventBuilder()
					            .setCategory("Button")
					            .setAction("Drempel")
					            .setLabel("$$$")
					            .build());*/
							Editor edit = settings.edit();
							edit.putBoolean("drempel", false);
							edit.commit();
							bovenbalk.addCoins((-1 * 400));
							finish();
						}
						else{
							popup = "geengeld";
							setTekstAndButton();
						}
					}
				}
			});
			
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		bovenbalk.setCoins();
		bovenbalk.setTitle();
		TapjoyConnect.getTapjoyConnectInstance().getTapPoints(this);
	}
	@Override 
	public void onDestroy(){
		super.onDestroy();
		TapjoyConnect.getTapjoyConnectInstance().sendShutDownEvent();
	}
	
	private void sentAnalytics(String text) {
	/*	Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-57548145-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, text);
		tracker.send(hitParameters);*/
	}
	
	
	private void setViews(){
		bovenbalk = new BovenBalk(settings, this, (Button)findViewById(R.id.buttonBack), (Button)findViewById(R.id.buttonCoins),(TextView)findViewById(R.id.textViewTitle) );
		
		text = (ImageView)findViewById(R.id.imageViewTekst);
		button = (Button)findViewById(R.id.buttonPopUp);
		button2 = (Button)findViewById(R.id.buttonPopUp2);
		next = (Button)findViewById(R.id.buttonSluit);
		
	}
	
	
	public void open() {
		startActivity(getOpenFacebookIntent(this));
	}

	public static Intent getOpenFacebookIntent(Context context2) {
		try {
			context2.getPackageManager().getPackageInfo("com.facebook.katana",
					0);
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("fb://profile/333078300148053"));
		} catch (Exception e) {
			return new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/pages/Appoh/333078300148053"));
		}
	}
	private void openOfferWall(){
		TapjoyConnect.getTapjoyConnectInstance().showOffers();
	}

	@Override
	public void viewDidClose(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewDidOpen(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewWillClose(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewWillOpen(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void earnedTapPoints(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSpendPointsResponse(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSpendPointsResponseFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUpdatePoints(String arg0, int paramInt) {
		// TODO Auto-generated method stub
		if (paramInt > 0) {
			TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(paramInt,
					this);
			bovenbalk.addCoins(paramInt);
			bovenbalk.showToastMsg("You earned " + paramInt + " coins!!!");
		}
		
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
}






