package com.geckoapps.raadhetvoorwerp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geckoapps.raadhetvoorwerp.R;
import com.geckoapps.raadhetvoorwerp.classes.BovenBalk;
import com.geckoapps.raadhetvoorwerp.classes.DatabaseHelper;
import com.geckoapps.raadhetvoorwerp.classes.Level;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {
    private SharedPreferences settings;
    private DatabaseHelper db;
    private Button playB, like, wihw;
    private BovenBalk bovenbalk;
    ProgressDialog progress;
    private WebView webview;
    /* private Tracker tracker;*/
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        settings.getInt("currentLevel", 1);
        settings.getInt("coins", 0);
        settings.getString("language", "en");
        settings.getBoolean("oneindig", false);

        db = new DatabaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* tracker = GoogleAnalytics.getInstance(this).getTracker(
                "UA-57548145-3");*/


//		
//		settings.edit().putInt("currentLevel", 2475).commit();
//		bovenbalk.addCoins(10000);

        setViews();
        setAds();
        doallLevelShit();

        sentAnalytics("main");


        if( !isPackageInstalled("com.geckoapps.emoticonquiz2016", this)  &&  !settings.getBoolean("downloadEmoticon", false)){
            showDownloadAPQPopup();
        }

    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void setAds() {
        if (settings.getBoolean("ads", true)) {
            mAdView = (AdView) findViewById(R.id.adView);
            mAdView.setVisibility(AdView.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }
    }

    private void setProgressMessage(final int i) {
        runOnUiThread(new Runnable() {
            public void run() {
                progress.setMessage(getString(R.string.loadlevels_text) + "\n" + i + " van de " + bovenbalk.getMaxLevel() + " levels geladen.");
            }
        });
    }

    private void doallLevelShit() {
        progress = ProgressDialog.show(this, getString(R.string.loadlevels_title),
                getString(R.string.loadlevels_text) + "\n 0 van de " + bovenbalk.getMaxLevel() + " levels.", true);
        progress.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Editor e1 = settings.edit();
                e1.putString("language", "nl");
                e1.commit();

                db.openDataBase();
                //add new levels to database-> levels_old
                Editor editorLevels = settings.edit();

                if (settings.getString("language", "en").equals("nl")) {
                    if (settings.getBoolean("levels200to260", true)) {
                        addLevels200to260();
                        editorLevels.putBoolean("levels200to260", false);
                        setProgressMessage(260);
                    }
                    if (settings.getBoolean("levels261to320", true)) {
                        addLevels261to320();
                        editorLevels.putBoolean("levels261to320", false);
                        setProgressMessage(320);
                    }
                    if (settings.getBoolean("levels321to350", true)) {
                        addLevels321to350();
                        editorLevels.putBoolean("levels321to350", false);
                        setProgressMessage(350);
                    }
                    if (settings.getBoolean("levels351to450", true)) {
                        addLevels351to450();
                        editorLevels.putBoolean("levels351to450", false);
                        setProgress(450);
                    }
                    if (settings.getBoolean("levels451to522", true)) {
                        addLevels451to522();
                        editorLevels.putBoolean("levels451to522", false);
                        setProgressMessage(522);
                    }
                    if (settings.getBoolean("levels523to700", true)) {
                        addLevels523to700();
                        editorLevels.putBoolean("levels523to700", false);
                        setProgressMessage(700);
                    }
                    if (settings.getBoolean("levels701to900", true)) {
                        addLevels701to900();
                        editorLevels.putBoolean("levels701to900", false);
                        setProgressMessage(900);
                    }
                    if (settings.getBoolean("levels901to1000", true)) {
                        addLevels901to1000();
                        editorLevels.putBoolean("levels901to1000", false);
                        setProgressMessage(1000);
                    }
                    if (settings.getBoolean("levels1001to1200", true)) {
                        addLevels1001to1200();
                        editorLevels.putBoolean("levels1001to1200", false);
                        setProgressMessage(1200);
                    }
                    if (settings.getBoolean("levels1201to1250", true)) {
                        addLevels1201to1250();
                        editorLevels.putBoolean("levels1201to1250", false);
                        setProgressMessage(1250);
                    }
                    if (settings.getBoolean("levels1251to1350", true)) {
                        addLevels1251to1350();
                        editorLevels.putBoolean("levels1251to1350", false);
                        setProgressMessage(1350);
                    }
                    if (settings.getBoolean("levels1351to1420", true)) {
                        addLevels1351to1420();
                        editorLevels.putBoolean("levels1351to1420", false);
                        setProgressMessage(1420);
                    }
                    if (settings.getBoolean("levels1421to1500", true)) {
                        addLevels1421to1500();
                        editorLevels.putBoolean("levels1421to1500", false);
                        setProgressMessage(1500);
                    }
                    if (settings.getBoolean("levels1501to1700", true)) {
                        addLevels1501to1700();
                        editorLevels.putBoolean("levels1501to1700", false);
                        setProgressMessage(1700);
                    }
                    if (settings.getBoolean("levels1701to1800", true)) {
                        addLevels1701to1800();
                        editorLevels.putBoolean("levels1701to1800", false);
                        setProgressMessage(1800);
                    }
                    if (settings.getBoolean("levels1801to1900", true)) {
                        addLevels1801to1900();
                        editorLevels.putBoolean("levels1801to1900", false);
                        setProgressMessage(1900);
                    }
                    if (settings.getBoolean("levels1901to2000", true)) {
                        addLevels1901to2000();
                        editorLevels.putBoolean("levels1901to2000", false);
                        setProgressMessage(2000);
                    }
                    if (settings.getBoolean("levels2001to2100", true)) {
                        addLevels2001to2100();
                        editorLevels.putBoolean("levels2001to2100", false);
                        setProgressMessage(2100);
                    }
                    if (settings.getBoolean("levels2101to2200", true)) {
                        addLevels2101to2200();
                        editorLevels.putBoolean("levels2101to2200", false);
                        setProgressMessage(2200);
                    }
                    if (settings.getBoolean("levels2201to2275", true)) {
                        addLevels2201to2275();
                        editorLevels.putBoolean("levels2201to2275", false);
                        setProgressMessage(2275);
                    }
                    if (settings.getBoolean("levels_raadhetgeluid", true)) {
                        addLevelsRaadhetGeluid();
                        editorLevels.putBoolean("levels_raadhetgeluid", false);
                        setProgressMessage(2375);
                    }
                    if (settings.getBoolean("levels2376to2450", true)) {
                        addLevels2376to2450();
                        editorLevels.putBoolean("levels2376to2450", false);
                        setProgressMessage(2450);
                    }
                    if(settings.getBoolean("levels2451to2525", true)) {
                        addLevels2451to2525();
                        editorLevels.putBoolean("levels2451to2525", false);
                        setProgressMessage(2525);
                    }
                }
                editorLevels.commit();

                //first time app opens
                //shuffle all levels and add them to levels_new
                if (settings.getBoolean("first", true)) {
                    bovenbalk.addCoins(250);
                    Editor editor = settings.edit();
                    editor.putBoolean("first", false);

                    editor.commit();

                    if (settings.getString("language", "en").equals("nl"))
                        setRandomLevels();
                }
                //other times app opens
                //check if there are new levels -> add them to levels_new random
                else {
                    if (settings.getString("language", "en").equals("nl")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progress.setMessage("Levels klaar maken voor gebruik...");
                            }
                        });
                        addExtraLevels();
                    }
                }

                db.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (settings.getBoolean("bonus", true)) {
                            Toast.makeText(getApplicationContext(), "Je krijgt 500 extra munten als bonus als trouwe gebruiker!", Toast.LENGTH_SHORT).show();
                            settings.edit().putBoolean("bonus", false).commit();
                            bovenbalk.addCoins(500);
                        }
                        progress.dismiss();
                    }
                });
            }
        }).start();
    }

    private void setRandomLevels() {
        //db.openDataBase();
        ArrayList<Level> levels = db.getAllLevel();

        for (int i = 0; i < 25; i++) {
            db.addLevelToDB(levels.get(i).getNummer(), levels.get(i).getAnswer(), levels.get(i).getPlaatje());
        }
        for (int i = 0; i < 25; i++) {
            levels.remove(0);
        }

        Collections.shuffle(levels);
        for (int i = 0; i < levels.size(); i++) {
            db.addLevelToDB(i + 26, levels.get(i).getAnswer(), levels.get(i).getPlaatje());
        }
        //db.close();
    }

    private void addExtraLevels() {
        //db.openDataBase();
        if (db.countLevelsNew() < db.countLevelsOld()) {
            int nr = db.countLevelsNew();
            ArrayList<Level> levels = db.getExtraLevels(nr);

            Collections.shuffle(levels);
            for (int i = 0; i < levels.size(); i++) {
                db.addLevelToDB((nr + i + 1), levels.get(i).getAnswer(), levels.get(i).getPlaatje());
            }
        }
    }

    private void sentAnalytics(String text) {
        /*Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
                "UA-57548145-3");
        HashMap<String, String> hitParameters = new HashMap<String, String>();
        hitParameters.put(Fields.HIT_TYPE, "appview");
        hitParameters.put(Fields.SCREEN_NAME, text);
        tracker.send(hitParameters);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        bovenbalk.setCoins();
        bovenbalk.setTitle();

        if(isPackageInstalled("com.geckoapps.emoticonquiz2016", this) && settings.getBoolean("emoticonCoins", true)){
            bovenbalk.addCoins(1000);
            settings.edit().putBoolean("emoticonCoins", false).commit();
        }
    }

    private void setLanguage(String l) {
        Editor edit = settings.edit();
        edit.putString("language", l);
        edit.putInt("currentLevel", 1);
        edit.commit();
        bovenbalk.setTitle();
    }

    private void setViews() {
        bovenbalk = new BovenBalk(settings, this,
                (Button) findViewById(R.id.buttonBack),
                (Button) findViewById(R.id.buttonCoins),
                (TextView) findViewById(R.id.textViewTitle));
        bovenbalk.cancelBack();

        playB = (Button) findViewById(R.id.buttonPlay);
        playB.startAnimation(AnimationUtils.loadAnimation(this, R.anim.play));
        playB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                play();
            }
        });

        like = (Button) findViewById(R.id.buttonLike);
        like.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (settings.getBoolean("fblike", true)) {
                    Editor edit = settings.edit();
                    edit.putBoolean("fblike", false);
                    edit.commit();
                    bovenbalk.addCoins(75);
                }
                open();
            }
        });
        wihw = (Button) findViewById(R.id.buttonWIHW);

        //	if(settings.getInt("currentLevel", 1) < 200){
        //		wihw.setVisibility(Button.GONE);
        //	}
        if (settings.getBoolean("raaddeslogan-claim", false)) {
            wihw.setVisibility(Button.GONE);
        } else {
            wihw.setVisibility(Button.VISIBLE);
            wihw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    apq50extralevels();
                }
            });
        }

    }

    public void apq50extralevels() {
       /* if (isPackageInstalled("com.geckoapps.raadhetmerk", this)) {
            //add extra levels
            settings.edit().putBoolean("raaddeslogan-claim", true).commit();
            bovenbalk.addCoins(450);
            bovenbalk.showToastMsg("Gefeliciteerd emt 450 extra munten! Veel speelplezier in Raad het Voorwerp en Raad de Slogan!");
            wihw.setVisibility(View.GONE);
        } else {
            //show popup
            showDownloadAPQPopup();
        }*/
    }

    private void showDownloadAPQPopup() {
        // custom dialog
        final Dialog localDialog = new Dialog(this);
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        localDialog.setContentView(R.layout.popup_add50levels);
        Window localWindow = localDialog.getWindow();
        localWindow.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        localWindow.setGravity(Gravity.CENTER);
        localDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button close = (Button) localDialog.findViewById(R.id.buttonSluit);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localDialog.dismiss();
            }
        });

        Button akq = (Button) localDialog.findViewById(R.id.buttonAKQ);
        akq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                settings.edit().putBoolean("downloadEmoticon", true).commit();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.geckoapps.emoticonquiz2016"));
                startActivity(browserIntent);
                localDialog.dismiss();
            }
        });

        localDialog.show();
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

    private void play() {
        if (settings.getInt("currentLevel", 1) > bovenbalk.getMaxLevel()) {
            Intent i = new Intent(MainActivity.this,
                    AllLevelCompleteActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, LevelActivity.class);
            startActivity(i);
			
		/*	Intent i = new Intent(MainActivity.this,
					AllLevelCompleteActivity.class);
			startActivity(i);*/
        }
    }

    private void addLevels2376to2450() {
        db.addLevelToDBoldTable(2376, "Knikkerzak", "knikkerzak");
        db.addLevelToDBoldTable(2377, "Peilstok", "peilstok");
        db.addLevelToDBoldTable(2378, "Lawine", "lawine");
        db.addLevelToDBoldTable(2379, "Bananen boom", "bananenboom");
        db.addLevelToDBoldTable(2380, "Mandoline", "mandoline");
        db.addLevelToDBoldTable(2381, "Stuiver", "stuiver");
        db.addLevelToDBoldTable(2382, "Cocktailshaker", "cocktailshaker2");
        db.addLevelToDBoldTable(2383, "Purschuim", "purschuim");
        db.addLevelToDBoldTable(2384, "Aansteeklont", "aansteeklont");
        db.addLevelToDBoldTable(2385, "Stormram", "stormram");
        db.addLevelToDBoldTable(2386, "Selfiestick", "selfiestick2");
        db.addLevelToDBoldTable(2387, "Buurman en buurman", "buurmanenbuurman");
        db.addLevelToDBoldTable(2388, "Richt microfoon", "richtmicrofoon");
        db.addLevelToDBoldTable(2389, "Air Force One", "airfoceone");
        db.addLevelToDBoldTable(2390, "brughuisje", "brughuisje");
        db.addLevelToDBoldTable(2391, "amaretto", "amaretto2");
        db.addLevelToDBoldTable(2392, "bruisbal", "bruisbal");
        db.addLevelToDBoldTable(2393, "kern explosie", "kernexplosie");
        db.addLevelToDBoldTable(2394, "paleis het loo", "paleishetloo");
        db.addLevelToDBoldTable(2395, "wake up light", "wakeuplight");
        db.addLevelToDBoldTable(2396, "kaviaar", "kaviaar");
        db.addLevelToDBoldTable(2397, "whisky glas", "whiskyglas");
        db.addLevelToDBoldTable(2398, "whisky vat", "whiskyvat");
        db.addLevelToDBoldTable(2399, "schoepen rad", "schoepenrad");
        db.addLevelToDBoldTable(2400, "trolley bus", "trolleybus");
        db.addLevelToDBoldTable(2401, "schuim beton", "schuimbeton");
        db.addLevelToDBoldTable(2402, "sliptong", "sliptong");
        db.addLevelToDBoldTable(2403, "water scherm", "waterscherm");
        db.addLevelToDBoldTable(2404, "kersen vlaai", "kersenvlaai");
        db.addLevelToDBoldTable(2405, "braadslee", "braadslee");
        db.addLevelToDBoldTable(2406, "grillworst", "grillworst");
        db.addLevelToDBoldTable(2407, "panini", "panini");
        db.addLevelToDBoldTable(2408, "aardappel gratin", "aardappelgratin");
        db.addLevelToDBoldTable(2409, "broccoli soep", "broccolisoep");
        db.addLevelToDBoldTable(2410, "keuken brander", "keukenbrander");
        db.addLevelToDBoldTable(2411, "koksmes", "koksmes");
        db.addLevelToDBoldTable(2412, "keuken weegschaal", "keukenweegschaal");
        db.addLevelToDBoldTable(2413, "kroos", "kroos");
        db.addLevelToDBoldTable(2414, "bultrug", "bultrug");
        db.addLevelToDBoldTable(2415, "traangas", "traangas");
        db.addLevelToDBoldTable(2416, "politie paard", "politiepaard");
        db.addLevelToDBoldTable(2417, "veiling hamer", "veilinghamer");
        db.addLevelToDBoldTable(2418, "chocolade letter", "chocoladeletter");
        db.addLevelToDBoldTable(2419, "pepernoten", "pepernoten");
        db.addLevelToDBoldTable(2420, "dranger", "dranger");
        db.addLevelToDBoldTable(2421, "ja knikker", "jaknikker");
        db.addLevelToDBoldTable(2422, "sleutelhouder", "sleutelhouder");
        db.addLevelToDBoldTable(2423, "duimdrop", "duimdrop");
        db.addLevelToDBoldTable(2424, "stroop soldaatje", "stroopsoldaatje");
        db.addLevelToDBoldTable(2425, "trimmer", "trimmer");
        db.addLevelToDBoldTable(2426, "wegenkaart", "wegenkaart2");
        db.addLevelToDBoldTable(2427, "brie", "brie");
        db.addLevelToDBoldTable(2428, "jupiter", "jupiter");
        db.addLevelToDBoldTable(2429, "hotwing", "hotwing");
        db.addLevelToDBoldTable(2430, "handtas", "handtas");
        db.addLevelToDBoldTable(2431, "rendier", "rendier");
        db.addLevelToDBoldTable(2432, "led lamp", "ledlamp2");
        db.addLevelToDBoldTable(2433, "handrem", "handrem");
        db.addLevelToDBoldTable(2434, "borrelnootjes", "borrelnootjes");
        db.addLevelToDBoldTable(2435, "iron man", "ironman");
        db.addLevelToDBoldTable(2436, "hulk", "hulk");
        db.addLevelToDBoldTable(2437, "kerstverlichting", "kerstverlichting");
        db.addLevelToDBoldTable(2438, "droger", "droger");
        db.addLevelToDBoldTable(2439, "bestekbak", "bestekbak");
        db.addLevelToDBoldTable(2440, "blokhaak", "blokhaak");
        db.addLevelToDBoldTable(2441, "plinten", "plinten");
        db.addLevelToDBoldTable(2442, "verfpot", "verfpot");
        db.addLevelToDBoldTable(2443, "gewei", "gewei");
        db.addLevelToDBoldTable(2444, "bakkebaard", "bakkebaard");
        db.addLevelToDBoldTable(2445, "bus", "bus");
        db.addLevelToDBoldTable(2446, "metro", "metro");
        db.addLevelToDBoldTable(2447, "rotonde", "rotonde");
        db.addLevelToDBoldTable(2448, "kaki", "kaki");
        db.addLevelToDBoldTable(2449, "hovercraft", "hovercraft");
        db.addLevelToDBoldTable(2450, "laadbalk", "laadbalk");

    }

    private void addLevelsRaadhetGeluid() {
        db.addLevelToDBoldTable(2326, "pion", "pion2");
        db.addLevelToDBoldTable(2327, "bioscoop", "bioscoop");
        db.addLevelToDBoldTable(2328, "magnetron popcorn", "magnetronpopcorn");
        db.addLevelToDBoldTable(2329, "beschuit blik", "beschuitblik");
        db.addLevelToDBoldTable(2330, "wespennest", "wespennest");
        db.addLevelToDBoldTable(2331, "te koop bord", "tekoopbord");
        db.addLevelToDBoldTable(2332, "creditcard", "creditcard");
        db.addLevelToDBoldTable(2333, "tommie", "tommiehond");
        db.addLevelToDBoldTable(2334, "reisgids", "reisgids");
        db.addLevelToDBoldTable(2335, "onderuit", "onderuit");
        db.addLevelToDBoldTable(2336, "potloodpunt", "potloodpunt");
        db.addLevelToDBoldTable(2337, "gestampte muisjes", "gestamptemuisjes");
        db.addLevelToDBoldTable(2338, "rouwkaart", "rouwkaart");
        db.addLevelToDBoldTable(2339, "braakzakje", "braakzakje");
        db.addLevelToDBoldTable(2340, "vliegtuig toilet", "vliegtuigtoilet");
        db.addLevelToDBoldTable(2341, "Stofzuigerslang", "stofzuigerslang");
        db.addLevelToDBoldTable(2342, "Kattenluikje", "kattenluikje");
        db.addLevelToDBoldTable(2343, "Graffiti", "graffiti");
        db.addLevelToDBoldTable(2344, "Winkelmandje", "winkelmandje");
        db.addLevelToDBoldTable(2345, "Ijsstokje", "ijsstokje");
        db.addLevelToDBoldTable(2346, "peace teken", "peaceteken");
        db.addLevelToDBoldTable(2347, "Motorolie", "motorolie");
        db.addLevelToDBoldTable(2348, "Dunschiller", "dunschiller2");
        db.addLevelToDBoldTable(2349, "Scheetzak", "scheetzak");
        db.addLevelToDBoldTable(2350, "Ijsblokjesmaker", "ijsblokjesmaker");
        db.addLevelToDBoldTable(2351, "poepzuiger", "poepzuiger");
        db.addLevelToDBoldTable(2352, "bekerhouder", "bekerhouder");
        db.addLevelToDBoldTable(2353, "dirndl", "dirndl");
        db.addLevelToDBoldTable(2354, "ballenbak", "ballenbak");
        db.addLevelToDBoldTable(2355, "crematorium", "crematorium");
        db.addLevelToDBoldTable(2356, "lavagrill", "lavagrill");
        db.addLevelToDBoldTable(2357, "ponte vecchio", "pontevecchio");
        db.addLevelToDBoldTable(2358, "lamineermachine", "lamineermachine");
        db.addLevelToDBoldTable(2359, "briketten starter", "brikettenstarter");
        db.addLevelToDBoldTable(2360, "bbq donut", "bbqdonut");
        db.addLevelToDBoldTable(2361, "donutmaker", "donutmaker");
        db.addLevelToDBoldTable(2362, "chocoladefontein", "chocoladefontein");
        db.addLevelToDBoldTable(2363, "koksmuts", "koksmuts");
        db.addLevelToDBoldTable(2364, "verstekbak", "verstekbak");
        db.addLevelToDBoldTable(2365, "bladerdeeg roosje", "bladerdeegroosje");
        db.addLevelToDBoldTable(2366, "cocktail shaker", "cocktailshaker");
        db.addLevelToDBoldTable(2367, "babyfoon", "babyfoon");
        db.addLevelToDBoldTable(2368, "laminaatsnijder", "laminaatsnijder");
        db.addLevelToDBoldTable(2369, "kroonsteentje", "kroonsteentje");
        db.addLevelToDBoldTable(2370, "eerste kamer", "eerstekamer");
        db.addLevelToDBoldTable(2371, "broodmandje", "broodmandje");
        db.addLevelToDBoldTable(2372, "kofferband", "kofferband");
        db.addLevelToDBoldTable(2373, "emoji", "emoji");
        db.addLevelToDBoldTable(2374, "hofnar", "hofnar");
        db.addLevelToDBoldTable(2375, "pacman", "pacman");

    }

    private void addLevels200to260() {
        db.addLevelToDBoldTable(201, "denneboom", "denneboom");
        db.addLevelToDBoldTable(202, "spaken", "spaken");
        db.addLevelToDBoldTable(203, "brievenbus", "brievenbus");
        db.addLevelToDBoldTable(204, "sleutel", "sleutel2");
        db.addLevelToDBoldTable(205, "leeuw", "leeuw");
        db.addLevelToDBoldTable(206, "ananas", "ananas");
        db.addLevelToDBoldTable(207, "beschuit", "beschuit");
        db.addLevelToDBoldTable(208, "nagel", "nagel");
        db.addLevelToDBoldTable(209, "citroen", "citroen");
        db.addLevelToDBoldTable(210, "tong", "tong");
        db.addLevelToDBoldTable(211, "palmboom", "palmboom");
        db.addLevelToDBoldTable(212, "chocolade", "chocolade");
        db.addLevelToDBoldTable(213, "katrol", "katrol");
        db.addLevelToDBoldTable(214, "tulp", "tulp");
        db.addLevelToDBoldTable(215, "strobaal", "strobaal");
        db.addLevelToDBoldTable(216, "kokosnoot", "kokosnoot");
        db.addLevelToDBoldTable(217, "waterval", "waterval");
        db.addLevelToDBoldTable(218, "kano", "kano");
        db.addLevelToDBoldTable(219, "blikje", "blikje");
        db.addLevelToDBoldTable(220, "wijnglas", "wijnglas");
        db.addLevelToDBoldTable(221, "winegum", "wijngum");
        db.addLevelToDBoldTable(222, "garnaal", "garnaal");
        db.addLevelToDBoldTable(223, "kachel", "kachel");
        db.addLevelToDBoldTable(224, "zeester", "zeester");
        db.addLevelToDBoldTable(225, "kippenpoot", "kippenpoot");
        db.addLevelToDBoldTable(226, "zwaard", "zwaard");
        db.addLevelToDBoldTable(227, "klomp", "klomp");
        db.addLevelToDBoldTable(228, "spaarpot", "spaarpot");
        db.addLevelToDBoldTable(229, "rijst", "rijst");
        db.addLevelToDBoldTable(230, "kaas", "kaas2");
        db.addLevelToDBoldTable(231, "oliebol", "oliebol");
        db.addLevelToDBoldTable(232, "diamant", "diamant");
        db.addLevelToDBoldTable(233, "bidon", "bidon");
        db.addLevelToDBoldTable(234, "medaille", "medaille");
        db.addLevelToDBoldTable(235, "keycord", "keycord");
        db.addLevelToDBoldTable(236, "zee egel", "zeeegel");
        db.addLevelToDBoldTable(237, "spaghetti", "spaghetti");
        db.addLevelToDBoldTable(238, "kraslot", "kraslot");
        db.addLevelToDBoldTable(239, "suiker klontje", "suikerklontje");
        db.addLevelToDBoldTable(240, "aardappel", "aardappel");
        db.addLevelToDBoldTable(241, "navel", "navel");
        db.addLevelToDBoldTable(242, "tie wrap", "tiewrap");
        db.addLevelToDBoldTable(243, "mango", "mango");
        db.addLevelToDBoldTable(244, "vlinder", "vlinder");
        db.addLevelToDBoldTable(245, "schep", "schep");
        db.addLevelToDBoldTable(246, "knuppel", "knuppel");
        db.addLevelToDBoldTable(247, "magnetron", "magnetron");
        db.addLevelToDBoldTable(248, "vijf euro", "vijfeuro");
        db.addLevelToDBoldTable(249, "koe", "koe");
        db.addLevelToDBoldTable(250, "tondeuse", "tondeuse");
        db.addLevelToDBoldTable(251, "grafkist", "grafkist");
        db.addLevelToDBoldTable(252, "afstandsbediening", "afstandsbediening");
        db.addLevelToDBoldTable(253, "zeepbel", "zeepbel");
        db.addLevelToDBoldTable(254, "winkelwagen", "winkelwagen");
        db.addLevelToDBoldTable(255, "floppy", "floppy");
        db.addLevelToDBoldTable(256, "bieropener", "bieropener");
        db.addLevelToDBoldTable(257, "zonnebril", "zonnebril");
        db.addLevelToDBoldTable(258, "kers", "kers");
        db.addLevelToDBoldTable(259, "monopoly", "monopoly");
        db.addLevelToDBoldTable(260, "patat", "patat");
    }

    private void addLevels261to320() {
        db.addLevelToDBoldTable(261, "zalm", "zalm");
        db.addLevelToDBoldTable(262, "boek", "boek");
        db.addLevelToDBoldTable(263, "rookworst", "rookworst");
        db.addLevelToDBoldTable(264, "speaker", "speaker");
        db.addLevelToDBoldTable(265, "dennen appel", "dennenappel");
        db.addLevelToDBoldTable(266, "badminton racket", "badmintonracket");
        db.addLevelToDBoldTable(267, "cassette bandje", "cassettebandje");
        db.addLevelToDBoldTable(268, "bureaustoel", "bureaustoel");
        db.addLevelToDBoldTable(269, "klokhuis", "klokhuis");
        db.addLevelToDBoldTable(270, "schelp", "schelp");
        db.addLevelToDBoldTable(271, "donald duck", "donaldduck");
        db.addLevelToDBoldTable(272, "eiffeltoren", "eiffeltoren");
        db.addLevelToDBoldTable(273, "handboeien", "handboeien");
        db.addLevelToDBoldTable(274, "kiwi", "kiwi2");
        db.addLevelToDBoldTable(275, "kluis", "kluis");
        db.addLevelToDBoldTable(276, "kurken trekker", "kurkentrekker");
        db.addLevelToDBoldTable(277, "schaakbord", "schaakbord");
        db.addLevelToDBoldTable(278, "ontstopper", "ontstopper");
        db.addLevelToDBoldTable(279, "lychee", "lychee");
        db.addLevelToDBoldTable(280, "megafoon", "megafoon");
        db.addLevelToDBoldTable(281, "suikerspin", "suikerspin");
        db.addLevelToDBoldTable(282, "risk", "risk");
        db.addLevelToDBoldTable(283, "wijnfles", "wijnfles");
        db.addLevelToDBoldTable(284, "prei", "prei");
        db.addLevelToDBoldTable(285, "moer sleutel", "moersleutel");
        db.addLevelToDBoldTable(286, "tompoes", "tompoes");
        db.addLevelToDBoldTable(287, "eend", "eend");
        db.addLevelToDBoldTable(288, "huifkar", "huifkar");
        db.addLevelToDBoldTable(289, "pardoes", "pardoes");
        db.addLevelToDBoldTable(290, "douchekop", "douchekop");
        db.addLevelToDBoldTable(291, "praatpaal", "praatpaal");
        db.addLevelToDBoldTable(292, "flipper kast", "flipperkast");
        db.addLevelToDBoldTable(293, "perforator", "perforator");
        db.addLevelToDBoldTable(294, "scharnier", "scharnier");
        db.addLevelToDBoldTable(295, "asbak", "asbak");
        db.addLevelToDBoldTable(296, "vlieger", "vlieger");
        db.addLevelToDBoldTable(297, "brood rooster", "tostiijzer");
        db.addLevelToDBoldTable(298, "spiegelei", "spiegelei");
        db.addLevelToDBoldTable(299, "rode ui", "rodeui");
        db.addLevelToDBoldTable(300, "strippen kaart", "strippenkaart");
        db.addLevelToDBoldTable(301, "pincet", "pincet");
        db.addLevelToDBoldTable(302, "prikkeldraad", "prikkeldraad");
        db.addLevelToDBoldTable(303, "kwal", "kwal");
        db.addLevelToDBoldTable(304, "zand", "zand");
        db.addLevelToDBoldTable(305, "duct tape", "ducktape");
        db.addLevelToDBoldTable(306, "kastanje", "kastanje");
        db.addLevelToDBoldTable(307, "fluitje", "fluitje");
        db.addLevelToDBoldTable(308, "boks handschoen", "bokshandschoen");
        db.addLevelToDBoldTable(309, "kruiswoord puzzel", "kruiswoordpuzzel");
        db.addLevelToDBoldTable(310, "stoepkrijt", "stoepkrijt");
        db.addLevelToDBoldTable(311, "haai", "haai");
        db.addLevelToDBoldTable(312, "sjoelbak", "sjoelbak");
        db.addLevelToDBoldTable(313, "geo driehoek", "geodriehoek");
        db.addLevelToDBoldTable(314, "sultana", "sultana");
        db.addLevelToDBoldTable(315, "touw", "touw");
        db.addLevelToDBoldTable(316, "treinrails", "treinrails");
        db.addLevelToDBoldTable(317, "peper", "peper");
        db.addLevelToDBoldTable(318, "borden wisser", "bordenwisser");
        db.addLevelToDBoldTable(319, "lamellen", "lamellen");
        db.addLevelToDBoldTable(320, "ruiten wisser", "ruitenwisser");
    }


    private void addLevels321to350() {
        db.addLevelToDBoldTable(321, "Batterij", "batterij2");
        db.addLevelToDBoldTable(322, "Zakmes", "zakmes");
        db.addLevelToDBoldTable(323, "Kompas", "kompas");
        db.addLevelToDBoldTable(324, "Bamboe", "bamboe");
        db.addLevelToDBoldTable(325, "Afvoer putje", "afvoerputje");
        db.addLevelToDBoldTable(326, "Bassie", "bassie");
        db.addLevelToDBoldTable(327, "Spotify", "spotify");
        db.addLevelToDBoldTable(328, "Waterkoker", "waterkoker");
        db.addLevelToDBoldTable(329, "Koekenpan", "koekenpan");
        db.addLevelToDBoldTable(330, "Vliegtuig", "vliegtuig");
        db.addLevelToDBoldTable(331, "Waterpas", "waterpas");
        db.addLevelToDBoldTable(332, "Frituurpan", "frituurpan");
        db.addLevelToDBoldTable(333, "Popcorn", "popcorn");
        db.addLevelToDBoldTable(334, "Tuinslang", "tuinslang");
        db.addLevelToDBoldTable(335, "Elpee", "elpee");
        db.addLevelToDBoldTable(336, "Rolmaat", "rolmaat");
        db.addLevelToDBoldTable(337, "Snoei schaar", "snoeischaar");
        db.addLevelToDBoldTable(338, "Skippybal", "skippybal");
        db.addLevelToDBoldTable(339, "Roze koek", "rozekoek");
        db.addLevelToDBoldTable(340, "Kruiwagen", "kruiwagen");
        db.addLevelToDBoldTable(341, "Zeep pompje", "zeeppompje");
        db.addLevelToDBoldTable(342, "Bowling kegel", "bowlingkegel");
        db.addLevelToDBoldTable(343, "Reddings boei", "reddingsboei");
        db.addLevelToDBoldTable(344, "dromen vanger", "dromenvanger");
        db.addLevelToDBoldTable(345, "Fietszadel", "fietszadel");
        db.addLevelToDBoldTable(346, "Zonne paneel", "zonnepaneel");
        db.addLevelToDBoldTable(347, "Blikopener", "blikopener");
        db.addLevelToDBoldTable(348, "Mikado", "mikado");
        db.addLevelToDBoldTable(349, "planten spuit", "plantenspuit");
        db.addLevelToDBoldTable(350, "horloge", "horloge2");
    }

    private void addLevels351to450() {
        db.addLevelToDBoldTable(351, "ijskrabber", "ijskrabber");
        db.addLevelToDBoldTable(352, "kroket", "kroket");
        db.addLevelToDBoldTable(353, "bbq", "bbq");
        db.addLevelToDBoldTable(354, "bierkrat", "bierkrat");
        db.addLevelToDBoldTable(355, "cartridge", "cartridge");
        db.addLevelToDBoldTable(356, "asperges", "asperges");
        db.addLevelToDBoldTable(357, "flippers", "flippers");
        db.addLevelToDBoldTable(358, "deurmat", "deurmat");
        db.addLevelToDBoldTable(359, "foto rolletje", "fotorolletje");
        db.addLevelToDBoldTable(360, "braam", "braam");
        db.addLevelToDBoldTable(361, "bladblazer", "bladblazer");
        db.addLevelToDBoldTable(362, "croissant", "croissant");
        db.addLevelToDBoldTable(363, "rijbewijs", "rijbewijs");
        db.addLevelToDBoldTable(364, "thee", "thee");
        db.addLevelToDBoldTable(365, "hersenen", "hersenen");
        db.addLevelToDBoldTable(366, "kattenbak", "kattenbak");
        db.addLevelToDBoldTable(367, "schaap", "schaap");
        db.addLevelToDBoldTable(368, "ijsbergsla", "ijsbergsla");
        db.addLevelToDBoldTable(369, "taj mahal", "tajmahal");
        db.addLevelToDBoldTable(370, "amandel", "amandel");
        db.addLevelToDBoldTable(371, "wc pot", "wcpot");
        db.addLevelToDBoldTable(372, "politie auto", "politieauto");
        db.addLevelToDBoldTable(373, "krijtbord", "krijtbord");
        db.addLevelToDBoldTable(374, "vinger afdruk", "vingerafdruk");
        db.addLevelToDBoldTable(375, "usa", "usa");
        db.addLevelToDBoldTable(376, "ventilator", "ventilator");
        db.addLevelToDBoldTable(377, "tosti", "tosti");
        db.addLevelToDBoldTable(378, "ton", "ton");
        db.addLevelToDBoldTable(379, "tarwe", "tarwe");
        db.addLevelToDBoldTable(380, "sushi", "sushi");
        db.addLevelToDBoldTable(381, "voetbal tafel", "voetbaltafel");
        db.addLevelToDBoldTable(382, "strijk plank", "strijkplank");
        db.addLevelToDBoldTable(383, "straal jager", "straaljager");
        db.addLevelToDBoldTable(384, "stokbrood", "stokbrood");
        db.addLevelToDBoldTable(385, "speen", "speen");
        db.addLevelToDBoldTable(386, "sombrero", "sombrero");
        db.addLevelToDBoldTable(387, "scheer apparaat", "scheerapparaat");
        db.addLevelToDBoldTable(388, "pudding", "pudding");
        db.addLevelToDBoldTable(389, "mijter", "mijter");
        db.addLevelToDBoldTable(390, "perzik", "perzik");
        db.addLevelToDBoldTable(391, "touwladder", "touwladder");
        db.addLevelToDBoldTable(392, "zebra", "zebra");
        db.addLevelToDBoldTable(393, "wortel", "wortel");
        db.addLevelToDBoldTable(394, "paarden bloem", "paardebloem");
        db.addLevelToDBoldTable(395, "loopband", "loopband");
        db.addLevelToDBoldTable(396, "punaise", "punaise");
        db.addLevelToDBoldTable(397, "sleutel gat", "sleutelgat");
        db.addLevelToDBoldTable(398, "tijger", "tijger");
        db.addLevelToDBoldTable(399, "papegaai", "papegaai");
        db.addLevelToDBoldTable(400, "cornflakes", "cornflakes");
        db.addLevelToDBoldTable(401, "muesli", "muesli");
        db.addLevelToDBoldTable(402, "spinnen web", "spinnenweb");
        db.addLevelToDBoldTable(403, "bolletje wol", "bolletjewol");
        db.addLevelToDBoldTable(404, "revolver", "revolver");
        db.addLevelToDBoldTable(405, "ei", "ei");
        db.addLevelToDBoldTable(406, "zout", "zout");
        db.addLevelToDBoldTable(407, "mens erger je niet", "mensergerjeniet");
        db.addLevelToDBoldTable(408, "helm", "helm");
        db.addLevelToDBoldTable(409, "post it", "postit");
        db.addLevelToDBoldTable(410, "balpen", "balpen");
        db.addLevelToDBoldTable(411, "koptelefoon", "koptelefoon");
        db.addLevelToDBoldTable(412, "vuurwerk", "vuurwerk");
        db.addLevelToDBoldTable(413, "sprinkhaan", "sprinkhaan");
        db.addLevelToDBoldTable(414, "internet kabel", "internetkabel");
        db.addLevelToDBoldTable(415, "wasrek", "wasrek");
        db.addLevelToDBoldTable(416, "autodrop", "autodrop");
        db.addLevelToDBoldTable(417, "peer", "peer");
        db.addLevelToDBoldTable(418, "gameboy", "gameboy");
        db.addLevelToDBoldTable(419, "bushalte", "bushalte");
        db.addLevelToDBoldTable(420, "geit", "geit");
        db.addLevelToDBoldTable(421, "schoorsteen", "schoorsteen");
        db.addLevelToDBoldTable(422, "panty", "panty");
        db.addLevelToDBoldTable(423, "sok", "sok");
        db.addLevelToDBoldTable(424, "sandaal", "sandaal");
        db.addLevelToDBoldTable(425, "teenslipper", "teenslipper");
        db.addLevelToDBoldTable(426, "houtskool", "houtskool");
        db.addLevelToDBoldTable(427, "kipsate", "kipsate");
        db.addLevelToDBoldTable(428, "hamburger", "hamburger");
        db.addLevelToDBoldTable(429, "augurk", "augurk");
        db.addLevelToDBoldTable(430, "discobal", "discobal");
        db.addLevelToDBoldTable(431, "elastiek", "elastiek");
        db.addLevelToDBoldTable(432, "framboos", "framboos");
        db.addLevelToDBoldTable(433, "knoflook", "knoflook");
        db.addLevelToDBoldTable(434, "sesamzaad", "sesamzaad");
        db.addLevelToDBoldTable(435, "filet americain", "filetamericain");
        db.addLevelToDBoldTable(436, "koffiemelk", "koffiemelk");
        db.addLevelToDBoldTable(437, "roltrap", "roltrap");
        db.addLevelToDBoldTable(438, "karton", "karton");
        db.addLevelToDBoldTable(439, "uier", "uier");
        db.addLevelToDBoldTable(440, "krokodil", "krokodil");
        db.addLevelToDBoldTable(441, "uil", "uil");
        db.addLevelToDBoldTable(442, "tanden", "tanden");
        db.addLevelToDBoldTable(443, "tandwiel", "tandwiel");
        db.addLevelToDBoldTable(444, "voetbal veld", "voetbalveld");
        db.addLevelToDBoldTable(445, "rollade", "rollade");
        db.addLevelToDBoldTable(446, "cobra", "cobra");
        db.addLevelToDBoldTable(447, "baklava", "baklava");
        db.addLevelToDBoldTable(448, "shoarma", "shoarma");
        db.addLevelToDBoldTable(449, "knikker", "knikker");
        db.addLevelToDBoldTable(450, "kraaltjes", "kraaltjes");
    }

    private void addLevels451to522() {
        db.addLevelToDBoldTable(451, "bijbel", "bijbel");
        db.addLevelToDBoldTable(452, "strandbal", "strandbal");
        db.addLevelToDBoldTable(453, "zand kasteel", "zandkasteel");
        db.addLevelToDBoldTable(454, "afwas borstel", "afwasborstel");
        db.addLevelToDBoldTable(455, "dweil", "dweil");
        db.addLevelToDBoldTable(456, "lava", "lava");
        db.addLevelToDBoldTable(457, "regenpijp", "regenpijp");
        db.addLevelToDBoldTable(458, "kalender", "kalender");
        db.addLevelToDBoldTable(459, "verrekijker", "verrekijker");
        db.addLevelToDBoldTable(460, "microscoop", "microscoop");
        db.addLevelToDBoldTable(461, "blokfluit", "blokfluit");
        db.addLevelToDBoldTable(462, "kilometer teller", "kilometerteller");
        db.addLevelToDBoldTable(463, "stopwatch", "stopwatch");
        db.addLevelToDBoldTable(464, "washand", "washand");
        db.addLevelToDBoldTable(465, "stift", "stift");
        db.addLevelToDBoldTable(466, "slak", "slak");
        db.addLevelToDBoldTable(467, "tulp", "tulp");
        db.addLevelToDBoldTable(468, "bruine bonen", "bruinebonen");
        db.addLevelToDBoldTable(469, "paddenstoel", "paddestoel");
        db.addLevelToDBoldTable(470, "dakpannen", "dakpannen");
        db.addLevelToDBoldTable(471, "eland", "eland");
        db.addLevelToDBoldTable(472, "spaanse peper", "spaansepeper");
        db.addLevelToDBoldTable(473, "koraal", "koraal");
        db.addLevelToDBoldTable(474, "lelie", "lelie");
        db.addLevelToDBoldTable(475, "colosseum", "colosseum");
        db.addLevelToDBoldTable(476, "spijker broek", "spijkerbroek");
        db.addLevelToDBoldTable(477, "water meloen", "watermeloen");
        db.addLevelToDBoldTable(478, "vishaak", "vishaak");
        db.addLevelToDBoldTable(479, "flipperkast", "flipperkast");
        db.addLevelToDBoldTable(480, "malien kolder", "malienkolder");
        db.addLevelToDBoldTable(481, "kapstok", "kapstok");
        db.addLevelToDBoldTable(482, "pedaal emmer", "pedaalemmer");
        db.addLevelToDBoldTable(483, "neusspray", "neusspray");
        db.addLevelToDBoldTable(484, "vulpen", "vulpen");
        db.addLevelToDBoldTable(485, "mok", "mok");
        db.addLevelToDBoldTable(486, "telefoon draad", "telefoondraad");
        db.addLevelToDBoldTable(487, "kaarten", "kaarten");
        db.addLevelToDBoldTable(488, "hangslot", "hangslot");
        db.addLevelToDBoldTable(489, "hagelslag", "hagelslag");
        db.addLevelToDBoldTable(490, "trein", "trein");
        db.addLevelToDBoldTable(491, "hart monitor", "hartmonitor");
        db.addLevelToDBoldTable(492, "jacuzzi", "jacuzzi");
        db.addLevelToDBoldTable(493, "roulette", "roulette");
        db.addLevelToDBoldTable(494, "fruit machine", "fruitmachine");
        db.addLevelToDBoldTable(495, "sheriff ster", "sheriffster");
        db.addLevelToDBoldTable(496, "mixer", "mixer");
        db.addLevelToDBoldTable(497, "kraan", "kraan");
        db.addLevelToDBoldTable(498, "marsh mallow", "marshmallow");
        db.addLevelToDBoldTable(499, "hamer", "hamer");
        db.addLevelToDBoldTable(500, "glas in lood", "glasinlood");
        db.addLevelToDBoldTable(501, "visstick", "visstick");
        db.addLevelToDBoldTable(502, "blender", "blender");
        db.addLevelToDBoldTable(503, "vaatwasser", "vaatwasser");
        db.addLevelToDBoldTable(504, "koffiezet apparaat", "koffiezetapparaat");
        db.addLevelToDBoldTable(505, "citruspers", "citruspers");
        db.addLevelToDBoldTable(506, "ijsblokje", "ijsblokje");
        db.addLevelToDBoldTable(507, "halter", "halter");
        db.addLevelToDBoldTable(508, "ski", "ski");
        db.addLevelToDBoldTable(509, "zoete aardappel", "zoeteaardappel");
        db.addLevelToDBoldTable(510, "whatsapp", "whatsapp");
        db.addLevelToDBoldTable(511, "receptie bel", "receptiebel");
        db.addLevelToDBoldTable(512, "cycloop", "cycloop");
        db.addLevelToDBoldTable(513, "musket", "musket");
        db.addLevelToDBoldTable(514, "superman", "superman");
        db.addLevelToDBoldTable(515, "doolhof", "doolhof");
        db.addLevelToDBoldTable(516, "mosterd", "mosterd");
        db.addLevelToDBoldTable(517, "piraten vlag", "piratenvlag");
        db.addLevelToDBoldTable(518, "vishengel", "vishengel");
        db.addLevelToDBoldTable(519, "skibril", "skibril");
        db.addLevelToDBoldTable(520, "rolstoel", "rolstoel");
        db.addLevelToDBoldTable(521, "vliegtuig raam", "vliegtuigraam");
        db.addLevelToDBoldTable(522, "ghostbusters", "ghostbusters");

    }

    private void addLevels523to700() {
        db.addLevelToDBoldTable(523, "Euromast", "euromast");
        db.addLevelToDBoldTable(524, "tomaat", "tomaat");
        db.addLevelToDBoldTable(525, "Akropolis", "akropolis");
        db.addLevelToDBoldTable(526, "Domtoren", "domtoren");
        db.addLevelToDBoldTable(527, "Erasmus brug", "erasmusbrug");
        db.addLevelToDBoldTable(528, "Toren van Pisa", "torenvanpisa");
        db.addLevelToDBoldTable(529, "Notre Dame", "notredame");
        db.addLevelToDBoldTable(530, "Opera House", "operahouse");
        db.addLevelToDBoldTable(531, "Tower Bridge", "towerbridge");
        db.addLevelToDBoldTable(532, "Sagrada Familia", "sagradafamilia");
        db.addLevelToDBoldTable(533, "prei", "prei2");
        db.addLevelToDBoldTable(534, "Chinese Muur", "chinesemuur");
        db.addLevelToDBoldTable(535, "Arena", "arena");
        db.addLevelToDBoldTable(536, "Twin Towers", "twintowers");
        db.addLevelToDBoldTable(537, "Big Ben", "bigben");
        db.addLevelToDBoldTable(538, "Burj al Arab", "burjalarab");
        db.addLevelToDBoldTable(539, "Vrijheidsbeeld", "vrijheidsbeeld");
        db.addLevelToDBoldTable(540, "sate", "sate");
        db.addLevelToDBoldTable(541, "pita broodje", "pitabroodje");
        db.addLevelToDBoldTable(542, "wietblad", "wietblad");
        db.addLevelToDBoldTable(543, "chicken wing", "chickenwing");
        db.addLevelToDBoldTable(544, "creme brulee", "cremebrulee");
        db.addLevelToDBoldTable(545, "tiramisu", "tiramisu");
        db.addLevelToDBoldTable(546, "witlof", "witlof");
        db.addLevelToDBoldTable(547, "broccoli", "broccoli");
        db.addLevelToDBoldTable(548, "rode kool", "rodekool");
        db.addLevelToDBoldTable(549, "nagelvijl", "nagelvijl");
        db.addLevelToDBoldTable(550, "radijs", "radijs");
        db.addLevelToDBoldTable(551, "spruit", "spruit");
        db.addLevelToDBoldTable(552, "kabouter plop", "kabouterplop");
        db.addLevelToDBoldTable(553, "m en m", "menm");
        db.addLevelToDBoldTable(554, "zure mat", "zuremat");
        db.addLevelToDBoldTable(555, "engelse drop", "engelsedrop");
        db.addLevelToDBoldTable(556, "kauwgom", "kauwgom");
        db.addLevelToDBoldTable(557, "koevoet", "koevoet");
        db.addLevelToDBoldTable(558, "lijmklem", "lijmklem");
        db.addLevelToDBoldTable(559, "schuur machine", "schuurmachine");
        db.addLevelToDBoldTable(560, "vijzel", "vijzel");
        db.addLevelToDBoldTable(561, "wokpan", "wokpan");
        db.addLevelToDBoldTable(562, "maatbeker", "maatbeker");
        db.addLevelToDBoldTable(563, "pizza snijder", "pizzasnijder");
        db.addLevelToDBoldTable(564, "dollar", "dollar");
        db.addLevelToDBoldTable(565, "action man", "actionman");
        db.addLevelToDBoldTable(566, "abrikoos", "abrikoos");
        db.addLevelToDBoldTable(567, "avocado", "avocado");
        db.addLevelToDBoldTable(568, "auto sleutel", "autosleutel");
        db.addLevelToDBoldTable(569, "atomium", "atomium");
        db.addLevelToDBoldTable(570, "baksteen", "baksteen");
        db.addLevelToDBoldTable(571, "basketbal net", "basketbalnet");
        db.addLevelToDBoldTable(572, "pool tafel", "pooltafel");
        db.addLevelToDBoldTable(573, "big mac", "bigmac");
        db.addLevelToDBoldTable(574, "portemonnee", "portemonnee");
        db.addLevelToDBoldTable(575, "boomstam", "boomstam");
        db.addLevelToDBoldTable(576, "boter", "boter");
        db.addLevelToDBoldTable(577, "braadworst", "braadworst");
        db.addLevelToDBoldTable(578, "zuurkool", "zuurkool");
        db.addLevelToDBoldTable(579, "rookmelder", "rookmelder");
        db.addLevelToDBoldTable(580, "red bull", "redbull");
        db.addLevelToDBoldTable(581, "pallet", "pallet");
        db.addLevelToDBoldTable(582, "cavia", "cavia");
        db.addLevelToDBoldTable(583, "cijferslot", "cijferslot");
        db.addLevelToDBoldTable(584, "cornetto", "cornetto");
        db.addLevelToDBoldTable(585, "bugs bunny", "bugsbunny");
        db.addLevelToDBoldTable(586, "dolfijn", "dolfijn");
        db.addLevelToDBoldTable(587, "draaimolen", "draaimolen");
        db.addLevelToDBoldTable(588, "fietsbel", "fietsbel");
        db.addLevelToDBoldTable(589, "flitspaal", "flitspaal");
        db.addLevelToDBoldTable(590, "gasmasker", "gasmasker");
        db.addLevelToDBoldTable(591, "nummerbord", "nummerbord");
        db.addLevelToDBoldTable(592, "garenklos", "garenklos");
        db.addLevelToDBoldTable(593, "gasfles", "gasfles");
        db.addLevelToDBoldTable(594, "hand granaat", "handgranaat");
        db.addLevelToDBoldTable(595, "zebrapad", "zebrapad2");
        db.addLevelToDBoldTable(596, "hazelnoot", "hazelnoot");
        db.addLevelToDBoldTable(597, "hobbelpaard", "hobbelpaard");
        db.addLevelToDBoldTable(598, "ijslepel", "ijslepel");
        db.addLevelToDBoldTable(599, "jojo", "jojo");
        db.addLevelToDBoldTable(600, "kanon", "kanon");
        db.addLevelToDBoldTable(601, "kinder surprise", "kindersurprise");
        db.addLevelToDBoldTable(602, "kinder bueno", "kinderbueno");
        db.addLevelToDBoldTable(603, "kitkat", "kitkat");
        db.addLevelToDBoldTable(604, "pistache noten", "pistachenoten");
        db.addLevelToDBoldTable(605, "kruisboog", "kruisboog");
        db.addLevelToDBoldTable(606, "lampion", "lampion");
        db.addLevelToDBoldTable(607, "loempia", "loempia");
        db.addLevelToDBoldTable(608, "magneet", "magneet");
        db.addLevelToDBoldTable(609, "frikandel", "frikandel");
        db.addLevelToDBoldTable(610, "magnum", "magnum");
        db.addLevelToDBoldTable(611, "varken", "varken");
        db.addLevelToDBoldTable(612, "lamsrack", "lamsrack");
        db.addLevelToDBoldTable(613, "olijf", "olijf");
        db.addLevelToDBoldTable(614, "zonne wijzer", "zonnewijzer");
        db.addLevelToDBoldTable(615, "wielklem", "wielklem");
        db.addLevelToDBoldTable(616, "volleybal", "volleybal");
        db.addLevelToDBoldTable(617, "cashew noten", "cashewnoten");
        db.addLevelToDBoldTable(618, "webcam", "webcam");
        db.addLevelToDBoldTable(619, "vuvuzela", "vuvuzela");
        db.addLevelToDBoldTable(620, "vuurkorf", "vuurkorf");
        db.addLevelToDBoldTable(621, "cd hoesje", "cdhoesje");
        db.addLevelToDBoldTable(622, "tandpasta", "tandpasta");
        db.addLevelToDBoldTable(623, "lippen balsem", "lippenbalsem");
        db.addLevelToDBoldTable(624, "lippen", "lippen");
        db.addLevelToDBoldTable(625, "watten staafjes", "wattenstaafjes");
        db.addLevelToDBoldTable(626, "kubuswoning", "kubuswoning");
        db.addLevelToDBoldTable(627, "mascara", "mascara");
        db.addLevelToDBoldTable(628, "xylofoon", "xylofoon");
        db.addLevelToDBoldTable(629, "accordeon", "accordion");
        db.addLevelToDBoldTable(630, "gong", "gong");
        db.addLevelToDBoldTable(631, "panfluit", "panfluit");
        db.addLevelToDBoldTable(632, "airco", "airco");
        db.addLevelToDBoldTable(633, "skilift", "skilift");
        db.addLevelToDBoldTable(634, "achtbaan", "achtbaan");
        db.addLevelToDBoldTable(635, "reuzenrad", "reuzenrad");
        db.addLevelToDBoldTable(636, "tennisveld", "tennisveld");
        db.addLevelToDBoldTable(637, "korf", "korf");
        db.addLevelToDBoldTable(638, "wielren fiets", "wielrenfiets");
        db.addLevelToDBoldTable(639, "neushoorn", "neushoorn");
        db.addLevelToDBoldTable(640, "Disney kasteel", "disneykasteel");
        db.addLevelToDBoldTable(641, "bram ladage", "bramladage");
        db.addLevelToDBoldTable(642, "nachtwacht", "nachtwacht");
        db.addLevelToDBoldTable(643, "olifant", "olifant");
        db.addLevelToDBoldTable(644, "zeewier", "zeewier");
        db.addLevelToDBoldTable(645, "nutella", "nutella");
        db.addLevelToDBoldTable(646, "brownie", "brownie");
        db.addLevelToDBoldTable(647, "kaiser broodje", "keizerbroodje");
        db.addLevelToDBoldTable(648, "rijstwafel", "rijstwafel");
        db.addLevelToDBoldTable(649, "oreo", "oreo");
        db.addLevelToDBoldTable(650, "rozen bottel", "rozenbottel");
        db.addLevelToDBoldTable(651, "poffertjes", "poffertjes");
        db.addLevelToDBoldTable(652, "pringles", "pringles");
        db.addLevelToDBoldTable(653, "duplo", "duplo");
        db.addLevelToDBoldTable(654, "playmobil", "playmobil");
        db.addLevelToDBoldTable(655, "muisjes", "muisjes");
        db.addLevelToDBoldTable(656, "bosbes", "bosbes");
        db.addLevelToDBoldTable(657, "pepsi", "pepsi");
        db.addLevelToDBoldTable(658, "krultang", "krultang");
        db.addLevelToDBoldTable(659, "haar borstel", "haarborstel");
        db.addLevelToDBoldTable(660, "tampon", "tampon");
        db.addLevelToDBoldTable(661, "waterpijp", "waterpijp");
        db.addLevelToDBoldTable(662, "kipfilet", "kipfilet");
        db.addLevelToDBoldTable(663, "witte huis", "wittehuis");
        db.addLevelToDBoldTable(664, "haring", "haring");
        db.addLevelToDBoldTable(665, "wokkels", "wokkels");
        db.addLevelToDBoldTable(666, "tuitbeker", "tuitbeker");
        db.addLevelToDBoldTable(667, "vlokken", "vlokken");
        db.addLevelToDBoldTable(668, "waterrad", "waterrad");
        db.addLevelToDBoldTable(669, "koekoeks klok", "koekoeksklok");
        db.addLevelToDBoldTable(670, "sperziebonen", "sperziebonen");
        db.addLevelToDBoldTable(671, "pindakaas", "pindakaas");
        db.addLevelToDBoldTable(672, "scheer kwast", "scheerkwast");
        db.addLevelToDBoldTable(673, "schoffel", "schoffel");
        db.addLevelToDBoldTable(674, "theedoek", "theedoek");
        db.addLevelToDBoldTable(675, "thee ei", "theeei");
        db.addLevelToDBoldTable(676, "postzegel", "postzegel");
        db.addLevelToDBoldTable(677, "fluitketel", "fluitketel");
        db.addLevelToDBoldTable(678, "snijbonen", "snijbonen");
        db.addLevelToDBoldTable(679, "rucola", "rucola");
        db.addLevelToDBoldTable(680, "grapefruit", "grapefruit");
        db.addLevelToDBoldTable(681, "duikboot", "duikboot");
        db.addLevelToDBoldTable(682, "kiwi", "kiwivogel");
        db.addLevelToDBoldTable(683, "mickey mouse", "mickeymouse");
        db.addLevelToDBoldTable(684, "boerenkool", "boerenkool");
        db.addLevelToDBoldTable(685, "zweedse puzzel", "zweedsepuzzel");
        db.addLevelToDBoldTable(686, "sudoku", "sudoku");
        db.addLevelToDBoldTable(687, "woordzoeker", "woordzoeker");
        db.addLevelToDBoldTable(688, "spatel", "spatel");
        db.addLevelToDBoldTable(689, "vergiet", "vergiet");
        db.addLevelToDBoldTable(690, "madurodam", "madurodam");
        db.addLevelToDBoldTable(691, "kroon", "kroon");
        db.addLevelToDBoldTable(692, "hoepel", "hoepel");
        db.addLevelToDBoldTable(693, "popeye", "popeye");
        db.addLevelToDBoldTable(694, "zuurstok", "zuurstok");
        db.addLevelToDBoldTable(695, "teletubbie", "teletubbie");
        db.addLevelToDBoldTable(696, "vikinghelm", "vikinghelm");
        db.addLevelToDBoldTable(697, "banjo", "banjo");
        db.addLevelToDBoldTable(698, "ukelele", "ukulele");
        db.addLevelToDBoldTable(699, "viool", "viool");
        db.addLevelToDBoldTable(700, "doperwten", "doperwten");
    }

    private void addLevels701to900() {
        db.addLevelToDBoldTable(701, "Cupcake", "cupcake");
        db.addLevelToDBoldTable(702, "Koelkast", "koelkast");
        db.addLevelToDBoldTable(703, "Ladder", "ladder");
        db.addLevelToDBoldTable(704, "Tractor", "tractor");
        db.addLevelToDBoldTable(705, "Carpaccio", "carpaccio");
        db.addLevelToDBoldTable(706, "Kerk", "kerk");
        db.addLevelToDBoldTable(707, "Moskee", "moskee");
        db.addLevelToDBoldTable(708, "Vlinder das", "vlinderdas");
        db.addLevelToDBoldTable(709, "Dwarsfluit", "dwarsfluit");
        db.addLevelToDBoldTable(710, "Picknick mand", "picknickmand");
        db.addLevelToDBoldTable(711, "Appeltaart", "appeltaart");
        db.addLevelToDBoldTable(712, "Stroopwafel", "stroopwafel");
        db.addLevelToDBoldTable(713, "Wigwam", "wigwam");
        db.addLevelToDBoldTable(714, "Oor", "oor");
        db.addLevelToDBoldTable(715, "Neus", "neus");
        db.addLevelToDBoldTable(716, "Totempaal", "totempaal");
        db.addLevelToDBoldTable(717, "Veren tooi", "verentooi");
        db.addLevelToDBoldTable(718, "Gevulde koek", "gevuldekoek");
        db.addLevelToDBoldTable(719, "Speculaas", "speculaas");
        db.addLevelToDBoldTable(720, "Kruidnoten", "kruidnoten");
        db.addLevelToDBoldTable(721, "Zonnebank", "zonnebank");
        db.addLevelToDBoldTable(722, "Sauna", "sauna");
        db.addLevelToDBoldTable(723, "Beamer", "beamer");
        db.addLevelToDBoldTable(724, "Tonijn", "tonijn");
        db.addLevelToDBoldTable(725, "Zaagsel", "zaagsel");
        db.addLevelToDBoldTable(726, "Cola", "cola");
        db.addLevelToDBoldTable(727, "Erwtensoep", "erwtensoep");
        db.addLevelToDBoldTable(728, "Stoffer en blik", "stofferenblik");
        db.addLevelToDBoldTable(729, "Boterham worst", "boterhamworst");
        db.addLevelToDBoldTable(730, "Bonbon", "bonbon");
        db.addLevelToDBoldTable(731, "Split ijsje", "splitijsje");
        db.addLevelToDBoldTable(732, "Calippo", "calippo");
        db.addLevelToDBoldTable(733, "Verfroller", "verfroller");
        db.addLevelToDBoldTable(734, "Krokus", "krokus");
        db.addLevelToDBoldTable(735, "tandem", "tandem");
        db.addLevelToDBoldTable(736, "kliko", "kliko");
        db.addLevelToDBoldTable(737, "slee", "slee");
        db.addLevelToDBoldTable(738, "brieven opener", "brievenopener");
        db.addLevelToDBoldTable(739, "bitterbal", "bitterbal");
        db.addLevelToDBoldTable(740, "kaas souffle", "kaassouffle");
        db.addLevelToDBoldTable(741, "mojito", "mojito");
        db.addLevelToDBoldTable(742, "droogkap", "droogkap");
        db.addLevelToDBoldTable(743, "champignon", "champignon");
        db.addLevelToDBoldTable(744, "pretzel", "pretzel");
        db.addLevelToDBoldTable(745, "bifi", "bifi");
        db.addLevelToDBoldTable(746, "lens", "lens");
        db.addLevelToDBoldTable(747, "satelliet", "satelliet");
        db.addLevelToDBoldTable(748, "airbag", "airbag");
        db.addLevelToDBoldTable(749, "ernie", "ernie");
        db.addLevelToDBoldTable(750, "mastermind", "mastermind");
        db.addLevelToDBoldTable(751, "jerrycan", "jerrycan");
        db.addLevelToDBoldTable(752, "staatslot", "staatslot");
        db.addLevelToDBoldTable(753, "barbie", "barbie");
        db.addLevelToDBoldTable(754, "draken vrucht", "drakenvrucht");
        db.addLevelToDBoldTable(755, "honden brokken", "hondenbrokken");
        db.addLevelToDBoldTable(756, "ruitjes papier", "ruitjespapier");
        db.addLevelToDBoldTable(757, "mol", "mol");
        db.addLevelToDBoldTable(758, "surfplank", "surfplank");
        db.addLevelToDBoldTable(759, "reddings vest", "reddingsvest");
        db.addLevelToDBoldTable(760, "bankschroef", "bankschroef");
        db.addLevelToDBoldTable(761, "locomotief", "locomotief");
        db.addLevelToDBoldTable(762, "kunstgebit", "kunstgebit");
        db.addLevelToDBoldTable(763, "egel", "egel");
        db.addLevelToDBoldTable(764, "parachute", "parachute");
        db.addLevelToDBoldTable(765, "passie vrucht", "passievrucht");
        db.addLevelToDBoldTable(766, "nijptang", "nijptang");
        db.addLevelToDBoldTable(767, "bliksem", "bliksem");
        db.addLevelToDBoldTable(768, "hijskraan", "hijskraan");
        db.addLevelToDBoldTable(769, "bulldozer", "bulldozer");
        db.addLevelToDBoldTable(770, "batman", "batman");
        db.addLevelToDBoldTable(771, "kroon luchter", "kroonluchter");
        db.addLevelToDBoldTable(772, "rontgen foto", "rontgenfoto");
        db.addLevelToDBoldTable(773, "playstation", "playstation");
        db.addLevelToDBoldTable(774, "wii", "wii");
        db.addLevelToDBoldTable(775, "dienblad", "dienblad");
        db.addLevelToDBoldTable(776, "pijp", "pijp");
        db.addLevelToDBoldTable(777, "openhaard", "openhaard");
        db.addLevelToDBoldTable(778, "vlecht", "vlecht");
        db.addLevelToDBoldTable(779, "tuin kabouter", "tuinkabouter");
        db.addLevelToDBoldTable(780, "mier", "mier");
        db.addLevelToDBoldTable(781, "honing", "honing");
        db.addLevelToDBoldTable(782, "grafiek", "grafiek");
        db.addLevelToDBoldTable(783, "sneeuwpop", "sneeuwpop");
        db.addLevelToDBoldTable(784, "parel", "parel");
        db.addLevelToDBoldTable(785, "oester", "oester");
        db.addLevelToDBoldTable(786, "hierogliefen", "hierogliefen");
        db.addLevelToDBoldTable(787, "pikhouweel", "pikhouweel");
        db.addLevelToDBoldTable(788, "wc bril", "wcbril");
        db.addLevelToDBoldTable(789, "monocle", "monocle");
        db.addLevelToDBoldTable(790, "bolhoed", "bolhoed");
        db.addLevelToDBoldTable(791, "confetti", "confetti");
        db.addLevelToDBoldTable(792, "heks", "heks");
        db.addLevelToDBoldTable(793, "kruimeldief", "kruimeldief");
        db.addLevelToDBoldTable(794, "wandelstok", "wandelstok");
        db.addLevelToDBoldTable(795, "domino steen", "dominosteen");
        db.addLevelToDBoldTable(796, "veer", "veer");
        db.addLevelToDBoldTable(797, "melkbus", "melkbus");
        db.addLevelToDBoldTable(798, "draak", "draak");
        db.addLevelToDBoldTable(799, "piramide", "piramide");
        db.addLevelToDBoldTable(800, "farao", "farao");
        db.addLevelToDBoldTable(801, "Ordner", "ordner");
        db.addLevelToDBoldTable(802, "wereldbol", "globe");
        db.addLevelToDBoldTable(803, "delfts blauw", "delftsblauw");
        db.addLevelToDBoldTable(804, "bobslee", "bobslee");
        db.addLevelToDBoldTable(805, "parfum", "parfum");
        db.addLevelToDBoldTable(806, "goudstaaf", "goudstaaf");
        db.addLevelToDBoldTable(807, "olievat", "olievat");
        db.addLevelToDBoldTable(808, "wifi", "wifi");
        db.addLevelToDBoldTable(809, "brief", "brief");
        db.addLevelToDBoldTable(810, "pvc buis", "pvcbuis");
        db.addLevelToDBoldTable(811, "brandtrap", "brandtrap");
        db.addLevelToDBoldTable(812, "kastanje blad", "kastanjeblad");
        db.addLevelToDBoldTable(813, "churros", "churros");
        db.addLevelToDBoldTable(814, "soldeerbout", "soldeerbout");
        db.addLevelToDBoldTable(815, "Printer", "printer");
        db.addLevelToDBoldTable(816, "Telelens", "telelens");
        db.addLevelToDBoldTable(817, "verboden te roken", "verbodenteroken");
        db.addLevelToDBoldTable(818, "wuppie", "wuppie");
        db.addLevelToDBoldTable(819, "flippo", "flippo");
        db.addLevelToDBoldTable(820, "kakkerlak", "kakkerlak");
        db.addLevelToDBoldTable(821, "limousine", "limousine");
        db.addLevelToDBoldTable(822, "ernie", "ernie");
        db.addLevelToDBoldTable(823, "warmhoud plaat", "warmhoudplaat");
        db.addLevelToDBoldTable(824, "snijplank", "snijplank");
        db.addLevelToDBoldTable(825, "legpuzzel", "legpuzzel");
        db.addLevelToDBoldTable(826, "bert", "bert");
        db.addLevelToDBoldTable(827, "pino", "pino");
        db.addLevelToDBoldTable(828, "rotje", "rotje");
        db.addLevelToDBoldTable(829, "Aquarium", "aquarium");
        db.addLevelToDBoldTable(830, "suske", "suske");
        db.addLevelToDBoldTable(831, "obelix", "obelix");
        db.addLevelToDBoldTable(832, "lucky luke", "luckyluke");
        db.addLevelToDBoldTable(833, "Boterbloem", "boterbloem");
        db.addLevelToDBoldTable(834, "Boterletter", "boterletter");
        db.addLevelToDBoldTable(835, "Kruidnagel", "kruidnagel");
        db.addLevelToDBoldTable(836, "Ping pong bal", "pingpongbal");
        db.addLevelToDBoldTable(837, "Urn", "urn");
        db.addLevelToDBoldTable(838, "Grafsteen", "grafsteen");
        db.addLevelToDBoldTable(839, "Biertap", "biertap");
        db.addLevelToDBoldTable(840, "Stroop", "stroop");
        db.addLevelToDBoldTable(841, "Pannenkoek", "pannenkoek");
        db.addLevelToDBoldTable(842, "Frisbee", "frisbee");
        db.addLevelToDBoldTable(843, "Trekdrop", "trekdrop");
        db.addLevelToDBoldTable(844, "Bros", "bros");
        db.addLevelToDBoldTable(845, "Strop", "strop");
        db.addLevelToDBoldTable(846, "Vetbol", "vetbol");
        db.addLevelToDBoldTable(847, "Vuurpijl", "vuurpijl");
        db.addLevelToDBoldTable(848, "Gulden", "gulden");
        db.addLevelToDBoldTable(849, "Mossel", "mossel");
        db.addLevelToDBoldTable(850, "Champagne", "champagne");
        db.addLevelToDBoldTable(851, "Koelbox", "koelbox");
        db.addLevelToDBoldTable(852, "circus", "circus");
        db.addLevelToDBoldTable(853, "Ambulance", "ambulance");
        db.addLevelToDBoldTable(854, "Zwaan", "zwaan");
        db.addLevelToDBoldTable(855, "Pipet", "pipet");
        db.addLevelToDBoldTable(856, "Trommel", "trommel");
        db.addLevelToDBoldTable(857, "zee container", "zeecontainer");
        db.addLevelToDBoldTable(858, "ov poortje", "ovpoortje");
        db.addLevelToDBoldTable(859, "tramhalte", "tramhalte");
        db.addLevelToDBoldTable(860, "kroepoek", "kroepoek");
        db.addLevelToDBoldTable(861, "inktvis", "inktvis");
        db.addLevelToDBoldTable(862, "zandbak", "zandbak");
        db.addLevelToDBoldTable(863, "platen speler", "platenspeler");
        db.addLevelToDBoldTable(864, "licht knopje", "lichtknopje");
        db.addLevelToDBoldTable(865, "schommel stoel", "schommelstoel");
        db.addLevelToDBoldTable(866, "stretcher", "stretcher");
        db.addLevelToDBoldTable(867, "brancard", "brancard");
        db.addLevelToDBoldTable(868, "racebaan", "racebaan");
        db.addLevelToDBoldTable(869, "deo stick", "deostick");
        db.addLevelToDBoldTable(870, "schildpad", "schildpad");
        db.addLevelToDBoldTable(871, "vliegen mepper", "vliegenmepper2");
        db.addLevelToDBoldTable(872, "waxine lichtje", "waxinelichtje");
        db.addLevelToDBoldTable(873, "kabelbaan", "kabelbaan");
        db.addLevelToDBoldTable(874, "mexicano", "mexicano");
        db.addLevelToDBoldTable(875, "sleep lift", "sleeplift");
        db.addLevelToDBoldTable(876, "oorwurm", "oorwurm");
        db.addLevelToDBoldTable(877, "wegenkaart", "wegenkaart");
        db.addLevelToDBoldTable(878, "chemisch toilet", "chemischtoilet");
        db.addLevelToDBoldTable(879, "windscherm", "windscherm");
        db.addLevelToDBoldTable(880, "laptop tas", "laptoptas");
        db.addLevelToDBoldTable(881, "gember", "gember");
        db.addLevelToDBoldTable(882, "appelflap", "appelflap");
        db.addLevelToDBoldTable(883, "slagroom taart", "slagroomtaart");
        db.addLevelToDBoldTable(884, "dropveter", "dropveter");
        db.addLevelToDBoldTable(885, "munt drop", "muntdrop");
        db.addLevelToDBoldTable(886, "biscuit", "biscuit");
        db.addLevelToDBoldTable(887, "heggen schaar", "heggenschaar");
        db.addLevelToDBoldTable(888, "pennen dop", "pennendop");
        db.addLevelToDBoldTable(889, "drie d bril", "driedbril");
        db.addLevelToDBoldTable(890, "goofy", "goofy");
        db.addLevelToDBoldTable(891, "badeend", "badeend");
        db.addLevelToDBoldTable(892, "mario", "mario");
        db.addLevelToDBoldTable(893, "kinderwagen", "kinderwagen");
        db.addLevelToDBoldTable(894, "cake", "cake");
        db.addLevelToDBoldTable(895, "container", "container");
        db.addLevelToDBoldTable(896, "cruesli", "cruesli");
        db.addLevelToDBoldTable(897, "glijbaan", "glijbaan");
        db.addLevelToDBoldTable(898, "drietand", "drietand");
        db.addLevelToDBoldTable(899, "elmo", "elmo");
        db.addLevelToDBoldTable(900, "etui", "etui");
    }

    private void addLevels901to1000() {
        db.addLevelToDBoldTable(901, "boterkoek", "boterkoek");
        db.addLevelToDBoldTable(902, "kas", "kas");
        db.addLevelToDBoldTable(903, "bretels", "bretels");
        db.addLevelToDBoldTable(904, "gummi knuppel", "gummiknuppel");
        db.addLevelToDBoldTable(905, "bouwhelm", "bouwhelm");
        db.addLevelToDBoldTable(906, "honden hok", "hondenhok");
        db.addLevelToDBoldTable(907, "vissenkom", "vissenkom");
        db.addLevelToDBoldTable(908, "hooivork", "hooivork");
        db.addLevelToDBoldTable(909, "pinpas", "pinpas");
        db.addLevelToDBoldTable(910, "kaplaars", "kaplaars");
        db.addLevelToDBoldTable(911, "kassa", "kassa");
        db.addLevelToDBoldTable(912, "pin automaat", "pinautomaat");
        db.addLevelToDBoldTable(913, "kikkervisje", "kikkervisje");
        db.addLevelToDBoldTable(914, "klamboe", "klamboe");
        db.addLevelToDBoldTable(915, "verfblik", "verfblik");
        db.addLevelToDBoldTable(916, "koala", "koala");
        db.addLevelToDBoldTable(917, "apple", "apple");
        db.addLevelToDBoldTable(918, "lans", "lans");
        db.addLevelToDBoldTable(919, "monalisa", "monalisa");
        db.addLevelToDBoldTable(920, "spekkoek", "spekkoek");
        db.addLevelToDBoldTable(921, "orka", "orka");
        db.addLevelToDBoldTable(922, "pissebed", "pissebed");
        db.addLevelToDBoldTable(923, "springvorm", "springvorm");
        db.addLevelToDBoldTable(924, "deegroller", "deegroller");
        db.addLevelToDBoldTable(925, "pluto", "pluto");
        db.addLevelToDBoldTable(926, "rozijnen", "rozijnen");
        db.addLevelToDBoldTable(927, "shrek", "shrek");
        db.addLevelToDBoldTable(928, "stethoscoop", "stethoscoop");
        db.addLevelToDBoldTable(929, "stinkdier", "stinkdier");
        db.addLevelToDBoldTable(930, "tweety", "tweety");
        db.addLevelToDBoldTable(931, "bingo kaart", "bingokaart");
        db.addLevelToDBoldTable(932, "ganzenbord", "ganzenbord");
        db.addLevelToDBoldTable(933, "springtouw", "springtouw");
        db.addLevelToDBoldTable(934, "tanden ragers", "tandenragers");
        db.addLevelToDBoldTable(935, "gletsjer", "gletsjer");
        db.addLevelToDBoldTable(936, "bazooka", "bazooka");
        db.addLevelToDBoldTable(937, "peterselie", "peterselie");
        db.addLevelToDBoldTable(938, "aalbes", "aalbes");
        db.addLevelToDBoldTable(939, "televisie", "televisie");
        db.addLevelToDBoldTable(940, "markeer stift", "markeerstift");
        db.addLevelToDBoldTable(941, "vos", "vos");
        db.addLevelToDBoldTable(942, "laserpen", "laserpen");
        db.addLevelToDBoldTable(943, "puck", "puck");
        db.addLevelToDBoldTable(944, "scheer schuim", "scheerschuim");
        db.addLevelToDBoldTable(945, "scheer spiegel", "scheerspiegel");
        db.addLevelToDBoldTable(946, "bieslook", "bieslook");
        db.addLevelToDBoldTable(947, "rozemarijn", "rozemarijn");
        db.addLevelToDBoldTable(948, "dille", "dille");
        db.addLevelToDBoldTable(949, "steiger", "steiger");
        db.addLevelToDBoldTable(950, "karbonade", "karbonade");
        db.addLevelToDBoldTable(951, "wierook", "wierook");
        db.addLevelToDBoldTable(952, "wip", "wip");
        db.addLevelToDBoldTable(953, "schommel", "schommel");
        db.addLevelToDBoldTable(954, "wieg", "wieg");
        db.addLevelToDBoldTable(955, "waterpomp", "waterpomp");
        db.addLevelToDBoldTable(956, "vuilnis wagen", "vuilniswagen");
        db.addLevelToDBoldTable(957, "melk", "melk");
        db.addLevelToDBoldTable(958, "triangel", "triangel");
        db.addLevelToDBoldTable(959, "dynamiet", "dynamiet");
        db.addLevelToDBoldTable(960, "body warmer", "bodywarmer");
        db.addLevelToDBoldTable(961, "tiara", "tiara");
        db.addLevelToDBoldTable(962, "zadel", "paardzadel");
        db.addLevelToDBoldTable(963, "picknick tafel", "picknicktafel");
        db.addLevelToDBoldTable(964, "ooievaar", "ooievaar");
        db.addLevelToDBoldTable(965, "duif", "duif");
        db.addLevelToDBoldTable(966, "duiventil", "duiventil");
        db.addLevelToDBoldTable(967, "keukenrol", "keukenrol");
        db.addLevelToDBoldTable(968, "ping pong batje", "pingpongbatje");
        db.addLevelToDBoldTable(969, "pomp", "pomp");
        db.addLevelToDBoldTable(970, "dombo", "dombo");
        db.addLevelToDBoldTable(971, "zeemeermin", "zeemeermin");
        db.addLevelToDBoldTable(972, "telefoon klapper", "telefoonklapper");
        db.addLevelToDBoldTable(973, "zak horloge", "zakhorloge");
        db.addLevelToDBoldTable(974, "kaneel", "kaneel");
        db.addLevelToDBoldTable(975, "snowboard", "snowboard");
        db.addLevelToDBoldTable(976, "ligfiets", "ligfiets");
        db.addLevelToDBoldTable(977, "katapult", "katapult");
        db.addLevelToDBoldTable(978, "tornado", "tornado");
        db.addLevelToDBoldTable(979, "spring kussen", "springkussen");
        db.addLevelToDBoldTable(980, "crocs", "crocs");
        db.addLevelToDBoldTable(981, "vleermuis", "vleermuis");
        db.addLevelToDBoldTable(982, "carnaval festival", "carnavalfestival");
        db.addLevelToDBoldTable(983, "gevaren driehoek", "gevarendriehoek");
        db.addLevelToDBoldTable(984, "tetris", "tetris");
        db.addLevelToDBoldTable(985, "bever", "bever");
        db.addLevelToDBoldTable(986, "saturnus", "saturnus");
        db.addLevelToDBoldTable(987, "speld", "speld");
        db.addLevelToDBoldTable(988, "iglo", "iglo");
        db.addLevelToDBoldTable(989, "buggy", "buggy");
        db.addLevelToDBoldTable(990, "hamkaas chips", "hamkaaschips");
        db.addLevelToDBoldTable(991, "noodhamer", "noodhamer");
        db.addLevelToDBoldTable(992, "wasmand", "wasmand");
        db.addLevelToDBoldTable(993, "stemvork", "stemvork");
        db.addLevelToDBoldTable(994, "twister", "twister");
        db.addLevelToDBoldTable(995, "skelter", "skelter");
        db.addLevelToDBoldTable(996, "bidet", "bidet");
        db.addLevelToDBoldTable(997, "telefoon boek", "telefoonboek");
        db.addLevelToDBoldTable(998, "schatkist", "schatkist");
        db.addLevelToDBoldTable(999, "piraten haak", "piratenhaak");
        db.addLevelToDBoldTable(1000, "gehakt bal", "gehaktbal");

    }

    private void addLevels1001to1200() {
        db.addLevelToDBoldTable(1001, "trevi fontein", "trevifontein");
        db.addLevelToDBoldTable(1002, "stijltang", "stijltang");
        db.addLevelToDBoldTable(1003, "muziek noot", "muzieknoot");
        db.addLevelToDBoldTable(1004, "pijporgel", "pijporgel");
        db.addLevelToDBoldTable(1005, "tien gulden", "tiengulden");
        db.addLevelToDBoldTable(1006, "klaproos", "klaproos");
        db.addLevelToDBoldTable(1007, "klapstoel", "klapstoel");
        db.addLevelToDBoldTable(1008, "knalerwt", "knalerwt");
        db.addLevelToDBoldTable(1009, "bontjas", "bontjas");
        db.addLevelToDBoldTable(1010, "sneeuw schuiver", "sneeuwschuiver");
        db.addLevelToDBoldTable(1011, "brandnetel", "brandnetel");
        db.addLevelToDBoldTable(1012, "zwembandje", "zwembandje");
        db.addLevelToDBoldTable(1013, "duikplank", "duikplank");
        db.addLevelToDBoldTable(1014, "botsauto", "botsauto");
        db.addLevelToDBoldTable(1015, "slush puppy", "slushpuppy");
        db.addLevelToDBoldTable(1016, "kokos makroon", "kokosmakroon");
        db.addLevelToDBoldTable(1017, "manneke pis", "mannekepis");
        db.addLevelToDBoldTable(1018, "stamper", "stamper");
        db.addLevelToDBoldTable(1019, "drilboor", "drilboor");
        db.addLevelToDBoldTable(1020, "diamant boor", "diamantboor");
        db.addLevelToDBoldTable(1021, "pion", "pion");
        db.addLevelToDBoldTable(1022, "honkbal", "honkbal");
        db.addLevelToDBoldTable(1023, "pantoffel", "pantoffel");
        db.addLevelToDBoldTable(1024, "panter", "panter");
        db.addLevelToDBoldTable(1025, "pasfoto hokje", "pasfotohokje");
        db.addLevelToDBoldTable(1026, "diabolo", "diabolo");
        db.addLevelToDBoldTable(1027, "messen slijper", "messenslijper");
        db.addLevelToDBoldTable(1028, "bromfiets", "bromfiets");
        db.addLevelToDBoldTable(1029, "quad", "quad");
        db.addLevelToDBoldTable(1030, "venus van milo", "venusvanmilo");
        db.addLevelToDBoldTable(1031, "bubbelbad", "bubbelbad");
        db.addLevelToDBoldTable(1032, "brillen koker", "brillenkoker");
        db.addLevelToDBoldTable(1033, "wolk", "wolk");
        db.addLevelToDBoldTable(1034, "patatje oorlog", "patatjeoorlog");
        db.addLevelToDBoldTable(1035, "saxofoon", "saxofoon");
        db.addLevelToDBoldTable(1036, "sterretje", "sterretje");
        db.addLevelToDBoldTable(1037, "vaatdoekje", "vaatdoekje");
        db.addLevelToDBoldTable(1038, "jeu de boules", "jeuxdeboules");
        db.addLevelToDBoldTable(1039, "vaatwas tablet", "vaatwastablet");
        db.addLevelToDBoldTable(1040, "paard", "paard");
        db.addLevelToDBoldTable(1041, "knapzak", "knapzak");
        db.addLevelToDBoldTable(1042, "kermit", "kermit");
        db.addLevelToDBoldTable(1043, "boksring", "boksring");
        db.addLevelToDBoldTable(1044, "boksbal", "boksbal");
        db.addLevelToDBoldTable(1045, "kop van jut", "kopvanjut");
        db.addLevelToDBoldTable(1046, "honden riem", "hondenriem");
        db.addLevelToDBoldTable(1047, "droom vlucht", "droomvlucht");
        db.addLevelToDBoldTable(1048, "dagobert duck", "dagobertduck");
        db.addLevelToDBoldTable(1049, "draaideur", "draaideur");
        db.addLevelToDBoldTable(1050, "maxicosi", "maxicosi");
        db.addLevelToDBoldTable(1051, "startkabel", "startkabel");
        db.addLevelToDBoldTable(1052, "benzine meter", "benzinemeter");
        db.addLevelToDBoldTable(1053, "camper", "camper");
        db.addLevelToDBoldTable(1054, "vouwfiets", "vouwfiets");
        db.addLevelToDBoldTable(1055, "kamperfoelie", "kamperfoelie");
        db.addLevelToDBoldTable(1056, "hunebed", "hunebed");
        db.addLevelToDBoldTable(1057, "flos draad", "flosdraad");
        db.addLevelToDBoldTable(1058, "vuurtoren", "vuurtoren");
        db.addLevelToDBoldTable(1059, "sprits", "sprits");
        db.addLevelToDBoldTable(1060, "stok staartje", "stokstaartje");
        db.addLevelToDBoldTable(1061, "wasbeer", "wasbeer");
        db.addLevelToDBoldTable(1062, "wasstraat", "wasstraat");
        db.addLevelToDBoldTable(1063, "mc drive", "mcdrive");
        db.addLevelToDBoldTable(1064, "karamel appel", "karamelappel");
        db.addLevelToDBoldTable(1065, "aubergine", "aubergine");
        db.addLevelToDBoldTable(1066, "bellen blaas", "bellenblaas");
        db.addLevelToDBoldTable(1067, "kauwgom ballen", "kauwgomballen");
        db.addLevelToDBoldTable(1068, "cranberry", "cranberry");
        db.addLevelToDBoldTable(1069, "postzak", "postzak");
        db.addLevelToDBoldTable(1070, "spatiebalk", "spatiebalk");
        db.addLevelToDBoldTable(1071, "bekeuring", "bekeuring");
        db.addLevelToDBoldTable(1072, "parkeer meter", "parkeermeter");
        db.addLevelToDBoldTable(1073, "mus", "mus");
        db.addLevelToDBoldTable(1074, "lounge bank", "loungebank");
        db.addLevelToDBoldTable(1075, "drone", "drone");
        db.addLevelToDBoldTable(1076, "mestkever", "mestkever");
        db.addLevelToDBoldTable(1077, "android", "android");
        db.addLevelToDBoldTable(1078, "sjoelsteen", "sjoelsteen");
        db.addLevelToDBoldTable(1079, "oregano", "oregano");
        db.addLevelToDBoldTable(1080, "douchemat", "douchemat");
        db.addLevelToDBoldTable(1081, "anti slip mat", "antislipmat");
        db.addLevelToDBoldTable(1082, "krab", "krab");
        db.addLevelToDBoldTable(1083, "sneeuw ketting", "sneeuwketting");
        db.addLevelToDBoldTable(1084, "typemachine", "typemachine");
        db.addLevelToDBoldTable(1085, "opblaas boot", "opblaasboot");
        db.addLevelToDBoldTable(1086, "gorilla", "gorilla");
        db.addLevelToDBoldTable(1087, "chimpansee", "chimpansee");
        db.addLevelToDBoldTable(1088, "bijl", "bijl");
        db.addLevelToDBoldTable(1089, "artisjok", "artisjok");
        db.addLevelToDBoldTable(1090, "courgette", "courgette");
        db.addLevelToDBoldTable(1091, "spinazie", "spinazie");
        db.addLevelToDBoldTable(1092, "tauge", "tauge");
        db.addLevelToDBoldTable(1093, "dadel", "dadel");
        db.addLevelToDBoldTable(1094, "woordenboek", "woordenboek");
        db.addLevelToDBoldTable(1095, "kerststukje", "kerststukje");
        db.addLevelToDBoldTable(1096, "kerstboom", "kerstboom");
        db.addLevelToDBoldTable(1097, "elleboog", "elleboog");
        db.addLevelToDBoldTable(1098, "hand", "hand");
        db.addLevelToDBoldTable(1099, "kruik", "kruik");
        db.addLevelToDBoldTable(1100, "voet", "voet");
        db.addLevelToDBoldTable(1101, "wenkbrauw", "wenkbrauw");
        db.addLevelToDBoldTable(1102, "snor", "snor");
        db.addLevelToDBoldTable(1103, "baard", "baard");
        db.addLevelToDBoldTable(1104, "skelet", "skelet");
        db.addLevelToDBoldTable(1105, "tank", "tank");
        db.addLevelToDBoldTable(1106, "lauwerkrans", "lauwerkrans");
        db.addLevelToDBoldTable(1107, "vogelkooi", "vogelkooi");
        db.addLevelToDBoldTable(1108, "pet", "pet");
        db.addLevelToDBoldTable(1109, "zwarte piet", "zwartepiet");
        db.addLevelToDBoldTable(1110, "bowling baan", "bowlingbaan");
        db.addLevelToDBoldTable(1111, "muesli reep", "mueslireep");
        db.addLevelToDBoldTable(1112, "lasbril", "lasbril");
        db.addLevelToDBoldTable(1113, "zweep", "zweep");
        db.addLevelToDBoldTable(1114, "buzz lightyear", "buzzlightyear");
        db.addLevelToDBoldTable(1115, "chocolade mousse", "chocolademousse");
        db.addLevelToDBoldTable(1116, "clown", "clown");
        db.addLevelToDBoldTable(1117, "stelten", "stelten");
        db.addLevelToDBoldTable(1118, "schnitzel", "schnitzel");
        db.addLevelToDBoldTable(1119, "geldtel machine", "geldtelmachine");
        db.addLevelToDBoldTable(1120, "pepper spray", "pepperspray");
        db.addLevelToDBoldTable(1121, "terras warmer", "terraswarmer");
        db.addLevelToDBoldTable(1122, "kuiken", "kuiken");
        db.addLevelToDBoldTable(1123, "tequila sunrise", "tequilasunrise");
        db.addLevelToDBoldTable(1124, "stempel", "stempel");
        db.addLevelToDBoldTable(1125, "mitella", "mitella");
        db.addLevelToDBoldTable(1126, "prikklok", "prikklok");
        db.addLevelToDBoldTable(1127, "fred flintstone", "fredflintstone");
        db.addLevelToDBoldTable(1128, "tweede kamer", "tweedekamer");
        db.addLevelToDBoldTable(1129, "slagboom", "slagboom");
        db.addLevelToDBoldTable(1130, "clowns neus", "clownsneus");
        db.addLevelToDBoldTable(1131, "cirkel diagram", "cirkeldiagram");
        db.addLevelToDBoldTable(1132, "hor", "hor");
        db.addLevelToDBoldTable(1133, "uitlaat", "uitlaat");
        db.addLevelToDBoldTable(1134, "kattenbrokjes", "kattenbrokjes");
        db.addLevelToDBoldTable(1135, "kattengrind", "kattengrind");
        db.addLevelToDBoldTable(1136, "krabpaal", "krabpaal");
        db.addLevelToDBoldTable(1137, "barometer", "barometer");
        db.addLevelToDBoldTable(1138, "simkaart", "simkaart");
        db.addLevelToDBoldTable(1139, "sabel", "sabel");
        db.addLevelToDBoldTable(1140, "pepermolen", "pepermolen");
        db.addLevelToDBoldTable(1141, "vuilniszak", "vuilniszak");
        db.addLevelToDBoldTable(1142, "plastic tas", "plastictas");
        db.addLevelToDBoldTable(1143, "laars", "laars");
        db.addLevelToDBoldTable(1144, "aanhanger", "aanhanger");
        db.addLevelToDBoldTable(1145, "caravan", "caravan");
        db.addLevelToDBoldTable(1146, "pick up truck", "pickuptruck");
        db.addLevelToDBoldTable(1147, "ringband", "ringband");
        db.addLevelToDBoldTable(1148, "airhockey", "airhockey");
        db.addLevelToDBoldTable(1149, "nemo", "nemo");
        db.addLevelToDBoldTable(1150, "tram", "tram");
        db.addLevelToDBoldTable(1151, "oscar", "oscar");
        db.addLevelToDBoldTable(1152, "staalkabel", "staalkabel");
        db.addLevelToDBoldTable(1153, "urinoir", "urinoir");
        db.addLevelToDBoldTable(1154, "stanley mes", "stanleymes");
        db.addLevelToDBoldTable(1155, "krat", "krat");
        db.addLevelToDBoldTable(1156, "reageer buisje", "reageerbuisje");
        db.addLevelToDBoldTable(1157, "wegwijzer", "wegwijzer");
        db.addLevelToDBoldTable(1158, "roggebrood", "roggebrood");
        db.addLevelToDBoldTable(1159, "graaf machine", "graafmachine");
        db.addLevelToDBoldTable(1160, "hamsterrad", "hamsterrad");
        db.addLevelToDBoldTable(1161, "hamster", "hamster");
        db.addLevelToDBoldTable(1162, "rollerskate", "rollerskate");
        db.addLevelToDBoldTable(1163, "skeeler", "skeeler");
        db.addLevelToDBoldTable(1164, "kern centrale", "kerncentrale");
        db.addLevelToDBoldTable(1165, "qr code", "qrcode");
        db.addLevelToDBoldTable(1166, "teken tang", "tekentang");
        db.addLevelToDBoldTable(1167, "hawaii krans", "hawaiikrans");
        db.addLevelToDBoldTable(1168, "hooi", "hooi");
        db.addLevelToDBoldTable(1169, "haarband", "haarband");
        db.addLevelToDBoldTable(1170, "teddy beer", "teddybeer");
        db.addLevelToDBoldTable(1171, "plug", "plug");
        db.addLevelToDBoldTable(1172, "kampvuur", "kampvuur");
        db.addLevelToDBoldTable(1173, "overhemd", "overhemd");
        db.addLevelToDBoldTable(1174, "hemd", "hemd");
        db.addLevelToDBoldTable(1175, "candy crush", "candycrush");
        db.addLevelToDBoldTable(1176, "spongebob", "spongebob");
        db.addLevelToDBoldTable(1177, "monopoly geld", "monopolygeld");
        db.addLevelToDBoldTable(1178, "ipad", "ipad");
        db.addLevelToDBoldTable(1179, "zeeslag", "zeeslag");
        db.addLevelToDBoldTable(1180, "ring", "ring");
        db.addLevelToDBoldTable(1181, "bruilofts taart", "bruiloftstaart");
        db.addLevelToDBoldTable(1182, "cheese burger", "cheeseburger");
        db.addLevelToDBoldTable(1183, "soesje", "soesje");
        db.addLevelToDBoldTable(1184, "bossche bol", "bosschebol");
        db.addLevelToDBoldTable(1185, "karrewiel", "karrewiel");
        db.addLevelToDBoldTable(1186, "afwas middel", "afwasmiddel");
        db.addLevelToDBoldTable(1187, "vliegdek schip", "vliegdekschip");
        db.addLevelToDBoldTable(1188, "louvre", "louvre");
        db.addLevelToDBoldTable(1189, "bananen schil", "bananenschil");
        db.addLevelToDBoldTable(1190, "mario kart", "mariokart");
        db.addLevelToDBoldTable(1191, "vingerhoed", "vingerhoed");
        db.addLevelToDBoldTable(1192, "honing drop", "honingdrop");
        db.addLevelToDBoldTable(1193, "tomtom", "tomtom");
        db.addLevelToDBoldTable(1194, "bh", "bh");
        db.addLevelToDBoldTable(1195, "laptop", "laptop");
        db.addLevelToDBoldTable(1196, "zwaluw", "zwaluw");
        db.addLevelToDBoldTable(1197, "sambal", "sambal");
        db.addLevelToDBoldTable(1198, "skistok", "skistok");
        db.addLevelToDBoldTable(1199, "barkruk", "barkruk");
        db.addLevelToDBoldTable(1200, "bierpul", "bierpul");
    }

    private void addLevels1201to1250() {
        db.addLevelToDBoldTable(1201, "vlierbessen", "vlierbessen");
        db.addLevelToDBoldTable(1202, "infuus", "infuus");
        db.addLevelToDBoldTable(1203, "kapsalon", "kapsalon");
        db.addLevelToDBoldTable(1204, "scalpel", "scalpel");
        db.addLevelToDBoldTable(1205, "flugel", "flugel");
        db.addLevelToDBoldTable(1206, "vijf gulden", "vijfgulden");
        db.addLevelToDBoldTable(1207, "mars", "mars");
        db.addLevelToDBoldTable(1208, "skateboard", "skateboard");
        db.addLevelToDBoldTable(1209, "spaar varken", "spaarvarken");
        db.addLevelToDBoldTable(1210, "veter", "veter");
        db.addLevelToDBoldTable(1211, "eier snijder", "eiersnijder");
        db.addLevelToDBoldTable(1212, "knoflook pers", "knoflookpers");
        db.addLevelToDBoldTable(1213, "zuurstof masker", "zuurstofmasker");
        db.addLevelToDBoldTable(1214, "furby", "furby");
        db.addLevelToDBoldTable(1215, "tumtum", "tumtum");
        db.addLevelToDBoldTable(1216, "mandarijn", "mandarijn");
        db.addLevelToDBoldTable(1217, "pinda", "pinda");
        db.addLevelToDBoldTable(1218, "vlag", "vlag");
        db.addLevelToDBoldTable(1219, "grind", "grind");
        db.addLevelToDBoldTable(1220, "regen", "regen");
        db.addLevelToDBoldTable(1221, "tafel kleed", "tafelkleed");
        db.addLevelToDBoldTable(1222, "aux kabel", "auxkabel");
        db.addLevelToDBoldTable(1223, "ijsbeer", "ijsbeer");
        db.addLevelToDBoldTable(1224, "bruine beer", "bruinebeer");
        db.addLevelToDBoldTable(1225, "rieten mand", "rietenmand");
        db.addLevelToDBoldTable(1226, "luipaard", "luipaard");
        db.addLevelToDBoldTable(1227, "steenbok", "steenbok");
        db.addLevelToDBoldTable(1228, "hoef", "hoef");
        db.addLevelToDBoldTable(1229, "boomwortel", "boomwortel");
        db.addLevelToDBoldTable(1230, "speer", "speer");
        db.addLevelToDBoldTable(1231, "gier", "gier");
        db.addLevelToDBoldTable(1232, "raaf", "raaf");
        db.addLevelToDBoldTable(1233, "meeuw", "meeuw");
        db.addLevelToDBoldTable(1234, "kangoeroe", "kangoeroe");
        db.addLevelToDBoldTable(1235, "tinkerbell", "tinkerbell");
        db.addLevelToDBoldTable(1236, "ledikant", "ledikant");
        db.addLevelToDBoldTable(1237, "luier", "luier");
        db.addLevelToDBoldTable(1238, "golf", "golf");
        db.addLevelToDBoldTable(1239, "lenzen bakje", "lenzenbakje");
        db.addLevelToDBoldTable(1240, "hark", "hark");
        db.addLevelToDBoldTable(1241, "rollator", "rollator");
        db.addLevelToDBoldTable(1242, "spook", "spook");
        db.addLevelToDBoldTable(1243, "boeken legger", "boekenlegger");
        db.addLevelToDBoldTable(1244, "halsband", "halsbamd");
        db.addLevelToDBoldTable(1245, "tolpoortje", "tolpoortje");
        db.addLevelToDBoldTable(1246, "belasting envelop", "belastingenvelop");
        db.addLevelToDBoldTable(1247, "vijftig euro", "vijftigeuro");
        db.addLevelToDBoldTable(1248, "led lamp", "ledlamp");
        db.addLevelToDBoldTable(1249, "pepermunt", "pepermunt");
        db.addLevelToDBoldTable(1250, "smarties", "smarties");
    }

    private void addLevels1251to1350() {
        db.addLevelToDBoldTable(1251, "stripboek", "stripboek");
        db.addLevelToDBoldTable(1252, "acai bes", "acaibes");
        db.addLevelToDBoldTable(1253, "poffertjes pan", "poffertjespan");
        db.addLevelToDBoldTable(1254, "blinden stok", "blindenstok");
        db.addLevelToDBoldTable(1255, "toiletblok", "toiletblok");
        db.addLevelToDBoldTable(1256, "messenblok", "messenblok");
        db.addLevelToDBoldTable(1257, "bioscoop bon", "bioscoopbon");
        db.addLevelToDBoldTable(1258, "berenklem", "berenklem");
        db.addLevelToDBoldTable(1259, "berenklauw", "berenklauw");
        db.addLevelToDBoldTable(1260, "munten schuiver", "muntenschuiver");
        db.addLevelToDBoldTable(1261, "vissenvoer", "vissenvoer");
        db.addLevelToDBoldTable(1262, "magnetron maaltijd", "magnetronmaaltijd");
        db.addLevelToDBoldTable(1263, "netflix", "netflix");
        db.addLevelToDBoldTable(1264, "helmgras", "helmgras");
        db.addLevelToDBoldTable(1265, "angry birds", "angrybirds");
        db.addLevelToDBoldTable(1266, "stofzuiger zak", "stofzuigerzak");
        db.addLevelToDBoldTable(1267, "matrixbord", "matrixbord");
        db.addLevelToDBoldTable(1268, "afzuigkap", "afzuigkap");
        db.addLevelToDBoldTable(1269, "bloed zuiger", "bloedzuiger");
        db.addLevelToDBoldTable(1270, "stortkoker", "stortkoker");
        db.addLevelToDBoldTable(1271, "ronald mcdonald", "ronaldmcdonald");
        db.addLevelToDBoldTable(1272, "bowling schoen", "bowlingschoen");
        db.addLevelToDBoldTable(1273, "scheen beschermer", "scheenbeschermer");
        db.addLevelToDBoldTable(1274, "buddha beeld", "buddhabeeld");
        db.addLevelToDBoldTable(1275, "schorpioen", "schorpioen");
        db.addLevelToDBoldTable(1276, "vorkhef truck", "vorkheftruck");
        db.addLevelToDBoldTable(1277, "koffie molen", "koffiemolen");
        db.addLevelToDBoldTable(1278, "mc flurry", "mcflurry");
        db.addLevelToDBoldTable(1279, "piepschuim", "piepschuim");
        db.addLevelToDBoldTable(1280, "tuinschaar", "tuinschaar");
        db.addLevelToDBoldTable(1281, "knijptang", "knijptang");
        db.addLevelToDBoldTable(1282, "stier", "stier");
        db.addLevelToDBoldTable(1283, "confetti", "confetti2");
        db.addLevelToDBoldTable(1284, "sneeuw scooter", "sneeuwscooter");
        db.addLevelToDBoldTable(1285, "hangmat", "hangmat");
        db.addLevelToDBoldTable(1286, "trap leuning", "trapleuning");
        db.addLevelToDBoldTable(1287, "garage", "garage");
        db.addLevelToDBoldTable(1288, "telbord", "telbord");
        db.addLevelToDBoldTable(1289, "hectometerpaaltje", "hectometerpaaltje");
        db.addLevelToDBoldTable(1290, "plantenbak", "plantenbak");
        db.addLevelToDBoldTable(1291, "ringen", "ringen");
        db.addLevelToDBoldTable(1292, "deurbel", "deurbel");
        db.addLevelToDBoldTable(1293, "kayak", "kayak");
        db.addLevelToDBoldTable(1294, "riool put", "rioolput");
        db.addLevelToDBoldTable(1295, "cross motor", "crossmotor");
        db.addLevelToDBoldTable(1296, "zijwieltjes", "zijwieltjes");
        db.addLevelToDBoldTable(1297, "agenda", "agenda");
        db.addLevelToDBoldTable(1298, "dagboek", "dagboek");
        db.addLevelToDBoldTable(1299, "graansilo", "graansilo");
        db.addLevelToDBoldTable(1300, "wc rol houder", "wcrolhouder");
        db.addLevelToDBoldTable(1301, "koffie pad", "koffiepad");
        db.addLevelToDBoldTable(1302, "schoenlepel", "schoenlepel");
        db.addLevelToDBoldTable(1303, "braadpan", "braadpan");
        db.addLevelToDBoldTable(1304, "kartelschaar", "kartelschaar");
        db.addLevelToDBoldTable(1305, "plastic beker", "plasticbeker");
        db.addLevelToDBoldTable(1306, "zweetband", "zweetband");
        db.addLevelToDBoldTable(1307, "grijp machine", "grijpmachine");
        db.addLevelToDBoldTable(1308, "slagtand", "slachttand");
        db.addLevelToDBoldTable(1309, "afsluitdijk", "afsluitdijk");
        db.addLevelToDBoldTable(1310, "sluis", "sluis");
        db.addLevelToDBoldTable(1311, "zandzak", "zandzak");
        db.addLevelToDBoldTable(1312, "marsepein", "marsepein");
        db.addLevelToDBoldTable(1313, "propellor", "propellor");
        db.addLevelToDBoldTable(1314, "fanta", "fanta");
        db.addLevelToDBoldTable(1315, "knijpkat", "knijpkat");
        db.addLevelToDBoldTable(1316, "dun schiller", "dunschiller");
        db.addLevelToDBoldTable(1317, "patat snijder", "patatsnijder");
        db.addLevelToDBoldTable(1318, "cement wagen", "cementwagen");
        db.addLevelToDBoldTable(1319, "vliegende fakir", "vliegendefakir");
        db.addLevelToDBoldTable(1320, "inktpot", "inktpot");
        db.addLevelToDBoldTable(1321, "vlaskam", "vlaskam");
        db.addLevelToDBoldTable(1322, "schrik draad", "schrikdraad");
        db.addLevelToDBoldTable(1323, "trechter", "trechter");
        db.addLevelToDBoldTable(1324, "knoflook brood", "knoflookbrood");
        db.addLevelToDBoldTable(1325, "wrap", "wrap");
        db.addLevelToDBoldTable(1326, "smiley", "smiley");
        db.addLevelToDBoldTable(1327, "badpak", "badpak");
        db.addLevelToDBoldTable(1328, "locker", "locker");
        db.addLevelToDBoldTable(1329, "thermos fles", "thermosfles");
        db.addLevelToDBoldTable(1330, "schijn werper", "schijnwerper");
        db.addLevelToDBoldTable(1331, "drinkpak", "drinkpak");
        db.addLevelToDBoldTable(1332, "water glijbaan", "waterglijbaan");
        db.addLevelToDBoldTable(1333, "cornervlag", "cornervlag");
        db.addLevelToDBoldTable(1334, "brooddoos", "brooddoos");
        db.addLevelToDBoldTable(1335, "boterham", "boterham");
        db.addLevelToDBoldTable(1336, "landings baan", "landingsbaan");
        db.addLevelToDBoldTable(1337, "rabarber", "rabarber");
        db.addLevelToDBoldTable(1338, "walvis", "walvis");
        db.addLevelToDBoldTable(1339, "hummer", "hummer");
        db.addLevelToDBoldTable(1340, "cockpit", "cockpit");
        db.addLevelToDBoldTable(1341, "corned beef", "cornedbeef");
        db.addLevelToDBoldTable(1342, "google", "google");
        db.addLevelToDBoldTable(1343, "amaretto", "amaretto");
        db.addLevelToDBoldTable(1344, "beitel", "beitel");
        db.addLevelToDBoldTable(1345, "guts", "guts");
        db.addLevelToDBoldTable(1346, "trog", "trog");
        db.addLevelToDBoldTable(1347, "rasp", "rasp");
        db.addLevelToDBoldTable(1348, "rode biet", "rodebiet");
        db.addLevelToDBoldTable(1349, "suikerbiet", "suikerbiet");
        db.addLevelToDBoldTable(1350, "mais plant", "maisplant");
    }


    public void addLevels1351to1420() {
        db.addLevelToDBoldTable(1351, "brief opener", "briefopener");
        db.addLevelToDBoldTable(1352, "naaimachine", "naaimachine");
        db.addLevelToDBoldTable(1353, "brillen glas", "brillenglas");
        db.addLevelToDBoldTable(1354, "zegel", "zegel");
        db.addLevelToDBoldTable(1355, "zegelring", "zegelring");
        db.addLevelToDBoldTable(1356, "priem", "priem");
        db.addLevelToDBoldTable(1357, "meetlint", "meetlint");
        db.addLevelToDBoldTable(1358, "lintje", "lintje");
        db.addLevelToDBoldTable(1359, "meetlat", "meetlat");
        db.addLevelToDBoldTable(1360, "telefoon hoesje", "telefoonhoesje");
        db.addLevelToDBoldTable(1361, "sprei", "sprei");
        db.addLevelToDBoldTable(1362, "kussen", "kussen");
        db.addLevelToDBoldTable(1363, "molton", "molton");
        db.addLevelToDBoldTable(1364, "lente ui", "lenteui");
        db.addLevelToDBoldTable(1365, "bloempot", "bloempot");
        db.addLevelToDBoldTable(1366, "spotje", "spotje");
        db.addLevelToDBoldTable(1367, "motje", "motje");
        db.addLevelToDBoldTable(1368, "glij ijzer", "glijijzer");
        db.addLevelToDBoldTable(1369, "noren", "noren");
        db.addLevelToDBoldTable(1370, "schrepel", "schrepel");
        db.addLevelToDBoldTable(1371, "trouwjurk", "trouwjurk");
        db.addLevelToDBoldTable(1372, "sleepkabel", "sleepkabel");
        db.addLevelToDBoldTable(1373, "aanrecht", "aanrecht");
        db.addLevelToDBoldTable(1374, "badjas", "badjas");
        db.addLevelToDBoldTable(1375, "viltstiften", "viltstiften");
        db.addLevelToDBoldTable(1376, "kleurboek", "kleurboek");
        db.addLevelToDBoldTable(1377, "vriezer", "vriezer");
        db.addLevelToDBoldTable(1378, "wijn", "wijn");
        db.addLevelToDBoldTable(1379, "wijnkelder", "wijnkelder");
        db.addLevelToDBoldTable(1380, "vaas", "vaas");
        db.addLevelToDBoldTable(1381, "schilderij", "schilderij");
        db.addLevelToDBoldTable(1382, "schilders ezel", "schildersezel");
        db.addLevelToDBoldTable(1383, "brieven standaard", "brievenstandaard");
        db.addLevelToDBoldTable(1384, "aster", "aster");
        db.addLevelToDBoldTable(1385, "narcis", "narcis");
        db.addLevelToDBoldTable(1386, "hyacint", "hyacint");
        db.addLevelToDBoldTable(1387, "konijnen keutels", "konijnenkeutels");
        db.addLevelToDBoldTable(1388, "hondendrol", "hondendrol");
        db.addLevelToDBoldTable(1389, "multomap", "multomap");
        db.addLevelToDBoldTable(1390, "nacht lampje", "nachtlampje");
        db.addLevelToDBoldTable(1391, "voerbak", "voerbak");
        db.addLevelToDBoldTable(1392, "vlooienband", "vlooienband");
        db.addLevelToDBoldTable(1393, "spalk", "spalk");
        db.addLevelToDBoldTable(1394, "hek", "hek");
        db.addLevelToDBoldTable(1395, "deur", "deur");
        db.addLevelToDBoldTable(1396, "vogelzaad", "vogelzaad");
        db.addLevelToDBoldTable(1397, "telefooncel", "telefooncel");
        db.addLevelToDBoldTable(1398, "spelt", "spelt");
        db.addLevelToDBoldTable(1399, "graan", "graan");
        db.addLevelToDBoldTable(1400, "rog", "rog");
        db.addLevelToDBoldTable(1401, "zeep", "zeep");
        db.addLevelToDBoldTable(1402, "zee", "zee");
        db.addLevelToDBoldTable(1403, "zetel", "zetel");
        db.addLevelToDBoldTable(1404, "rekening", "rekening");
        db.addLevelToDBoldTable(1405, "ree", "ree");
        db.addLevelToDBoldTable(1406, "hert", "hert");
        db.addLevelToDBoldTable(1407, "wolf", "wolf");
        db.addLevelToDBoldTable(1408, "bij", "bij");
        db.addLevelToDBoldTable(1409, "wesp", "wesp");
        db.addLevelToDBoldTable(1410, "chocopasta", "chocopasta");
        db.addLevelToDBoldTable(1411, "pasta", "pasta");
        db.addLevelToDBoldTable(1412, "lieveheersbeest", "lieveheersbeest");
        db.addLevelToDBoldTable(1413, "nier", "nier");
        db.addLevelToDBoldTable(1414, "toffee", "toffee");
        db.addLevelToDBoldTable(1415, "tictac", "tictac");
        db.addLevelToDBoldTable(1416, "kopje", "kopje");
        db.addLevelToDBoldTable(1417, "breinaald", "breinaald");
        db.addLevelToDBoldTable(1418, "breimachine", "breimachine");
        db.addLevelToDBoldTable(1419, "bizon", "bizon");
        db.addLevelToDBoldTable(1420, "molen", "molen");
    }

    private void addLevels1421to1500() {
        db.addLevelToDBoldTable(1421, "geld automaat", "geldautomaat");
        db.addLevelToDBoldTable(1422, "haaknaald", "haaknaald");
        db.addLevelToDBoldTable(1423, "haakwerkje", "haakwerkje");
        db.addLevelToDBoldTable(1424, "telescoop", "telescoop");
        db.addLevelToDBoldTable(1425, "sproeten", "sproeten");
        db.addLevelToDBoldTable(1426, "wandelende tak", "wandelendetak");
        db.addLevelToDBoldTable(1427, "koolmonoxidemelder", "koolmonoxidemelder");
        db.addLevelToDBoldTable(1428, "fiets paaltje", "fietspaaltje");
        db.addLevelToDBoldTable(1429, "heipaal", "heipaal");
        db.addLevelToDBoldTable(1430, "theemuts", "theemuts");
        db.addLevelToDBoldTable(1431, "pannenlap", "pannenlap");
        db.addLevelToDBoldTable(1432, "zeem", "zeem");
        db.addLevelToDBoldTable(1433, "molensteen", "molensteen");
        db.addLevelToDBoldTable(1434, "bijenkorf", "bijenkorf");
        db.addLevelToDBoldTable(1435, "wiek", "wiek");
        db.addLevelToDBoldTable(1436, "onder zetters", "onderzetters");
        db.addLevelToDBoldTable(1437, "kaarsen standaard", "kaarsenstandaard");
        db.addLevelToDBoldTable(1438, "insecten hotel", "insectenhotel");
        db.addLevelToDBoldTable(1439, "afwasteil", "afwasteil");
        db.addLevelToDBoldTable(1440, "oven", "oven");
        db.addLevelToDBoldTable(1441, "fruit schaal", "fruitschaal");
        db.addLevelToDBoldTable(1442, "licht schakelaar", "lichtschakelaar");
        db.addLevelToDBoldTable(1443, "schuur", "schuur");
        db.addLevelToDBoldTable(1444, "zolder", "zolder");
        db.addLevelToDBoldTable(1445, "sport schoen", "sportschoen");
        db.addLevelToDBoldTable(1446, "nagellak", "nagellak");
        db.addLevelToDBoldTable(1447, "sleutel hanger", "sleutelhanger");
        db.addLevelToDBoldTable(1448, "wegenwacht", "wegenwacht");
        db.addLevelToDBoldTable(1449, "kerkhof", "kerkhof");
        db.addLevelToDBoldTable(1450, "graszode", "graszode");
        db.addLevelToDBoldTable(1451, "pink", "pink");
        db.addLevelToDBoldTable(1452, "duim", "duim");
        db.addLevelToDBoldTable(1453, "knie", "knie");
        db.addLevelToDBoldTable(1454, "meel", "meel");
        db.addLevelToDBoldTable(1455, "meelworm", "meelworm");
        db.addLevelToDBoldTable(1456, "lever", "lever");
        db.addLevelToDBoldTable(1457, "hart", "hart");
        db.addLevelToDBoldTable(1458, "engel", "engel");
        db.addLevelToDBoldTable(1459, "vacuvin", "vacuvin");
        db.addLevelToDBoldTable(1460, "goldstrike", "goldstrike");
        db.addLevelToDBoldTable(1461, "waterput", "waterput");
        db.addLevelToDBoldTable(1462, "doorn roosje", "doornroosje");
        db.addLevelToDBoldTable(1463, "blaaspijp", "blaaspijp");
        db.addLevelToDBoldTable(1464, "rugzak", "rugzak");
        db.addLevelToDBoldTable(1465, "zaklamp", "zaklamp");
        db.addLevelToDBoldTable(1466, "scheergel", "scheergel");
        db.addLevelToDBoldTable(1467, "pruik", "pruik");
        db.addLevelToDBoldTable(1468, "diadeem", "diadeem");
        db.addLevelToDBoldTable(1469, "ligstoel", "ligstoel");
        db.addLevelToDBoldTable(1470, "pruim", "pruim");
        db.addLevelToDBoldTable(1471, "fossiel", "fossiel");
        db.addLevelToDBoldTable(1472, "begrafenis", "begrafenis");
        db.addLevelToDBoldTable(1473, "luxaflex", "luxaflex");
        db.addLevelToDBoldTable(1474, "wierook brander", "wierookbrander");
        db.addLevelToDBoldTable(1475, "dokter bibber", "dokterbibber");
        db.addLevelToDBoldTable(1476, "cluedo", "cluedo");
        db.addLevelToDBoldTable(1477, "memory", "memory");
        db.addLevelToDBoldTable(1478, "rummikub", "rummikub");
        db.addLevelToDBoldTable(1479, "stratego", "stratego");
        db.addLevelToDBoldTable(1480, "scrabble", "scrabble");
        db.addLevelToDBoldTable(1481, "pim pam pet", "pimpampet");
        db.addLevelToDBoldTable(1482, "wie is het", "wieishet");
        db.addLevelToDBoldTable(1483, "jenga", "jenga");
        db.addLevelToDBoldTable(1484, "random reader", "randomreader");
        db.addLevelToDBoldTable(1485, "snoeppot", "snoeppot");
        db.addLevelToDBoldTable(1486, "bureau lamp", "bureaulamp");
        db.addLevelToDBoldTable(1487, "stoeptegel", "stoeptegel");
        db.addLevelToDBoldTable(1488, "vijver", "vijver");
        db.addLevelToDBoldTable(1489, "seconde lijm", "secondelijm");
        db.addLevelToDBoldTable(1490, "senseo", "senseo");
        db.addLevelToDBoldTable(1491, "foundation", "foundation");
        db.addLevelToDBoldTable(1492, "oog schaduw", "oogschaduw");
        db.addLevelToDBoldTable(1493, "mascara", "mascara");
        db.addLevelToDBoldTable(1494, "HDMI kabel", "hdmikabel");
        db.addLevelToDBoldTable(1495, "tuinstoel", "tuinstoel");
        db.addLevelToDBoldTable(1496, "verleng snoer", "verlengsnoer");
        db.addLevelToDBoldTable(1497, "sleutelbos", "sleutelbos");
        db.addLevelToDBoldTable(1498, "hogedruk reiniger", "hogedrukreiniger");
        db.addLevelToDBoldTable(1499, "snoeizaag", "snoeizaag");
        db.addLevelToDBoldTable(1500, "voegen krabber", "voegenkrabber");
    }

    private void addLevels1501to1700() {
        db.addLevelToDBoldTable(1501, "water pistool", "waterpistool");
        db.addLevelToDBoldTable(1502, "slijptol", "slijptol");
        db.addLevelToDBoldTable(1503, "inbus sleutel", "inbussleutel");
        db.addLevelToDBoldTable(1504, "steek sleutel", "steeksleutel");
        db.addLevelToDBoldTable(1505, "waterpomp tang", "waterpomptang");
        db.addLevelToDBoldTable(1506, "gereedschapskist", "gereedschapskist");
        db.addLevelToDBoldTable(1507, "dvd speler", "dvdspeler");
        db.addLevelToDBoldTable(1508, "lucht verfrisser", "luchtverfrisser");
        db.addLevelToDBoldTable(1509, "bakpapier", "bakpapier");
        db.addLevelToDBoldTable(1510, "aluminium folie", "aluminiumfolie");
        db.addLevelToDBoldTable(1511, "spuitzak", "spuitzak");
        db.addLevelToDBoldTable(1512, "taartschep", "taartschep");
        db.addLevelToDBoldTable(1513, "vleesvork", "vleesvork");
        db.addLevelToDBoldTable(1514, "massage stoel", "massagestoel");
        db.addLevelToDBoldTable(1515, "sigaretten maker", "sigarettenmaker");
        db.addLevelToDBoldTable(1516, "sigaretten pakje", "sigarettenpakje");
        db.addLevelToDBoldTable(1517, "aardappel stamper", "aardappelstamper");
        db.addLevelToDBoldTable(1518, "soeplepel", "soeplepel");
        db.addLevelToDBoldTable(1519, "koel element", "koelelement");
        db.addLevelToDBoldTable(1520, "schuim spaan", "schuimspaan");
        db.addLevelToDBoldTable(1521, "margriet", "margriet");
        db.addLevelToDBoldTable(1522, "libelle", "libelle");
        db.addLevelToDBoldTable(1523, "hagedis", "hagedis");
        db.addLevelToDBoldTable(1524, "lama", "lama");
        db.addLevelToDBoldTable(1525, "reiger", "reiger");
        db.addLevelToDBoldTable(1526, "paling", "paling");
        db.addLevelToDBoldTable(1527, "struis vogel", "struisvogel");
        db.addLevelToDBoldTable(1528, "broodje haring", "broodjeharing");
        db.addLevelToDBoldTable(1529, "jam", "jam");
        db.addLevelToDBoldTable(1530, "geraspte kaas", "gerasptekaas");
        db.addLevelToDBoldTable(1531, "tonijn salade", "tonijnsalade");
        db.addLevelToDBoldTable(1532, "komkommer salade", "komkommersalade");
        db.addLevelToDBoldTable(1533, "kruisbes", "kruisbes");
        db.addLevelToDBoldTable(1534, "poeder suiker", "poedersuiker");
        db.addLevelToDBoldTable(1535, "soep stengels", "soepstengels");
        db.addLevelToDBoldTable(1536, "wicky", "wicky");
        db.addLevelToDBoldTable(1537, "pudding broodje", "puddingbroodje");
        db.addLevelToDBoldTable(1538, "ruiten vloeistof", "ruitenvloeistof");
        db.addLevelToDBoldTable(1539, "hockey stick", "hockeystick");
        db.addLevelToDBoldTable(1540, "bob de bouwer", "bobdebouwer");
        db.addLevelToDBoldTable(1541, "golden gate brug", "goldengatebrug");
        db.addLevelToDBoldTable(1542, "instagram", "instagram");
        db.addLevelToDBoldTable(1543, "papaya", "papaya");
        db.addLevelToDBoldTable(1544, "guave", "guave");
        db.addLevelToDBoldTable(1545, "restaurant", "restaurant");
        db.addLevelToDBoldTable(1546, "krantenbak", "krantenbak");
        db.addLevelToDBoldTable(1547, "toblerone", "toblerone");
        db.addLevelToDBoldTable(1548, "slakkenhuis", "slakkenhuis");
        db.addLevelToDBoldTable(1549, "sifon", "sifon");
        db.addLevelToDBoldTable(1550, "bagagedrager", "bagagedrager");
        db.addLevelToDBoldTable(1551, "kettingkast", "kettingkast");
        db.addLevelToDBoldTable(1552, "druiven", "druiven");
        db.addLevelToDBoldTable(1553, "e reader", "ereader");
        db.addLevelToDBoldTable(1554, "kikkererwt", "kikkererwt");
        db.addLevelToDBoldTable(1555, "bibliotheek", "bibliotheek");
        db.addLevelToDBoldTable(1556, "secretaire", "secretaire");
        db.addLevelToDBoldTable(1557, "vogelnest", "vogelnest");
        db.addLevelToDBoldTable(1558, "hangbrug", "hangbrug");
        db.addLevelToDBoldTable(1559, "kies", "kies");
        db.addLevelToDBoldTable(1560, "watten schijfje", "wattenschijfje");
        db.addLevelToDBoldTable(1561, "staafmixer", "staafmixer");
        db.addLevelToDBoldTable(1562, "pyjama", "pyjama");
        db.addLevelToDBoldTable(1563, "draaiorgel", "draaiorgel");
        db.addLevelToDBoldTable(1564, "cafe", "cafe");
        db.addLevelToDBoldTable(1565, "appelstroop", "appelstroop");
        db.addLevelToDBoldTable(1566, "plumeau", "plumeau");
        db.addLevelToDBoldTable(1567, "maandverband", "maandverband");
        db.addLevelToDBoldTable(1568, "brace", "brace");
        db.addLevelToDBoldTable(1569, "antenne", "antenne");
        db.addLevelToDBoldTable(1570, "babybox", "babybox");
        db.addLevelToDBoldTable(1571, "bureau", "bureau");
        db.addLevelToDBoldTable(1572, "id kaart", "idkaart");
        db.addLevelToDBoldTable(1573, "paspoort", "paspoort");
        db.addLevelToDBoldTable(1574, "thermostaat", "thermostaat");
        db.addLevelToDBoldTable(1575, "verwarming", "verwarming");
        db.addLevelToDBoldTable(1576, "poezenmand", "poezenmand");
        db.addLevelToDBoldTable(1577, "beenwarmer", "beenwarmer");
        db.addLevelToDBoldTable(1578, "kniekousen", "kniekousen");
        db.addLevelToDBoldTable(1579, "zaadcel", "zaadcel");
        db.addLevelToDBoldTable(1580, "koffiecupje", "koffiecupje");
        db.addLevelToDBoldTable(1581, "nespresso apparaat", "nespressoapparaat");
        db.addLevelToDBoldTable(1582, "tulband", "tulband");
        db.addLevelToDBoldTable(1583, "shampoo", "shampoo");
        db.addLevelToDBoldTable(1584, "yoghurt", "yoghurt");
        db.addLevelToDBoldTable(1585, "tomatensoep", "tomatensoep");
        db.addLevelToDBoldTable(1586, "yakult", "yakult");
        db.addLevelToDBoldTable(1587, "snicker", "snicker");
        db.addLevelToDBoldTable(1588, "wetsuit", "wetsuit");
        db.addLevelToDBoldTable(1589, "gehoorapparaat", "gehoorapparaat");
        db.addLevelToDBoldTable(1590, "pacemaker", "pacemaker");
        db.addLevelToDBoldTable(1591, "defibrillator", "defibrillator");
        db.addLevelToDBoldTable(1592, "marmot", "marmot");
        db.addLevelToDBoldTable(1593, "mozzarella", "mozzarella");
        db.addLevelToDBoldTable(1594, "blaadje", "blaadje");
        db.addLevelToDBoldTable(1595, "marktkraam", "marktkraam");
        db.addLevelToDBoldTable(1596, "parkiet", "parkiet");
        db.addLevelToDBoldTable(1597, "beo", "beo");
        db.addLevelToDBoldTable(1598, "kaketoe", "kaketoe");
        db.addLevelToDBoldTable(1599, "larf", "larf");
        db.addLevelToDBoldTable(1600, "dakkapel", "dakkapel");
        db.addLevelToDBoldTable(1601, "muziekkorps", "muziekkorps");
        db.addLevelToDBoldTable(1602, "breekijzer", "breekijzer");
        db.addLevelToDBoldTable(1603, "Albert heijn", "albertheijn");
        db.addLevelToDBoldTable(1604, "bleekselderij", "bleekselderij");
        db.addLevelToDBoldTable(1605, "frituurvet", "frituurvet");
        db.addLevelToDBoldTable(1606, "regenjas", "regenjas");
        db.addLevelToDBoldTable(1607, "strohoed", "strohoed");
        db.addLevelToDBoldTable(1608, "cd speler", "cdspeler");
        db.addLevelToDBoldTable(1609, "glazen bol", "glazenbol");
        db.addLevelToDBoldTable(1610, "toverstaf", "toverstaf");
        db.addLevelToDBoldTable(1611, "veiligheidshesje", "veiligheidshesje");
        db.addLevelToDBoldTable(1612, "matras", "matras");
        db.addLevelToDBoldTable(1613, "bodylotion", "bodylotion");
        db.addLevelToDBoldTable(1614, "fotoalbum", "fotoalbum");
        db.addLevelToDBoldTable(1615, "zwembad", "zwembad");
        db.addLevelToDBoldTable(1616, "fitnesszaal", "fitnesszaal");
        db.addLevelToDBoldTable(1617, "handvat", "handvat");
        db.addLevelToDBoldTable(1618, "wijnrek", "wijnrek");
        db.addLevelToDBoldTable(1619, "braille", "braille");
        db.addLevelToDBoldTable(1620, "ikea", "ikea");
        db.addLevelToDBoldTable(1621, "haaientand", "haaientand");
        db.addLevelToDBoldTable(1622, "ns", "ns");
        db.addLevelToDBoldTable(1623, "glasvezel", "glasvezel");
        db.addLevelToDBoldTable(1624, "stekkerdoos", "stekkerdoos");
        db.addLevelToDBoldTable(1625, "strandhuisje", "strandhuisje");
        db.addLevelToDBoldTable(1626, "spa", "spa");
        db.addLevelToDBoldTable(1627, "dakgoot", "dakgoot");
        db.addLevelToDBoldTable(1628, "balkon", "balkon");
        db.addLevelToDBoldTable(1629, "krukken", "krukken");
        db.addLevelToDBoldTable(1630, "lammetje", "lammetje");
        db.addLevelToDBoldTable(1631, "paasei", "paasei");
        db.addLevelToDBoldTable(1632, "zoutlamp", "zoutlamp");
        db.addLevelToDBoldTable(1633, "wanten", "wanten");
        db.addLevelToDBoldTable(1634, "handschoen", "handschoen");
        db.addLevelToDBoldTable(1635, "bielzen", "bielzen");
        db.addLevelToDBoldTable(1636, "collectebus", "collectebus");
        db.addLevelToDBoldTable(1637, "kweekkas", "kweekkas");
        db.addLevelToDBoldTable(1638, "zuurtje", "zuurtje");
        db.addLevelToDBoldTable(1639, "slabbetje", "slabbetje");
        db.addLevelToDBoldTable(1640, "watertoren", "watertoren");
        db.addLevelToDBoldTable(1641, "tongpiercing", "tongpiercing");
        db.addLevelToDBoldTable(1642, "mie", "mie");
        db.addLevelToDBoldTable(1643, "kookwekker", "kookwekker");
        db.addLevelToDBoldTable(1644, "romanesco", "romanesco");
        db.addLevelToDBoldTable(1645, "midgetgolf", "midgetgolf");
        db.addLevelToDBoldTable(1646, "paksoi", "paksoi");
        db.addLevelToDBoldTable(1647, "rode kruis", "rodekruis");
        db.addLevelToDBoldTable(1648, "golfbaan", "golfbaan");
        db.addLevelToDBoldTable(1649, "reflector", "reflector");
        db.addLevelToDBoldTable(1650, "lavendel", "lavendel");
        db.addLevelToDBoldTable(1651, "goudsbloem", "goudsbloem");
        db.addLevelToDBoldTable(1652, "balletschoen", "balletschoen");
        db.addLevelToDBoldTable(1653, "golfstick", "golfstick");
        db.addLevelToDBoldTable(1654, "waterschoen", "waterschoen");
        db.addLevelToDBoldTable(1655, "waterkip", "waterkip");
        db.addLevelToDBoldTable(1656, "partytent", "partytent");
        db.addLevelToDBoldTable(1657, "betonmolen", "betonmolen");
        db.addLevelToDBoldTable(1658, "servet", "servet");
        db.addLevelToDBoldTable(1659, "mos", "mos");
        db.addLevelToDBoldTable(1660, "autostuur", "autostuur");
        db.addLevelToDBoldTable(1661, "konijnenhok", "konijnenhok");
        db.addLevelToDBoldTable(1662, "konijn", "konijn");
        db.addLevelToDBoldTable(1663, "vouwladder", "vouwladder");
        db.addLevelToDBoldTable(1664, "bolderkar", "bolderkar");
        db.addLevelToDBoldTable(1665, "badmuts", "badmuts");
        db.addLevelToDBoldTable(1666, "hanglamp", "hanglamp");
        db.addLevelToDBoldTable(1667, "lectuurbak", "lectuurbak");
        db.addLevelToDBoldTable(1668, "orchidee", "orchidee");
        db.addLevelToDBoldTable(1669, "kunst schaats", "kunstschaats");
        db.addLevelToDBoldTable(1670, "pesto", "pesto");
        db.addLevelToDBoldTable(1671, "magnolia", "magnolia");
        db.addLevelToDBoldTable(1672, "aloe vera", "aloevera");
        db.addLevelToDBoldTable(1673, "hortensia", "hortensia");
        db.addLevelToDBoldTable(1674, "fietshelm", "fietshelm");
        db.addLevelToDBoldTable(1675, "knex", "knex");
        db.addLevelToDBoldTable(1676, "lego", "lego");
        db.addLevelToDBoldTable(1677, "hyena", "hyena");
        db.addLevelToDBoldTable(1678, "bmx", "bmx");
        db.addLevelToDBoldTable(1679, "gips", "gips");
        db.addLevelToDBoldTable(1680, "varaan", "varaan");
        db.addLevelToDBoldTable(1681, "leguaan", "leguaan");
        db.addLevelToDBoldTable(1682, "pinata", "pinata");
        db.addLevelToDBoldTable(1683, "haan", "haan");
        db.addLevelToDBoldTable(1684, "kip", "kip");
        db.addLevelToDBoldTable(1685, "pauw", "pauw");
        db.addLevelToDBoldTable(1686, "olielamp", "olielamp");
        db.addLevelToDBoldTable(1687, "sleehak", "sleehak");
        db.addLevelToDBoldTable(1688, "klimop", "klimop");
        db.addLevelToDBoldTable(1689, "fineliner", "fineliner");
        db.addLevelToDBoldTable(1690, "koplamp", "koplamp");
        db.addLevelToDBoldTable(1691, "visnet", "visnet");
        db.addLevelToDBoldTable(1692, "lasagne", "lasagne");
        db.addLevelToDBoldTable(1693, "vampier", "vampier");
        db.addLevelToDBoldTable(1694, "dixi", "dixi");
        db.addLevelToDBoldTable(1695, "ballenbak", "ballenbak");
        db.addLevelToDBoldTable(1696, "radar", "radar");
        db.addLevelToDBoldTable(1697, "turkse pizza", "turksepizza");
        db.addLevelToDBoldTable(1698, "turks fruit", "turksfruit");
        db.addLevelToDBoldTable(1699, "springveer", "springveer");
        db.addLevelToDBoldTable(1700, "et", "et");

    }

    private void addLevels1701to1800() {
        db.addLevelToDBoldTable(1701, "desperados", "desperados");
        db.addLevelToDBoldTable(1702, "autolamp", "autolamp");
        db.addLevelToDBoldTable(1703, "wasmiddel", "wasmiddel");
        db.addLevelToDBoldTable(1704, "wasverzachter", "wasverzachter");
        db.addLevelToDBoldTable(1705, "kristal", "kristal");
        db.addLevelToDBoldTable(1706, "robijn", "robijn");
        db.addLevelToDBoldTable(1707, "smaragd", "smaragd");
        db.addLevelToDBoldTable(1708, "chocolade paashaas", "chocoladepaashaas");
        db.addLevelToDBoldTable(1709, "nagel borstel", "nagelborstel");
        db.addLevelToDBoldTable(1710, "nagel schaartje", "nagelschaartje");
        db.addLevelToDBoldTable(1711, "metaaldetector", "metaaldetector");
        db.addLevelToDBoldTable(1712, "toiletpapier", "toiletpapier");
        db.addLevelToDBoldTable(1713, "haargel", "haargel");
        db.addLevelToDBoldTable(1714, "haarextensions", "haarextensions");
        db.addLevelToDBoldTable(1715, "haarspray", "haarspray");
        db.addLevelToDBoldTable(1716, "vuursteen", "vuursteen");
        db.addLevelToDBoldTable(1717, "hoofdlamp", "hoofdlamp");
        db.addLevelToDBoldTable(1718, "fakkel", "fakkel");
        db.addLevelToDBoldTable(1719, "EHBO doos", "ehbodoos");
        db.addLevelToDBoldTable(1720, "mondwater", "mondwater");
        db.addLevelToDBoldTable(1721, "tentharing", "tentharing");
        db.addLevelToDBoldTable(1722, "bitterkoekje", "bitterkoekje");
        db.addLevelToDBoldTable(1723, "viooltje", "viooltje");
        db.addLevelToDBoldTable(1724, "zandkoekjes", "zandkoekjes");
        db.addLevelToDBoldTable(1725, "heide", "heide");
        db.addLevelToDBoldTable(1726, "waterballon", "waterballon");
        db.addLevelToDBoldTable(1727, "cv ketel", "cvketel");
        db.addLevelToDBoldTable(1728, "staalwol", "staalwol");
        db.addLevelToDBoldTable(1729, "tatoeage", "tatoeage");
        db.addLevelToDBoldTable(1730, "velg", "velg");
        db.addLevelToDBoldTable(1731, "wieldop", "wieldop");
        db.addLevelToDBoldTable(1732, "vrachtwagen", "vrachtwagen");
        db.addLevelToDBoldTable(1733, "tippexroller", "tippexroller");
        db.addLevelToDBoldTable(1734, "kooiladder", "kooiladder");
        db.addLevelToDBoldTable(1735, "meterkast", "meterkast");
        db.addLevelToDBoldTable(1736, "behangrol", "behangrol");
        db.addLevelToDBoldTable(1737, "behanglijm", "behanglijm");
        db.addLevelToDBoldTable(1738, "youtube", "youtube");
        db.addLevelToDBoldTable(1739, "gipsplaat", "gipsplaat");
        db.addLevelToDBoldTable(1740, "MRI scan", "mriscan");
        db.addLevelToDBoldTable(1741, "longen", "longen");
        db.addLevelToDBoldTable(1742, "landmijn", "landmijn");
        db.addLevelToDBoldTable(1743, "oliebrander", "oliebrander");
        db.addLevelToDBoldTable(1744, "chloortablet", "chloortablet");
        db.addLevelToDBoldTable(1745, "handleiding", "handleiding");
        db.addLevelToDBoldTable(1746, "oldtimer", "oldtimer");
        db.addLevelToDBoldTable(1747, "klaslokaal", "klaslokaal");
        db.addLevelToDBoldTable(1748, "whiskey", "whiskey");
        db.addLevelToDBoldTable(1749, "stempas", "stempas");
        db.addLevelToDBoldTable(1750, "tandvlees", "tandvlees");
        db.addLevelToDBoldTable(1751, "kledingkast", "kledingkast");
        db.addLevelToDBoldTable(1752, "fietsmand", "fietsmand");
        db.addLevelToDBoldTable(1753, "bladmuziek", "bladmuziek");
        db.addLevelToDBoldTable(1754, "ijzerdraad", "ijzerdraad");
        db.addLevelToDBoldTable(1755, "kipnuggets", "kipnuggets");
        db.addLevelToDBoldTable(1756, "cruise schip", "cruiseschip");
        db.addLevelToDBoldTable(1757, "parkeer kaart", "parkeerkaart");
        db.addLevelToDBoldTable(1758, "invaliden kaart", "invalidenkaart");
        db.addLevelToDBoldTable(1759, "corset", "corset");
        db.addLevelToDBoldTable(1760, "boodschappentas", "boodschappentas");
        db.addLevelToDBoldTable(1761, "scheur kalender", "scheurkalender");
        db.addLevelToDBoldTable(1762, "ruggen krabber", "ruggenkrabber");
        db.addLevelToDBoldTable(1763, "ganzenbord", "ganzenbord");
        db.addLevelToDBoldTable(1764, "origami", "origami");
        db.addLevelToDBoldTable(1765, "kaasfondue", "kaasfondue");
        db.addLevelToDBoldTable(1766, "bungee jump", "bungeejump");
        db.addLevelToDBoldTable(1767, "selfie stick", "selfiestick");
        db.addLevelToDBoldTable(1768, "noorderlicht", "noorderlicht");
        db.addLevelToDBoldTable(1769, "tol", "tol");
        db.addLevelToDBoldTable(1770, "uggs", "uggs");
        db.addLevelToDBoldTable(1771, "zippo", "zippo");
        db.addLevelToDBoldTable(1772, "muilkorf", "muilkorf");
        db.addLevelToDBoldTable(1773, "wilg", "wilg");
        db.addLevelToDBoldTable(1774, "kolen", "kolen");
        db.addLevelToDBoldTable(1775, "blackjack", "blackjack");
        db.addLevelToDBoldTable(1776, "taxi", "taxi");
        db.addLevelToDBoldTable(1777, "minecraft", "minecraft");
        db.addLevelToDBoldTable(1778, "shotgun", "shotgun");
        db.addLevelToDBoldTable(1779, "nooduitgang", "nooduitgang");
        db.addLevelToDBoldTable(1780, "ahoy", "ahoy");
        db.addLevelToDBoldTable(1781, "de kuip", "dekuip");
        db.addLevelToDBoldTable(1782, "schrift", "schrift");
        db.addLevelToDBoldTable(1783, "pilaar", "pilaar");
        db.addLevelToDBoldTable(1784, "snoek", "snoek");
        db.addLevelToDBoldTable(1785, "yogabal", "yogabal");
        db.addLevelToDBoldTable(1786, "theedoos", "theedoos");
        db.addLevelToDBoldTable(1787, "chocolade melk", "chocolademelk");
        db.addLevelToDBoldTable(1788, "kofferlabel", "kofferlabel");
        db.addLevelToDBoldTable(1789, "skittles", "skittles");
        db.addLevelToDBoldTable(1790, "aladdin", "aladdin");
        db.addLevelToDBoldTable(1791, "aanmaakblokje", "aanmaakblokje");
        db.addLevelToDBoldTable(1792, "kleurplaat", "kleurplaat");
        db.addLevelToDBoldTable(1793, "maltesers", "maltesers");
        db.addLevelToDBoldTable(1794, "like", "like");
        db.addLevelToDBoldTable(1795, "stoofpeer", "stoofpeer");
        db.addLevelToDBoldTable(1796, "stappenteller", "stappenteller");
        db.addLevelToDBoldTable(1797, "tuinpak", "tuinpak");
        db.addLevelToDBoldTable(1798, "tuin kabouter", "tuinkabouter");
        db.addLevelToDBoldTable(1799, "walky talky", "walkytalky");
        db.addLevelToDBoldTable(1800, "monorail", "monorail");
    }

    private void addLevels1801to1900() {
        db.addLevelToDBoldTable(1800, "monorail", "monorail");
        db.addLevelToDBoldTable(1801, "naaldhak", "naaldhak");
        db.addLevelToDBoldTable(1802, "brillen doekje", "brillendoekje");
        db.addLevelToDBoldTable(1803, "ballen pomp", "ballenpomp");
        db.addLevelToDBoldTable(1804, "rammelaar", "rammelaar");
        db.addLevelToDBoldTable(1805, "eierkoker", "eierkoker");
        db.addLevelToDBoldTable(1806, "taser", "taser");
        db.addLevelToDBoldTable(1807, "chihuahua", "chihuahua");
        db.addLevelToDBoldTable(1808, "glasbak", "glasbak");
        db.addLevelToDBoldTable(1809, "rotjes", "rotjes");
        db.addLevelToDBoldTable(1810, "theelepel", "theelepel");
        db.addLevelToDBoldTable(1811, "gasaansteker", "gasaansteker");
        db.addLevelToDBoldTable(1812, "eierkoek", "eierkoek");
        db.addLevelToDBoldTable(1813, "atlas", "atlas");
        db.addLevelToDBoldTable(1814, "paspop", "paspop");
        db.addLevelToDBoldTable(1815, "leverkaas", "leverkaas");
        db.addLevelToDBoldTable(1816, "patience", "patience");
        db.addLevelToDBoldTable(1817, "smeerworst", "smeerworst");
        db.addLevelToDBoldTable(1818, "dna", "dna");
        db.addLevelToDBoldTable(1819, "harpoen", "harpoen");
        db.addLevelToDBoldTable(1820, "aardpeer", "aardpeer");
        db.addLevelToDBoldTable(1821, "verhuisdoos", "verhuisdoos");
        db.addLevelToDBoldTable(1822, "bomberjack", "bomberjack");
        db.addLevelToDBoldTable(1823, "kogelvrij vest", "kogelvrijvest");
        db.addLevelToDBoldTable(1824, "laminaat", "laminaat");
        db.addLevelToDBoldTable(1825, "vloerbedekking", "vloerbedekking");
        db.addLevelToDBoldTable(1826, "tissue", "tissue");
        db.addLevelToDBoldTable(1827, "kookboek", "kookboek");
        db.addLevelToDBoldTable(1828, "kruidenboter", "kruidenboter");
        db.addLevelToDBoldTable(1829, "world of warcraft", "worldofwarcraft");
        db.addLevelToDBoldTable(1830, "excel", "excel");
        db.addLevelToDBoldTable(1831, "raad het voorwerp", "raadhetvoorwerp");
        db.addLevelToDBoldTable(1832, "terminator", "terminator");
        db.addLevelToDBoldTable(1833, "zonsverduistering", "zonsverduistering");
        db.addLevelToDBoldTable(1834, "halve maan", "halvemaan");
        db.addLevelToDBoldTable(1835, "ragout", "ragout");
        db.addLevelToDBoldTable(1836, "ontbijtspek", "ontbijtspek");
        db.addLevelToDBoldTable(1837, "traktor", "traktor");
        db.addLevelToDBoldTable(1838, "vogelverschrikker", "vogelverschrikker");
        db.addLevelToDBoldTable(1839, "paint", "paint");
        db.addLevelToDBoldTable(1840, "rode loper", "rodeloper");
        db.addLevelToDBoldTable(1841, "dranghek", "dranghek");
        db.addLevelToDBoldTable(1842, "ninja", "ninja");
        db.addLevelToDBoldTable(1843, "kalebas", "kalebas");
        db.addLevelToDBoldTable(1844, "kiosk", "kiosk");
        db.addLevelToDBoldTable(1845, "kist", "kist");
        db.addLevelToDBoldTable(1846, "slang", "slang");
        db.addLevelToDBoldTable(1847, "broekzak", "broekzak");
        db.addLevelToDBoldTable(1848, "daffy duck", "daffyduck");
        db.addLevelToDBoldTable(1849, "handdroger", "handdroger");
        db.addLevelToDBoldTable(1850, "pokemon bal", "pokemonbal");
        db.addLevelToDBoldTable(1851, "rollerbank", "rollerbank");
        db.addLevelToDBoldTable(1852, "kuifje", "kuifje");
        db.addLevelToDBoldTable(1853, "rechtbank", "rechtbank");
        db.addLevelToDBoldTable(1854, "kipvingers", "kipvingers");
        db.addLevelToDBoldTable(1855, "pizza oven", "pizzaoven");
        db.addLevelToDBoldTable(1856, "menukaart", "menukaart");
        db.addLevelToDBoldTable(1857, "weer station", "weerstation");
        db.addLevelToDBoldTable(1858, "veldfles", "veldfles");
        db.addLevelToDBoldTable(1859, "bonsai boom", "bonsaiboom");
        db.addLevelToDBoldTable(1860, "graffiti", "graffiti");
        db.addLevelToDBoldTable(1861, "swiffer", "swiffer");
        db.addLevelToDBoldTable(1862, "afzetlint", "afzetlint");
        db.addLevelToDBoldTable(1863, "bouwlamp", "bouwlamp");
        db.addLevelToDBoldTable(1864, "stracciatellaijs", "stracciatellaijs");
        db.addLevelToDBoldTable(1865, "cassave chips", "cassavechips");
        db.addLevelToDBoldTable(1866, "print plaatje", "printplaatje");
        db.addLevelToDBoldTable(1867, "kopieer apparaat", "kopieerapparaat");
        db.addLevelToDBoldTable(1868, "sinaasappelpers", "sinaasappelpers");
        db.addLevelToDBoldTable(1869, "pantheon", "pantheon");
        db.addLevelToDBoldTable(1870, "slow juicer", "slowjuicer");
        db.addLevelToDBoldTable(1871, "eekhoorn", "eekhoorn");
        db.addLevelToDBoldTable(1872, "beuken nootje", "beukennootje");
        db.addLevelToDBoldTable(1873, "tamme kastanje", "tammekastanje");
        db.addLevelToDBoldTable(1874, "vuur vliegje", "vuurvliegje");
        db.addLevelToDBoldTable(1875, "macaron", "macaron");
        db.addLevelToDBoldTable(1876, "bliksem afleider", "bliksemafleider");
        db.addLevelToDBoldTable(1877, "stopknop", "stopknop");
        db.addLevelToDBoldTable(1878, "legbatterij", "legbatterij");
        db.addLevelToDBoldTable(1879, "modelschip", "modelschip");
        db.addLevelToDBoldTable(1880, "kappers stoel", "kappersstoel");
        db.addLevelToDBoldTable(1881, "inkt patroon", "inktpatroon");
        db.addLevelToDBoldTable(1882, "gummy beer", "gummybeer");
        db.addLevelToDBoldTable(1883, "kinder zitje", "kinderzitje");
        db.addLevelToDBoldTable(1884, "rad van fortuin", "radvanfortuin");
        db.addLevelToDBoldTable(1885, "laser gun", "lasergun");
        db.addLevelToDBoldTable(1886, "goochel hoed", "goochelhoed");
        db.addLevelToDBoldTable(1887, "drilpudding", "drilpudding");
        db.addLevelToDBoldTable(1888, "bugles", "bugles");
        db.addLevelToDBoldTable(1889, "the sims", "thesims");
        db.addLevelToDBoldTable(1890, "robot", "robot");
        db.addLevelToDBoldTable(1891, "cracker", "cracker");
        db.addLevelToDBoldTable(1892, "ontbijtkoek", "ontbijtkoek");
        db.addLevelToDBoldTable(1893, "stuiterbal", "stuiterbal");
        db.addLevelToDBoldTable(1894, "postzegel album", "postzegelalbum");
        db.addLevelToDBoldTable(1895, "walrus", "walrus");
        db.addLevelToDBoldTable(1896, "leesbril", "leesbril");
        db.addLevelToDBoldTable(1897, "gordijn", "gordijn");
        db.addLevelToDBoldTable(1898, "aardappel mesje", "aardappelmesje");
        db.addLevelToDBoldTable(1899, "shotglas", "shotglas");
        db.addLevelToDBoldTable(1900, "jagermeister", "jagermeister");

    }

    private void addLevels1901to2000() {
        db.addLevelToDBoldTable(1901, "wimperkrultang", "wimperkrultang");
        db.addLevelToDBoldTable(1902, "rups", "rups");
        db.addLevelToDBoldTable(1903, "mattenklopper", "mattenklopper");
        db.addLevelToDBoldTable(1904, "ijsvogel", "ijsvogel");
        db.addLevelToDBoldTable(1905, "aal", "aal");
        db.addLevelToDBoldTable(1906, "azijn", "azijn");
        db.addLevelToDBoldTable(1907, "abdij", "abdij");
        db.addLevelToDBoldTable(1908, "abraham pop", "abrahampop");
        db.addLevelToDBoldTable(1909, "sarah pop", "sarahpop");
        db.addLevelToDBoldTable(1910, "adelaar", "adelaar");
        db.addLevelToDBoldTable(1911, "afro kapsel", "afrokapsel");
        db.addLevelToDBoldTable(1912, "agaat", "agaat");
        db.addLevelToDBoldTable(1913, "agent", "agent");
        db.addLevelToDBoldTable(1914, "babbelaar", "babbelaar");
        db.addLevelToDBoldTable(1915, "baby", "baby");
        db.addLevelToDBoldTable(1916, "baguette", "baguette");
        db.addLevelToDBoldTable(1917, "nectarine", "nectarine");
        db.addLevelToDBoldTable(1918, "bakfiets", "bakfiets");
        db.addLevelToDBoldTable(1919, "lachspiegel", "lachspiegel");
        db.addLevelToDBoldTable(1920, "balustrade", "balustrade");
        db.addLevelToDBoldTable(1921, "albino", "albino");
        db.addLevelToDBoldTable(1922, "amfoor", "amfoor");
        db.addLevelToDBoldTable(1923, "amsterdammertje", "amsterdammertje");
        db.addLevelToDBoldTable(1924, "baret", "baret");
        db.addLevelToDBoldTable(1925, "bedelaar", "bedelaar");
        db.addLevelToDBoldTable(1926, "goochelstaf", "goochelstaf");
        db.addLevelToDBoldTable(1927, "vuilnisbelt", "vuilnisbelt");
        db.addLevelToDBoldTable(1928, "berg", "berg");
        db.addLevelToDBoldTable(1929, "altaar", "altaar");
        db.addLevelToDBoldTable(1930, "betonblok", "betonblok");
        db.addLevelToDBoldTable(1931, "boekensteun", "boekensteun");
        db.addLevelToDBoldTable(1932, "haardblok", "haardblok");
        db.addLevelToDBoldTable(1933, "wasbak", "wasbak");
        db.addLevelToDBoldTable(1934, "klarinet", "klarinet");
        db.addLevelToDBoldTable(1935, "eiersplitser", "eiersplitser");
        db.addLevelToDBoldTable(1936, "moonboot", "moonboot");
        db.addLevelToDBoldTable(1937, "ketting", "ketting");
        db.addLevelToDBoldTable(1938, "oorbel", "oorbel");
        db.addLevelToDBoldTable(1939, "havermout", "havermout");
        db.addLevelToDBoldTable(1940, "spitskool", "spitskool");
        db.addLevelToDBoldTable(1941, "judaspenning", "judaspenning");
        db.addLevelToDBoldTable(1942, "cyclaam", "cyclaam");
        db.addLevelToDBoldTable(1943, "tuinboon", "tuinboon");
        db.addLevelToDBoldTable(1944, "azalea", "azalea");
        db.addLevelToDBoldTable(1945, "kerstster", "kerstster");
        db.addLevelToDBoldTable(1946, "houtkachel", "houtkachel");
        db.addLevelToDBoldTable(1947, "sterrenkijker", "sterrenkijker");
        db.addLevelToDBoldTable(1948, "lichtkoepel", "lichtkoepel");
        db.addLevelToDBoldTable(1949, "flessenhouder", "flessenhouder");
        db.addLevelToDBoldTable(1950, "luieremmer", "luieremmer");
        db.addLevelToDBoldTable(1951, "aankleedkussen", "aankleedkussen");
        db.addLevelToDBoldTable(1952, "commode", "commode");
        db.addLevelToDBoldTable(1953, "navelklem", "navelklem");
        db.addLevelToDBoldTable(1954, "boxkleed", "boxkleed");
        db.addLevelToDBoldTable(1955, "wipstoeltje", "wipstoeltje");
        db.addLevelToDBoldTable(1956, "vla", "vla");
        db.addLevelToDBoldTable(1957, "danoontje", "danoontje");
        db.addLevelToDBoldTable(1958, "fristi", "fristi");
        db.addLevelToDBoldTable(1959, "zeilboot", "zeilboot");
        db.addLevelToDBoldTable(1960, "trenchcoat", "trenchcoat");
        db.addLevelToDBoldTable(1961, "ING", "logo_ing");
        db.addLevelToDBoldTable(1962, "Nu", "logo_nu");
        db.addLevelToDBoldTable(1963, "tnt", "logo_tnt");
        db.addLevelToDBoldTable(1964, "cup a soup", "logo_cupasoup");
        db.addLevelToDBoldTable(1965, "Albert Heijn", "logo_albertheijn");
        db.addLevelToDBoldTable(1966, "KNVB", "logo_knvb");
        db.addLevelToDBoldTable(1967, "holland casino", "logo_hollandcasino");
        db.addLevelToDBoldTable(1968, "Volkskrant", "logo_volkskrant");
        db.addLevelToDBoldTable(1969, "da", "logo_da");
        db.addLevelToDBoldTable(1970, "deloitte", "logo_deloitte");
        db.addLevelToDBoldTable(1971, "Senseo", "logo_senseo");
        db.addLevelToDBoldTable(1972, "Febo", "logo_febo");
        db.addLevelToDBoldTable(1973, "Hi", "logo_hi");
        db.addLevelToDBoldTable(1974, "anwb", "logo_anwb");
        db.addLevelToDBoldTable(1975, "friesche vlag", "logo_frieschevlag");
        db.addLevelToDBoldTable(1976, "Ajax", "logo_ajax");
        db.addLevelToDBoldTable(1977, "Lotto", "logo_lotto");
        db.addLevelToDBoldTable(1978, "thuisbezorgd", "logo_thuisbezorgd");
        db.addLevelToDBoldTable(1979, "Rabobank", "logo_rabobank");
        db.addLevelToDBoldTable(1980, "pvda", "logo_pvda");
        db.addLevelToDBoldTable(1981, "sbs zes", "logo_sbszes");
        db.addLevelToDBoldTable(1982, "OHRA", "logo_ohra");
        db.addLevelToDBoldTable(1983, "HEMA", "logo_hema");
        db.addLevelToDBoldTable(1984, "marskramer", "logo_marskramer");
        db.addLevelToDBoldTable(1985, "Axe", "logo_axe");
        db.addLevelToDBoldTable(1986, "Bounty", "logo_bounty");
        db.addLevelToDBoldTable(1987, "perry sport", "logo_perrysport");
        db.addLevelToDBoldTable(1988, "hyves", "logo_hyves");
        db.addLevelToDBoldTable(1989, "Hoegaarden", "logo_hoegaarden");
        db.addLevelToDBoldTable(1990, "ritter sport", "logo_rittersport");
        db.addLevelToDBoldTable(1991, "Lassie", "logo_lassie");
        db.addLevelToDBoldTable(1992, "Bertolli", "logo_bertolli");
        db.addLevelToDBoldTable(1993, "volvo", "logo_volvo");
        db.addLevelToDBoldTable(1994, "scapino", "logo_scapino");
        db.addLevelToDBoldTable(1995, "Chupa chups", "logo_chupachups");
        db.addLevelToDBoldTable(1996, "Hornbach", "logo_hornbach");
        db.addLevelToDBoldTable(1997, "selexyz", "logo_selexyz");
        db.addLevelToDBoldTable(1998, "Youtube", "logo_youtube");
        db.addLevelToDBoldTable(1999, "vicks", "logo_vicks");
        db.addLevelToDBoldTable(2000, "nintendo", "logo_nintendo");
    }

    public void addLevels2001to2100() {
        db.addLevelToDBoldTable(2001, "schoenspanners", "schoenspanners");
        db.addLevelToDBoldTable(2002, "opzet borstel", "opzetborstel");
        db.addLevelToDBoldTable(2003, "schoensmeer", "schoensmeer");
        db.addLevelToDBoldTable(2004, "thermostaatkraan", "thermostaatkraan");
        db.addLevelToDBoldTable(2005, "kledingrek", "kledingrek");
        db.addLevelToDBoldTable(2006, "barcode scanner", "barcodescanner");
        db.addLevelToDBoldTable(2007, "detectiepoort", "detectiepoort");
        db.addLevelToDBoldTable(2008, "buitenboordmotor", "buitenboordmotor");
        db.addLevelToDBoldTable(2009, "buitenboordbeugel", "buitenboordbeugel");
        db.addLevelToDBoldTable(2010, "kraanvogel", "kraanvogel");
        db.addLevelToDBoldTable(2011, "schuifdeur", "schuifdeur");
        db.addLevelToDBoldTable(2012, "opel", "opel");
        db.addLevelToDBoldTable(2013, "diploma", "diploma");
        db.addLevelToDBoldTable(2014, "zwem diploma", "zwemdiploma");
        db.addLevelToDBoldTable(2015, "vlot", "vlot");
        db.addLevelToDBoldTable(2016, "zwemvleugel", "zwemvleugel");
        db.addLevelToDBoldTable(2017, "kleedkamer", "kleedkamer");
        db.addLevelToDBoldTable(2018, "kapstok haakje", "kapstokhaakje");
        db.addLevelToDBoldTable(2019, "rioolbuis", "rioolbuis");
        db.addLevelToDBoldTable(2020, "water leiding", "waterleiding");
        db.addLevelToDBoldTable(2021, "gaskraan", "gaskraan");
        db.addLevelToDBoldTable(2022, "helium ballon", "heliumballon");
        db.addLevelToDBoldTable(2023, "suiker beest", "suikerbeest");
        db.addLevelToDBoldTable(2024, "vlieger papier", "vliegerpapier");
        db.addLevelToDBoldTable(2025, "vlieger touw", "vliegertouw");
        db.addLevelToDBoldTable(2026, "graan cirkel", "graancirkel");
        db.addLevelToDBoldTable(2027, "tarwegras", "tarwegras");
        db.addLevelToDBoldTable(2028, "sigaar knipper", "sigaarknipper");
        db.addLevelToDBoldTable(2029, "stortbak", "stortbak");
        db.addLevelToDBoldTable(2030, "kruidenkaas", "kruidenkaas");
        db.addLevelToDBoldTable(2031, "vlindermes", "vlindermes");
        db.addLevelToDBoldTable(2032, "vlinder struik", "vlinderstruik");
        db.addLevelToDBoldTable(2033, "vlinder boor", "vlinderboor");
        db.addLevelToDBoldTable(2034, "go pro", "gopro");
        db.addLevelToDBoldTable(2035, "schudde buikjes", "schuddebuikjes");
        db.addLevelToDBoldTable(2036, "brommobiel", "brommobiel");
        db.addLevelToDBoldTable(2037, "geiser", "geiser");
        db.addLevelToDBoldTable(2038, "racefiets", "racefiets");
        db.addLevelToDBoldTable(2039, "racewagen", "racewagen");
        db.addLevelToDBoldTable(2040, "racehelm", "racehelm");
        db.addLevelToDBoldTable(2041, "alpenhoorn", "alpenhoorn");
        db.addLevelToDBoldTable(2042, "bamboe fluit", "bamboefluit");
        db.addLevelToDBoldTable(2043, "bandola", "bandola");
        db.addLevelToDBoldTable(2044, "basgitaar", "basgitaar");
        db.addLevelToDBoldTable(2045, "bekkens", "bekkens");
        db.addLevelToDBoldTable(2046, "bongo", "bongo");
        db.addLevelToDBoldTable(2047, "cello", "cello");
        db.addLevelToDBoldTable(2048, "contrabas", "contrabas");
        db.addLevelToDBoldTable(2049, "djembe", "djembe");
        db.addLevelToDBoldTable(2050, "hobo", "hobo");
        db.addLevelToDBoldTable(2051, "afkoel rooster", "afkoelrooster");
        db.addLevelToDBoldTable(2052, "ananas snijder", "ananassnijder");
        db.addLevelToDBoldTable(2053, "asperge schep", "aspergeschep");
        db.addLevelToDBoldTable(2054, "braadzak", "braadzak");
        db.addLevelToDBoldTable(2055, "citroen knijper", "citroenknijper");
        db.addLevelToDBoldTable(2056, "eetstokjes", "eetstokjes");
        db.addLevelToDBoldTable(2057, "eierprikker", "eierprikker");
        db.addLevelToDBoldTable(2058, "gehakt molen", "gehaktmolen");
        db.addLevelToDBoldTable(2059, "hogedruk pan", "hogedrukpan");
        db.addLevelToDBoldTable(2060, "juslepel", "juslepel");
        db.addLevelToDBoldTable(2061, "pasfoto hokje", "pasfotohokje");
        db.addLevelToDBoldTable(2062, "manchet knoop", "manchetknoop");
        db.addLevelToDBoldTable(2063, "bloedsinaasappel", "bloedsinaasappel");
        db.addLevelToDBoldTable(2064, "granaat appel", "granaatappel");
        db.addLevelToDBoldTable(2065, "kumquat", "kumquat");
        db.addLevelToDBoldTable(2066, "moerbei", "moerbei");
        db.addLevelToDBoldTable(2067, "pomelo", "pomelo");
        db.addLevelToDBoldTable(2068, "vijg", "vijg");
        db.addLevelToDBoldTable(2069, "andijvie", "andijvie");
        db.addLevelToDBoldTable(2070, "chinese kool", "chinesekool");
        db.addLevelToDBoldTable(2071, "knolraap", "knolraap");
        db.addLevelToDBoldTable(2072, "pastinaak", "pastinaak");
        db.addLevelToDBoldTable(2073, "zeekraal", "zeekraal");
        db.addLevelToDBoldTable(2074, "backgammon", "backgammon");
        db.addLevelToDBoldTable(2075, "carcassonne", "carcassonne");
        db.addLevelToDBoldTable(2076, "kubb", "kubb");
        db.addLevelToDBoldTable(2077, "rollit", "rollit");
        db.addLevelToDBoldTable(2078, "vier op een rij", "vieropeenrij");
        db.addLevelToDBoldTable(2079, "vlokken", "vlokken");
        db.addLevelToDBoldTable(2080, "kievitsei", "kievitsei");
        db.addLevelToDBoldTable(2081, "bok", "bok");
        db.addLevelToDBoldTable(2082, "paardrij cap", "paardrijcap");
        db.addLevelToDBoldTable(2083, "manege", "manege");
        db.addLevelToDBoldTable(2084, "bmx", "bmx");
        db.addLevelToDBoldTable(2085, "grashark", "grashark");
        db.addLevelToDBoldTable(2086, "grondboor", "grondboor");
        db.addLevelToDBoldTable(2087, "schutting", "schutting");
        db.addLevelToDBoldTable(2088, "houtversnipperaar", "houtversnipperaar");
        db.addLevelToDBoldTable(2089, "kantensteker", "kantensteker");
        db.addLevelToDBoldTable(2090, "kwartel", "kwartel");
        db.addLevelToDBoldTable(2091, "speeldoos", "speeldoos");
        db.addLevelToDBoldTable(2092, "micro sd", "microsd");
        db.addLevelToDBoldTable(2093, "sneeuwbol", "sneeuwbol");
        db.addLevelToDBoldTable(2094, "komijne kaas", "komijnekaas");
        db.addLevelToDBoldTable(2095, "fax", "fax");
        db.addLevelToDBoldTable(2096, "ufo", "ufo");
        db.addLevelToDBoldTable(2097, "boeken kast", "boekenkast");
        db.addLevelToDBoldTable(2098, "beveiligersbadge", "beveiligersbadge");
        db.addLevelToDBoldTable(2099, "kenteken bewijs", "kentekenbewijs");
        db.addLevelToDBoldTable(2100, "scanner", "scanner");

    }

    public void addLevels2101to2200() {
        db.addLevelToDBoldTable(2101, "Infuusnaald", "infuusnaald");
        db.addLevelToDBoldTable(2102, "Ziekenhuis bed", "ziekenhuisbed");
        db.addLevelToDBoldTable(2103, "Praalwagen", "praalwagen");
        db.addLevelToDBoldTable(2104, "Kruimel vlaai", "kruimelvlaai");
        db.addLevelToDBoldTable(2105, "Opblaasboot", "opblaasboot");
        db.addLevelToDBoldTable(2106, "Kraaienpoot", "kraaienpoot");
        db.addLevelToDBoldTable(2107, "T bone steak", "tbonesteak");
        db.addLevelToDBoldTable(2108, "Dranghek", "dranghek");
        db.addLevelToDBoldTable(2109, "Haaientand", "haaientand");
        db.addLevelToDBoldTable(2110, "Blikvanger", "blikvanger");
        db.addLevelToDBoldTable(2111, "Macadamia", "macadamia");
        db.addLevelToDBoldTable(2112, "Suiker wafel", "suikerwafel");
        db.addLevelToDBoldTable(2113, "Elektrischestoel", "elektrischestoel");
        db.addLevelToDBoldTable(2114, "Lavasteen", "lavasteen");
        db.addLevelToDBoldTable(2115, "Gelhaard", "gelhaard");
        db.addLevelToDBoldTable(2116, "Buiten keuken", "buitenkeuken");
        db.addLevelToDBoldTable(2117, "Midget golfbaan", "midgetgolfbaan");
        db.addLevelToDBoldTable(2118, "Letterbak", "letterbak");
        db.addLevelToDBoldTable(2119, "Prikpen", "prikpen");
        db.addLevelToDBoldTable(2120, "rugby helm", "rugbyhelm");
        db.addLevelToDBoldTable(2121, "barcode scanner", "barcodescanner");
        db.addLevelToDBoldTable(2122, "Waterschoen", "waterschoen");
        db.addLevelToDBoldTable(2123, "Koriander", "koriander");
        db.addLevelToDBoldTable(2124, "Rosti rondje", "rostirondje");
        db.addLevelToDBoldTable(2125, "Landingsgestel", "landingsgestel");
        db.addLevelToDBoldTable(2126, "Paastak", "paastak");
        db.addLevelToDBoldTable(2127, "Chicken sensation", "chickensensation");
        db.addLevelToDBoldTable(2128, "Popcorn maker", "popcornmaker");
        db.addLevelToDBoldTable(2129, "Parasol voet", "parasolvoet");
        db.addLevelToDBoldTable(2130, "Vuurspuwer", "vuurspuwer");
        db.addLevelToDBoldTable(2131, "Golfplaat", "golfplaat");
        db.addLevelToDBoldTable(2132, "Koffie broodje", "koffiebroodje");
        db.addLevelToDBoldTable(2133, "Tapijtsnijder", "tapijtsnijder");
        db.addLevelToDBoldTable(2134, "Ijskoffie", "ijskoffie");
        db.addLevelToDBoldTable(2135, "Vangrails", "vangrail");
        db.addLevelToDBoldTable(2136, "Buiten douche", "buitendouche");
        db.addLevelToDBoldTable(2137, "Airfryer", "airfryer");
        db.addLevelToDBoldTable(2138, "Bingobal", "bingobal");
        db.addLevelToDBoldTable(2139, "Bingorad", "bingorad");
        db.addLevelToDBoldTable(2140, "Neuspleister", "neuspleister");
        db.addLevelToDBoldTable(2141, "Vlammenwerper", "vlammenwerper");
        db.addLevelToDBoldTable(2142, "Tankwagen", "tankwagen");
        db.addLevelToDBoldTable(2143, "Dakpan", "dakpan");
        db.addLevelToDBoldTable(2144, "Slaapmuts", "slaapmuts");
        db.addLevelToDBoldTable(2145, "Melkmachine", "melkmachine");
        db.addLevelToDBoldTable(2146, "Salmiak lolly", "salmiaklolly");
        db.addLevelToDBoldTable(2147, "Zwangerschapstest", "zwangerschapstest");
        db.addLevelToDBoldTable(2148, "Muntthee", "muntthee");
        db.addLevelToDBoldTable(2149, "Klapstoel", "klapstoel2");
        db.addLevelToDBoldTable(2150, "Oliebollen kraam", "oliebollenkraam");
        db.addLevelToDBoldTable(2151, "Wachthuisje", "wachthuisje");
        db.addLevelToDBoldTable(2152, "Luchtbuks", "luchtbuks");
        db.addLevelToDBoldTable(2153, "erlenmeyer", "erlenmeyer");
        db.addLevelToDBoldTable(2154, "Apple watch", "applewatch");
        db.addLevelToDBoldTable(2155, "Kroon juwelen", "kroonjuwelen");
        db.addLevelToDBoldTable(2156, "Parelmoer", "parelmoer");
        db.addLevelToDBoldTable(2157, "Jumpsuit", "jumpsuit");
        db.addLevelToDBoldTable(2158, "Punto banco", "puntobanco");
        db.addLevelToDBoldTable(2159, "Regenpak", "regenpak");
        db.addLevelToDBoldTable(2160, "Lakmoes proef", "lakmoesproef");
        db.addLevelToDBoldTable(2161, "Zuignap", "zuignap");
        db.addLevelToDBoldTable(2162, "parasol", "parasol");
        db.addLevelToDBoldTable(2163, "rugby doel", "rugbydoel");
        db.addLevelToDBoldTable(2164, "gourmetstel", "gourmetstel");
        db.addLevelToDBoldTable(2165, "triviant", "triviant");
        db.addLevelToDBoldTable(2166, "bladerdeeg", "bladerdeeg");
        db.addLevelToDBoldTable(2167, "visitekaartje", "visitekaartje");
        db.addLevelToDBoldTable(2168, "bladluis", "bladluis");
        db.addLevelToDBoldTable(2169, "zuurstof fles", "zuurstoffles");
        db.addLevelToDBoldTable(2170, "photoshop", "photoshop");
        db.addLevelToDBoldTable(2171, "gymzaal", "gymzaal");
        db.addLevelToDBoldTable(2172, "water zuivering", "waterzuivering");
        db.addLevelToDBoldTable(2173, "maeslantkering", "maeslantkering");
        db.addLevelToDBoldTable(2174, "tekentablet", "tekentablet");
        db.addLevelToDBoldTable(2175, "lange mat", "langemat");
        db.addLevelToDBoldTable(2176, "klimrek", "klimrek");
        db.addLevelToDBoldTable(2177, "ongelijke brug", "ongelijkebrug");
        db.addLevelToDBoldTable(2178, "mierenlokdoosje", "mierenlokdoosje");
        db.addLevelToDBoldTable(2179, "miereneter", "miereneter");
        db.addLevelToDBoldTable(2180, "snorharen", "snorharen");
        db.addLevelToDBoldTable(2181, "gedroogde tomaat", "gedroogdetomaat");
        db.addLevelToDBoldTable(2182, "ekster", "ekster");
        db.addLevelToDBoldTable(2183, "merel", "merel");
        db.addLevelToDBoldTable(2184, "formule een", "formuleeen");
        db.addLevelToDBoldTable(2185, "preekstoel", "preekstoel");
        db.addLevelToDBoldTable(2186, "marmer", "marmer");
        db.addLevelToDBoldTable(2187, "bosui", "bosui");
        db.addLevelToDBoldTable(2188, "rieten stoel", "rietenstoel");
        db.addLevelToDBoldTable(2189, "marathon", "marathon");
        db.addLevelToDBoldTable(2190, "hommel", "hommel");
        db.addLevelToDBoldTable(2191, "bloembol", "bloembol");
        db.addLevelToDBoldTable(2192, "kristallen bol", "kristallenbol");
        db.addLevelToDBoldTable(2193, "zonwering", "zonwering");
        db.addLevelToDBoldTable(2194, "spreekstoel", "spreekstoel");
        db.addLevelToDBoldTable(2195, "erepodium", "erepodium");
        db.addLevelToDBoldTable(2196, "wandel sokken", "wandelsokken");
        db.addLevelToDBoldTable(2197, "bruistablet", "bruistablet");
        db.addLevelToDBoldTable(2198, "wedstrijdbad", "wedstrijdbad");
        db.addLevelToDBoldTable(2199, "startblok", "startblok");
        db.addLevelToDBoldTable(2200, "loopbrug", "loopbrug");

    }

    public void addLevels2201to2275() {
        db.addLevelToDBoldTable(2201, "Stoomtrein", "stoomtrein");
        db.addLevelToDBoldTable(2202, "blusdeken", "blusdeken");
        db.addLevelToDBoldTable(2203, "Voetenbad", "voetenbad");
        db.addLevelToDBoldTable(2204, "Kippengaas", "kippengaas");
        db.addLevelToDBoldTable(2205, "Tijgerbrood", "tijgerbrood");
        db.addLevelToDBoldTable(2206, "karamel", "karamel");
        db.addLevelToDBoldTable(2207, "Dobber", "dobber");
        db.addLevelToDBoldTable(2208, "Swiffer", "swiffer");
        db.addLevelToDBoldTable(2209, "Lelijke eend", "lelijkeeend");
        db.addLevelToDBoldTable(2210, "Gouden koets", "goudenkoets");
        db.addLevelToDBoldTable(2211, "Label printer", "labelprinter");
        db.addLevelToDBoldTable(2212, "Overall", "overall");
        db.addLevelToDBoldTable(2213, "Vliegende hollander", "vliegendehollander");
        db.addLevelToDBoldTable(2214, "Kartbaan", "kartbaan");
        db.addLevelToDBoldTable(2215, "Pistache ijs", "pistacheijs");
        db.addLevelToDBoldTable(2216, "Krulsla", "krulsla");
        db.addLevelToDBoldTable(2217, "Vlaamse friet", "vlaamsefriet");
        db.addLevelToDBoldTable(2218, "Drainage buis", "drainagebuis");
        db.addLevelToDBoldTable(2219, "Teek", "teek");
        db.addLevelToDBoldTable(2220, "Zilvervisje", "zilvervisje");
        db.addLevelToDBoldTable(2221, "Beugelfles", "beugelfles");
        db.addLevelToDBoldTable(2222, "Biljart krijt", "biljartkrijt");
        db.addLevelToDBoldTable(2223, "Gewapend beton", "gewapendbeton");
        db.addLevelToDBoldTable(2224, "Cocktail glas", "cocktailglas");
        db.addLevelToDBoldTable(2225, "Aardbei plant", "aardbeiplant");
        db.addLevelToDBoldTable(2226, "Tros tomaten", "trostomaten");
        db.addLevelToDBoldTable(2227, "Steenbok", "steenbok");
        db.addLevelToDBoldTable(2228, "Churros", "churros");
        db.addLevelToDBoldTable(2229, "Ras patat", "raspatat");
        db.addLevelToDBoldTable(2230, "dwangbuis", "dwangbuis");
        db.addLevelToDBoldTable(2231, "herdershond", "herdershond");
        db.addLevelToDBoldTable(2232, "vliegen zwam", "vliegenzwam");
        db.addLevelToDBoldTable(2233, "staalborstel", "staalborstel");
        db.addLevelToDBoldTable(2234, "zweinstein", "zweinstein");
        db.addLevelToDBoldTable(2235, "trolley koffer", "trolleykoffer");
        db.addLevelToDBoldTable(2236, "busbaan", "busbaan");
        db.addLevelToDBoldTable(2237, "plaszak", "plaszak");
        db.addLevelToDBoldTable(2238, "bellenblaaszwaard", "bellenblaaszwaard");
        db.addLevelToDBoldTable(2239, "zure bom", "zurebom");
        db.addLevelToDBoldTable(2240, "romertopf", "romertopf");
        db.addLevelToDBoldTable(2241, "waterflosser", "waterflosser");
        db.addLevelToDBoldTable(2242, "schuimblok", "schuimblok");
        db.addLevelToDBoldTable(2243, "ijspegel", "ijspegel");
        db.addLevelToDBoldTable(2244, "bouwplaat", "bouwplaat");
        db.addLevelToDBoldTable(2245, "imker", "imker");
        db.addLevelToDBoldTable(2246, "omafiets", "omafiets");
        db.addLevelToDBoldTable(2247, "hartslagmeter", "hartslagmeter");
        db.addLevelToDBoldTable(2248, "google maps", "googlemaps");
        db.addLevelToDBoldTable(2249, "corona", "corona");
        db.addLevelToDBoldTable(2250, "pergola", "pergola");
        db.addLevelToDBoldTable(2251, "dirk jan", "dirkjan");
        db.addLevelToDBoldTable(2252, "bussluis", "bussluis");
        db.addLevelToDBoldTable(2253, "apple tv", "appletv");
        db.addLevelToDBoldTable(2254, "walkman", "walkman");
        db.addLevelToDBoldTable(2255, "ijssalon", "ijssalon");
        db.addLevelToDBoldTable(2256, "scoubidou", "scoubidou");
        db.addLevelToDBoldTable(2257, "schaafijs", "schaafijs");
        db.addLevelToDBoldTable(2258, "vochtvreter", "vochtvreter");
        db.addLevelToDBoldTable(2259, "mammoet", "mammoet");
        db.addLevelToDBoldTable(2260, "kaasstengel", "kaasstengel");
        db.addLevelToDBoldTable(2261, "adapter", "adapter");
        db.addLevelToDBoldTable(2262, "accu", "accu");
        db.addLevelToDBoldTable(2263, "tochtstrip", "tochtstrip");
        db.addLevelToDBoldTable(2264, "florence", "florence");
        db.addLevelToDBoldTable(2265, "tempura", "tempura");
        db.addLevelToDBoldTable(2266, "vliegen lamp", "vliegenlamp");
        db.addLevelToDBoldTable(2267, "vliegend hert", "vliegendhert");
        db.addLevelToDBoldTable(2268, "bubbeltjes plastic", "bubbeltjesplastic");
        db.addLevelToDBoldTable(2269, "aambeeld", "aambeeld");
        db.addLevelToDBoldTable(2270, "baardaap", "baardaap");
        db.addLevelToDBoldTable(2271, "badhuis", "badhuis");
        db.addLevelToDBoldTable(2272, "everzwijn", "everzwijn");
        db.addLevelToDBoldTable(2273, "bamboe stok", "bamboestok");
        db.addLevelToDBoldTable(2274, "amandel spijs", "amandelspijs");
        db.addLevelToDBoldTable(2275, "kleefpasta", "kleefpasta");

    }


    public void addLevels2451to2525() {
        db.addLevelToDBoldTable(2451, "swarovski", "swarovski");
        db.addLevelToDBoldTable(2452, "wc emmer", "wcemmer");
        db.addLevelToDBoldTable(2453, "enter knop", "enterknop");
        db.addLevelToDBoldTable(2454, "kaneelstok", "kaneelstok");
        db.addLevelToDBoldTable(2455, "kaneelkussens", "kaneelkussens");
        db.addLevelToDBoldTable(2456, "suikerriet", "suikerriet");
        db.addLevelToDBoldTable(2457, "tulband cake", "tulbandcake");
        db.addLevelToDBoldTable(2458, "peperkoek", "peperkoek");
        db.addLevelToDBoldTable(2459, "pakketzegel", "pakketzegel");
        db.addLevelToDBoldTable(2460, "salmiakdrop", "salmiakdrop");
        db.addLevelToDBoldTable(2461, "apenkop", "apenkop");
        db.addLevelToDBoldTable(2462, "biggenkop", "biggenkop");
        db.addLevelToDBoldTable(2463, "grote mat", "grotemat");
        db.addLevelToDBoldTable(2464, "piepschuim bal", "piepschuimbal");
        db.addLevelToDBoldTable(2465, "vr bril", "vrbril");
        db.addLevelToDBoldTable(2466, "serpentine", "serpentine");
        db.addLevelToDBoldTable(2467, "haarverf", "haarverf");
        db.addLevelToDBoldTable(2468, "trol", "trol");
        db.addLevelToDBoldTable(2469, "schuimtaart", "schuimtaart");
        db.addLevelToDBoldTable(2470, "zeeschuim", "zeeschuim");
        db.addLevelToDBoldTable(2471, "david", "david");
        db.addLevelToDBoldTable(2472, "moestuin", "moestuin");
        db.addLevelToDBoldTable(2473, "woody", "woody");
        db.addLevelToDBoldTable(2474, "etagere", "etagere");
        db.addLevelToDBoldTable(2475, "verkeersdrempel", "verkeersdrempel");
        db.addLevelToDBoldTable(2476, "esdoorn", "esdoorn");
        db.addLevelToDBoldTable(2477, "kogelhuls", "kogelhuls");
        db.addLevelToDBoldTable(2478, "hulst", "hulst");
        db.addLevelToDBoldTable(2479, "nutella", "nutella");
        db.addLevelToDBoldTable(2480, "decanteerkaraf", "decanteerkaraf");
        db.addLevelToDBoldTable(2481, "wijnvat", "wijnvat");
        db.addLevelToDBoldTable(2482, "wijnvlek", "wijnvlek");
        db.addLevelToDBoldTable(2483, "tankdop", "tankdop");
        db.addLevelToDBoldTable(2484, "geurstokjes", "geurstokjes");
        db.addLevelToDBoldTable(2485, "sushi roller", "sushiroller");
        db.addLevelToDBoldTable(2486, "treurwilg", "treurwilg");
        db.addLevelToDBoldTable(2487, "houtworm", "houtworm");
        db.addLevelToDBoldTable(2488, "vaseline", "vaseline");
        db.addLevelToDBoldTable(2489, "twister ijsje", "twisterijsje");
        db.addLevelToDBoldTable(2490, "vlizotrap", "vlizotrap");
        db.addLevelToDBoldTable(2491, "zeep dispenser", "zeepdispenser");
        db.addLevelToDBoldTable(2492, "geluidswal", "geluidswal");
        db.addLevelToDBoldTable(2493, "kardemom", "kardemom");
        db.addLevelToDBoldTable(2494, "truffel", "truffel");
        db.addLevelToDBoldTable(2495, "zeevrucht bonbon", "zeevruchtbonbon");
        db.addLevelToDBoldTable(2496, "kraanmoertang", "kraanmoertang");
        db.addLevelToDBoldTable(2497, "fluitenkruid", "fluitenkruid");
        db.addLevelToDBoldTable(2498, "kaneelrol", "kaneelrol");
        db.addLevelToDBoldTable(2499, "zeeuwse bolus", "zeeuwsebolus");
        db.addLevelToDBoldTable(2500, "tongschraper", "tongschraper");
        db.addLevelToDBoldTable(2501, "brandweer", "brandweerlogo");
        db.addLevelToDBoldTable(2502, "gandalf", "gandalf");
        db.addLevelToDBoldTable(2503, "harry potter", "harrypotter");
        db.addLevelToDBoldTable(2504, "steunzool", "steunzool");
        db.addLevelToDBoldTable(2505, "regenboog vlag", "regenboogvlag");
        db.addLevelToDBoldTable(2506, "citrusrasp", "citrusrasp");
        db.addLevelToDBoldTable(2507, "valentijns kaart", "valentijnskaart");
        db.addLevelToDBoldTable(2508, "hitman", "hitman");
        db.addLevelToDBoldTable(2509, "stroopkoek", "stroopkoek");
        db.addLevelToDBoldTable(2510, "varkenshaas", "varkenshaas");
        db.addLevelToDBoldTable(2511, "entrecote", "entrecote");
        db.addLevelToDBoldTable(2512, "lamskotelet", "lamskotelet");
        db.addLevelToDBoldTable(2513, "staldeur", "staldeur");
        db.addLevelToDBoldTable(2514, "botercreme", "botercreme");
        db.addLevelToDBoldTable(2515, "cheesecake", "cheesecake");
        db.addLevelToDBoldTable(2516, "operatietafel", "operatietafel");
        db.addLevelToDBoldTable(2517, "spinnewiel", "spinnewiel");
        db.addLevelToDBoldTable(2518, "eenwieler", "eenwieler");
        db.addLevelToDBoldTable(2519, "mimespeler", "mimespeler");
        db.addLevelToDBoldTable(2520, "chocolade truffel", "chocoladetruffel");
        db.addLevelToDBoldTable(2521, "kauwgom bel", "kauwgombel");
        db.addLevelToDBoldTable(2522, "pizzarette", "pizzarette");
        db.addLevelToDBoldTable(2523, "draaitafel", "draaitafel");
        db.addLevelToDBoldTable(2524, "simkaart knipper", "simkaartknipper");
        db.addLevelToDBoldTable(2525, "titanic", "titanic");

    }

}




