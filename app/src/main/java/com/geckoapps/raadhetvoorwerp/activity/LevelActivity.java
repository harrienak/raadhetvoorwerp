package com.geckoapps.raadhetvoorwerp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.geckoapps.raadhetvoorwerp.R;
import com.geckoapps.raadhetvoorwerp.classes.BovenBalk;
import com.geckoapps.raadhetvoorwerp.classes.DatabaseHelper;
import com.geckoapps.raadhetvoorwerp.classes.Level;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TapjoyConnectNotifier;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;
import com.tapjoy.TapjoyViewNotifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.ads.AdView;

public class LevelActivity extends Activity implements TapjoyNotifier,
TapjoySpendPointsNotifier, TapjoyEarnedPointsNotifier, TapjoyViewNotifier{

	private SharedPreferences settings;
	private DatabaseHelper db;
	private Level level;
	private ImageView plaatje;
	private ArrayList<Button> letters, woord, lettersInWord;
	private Typeface typeface;
	private Button hint, remove, skip, share;
	private BovenBalk bovenbalk;
	/*private Tracker tracker;*/
	private LinearLayout normalView, correctView, background;
	AdView mAdView;
	private InterstitialAd interstitial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
	
		
		//getGameHelper().setMaxAutoSignInAttempts(0);
		settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		db = new DatabaseHelper(this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		
		/*tracker = GoogleAnalytics.getInstance(this).getTracker(
					"UA-57548145-3");*/
		
		setViews();
		setLevel();
		setAd();
	}
	
	
	private void sentAnalytics(String text) {
		//Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
		//		"UA-51142104-9");
	/*HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, text);
		tracker.send(hitParameters);*/
	}
	
	@Override
	public void onResume(){
		super.onResume();
		bovenbalk.setCoins();
		bovenbalk.setTitle();
		if (mAdView != null) {
			mAdView.resume();
		}
		TapjoyConnect.getTapjoyConnectInstance().getTapPoints(this);
	}
	
	@Override 
	public void onDestroy(){
		super.onDestroy();
		TapjoyConnect.getTapjoyConnectInstance().sendShutDownEvent();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if (mAdView != null) {
			mAdView.pause();
		}
	}
	
	private void setAd() {
		if(settings.getBoolean("ads", true)){
			// Create the interstitial.
		    interstitial = new InterstitialAd(this);
		    interstitial.setAdUnitId("ca-app-pub-2334535603900096/3371188567");
			
			
			mAdView = (AdView) findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			// Start loading the ad in the background.
			mAdView.loadAd(adRequest);
			
			LinearLayout bg = (LinearLayout)findViewById(R.id.bgAd);
			
		    interstitial.loadAd(adRequest);
		}
	}
	
	int iLetter= 0;
	Timer timerLetter;
	
	private void singleAnimationLetter(final int i){
		if(i < letters.size() ){
			Animation ani = AnimationUtils.loadAnimation(this, R.anim.letters);
			letters.get(i).setVisibility(Button.VISIBLE);
			letters.get(i).startAnimation(ani);
			ani.setAnimationListener(new AnimationListener() {
				public void onAnimationEnd(Animation animation) {
					singleAnimationLetter((i+1));
				}
				public void onAnimationRepeat(Animation arg0) {
				}
	
				public void onAnimationStart(Animation arg0) {
				}
			});
		}
	}
	
	
	private void setLevel(){
		if(settings.getInt("currentLevel", 1) > bovenbalk.getMaxLevel() ){
			Intent i = new Intent(LevelActivity.this,
					AllLevelCompleteActivity.class);
			startActivity(i);
			finish();
		}
		else if(settings.getBoolean("drempel", true) && settings.getInt("currentLevel", 1) == 2201){
			drempelPopUp();
		}
		else{
			db.openDataBase();
			level = db.getLevel(settings.getInt("currentLevel", 1), settings.getString("language", "en"));
			db.close();

	//		bovenbalk.showToastMsg(level.getAnswer()); //DEBUG
			for (Button l : letters) {
				l.setClickable(true);
			}
			
			sentAnalytics("level: " + level.getNummer() );
			canRemove = true;
		
//			plaatje.setImageBitmap(roundCornerImage(BitmapFactory
//					.decodeResource(
//							getResources(),
//							getResources().getIdentifier(level.getPlaatje(),
//									"drawable", getPackageName())), 8));

			plaatje.setImageResource( getResources().getIdentifier(level.getPlaatje(),
									"drawable", getPackageName()));
			
			for (int i = 0; i < letters.size(); i++) {
				letters.get(i).setVisibility(Button.INVISIBLE);
				letters.get(i).setText(  level.getLetters().get(i).toUpperCase(Locale.US) );
			}
			singleAnimationLetter(0);
	
			for (Button b : woord) {
				b.setVisibility(Button.GONE);
				b.setText("");
				LinearLayout.LayoutParams params = (LayoutParams) b.getLayoutParams();
				params.setMargins(0, 0, 0, 0);
				b.setLayoutParams(params);
			}
			int currenti =0;
			lettersInWord = new ArrayList<Button>();
			int currentPositie = 0;
			
			//1 word
			if(level.getWoorden().size() == 1 && level.getAnswer().length() <= 10){
				for (int i = 0; i < woord.size(); i++) {
					if(i < level.getAnswer().length() ){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
					}
					else{
						woord.get(i).setVisibility(Button.GONE);
					}
				}
			}	
			else if(level.getWoorden().size() == 1){
				for (int i = 0; i < woord.size(); i++) {
					if(i < level.getAnswer().length() ){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
					}
					else{
						woord.get(i).setVisibility(Button.GONE);
					}
				}
			}
			//2 words
			else if( level.getWoorden().size() == 2 ){
				if( (level.getWoorden().get(0).length() + level.getWoorden().get(1).length() ) <= 10){
					for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
						if(i == level.getWoorden().get(0).length() -1){
							LinearLayout.LayoutParams params = (LayoutParams) woord.get(i).getLayoutParams();
							params.setMargins(0, 0, 20, 0);
							woord.get(i).setLayoutParams(params);
						}
					}
					currenti = level.getWoorden().get(0).length();
					for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
					}
					currenti = currenti + level.getWoorden().get(1).length();
					for (int i = currenti; i < 20; i++) {
						woord.get(i).setVisibility(Button.GONE);
					}
				}
				else{
					for(int i = 0; i < 10; i ++){
						if(i < level.getWoorden().get(0).length()) {
							woord.get(i).setVisibility(Button.VISIBLE);
							lettersInWord.add(woord.get(i));
							level.origin_positie[i] = currentPositie;
							currentPositie++;
						}
						else
							woord.get(i).setVisibility(Button.GONE);
					}
					for (int i = 10; i < 20; i++) {
						if(i < (10 + level.getWoorden().get(1).length() )) {
							lettersInWord.add(woord.get(i));
							woord.get(i).setVisibility(Button.VISIBLE);
							level.origin_positie[i] = currentPositie;
							currentPositie++;
						}
						else
							woord.get(i).setVisibility(Button.GONE);
					}
				}
			}
			
			//3 words
			else if(level.getWoorden().size() == 3){
				for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
					woord.get(i).setVisibility(Button.VISIBLE);
					level.origin_positie[i] = currentPositie;
					currentPositie++;
					lettersInWord.add(woord.get(i));
					if(i == level.getWoorden().get(0).length() -1){
						LinearLayout.LayoutParams params = (LayoutParams) woord.get(i).getLayoutParams();
						params.setMargins(0, 0, 20, 0);
						woord.get(i).setLayoutParams(params);
					}
				}
				currenti = level.getWoorden().get(0).length();
				if( (level.getWoorden().get(0).length() + level.getWoorden().get(1).length() ) <= 10 ){
					for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
					}
					currenti = currenti + level.getWoorden().get(1).length();
				}
				for (int i = currenti; i < 10; i++) {
					woord.get(i).setVisibility(Button.GONE);
				}
				for(int i = 10; i < woord.size(); i++){
					if(i < (10 + level.getWoorden().get(2).length()) ){
						woord.get(i).setVisibility(Button.VISIBLE);
						lettersInWord.add(woord.get(i));
						level.origin_positie[i] = currentPositie;
						currentPositie++;
					}
					else{
						woord.get(i).setVisibility(Button.GONE);
					}
				}
			}
			//4 words
			else if(level.getWoorden().size() == 4){
				for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
					woord.get(i).setVisibility(Button.VISIBLE);
					level.origin_positie[i] = currentPositie;
					currentPositie++;
					lettersInWord.add(woord.get(i));
					if(i == level.getWoorden().get(0).length() -1){
						LinearLayout.LayoutParams params = (LayoutParams) woord.get(i).getLayoutParams();
						params.setMargins(0, 0, 20, 0);
						woord.get(i).setLayoutParams(params);
					}
				}
				currenti = level.getWoorden().get(0).length();
				for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
					woord.get(i).setVisibility(Button.VISIBLE);
					lettersInWord.add(woord.get(i));
					level.origin_positie[i] = currentPositie;
					currentPositie++;
				}
				currenti = currenti + level.getWoorden().get(1).length();
				for (int i = currenti; i < 10; i++) {
					woord.get(i).setVisibility(Button.GONE);
				}
				for(int i = 10; i < (10 + level.getWoorden().get(2).length()); i ++){
					woord.get(i).setVisibility(Button.VISIBLE);
					level.origin_positie[i] = currentPositie;
					currentPositie++;
					lettersInWord.add(woord.get(i));
					if(i == (10 + level.getWoorden().get(2).length() -1) ){
						LinearLayout.LayoutParams params = (LayoutParams) woord.get(i).getLayoutParams();
						params.setMargins(0, 0, 20, 0);
						woord.get(i).setLayoutParams(params);
					}
				}
				currenti = 10 +level.getWoorden().get(2).length();
				for(int i = currenti; i < (currenti + level.getWoorden().get(3).length() ); i ++){
					woord.get(i).setVisibility(Button.VISIBLE);
					lettersInWord.add(woord.get(i));
					level.origin_positie[i] = currentPositie;
					currentPositie++;
				}
				currenti = currenti + level.getWoorden().get(3).length();
				for (int i = currenti; i < 20; i++) {
					woord.get(i).setVisibility(Button.GONE);
				}
			}
			
			for (Button l : lettersInWord) {
				l.setClickable(true);
				l.setTextColor( Color.BLACK );
				l.setBackgroundResource(R.drawable.achtergrond_letter_woord);
				
			}
			
			//startCheckTimer();
		}
	}
	
	public Bitmap roundCornerImage(Bitmap src, float round) {
		// Source image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create result bitmap output
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// set canvas for painting
		Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);

		// configure paint
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		// configure rectangle for embedding
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		// draw Round rectangle to canvas
		canvas.drawRoundRect(rectF, round, round, paint);

		// create Xfer mode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// draw source image to canvas
		canvas.drawBitmap(src, rect, rect, paint);

		// return final image
		return result;
	}
	
	private void showCorrectView(){
		normalView.setVisibility(LinearLayout.GONE);
		correctView.setVisibility(LinearLayout.VISIBLE);
		background.setBackgroundResource(R.drawable.achtergrond_goed);
	}
	private void showNormalView(){
		normalView.setVisibility(LinearLayout.VISIBLE);
		correctView.setVisibility(LinearLayout.GONE);
		background.setBackgroundResource(R.drawable.achtergrond_main);
	}
	
	private void setViews(){
		bovenbalk = new BovenBalk(settings, this, (Button)findViewById(R.id.buttonBack), (Button)findViewById(R.id.buttonCoins),(TextView)findViewById(R.id.textViewTitle) );
		
		normalView = (LinearLayout)findViewById(R.id.normalView);
		correctView = (LinearLayout)findViewById(R.id.correctView);
		background = (LinearLayout)findViewById(R.id.background);
		showNormalView();
		
		
		typeface = Typeface.createFromAsset(getAssets(),
				"font/BRLNSDB.TTF");
		
		plaatje = (ImageView) findViewById(R.id.imageViewLogo);
		
		letters = new ArrayList<Button>();
		letters.add( (Button)findViewById(R.id.buttonLetter0) );
		letters.add( (Button)findViewById(R.id.buttonLetter1) );
		letters.add( (Button)findViewById(R.id.buttonLetter2) );
		letters.add( (Button)findViewById(R.id.buttonLetter3) );
		letters.add( (Button)findViewById(R.id.buttonLetter4) );
		letters.add( (Button)findViewById(R.id.buttonLetter5) );
		letters.add( (Button)findViewById(R.id.buttonLetter6) );
		letters.add( (Button)findViewById(R.id.buttonLetter7) );
		letters.add( (Button)findViewById(R.id.buttonLetter8) );
		letters.add( (Button)findViewById(R.id.buttonLetter9) );
		letters.add( (Button)findViewById(R.id.buttonLetter10) );
		letters.add( (Button)findViewById(R.id.buttonLetter11) );
		letters.add( (Button)findViewById(R.id.buttonLetter12) );
		letters.add( (Button)findViewById(R.id.buttonLetter13) );
		letters.add( (Button)findViewById(R.id.buttonLetter14) );
		letters.add( (Button)findViewById(R.id.buttonLetter15) );
		letters.add( (Button)findViewById(R.id.buttonLetter16) );
		letters.add( (Button)findViewById(R.id.buttonLetter17) );
		
		woord = new ArrayList<Button>();
		woord.add( (Button)findViewById(R.id.buttonWoord0) );
		woord.add( (Button)findViewById(R.id.buttonWoord1) );
		woord.add( (Button)findViewById(R.id.buttonWoord2) );
		woord.add( (Button)findViewById(R.id.buttonWoord3) );
		woord.add( (Button)findViewById(R.id.buttonWoord4) );
		woord.add( (Button)findViewById(R.id.buttonWoord5) );
		woord.add( (Button)findViewById(R.id.buttonWoord6) );
		woord.add( (Button)findViewById(R.id.buttonWoord7) );
		woord.add( (Button)findViewById(R.id.buttonWoord8) );
		woord.add( (Button)findViewById(R.id.buttonWoord9) );
		woord.add( (Button)findViewById(R.id.buttonWoord10) );
		woord.add( (Button)findViewById(R.id.buttonWoord11) );
		woord.add( (Button)findViewById(R.id.buttonWoord12) );
		woord.add( (Button)findViewById(R.id.buttonWoord13) );
		woord.add( (Button)findViewById(R.id.buttonWoord14) );
		woord.add( (Button)findViewById(R.id.buttonWoord15) );
		woord.add( (Button)findViewById(R.id.buttonWoord16) );
		woord.add( (Button)findViewById(R.id.buttonWoord17) );
		woord.add( (Button)findViewById(R.id.buttonWoord18) );
		woord.add( (Button)findViewById(R.id.buttonWoord19) );
		
		for (int i = 0; i < letters.size(); i++) {
			final int i2 = i;
			letters.get(i).setTypeface(typeface);
			letters.get(i).setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
				 	onLetterClick(i2);
				}
			});
		}
		for (int i = 0; i < woord.size(); i++) {
			final int i2 = i;
			woord.get(i).setTypeface(typeface);
			woord.get(i).setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
				 	onWoordLetterClick(i2);
				}
			});
		}
		
		hint = (Button)findViewById(R.id.buttonHint);
		hint.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	hintLetterDialog();
			}
		});
		
		remove = (Button)findViewById(R.id.buttonRemove);
		remove.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	removeLettersDialog();
			}
		});
		
		skip = (Button)findViewById(R.id.buttonSkip);
		skip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	skipLevelDialog();
			}
		});
		share = (Button)findViewById(R.id.buttonShare);
		share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
			 	shareLevel();
			}
		});
	
	}
	
	
	private void shareLevel(){
		// Get tracker.
		
		
        // Build and send an Event.
      /*  tracker.send(new HitBuilders.EventBuilder()
            .setCategory("Button")
            .setAction("Share")
            .setLabel("level: " + level.getNummer())
            .build());*/
		
		LinearLayout allImages = (LinearLayout)findViewById(R.id.shareLayout);
		allImages.setBackgroundResource(R.drawable.achtergrond_main);
		Bitmap emojis = screenShot(allImages);
		allImages.setBackgroundResource(android.R.color.transparent);
		 String pathofBmp = Images.Media.insertImage(getContentResolver(), emojis,"raadhetvoorwerp_"+level.getNummer(), null);
		 Uri bmpUri;
		 
		 if(pathofBmp != null){
			Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
			bmpUri = Uri.parse(pathofBmp);
			emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
		    emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    emailIntent1.putExtra(Intent.EXTRA_TEXT, "Download Raad het Voorwerp nu: https://play.google.com/store/apps/details?id=com.geckoapps.raadhetwoord");
		    emailIntent1.setType("image/png");
		    startActivity(emailIntent1);
		 }
		 else {
			Toast.makeText(this, "Uw toestel ondersteunt op dit moment niet deze functie. Om toch hulp te vragen aan anderen kun je altijd een screenshot maken van het scherm en deze delen.", Toast.LENGTH_SHORT).show();
		}
	}
	
	 public Bitmap screenShot(View view) {
	        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
	                view.getHeight(), Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(bitmap);
	        view.draw(canvas);
	        return bitmap;
	    }
	
	boolean canRemove= true;
	private void removeLettersDialog(){
		final int cost = 90;
		new AlertDialog.Builder(this)
	    .setTitle( getString(R.string.title_removeletters))
	    .setMessage( getString(R.string.text_removeletters) + " " + cost + " " + getString(R.string.coins_q) )
	    .setPositiveButton( getString(R.string.yes) , new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(!canRemove){
	        		showToastMsg(getString(R.string.toast_remove2));
	        	}
	        	else if(level.correct_letters.length == 18){
	        		dialog.dismiss();
	        		showToastMsg(getString(R.string.toast_remove));
	        	}
	        	else if(settings.getBoolean("oneindig", false)){
	        		removeLetters();
	        	}
	        	else if(settings.getInt("coins", 0) >= cost){
	            	bovenbalk.addCoins( (-1 * cost) ) ;
	            	dialog.dismiss();
	            	removeLetters();
	            }
	            else{
	            	dialog.dismiss();
	            	notEnoughCoinsPopUp(true);
	            }
	        }
	     })
	    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.dismiss();
	        }
	     })
	    .setIcon(R.drawable.icon_remove)
	     .show();
	}
	private void removeLetters(){
		 // Build and send an Event.
        /*tracker.send(new HitBuilders.EventBuilder()
            .setCategory("Button")
            .setAction("Remove")
            .setLabel("level: " + level.getNummer())
            .build());*/
		int aantalWeg = 0;
		canRemove = false;
		if(level.correct_letters.length == 17){
			aantalWeg = 1;
		} 
		else{
			aantalWeg = (int) (letters.size() - level.correct_letters.length)/ 2;
			if(aantalWeg>5)
				aantalWeg = 5;
			else if(aantalWeg < 1)
				aantalWeg = 1;
		}
		for (int i = 0; i < aantalWeg; i++) {
			for (int j = 0; j < letters.size(); j++) {
				if( level.getRemoveHelp().get(i).equalsIgnoreCase( (String) letters.get(j).getText() )){
					letters.get(j).setVisibility(Button.INVISIBLE);
					break;
				}
			}
		}
	}
	private void showToastMsg(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	private void hintLetterDialog(){
		final int cost = 60;
		new AlertDialog.Builder(this)
	    .setTitle( getString(R.string.title_hintletter))
	    .setMessage( getString(R.string.text_hintletter) + " " + cost + " " + getString(R.string.coins_q) )
	    .setPositiveButton( getString(R.string.yes) , new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(!level.lettersAreClickable() ){
	        		showToastMsg(getString(R.string.toast_hint));
	        	}
	        	else if(settings.getBoolean("oneindig", false)){
	        		hintLetter();
	        	}
	        	else if(settings.getInt("coins", 0) >= cost){
	            	bovenbalk.addCoins( (-1 * cost) ) ;
	            	dialog.dismiss();
	            	hintLetter();
	            }
	            else{
	            	dialog.dismiss();
	            	notEnoughCoinsPopUp(true);
	            }
	        }
	     })
	    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.dismiss();
	        }
	     })
	    .setIcon(R.drawable.icon_hint)
	     .show();
	}
	
	
	private void hintLetter(){
		 // Build and send an Event.
        /*tracker.send(new HitBuilders.EventBuilder()
            .setCategory("Button")
            .setAction("Hint")
            .setLabel("level: " + level.getNummer())
            .build());*/
		Random r = new Random();
		boolean notFound = true; 
		int nr = -1;
		while (notFound) {
			nr = r.nextInt(level.woord_letters.length);
			if(level.woord_letters[nr].equals("")){
				notFound = false;
			}
		}
		lettersInWord.get(nr).setText( level.correct_letters[nr].toUpperCase(Locale.US) );
		lettersInWord.get(nr).setTextColor(getResources().getColor(R.color.color_greencorrect));
		lettersInWord.get(nr).setBackgroundResource(R.drawable.letter_button);
		lettersInWord.get(nr).setClickable(false);
		for (int i = 0; i < letters.size(); i++) {
			if(  level.correct_letters[nr].equalsIgnoreCase( (String) letters.get(i).getText() ) ){
				letters.get(i).setVisibility(Button.INVISIBLE);
				level.woord_letters[nr] = (String) letters.get(i).getText();
				level.woord_positie[nr] = i;
				break;
			}
		}
		if(!level.lettersAreClickable()){
			checkLevel();
		}
	}
	
	private void skipLevelDialog(){
		final int cost = 200;
		new AlertDialog.Builder(this)
	    .setTitle( getString(R.string.title_skiplevel))
	    .setMessage( getString(R.string.text_skiplevel) + " " + cost + " " + getString(R.string.coins_q) )
	    .setPositiveButton( getString(R.string.yes) , new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(settings.getBoolean("oneindig", false)){
	        		skipLevel();
	        	}
	        	else if(settings.getInt("coins", 0) >= cost){
	            	bovenbalk.addCoins( (-1 * cost) ) ;
	            	dialog.dismiss();
	            	skipLevel();
	            }
	            else{
	            	dialog.dismiss();
	            	notEnoughCoinsPopUp(true);
	            }
	        }
	     })
	    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.dismiss();
	        }
	     })
	    .setIcon(R.drawable.icon_skip)
	     .show();
	}
	private void skipLevel(){
		 // Build and send an Event.
        /*tracker.send(new HitBuilders.EventBuilder()
            .setCategory("Button")
            .setAction("Skip")
            .setLabel("level: " + level.getNummer())
            .build());*/
		nextLevel(0);
	}
	
	private void notEnoughCoinsPopUp(final boolean cancable){
		Intent myIntent = new Intent(LevelActivity.this, PopUpActivity.class);
		myIntent.putExtra("popup","geengeld");
		startActivity(myIntent);

	}
	
	private void drempelPopUp(){
		Intent myIntent = new Intent(LevelActivity.this, PopUpActivity.class);
		myIntent.putExtra("popup","drempel");
		startActivity(myIntent);
		finish();
	}
	
	private void onLetterClick(int i){
		if(level.lettersAreClickable()){
			letters.get(i).setVisibility(Button.INVISIBLE);
			//addLetterinWord((String) letters.get(i).getText());
			
			boolean addWoord = true;
			int nr = 0;
			while (addWoord) {
				if( level.woord_letters[nr].length() < 1 ){
					lettersInWord.get(nr).setText( (String) letters.get(i).getText() );
					lettersInWord.get(nr).setBackgroundResource(R.drawable.letter_button);
					level.woord_letters[nr] = (String) letters.get(i).getText();
					level.woord_positie[nr] = i;
					addWoord = false;
				}
				nr++;
				if(!level.lettersAreClickable() ){
					checkLevel();
					break;
				}
			}
			startCheckTimer();
		}
		else{
			checkLevel();
		}
	}
	
	private void checkLevel(){
		boolean check = true;
		for (int i = 0; i < level.correct_letters.length; i++) {
			if( !level.correct_letters[i].equalsIgnoreCase(level.woord_letters[i])){
				check = false;
			}
		}
		
		if(check){
			singleAnimation(0);
		}
		else{
//			for (Button l : letters) {
//				l.setClickable(false);
//			}
			
			for (int i = 0; i < lettersInWord.size(); i++) {
				final int i2 = i;
				Animation load = AnimationUtils.loadAnimation(this, R.anim.shake);
				lettersInWord.get(i).startAnimation(load);
				load.setAnimationListener(new AnimationListener() {
					public void onAnimationEnd(Animation animation) {
						lettersInWord.get(i2).setTextColor(Color.BLACK);
					}
					public void onAnimationRepeat(Animation arg0) {
					}

					public void onAnimationStart(Animation arg0) {
						lettersInWord.get(i2).setTextColor( getResources().getColor(R.color.color_redfail));
					}
				});
			}
		}
	}
	
	
	private void onWoordLetterClick(int i){
		if( lettersInWord.get( level.origin_positie[i] ).length() > 0 ){
			 lettersInWord.get( level.origin_positie[i] ).setText( "" );
			 lettersInWord.get( level.origin_positie[i] ).setBackgroundResource(R.drawable.achtergrond_letter_woord);
			 letters.get( level.woord_positie[ level.origin_positie[i] ] ).setVisibility(Button.VISIBLE);
			 
			 level.woord_letters[ level.origin_positie[i] ] = "";
			 level.woord_positie[ level.origin_positie[i] ] = -1;
		}
		
		
	}
	
	private void singleAnimation(final int i){
		for (Button l : letters) {
			l.setClickable(false);
		}
		if(i == lettersInWord.size()){
			
			
			for (int j = 0; j < lettersInWord.size(); j++) {
				final int j2 = j;
				Animation load = AnimationUtils.loadAnimation(this, R.anim.correct2);
				lettersInWord.get(j).startAnimation(load);
				
				if(j2 == 1 ){
					load.setAnimationListener(new AnimationListener() {
						public void onAnimationEnd(Animation animation) {
							lettersInWord.get(j2).setText( level.correct_letters[j2].toUpperCase(Locale.US) );
								nextLevel(5);
						}
						public void onAnimationRepeat(Animation arg0) {
						}

						public void onAnimationStart(Animation arg0) {
							//lettersInWord.get(j2).setTextColor( getResources().getColor(R.color.color_greencorrect));
						}
					});
				}
			}
		}
		else{
			Animation ani = AnimationUtils.loadAnimation(this, R.anim.correct);
			lettersInWord.get(i).startAnimation(ani);
			
			ani.setAnimationListener(new AnimationListener() {
				public void onAnimationEnd(Animation animation) {
					//lettersInWord.get(i).setTextColor(Color.BLACK);
					singleAnimation((i+1));
				}
				public void onAnimationRepeat(Animation arg0) {
				}
	
				public void onAnimationStart(Animation arg0) {
					//lettersInWord.get(i).setTextColor( getResources().getColor(R.color.color_greencorrect));
				}
			});
		}
	}
	
	private void nextLevel(int coins){
		openNextPopUp();
		
		Editor edit = settings.edit();
		edit.putInt("currentLevel", settings.getInt("currentLevel", 1) + 1);
		edit.commit();
		bovenbalk.addCoins(coins);
		showCorrectView();
	}
	
	private void openNextPopUp(){
		ArrayList<Button> popup_letters = new ArrayList<Button>();
		ArrayList<Button> popup_lettersInWord = new ArrayList<Button>();
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect0) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect1) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect2) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect3) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect4) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect5) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect6) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect7) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect8) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect9) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect10) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect11) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect12) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect13) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect14) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect15) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect16) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect17) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect18) );
		popup_letters.add( (Button)findViewById(R.id.buttonWoordCorrect19) );
		
		for (Button b : popup_letters) {
			LinearLayout.LayoutParams params = (LayoutParams) b.getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			b.setLayoutParams(params);
		}
		
		
		//int currentPositie = 0;
		int currenti= 0;
		
		//1 word
		if(level.getWoorden().size() == 1 && level.getAnswer().length() <= 10){
			for (int i = 0; i < woord.size(); i++) {
				if(i < level.getAnswer().length() ){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
				}
				else{
					popup_letters.get(i).setVisibility(Button.GONE);
				}
			}
		}	
		else if(level.getWoorden().size() == 1){
			for (int i = 0; i < woord.size(); i++) {
				if(i < level.getAnswer().length() ){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
				}
				else{
					popup_letters.get(i).setVisibility(Button.GONE);
				}
			}
		}
		//2 words
		else if( level.getWoorden().size() == 2 ){
			if( (level.getWoorden().get(0).length() + level.getWoorden().get(1).length() ) <= 10){
				for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
					if(i == level.getWoorden().get(0).length() -1){
						LinearLayout.LayoutParams params = (LayoutParams) popup_letters.get(i).getLayoutParams();
						params.setMargins(0, 0, 20, 0);
						popup_letters.get(i).setLayoutParams(params);
					}
				}
				currenti = level.getWoorden().get(0).length();
				for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
				}
				currenti = currenti + level.getWoorden().get(1).length();
				for (int i = currenti; i < 20; i++) {
					popup_letters.get(i).setVisibility(Button.GONE);
				}
			}
			else{
				for(int i = 0; i < 10; i ++){
					if(i < level.getWoorden().get(0).length()) {
						popup_letters.get(i).setVisibility(Button.VISIBLE);
						popup_lettersInWord.add(popup_letters.get(i));
					}
					else
						popup_letters.get(i).setVisibility(Button.GONE);
				}
				for (int i = 10; i < 20; i++) {
					if(i < (10 + level.getWoorden().get(1).length() )) {
						popup_lettersInWord.add(popup_letters.get(i));
						popup_letters.get(i).setVisibility(Button.VISIBLE);
					}
					else
						popup_letters.get(i).setVisibility(Button.GONE);
				}
			}
		}
		
		//3 words
		else if(level.getWoorden().size() == 3){
			for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
				popup_letters.get(i).setVisibility(Button.VISIBLE);
				popup_lettersInWord.add(popup_letters.get(i));
				if(i == level.getWoorden().get(0).length() -1){
					LinearLayout.LayoutParams params = (LayoutParams) woord.get(i).getLayoutParams();
					params.setMargins(0, 0, 20, 0);
					popup_letters.get(i).setLayoutParams(params);
				}
			}
			currenti = level.getWoorden().get(0).length();
			if( (level.getWoorden().get(0).length() + level.getWoorden().get(1).length() ) <= 10 ){
				for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
				}
				currenti = currenti + level.getWoorden().get(1).length();
			}
			for (int i = currenti; i < 10; i++) {
				popup_letters.get(i).setVisibility(Button.GONE);
			}
			for(int i = 10; i < woord.size(); i++){
				if(i < (10 + level.getWoorden().get(2).length()) ){
					popup_letters.get(i).setVisibility(Button.VISIBLE);
					popup_lettersInWord.add(popup_letters.get(i));
				}
				else{
					popup_letters.get(i).setVisibility(Button.GONE);
				}
			}
		}
		//4 words
		else if(level.getWoorden().size() == 4){
			for(int i = 0; i < level.getWoorden().get(0).length(); i ++){
				popup_letters.get(i).setVisibility(Button.VISIBLE);
				popup_lettersInWord.add(popup_letters.get(i));
				if(i == level.getWoorden().get(0).length() -1){
					LinearLayout.LayoutParams params = (LayoutParams) popup_letters.get(i).getLayoutParams();
					params.setMargins(0, 0, 20, 0);
					popup_letters.get(i).setLayoutParams(params);
				}
			}
			currenti = level.getWoorden().get(0).length();
			for(int i = currenti; i < (currenti + level.getWoorden().get(1).length() ); i ++){
				popup_letters.get(i).setVisibility(Button.VISIBLE);
				popup_lettersInWord.add(popup_letters.get(i));
			}
			currenti = currenti + level.getWoorden().get(1).length();
			for (int i = currenti; i < 10; i++) {
				popup_letters.get(i).setVisibility(Button.GONE);
			}
			for(int i = 10; i < (10 + level.getWoorden().get(2).length()); i ++){
				popup_letters.get(i).setVisibility(Button.VISIBLE);
				popup_lettersInWord.add(popup_letters.get(i));
				if(i == (10 + level.getWoorden().get(2).length() -1) ){
					LinearLayout.LayoutParams params = (LayoutParams) popup_letters.get(i).getLayoutParams();
					params.setMargins(0, 0, 20, 0);
					popup_letters.get(i).setLayoutParams(params);
				}
			}
			currenti = 10 +level.getWoorden().get(2).length();
			for(int i = currenti; i < (currenti + level.getWoorden().get(3).length() ); i ++){
				popup_letters.get(i).setVisibility(Button.VISIBLE);
				popup_lettersInWord.add(popup_letters.get(i));
			}
			currenti = currenti + level.getWoorden().get(3).length();
			for (int i = currenti; i < 20; i++) {
				popup_letters.get(i).setVisibility(Button.GONE);
			}
		}
		
		
		for (int i = 0; i < popup_lettersInWord.size(); i++) {
			popup_lettersInWord.get(i).setTypeface(typeface);
			popup_lettersInWord.get(i).setText(level.correct_letters[i].toUpperCase(Locale.US));
		}
		
		Button pijl = (Button) findViewById(R.id.buttonPijl);
		pijl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				if(settings.getInt("currentLevel", 1) == 15 || settings.getInt("currentLevel", 1) == 65){
					Intent myIntent = new Intent(LevelActivity.this, PopUpActivity.class);
					myIntent.putExtra("popup","rate");
					startActivity(myIntent);
				}
				else if(settings.getInt("currentLevel", 1) == 35){
					if(settings.getBoolean("fblike", true)){
						Intent myIntent = new Intent(LevelActivity.this, PopUpActivity.class);
						myIntent.putExtra("popup","fb");
						startActivity(myIntent);
					}
				}
				else if(settings.getInt("currentLevel", 1) < 100){
					if(settings.getInt("currentLevel", 1)%20 == 0){
						showFullScreenAd();
					}
				}
				else{
					if(settings.getInt("currentLevel", 1)%7 == 0){
						showFullScreenAd();
					}
				}
				
				bovenbalk.setTitle();
				showNormalView();
				setLevel();	
			}
		});
	}
	
	Timer timer = null;
	private void startCheckTimer(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if(timer != null){
							timer.cancel();
							timer = null;
						}
						shakeButtons();
					}
				});
			}
		}, 10000, 10000);// Update text every second
	}

	public void showFullScreenAd(){
		if(settings.getBoolean("ads", true)){
			if (interstitial.isLoaded()) {
			      interstitial.show();
			      
				      interstitial = new InterstitialAd(this);
					  interstitial.setAdUnitId("ca-app-pub-2334535603900096/3371188567");
				      AdRequest adRequest = new AdRequest.Builder().build();
				      interstitial.loadAd(adRequest);
			    }
		}
	}
	
	private void shakeButtons(){
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake2);
		final Animation shake2 = AnimationUtils.loadAnimation(this, R.anim.shake2);
		final Animation shake3 = AnimationUtils.loadAnimation(this, R.anim.shake2);
		hint.startAnimation(shake);
		shake.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				remove.startAnimation(shake2);
				shake2.setAnimationListener(new AnimationListener() {
					public void onAnimationEnd(Animation animation) {
						skip.startAnimation(shake3);
					}
					public void onAnimationRepeat(Animation arg0) {
					}

					public void onAnimationStart(Animation arg0) {
					}
				});
			}
			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationStart(Animation arg0) {
			}
		});
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




