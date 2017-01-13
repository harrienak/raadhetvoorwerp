package com.geckoapps.raadhetvoorwerp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.geckoapps.raadhetvoorwerp.R;
import com.geckoapps.raadhetvoorwerp.classes.BovenBalk;

public class AllLevelCompleteActivity extends Activity {
    private BovenBalk bovenbalk;
    private SharedPreferences settings;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alllevelcomplete);

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        setViews();

        sentAnalytics("complete");
    }

    @Override
    public void onResume() {
        super.onResume();
        bovenbalk.setCoins();
        bovenbalk.setTitle();
    }

    private void sentAnalytics(String text) {
        /*Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-57548145-3");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, text);
		tracker.send(hitParameters);*/
    }


    private void setViews() {
        bovenbalk = new BovenBalk(settings, this, (Button) findViewById(R.id.buttonBack), (Button) findViewById(R.id.buttonCoins), (TextView) findViewById(R.id.textViewTitle));

        webview = (WebView) findViewById(R.id.webView1);
        webview.setBackgroundColor(0x00000000);

        webview.setVisibility(View.INVISIBLE);
		/*if(isOnline()){
			if (getResources().getBoolean(R.bool.isTablet)) {
				webview.loadUrl("http://www.geckoapps.nl/ads/raadhetvoorwerp_finish_ad_tablet.html");
			} else {
				webview.loadUrl("http://www.geckoapps.nl/ads/raadhetvoorwerp_finish_ad.html");
			}
			WebSettings s = webview.getSettings();
			s.setJavaScriptEnabled(true);
			webview.setOnTouchListener(new View.OnTouchListener() {
	
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return (arg1.getAction() == MotionEvent.ACTION_MOVE);
				}
			});
		}*/
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}






