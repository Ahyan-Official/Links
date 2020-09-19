package com.links.app.a4x2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //initializing WebView
    private WebView mwebView;
    SwipeRefreshLayout swipe;
    ActionBarDrawerToggle toggle;

    MyApplication myApplication;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myApplication = MyApplication.getmInstense();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){ ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }else{
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }
//        }
//        statusCheck();



        AdView adView = (AdView) findViewById(R.id.adView2);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Munyayi" + "</font>")));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mwebView.loadUrl("https://grotels.in/");

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();







        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    //open drawer
                    drawerLayout.openDrawer(GravityCompat.START);

                }
            }
        });

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                LoadWebPull();
                //LoadWeb();
            }
        });


//        AdView adView1 = new AdView(this);
//        adView1.setAdSize(AdSize.BANNER);
//        adView1.setAdUnitId("ca-app-pub-7874876290663268/1708233849");


//        AdRequest request = new AdRequest.Builder().build();
//        adView.loadAd(request);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Log.d("onAdFailedToLoad", "This is why: "+errorCode);
//            }});
//



        LoadWeb();





    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void LoadWebPull(){




        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(110);
        //mwebView.loadUrl("https://minitask.fun");
        //force links open in webview only
        //swipe.setRefreshing(true);



        String url = mwebView.getUrl();
        mwebView.loadUrl(url);




    }

    public void LoadWeb(){


        //WebView
        mwebView = (WebView) findViewById(R.id.myWebView);
        //mwebView.setListener(this, (AdvancedWebView.Listener) this);
        WebSettings webSettings = mwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);




        //improve webView performance
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(false);
        webSettings.setEnableSmoothTransition(true);

        mwebView.getSettings().setUseWideViewPort(true);
        mwebView.getSettings().setLoadWithOverviewMode(true);
        mwebView.getSettings().setBuiltInZoomControls(true);
        mwebView.getSettings().setDisplayZoomControls(false);

        mwebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);




        mwebView.setWebViewClient(new MyWebviewClient());


        mwebView.setWebChromeClient(new WebChromeClient()
        {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                // Always grant permission since the app itself requires location
                // permission and the user has therefore already granted it
                callback.invoke(origin, true, false);
            }

        });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mwebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(110);
        //mwebView.loadUrl("https://grotels.in/");
        //force links open in webview only
        //swipe.setRefreshing(true);

        mwebView.loadUrl("https://www.munyayi.com/");
        //Log.e("hellow", String.valueOf(mwebView.isFileUploadAvailable()));


    }

    ProgressBar progressBar;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        swipe.setRefreshing(true);
        if (id == R.id.nav_1) {
            
           mwebView.loadUrl("https://grotels.in/");
        } else if (id == R.id.nav_2) {
            mwebView.loadUrl("https://airvoice.in/index.php?route=product/category&path=82");
        } else if (id == R.id.nav_3) {
            mwebView.loadUrl("https://airvoice.in/index.php?route=product/category&path=81");
        }else if (id == R.id.nav_4) {
            mwebView.loadUrl("https://airvoice.in/index.php?route=product/category&path=73");
        }else if (id == R.id.nav_5) {
            mwebView.loadUrl("https://airvoice.in/index.php?route=product/category&path=78");
        }else if (id == R.id.nav_6) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if(mwebView!=null){

                            mwebView.loadUrl("https://www.munyayi.com/");

                        }
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {





            Log.e("url2", Uri.parse(url).getHost().toString());

            if (Uri.parse(url).getHost().equals("https://grotels.in/wp-login.php?action=logout&amp;_wpnonce=b18ef27113")) {
                //open url contents in webview
                Toast.makeText(getApplicationContext(),"sadas", Toast.LENGTH_SHORT).show();
                deleteCache(getApplicationContext());
                WebStorage webStorage = WebStorage.getInstance();
                webStorage.deleteAllData();
                CookieSyncManager.createInstance(getApplicationContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                mwebView.loadUrl(url);
                webStorage.deleteAllData();
                CookieSyncManager.createInstance(getApplicationContext());
                cookieManager.removeAllCookie();
                deleteCache(getApplicationContext());


                return true;
            } else {
                //here open external links in external browser or app
                mwebView.loadUrl(url);

                return true;
            }

        }
        //ProgressDialogue
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            pd=new ProgressDialog(MainActivity.this);
//            pd.setTitle("Please Wait..");
//            pd.setMessage("Website is Loading..");
//            pd.show();
            swipe.setRefreshing(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //pd.dismiss();
            swipe.setRefreshing(false);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);


            Log.e("Eroor","ssss"+error
            .toString());
            Toast.makeText(getApplicationContext(),"Error 404", Toast.LENGTH_LONG).show();
        }
    }
    //goto previous page when pressing back button

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mwebView.canGoBack()) {
                        mwebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //toggle.syncState();
    }
}
