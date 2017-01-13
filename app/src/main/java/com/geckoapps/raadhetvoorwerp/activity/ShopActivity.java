package com.geckoapps.raadhetvoorwerp.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.geckoapps.raadhetvoorwerp.R;
import com.geckoapps.raadhetvoorwerp.classes.BovenBalk;
import com.geckoapps.raadhetvoorwerp.util.IabHelper;
import com.geckoapps.raadhetvoorwerp.util.IabResult;
import com.geckoapps.raadhetvoorwerp.util.Inventory;
import com.geckoapps.raadhetvoorwerp.util.Purchase;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TapjoyConnectNotifier;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;
import com.tapjoy.TapjoyViewNotifier;

import java.util.Hashtable;

public class ShopActivity extends Activity implements TapjoyNotifier,
TapjoySpendPointsNotifier, TapjoyEarnedPointsNotifier, TapjoyViewNotifier{
	private BovenBalk bovenbalk;
	private SharedPreferences settings;
	private Button free, shop450, shop1200, shop2600, shop5700, shop12500, noads, twitter, oneindig;
	
	static final int RC_REQUEST = 10001;
	static final String SKU_coins450 = "coins450";
	static final String SKU_coins1200 = "coins1200";
	static final String SKU_coins2600 = "coins2600";
	static final String SKU_coins5700 = "coins5700";
	static final String SKU_coins12500 = "coins12500";
	static final String SKU_oneindig = "oneindigmunten";
	static final String SKU_noads = "noads";
	static final String TAG = "BUY";

	IabHelper mHelper;
	IInAppBillingService mService;
	int mTank;
	boolean continueBGMusic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		
		settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		setViews();

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
		
		// google play shop
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgCpWxNSp35SLFj6UD6HMYh3EajjWMeO8OKT+ar2je+GMV+GWCoLGzncAB0n2A4aRr7dCRNeDt2Lbr/SW06k4suoA9cgQ47TIoCejQ/HT46H3X1fgGVH1+a3zsjjAuNi41qHAUCdTodmPmsnU4kZGcUjNwkkxvwhTkh138eiyH1egc5SFAa3jWjw3L7U/7RPUne1p/4EWI+b3yDiYO57Td4ANggVB0GtpaQ16ZNWL7s1KDTJ7A2bhWmxaJ56/UQ/AzAxMLcW3DOmxiN119S6AWJ0I4tp74kldxItVGMIPy5Ve1ufI+KImFOe0EiWnOX7KfugMIHf3fZCaFKY4N4dr1wIDAQAB";
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					bovenbalk.showToastMsg( getString(R.string.toast_problemshop) + " "
									+ result);
					return;
				}
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
		sentAnalytics("shop");
		
		if(settings.getBoolean("firstInApp", true)){
			bovenbalk.showToastMsg(getString(R.string.inapp150));
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
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
		TapjoyConnect.getTapjoyConnectInstance().sendShutDownEvent();
	}
	
	private void sentAnalytics(String text) {
		/*Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-57548145-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, text);
		tracker.send(hitParameters);*/
	}
	
	private void openOfferWall(){
		TapjoyConnect.getTapjoyConnectInstance().showOffers();
	}
	
	private void setViews(){
		bovenbalk = new BovenBalk(settings, this, (Button)findViewById(R.id.buttonBack), (Button)findViewById(R.id.buttonCoins),(TextView)findViewById(R.id.textViewTitle) );
		bovenbalk.setCoinsNotClickable();
		
		free	=	(Button)findViewById(R.id.buttonFree);
		free.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	openOfferWall();
			}
		});
		shop450	=	(Button)findViewById(R.id.buttonShop450);
		shop450.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	getCoinsFromStore(SKU_coins450);
			}
		});
		shop1200 =	(Button)findViewById(R.id.buttonShop1200);
		shop1200.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				getCoinsFromStore(SKU_coins1200);
			}
		});
		shop2600 =	(Button)findViewById(R.id.buttonShop2600);
		shop2600.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				getCoinsFromStore(SKU_coins2600);
			}
		});
		shop5700 =	(Button)findViewById(R.id.buttonShop5700);
		shop5700.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				getCoinsFromStore(SKU_coins5700);
			}
		});
		shop12500 =	(Button)findViewById(R.id.buttonShop12500);
		shop12500.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				getCoinsFromStore(SKU_coins12500);
			}
		});
		
		twitter =	(Button)findViewById(R.id.buttonTwitter);
		twitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				openTwitter();
			}
		});
		
		oneindig = (Button)findViewById(R.id.buttonShopOneindig);
		oneindig.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getCoinsFromStore(SKU_oneindig);
				
			}
		});
		
		noads = (Button)findViewById(R.id.buttonShopNoAds);
		if(settings.getBoolean("ads", true)){
			noads.setVisibility(Button.VISIBLE);
			noads.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					getCoinsFromStore(SKU_noads);
				}
			});
		}
		else{
			noads.setVisibility(Button.GONE);
		}
		
	}
	
	private void openTwitter(){
		if(settings.getBoolean("twitter", true)){
			bovenbalk.addCoins(75);
			settings.edit().putBoolean("twitter", false).commit();
		}
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
			    Uri.parse("twitter://user?screen_name=appoh_apps"));
			startActivity(intent);

			}catch (Exception e) {
			    startActivity(new Intent(Intent.ACTION_VIEW,
			         Uri.parse("https://twitter.com/#!/appoh_apps"))); 
			} 
	}
	
	private void getCoinsFromStore(String paramString) {
		mHelper.launchPurchaseFlow(this, paramString, RC_REQUEST,
				mPurchaseFinishedListener);

	}

	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (result.isSuccess()) {
				int i = 0;
				if (purchase.getSku().equals(SKU_coins450)) {
					i =450;
				} else if (purchase.getSku().equals(SKU_coins1200)) {
					i = 1200;
				} else if (purchase.getSku().equals(SKU_coins2600)) {
					i = 2600;
				} else if (purchase.getSku().equals(SKU_coins5700)) {
					i = 5700;
				} else if (purchase.getSku().equals(SKU_coins12500)) {
					i = 12500;
				}
				
				if(i > 0){
					if(settings.getBoolean("firstInApp", true)){
						bovenbalk.addCoins( (int) (i * 1.5) );
						bovenbalk.showToastMsg(getString(R.string.toast_bought) + (int)(i * 1.5) + getString(R.string.coins));
						Editor edit = settings.edit();
						edit.putBoolean("firstInApp", false);
						edit.commit();
					}
					else{
						bovenbalk.addCoins( i );
						bovenbalk.showToastMsg(getString(R.string.toast_bought) + i + getString(R.string.coins));
					}
				}
				else if (purchase.getSku().equals(SKU_noads)) {
					Editor edit = settings.edit();
					edit.putBoolean("ads", false);
					edit.commit();
					bovenbalk.showToastMsg("No more ads in this app!");
				}
				else if(purchase.getSku().equals(SKU_oneindig)){
					Editor edit = settings.edit();
					edit.putBoolean("oneindig", true);
					edit.commit();
					bovenbalk.showToastMsg("Je hebt nu oneindig munten!");
				}
			} else {
				bovenbalk.showToastMsg("Error while consuming: " + result);
			}
		}
	};
	
	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if (result.isFailure()) {
				return;
			}
			if (inventory.hasPurchase(SKU_coins450)) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_coins450),
						mConsumeFinishedListener);
				return;
			} else if (inventory.hasPurchase(SKU_coins1200)) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_coins1200),
						mConsumeFinishedListener);
				return;
			} else if (inventory.hasPurchase(SKU_coins2600)) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_coins2600),
						mConsumeFinishedListener);
				return;
			} else if (inventory.hasPurchase(SKU_coins5700)) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_coins5700),
						mConsumeFinishedListener);
				return;
			} else if (inventory.hasPurchase(SKU_coins12500)) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_coins12500),
						mConsumeFinishedListener);
				return;
			}
			 else if (inventory.hasPurchase(SKU_noads)) {
					mHelper.consumeAsync(inventory.getPurchase(SKU_noads),
							mConsumeFinishedListener);
					return;
				}
			 else if (inventory.hasPurchase(SKU_oneindig)) {
					mHelper.consumeAsync(inventory.getPurchase(SKU_oneindig),
							mConsumeFinishedListener);
					return;
				}
		}
	};
	
	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				bovenbalk.showToastMsg("Error purchasing: " + result);
				return;
			}
			if (purchase.getSku().equals(SKU_coins450)
					|| purchase.getSku().equals(SKU_coins1200)
					|| purchase.getSku().equals(SKU_coins2600)
					|| purchase.getSku().equals(SKU_coins5700)
					|| purchase.getSku().equals(SKU_coins12500)
					|| purchase.getSku().equals(SKU_noads)
					|| purchase.getSku().equals(SKU_oneindig)) {
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
		}
	};
	ServiceConnection mServiceConn = new ServiceConnection() {
		public void onServiceConnected(
				ComponentName paramAnonymousComponentName,
				IBinder paramAnonymousIBinder) {
			ShopActivity.this.mService = IInAppBillingService.Stub
					.asInterface(paramAnonymousIBinder);
		}

		public void onServiceDisconnected(
				ComponentName paramAnonymousComponentName) {
			ShopActivity.this.mService = null;
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} 
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
		TapjoyConnect.getTapjoyConnectInstance().getTapPoints(this);
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
