package com.links.app.a4x2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Main3Activity extends AppCompatActivity {
    /** Called when the activity is first created. */

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;


    WebView mWebView;
    SwipeRefreshLayout swipe;


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
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

    private TabLayout tabLayout;

    TabItem blog,channel;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    int a = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);




        isWriteStoragePermissionGranted();
//        if (ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)){ ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }else{
//                ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }
//        }
//        statusCheck();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        mWebView= (WebView) findViewById(R.id.myWebView);
        mWebView.clearCache(true);
        mWebView.getSettings().getJavaScriptEnabled();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
       // mWebView.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().supportZoom();

        mWebView.setWebViewClient(new WebViewClient() {



            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                Log.e("weburl===",url.toString());
                if (url.startsWith("mailto:")) {
                    final Activity activity = Main3Activity.this;
                    if (activity != null) {
                        MailTo mt = MailTo.parse(url);
                        Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                        activity.startActivity(i);
                        view.reload();
                        return true;
                    }
                }




                PackageManager pm = getApplicationContext().getPackageManager();

//                if (url.startsWith("whatsapp://")) {
//
//
//                    if(isPackageInstalled("com.whatsapp",pm)){
//
//                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//
//
//                    }else{
//                        // create an intent for Play Store
//
//                        final Uri uri = Uri.parse("market://details?id=com.whatsapp");
//                        final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                        @SuppressWarnings({"NewApi", "deprecation"})
//                        final int newDocumentFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? Intent.FLAG_ACTIVITY_NEW_DOCUMENT : Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
//                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | newDocumentFlag | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//
//                        try {
//                            // open Whatsapp listing in Play Store app
//                            startActivity(goToMarket);
//                        } catch (ActivityNotFoundException ex) {
//                            // open Whatsapp listing in browser
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
//                        }
//
//
//                    }
//                    return true;
//
//                }else


                if (url != null && (url.startsWith("https://www.instagram.com") || url.startsWith("http://instagram.com/") || url.startsWith("https://instagram.com/"))) {

                    //mWebView.goBack();
                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    //view.loadUrl("https://www.google.com");

                    Log.e("insta","lassan");
                    return false;

                }else


                if(url.startsWith("https://www.facebook.com/sharer") || url.startsWith("https://m.facebook.com/sharer")){


                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else




                if(url.startsWith("https://www.facebook.com") || url.startsWith("https://m.facebook.com")){

                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else

                if (url != null && (url.startsWith("https://www.facebook.com/") || url.startsWith("https://m.facebook.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://www.pinterest.com/") || url.startsWith("https://pinterest.com"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://twitter.com/") || url.startsWith("https://www.twitter.com/") || url.startsWith("https://mobile.twitter.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else

                {
                    view.loadUrl(url);

                    return true;
                }

// then it is not handled by default action
            }





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



                if(a%3==0){

                    loadRewardedVideoAd();
                    a++;
                }

                    a++;




//                mInterstitialAd.setAdListener(new AdListener() {
//                    public void onAdLoaded() {
//                        showInterstitial();
//                    }
//                });

                super.onPageFinished(view, url);


            }


        });
        mWebView.setWebChromeClient(new ChromeClient());

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                LoadWebPull();
                //LoadWeb();
            }
        });


        mWebView.loadUrl("https://m.youtube.com/channel/UCdEV9YYWU3-W0iRxybraKRw");


        blog = (TabItem) findViewById(R.id.blog);
        channel = (TabItem) findViewById(R.id.channel);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0){


                    mWebView.loadUrl("https://m.youtube.com/channel/UCdEV9YYWU3-W0iRxybraKRw");

                }

                if(tab.getPosition()==1){

                    mWebView.loadUrl("https://linkzsharing.blogspot.com/?m=1");


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        mAdView = (AdView) findViewById(R.id.adView2);
        //mAdView.setAdUnitId("ca-app-pub-4633575141542408/5428361042");

        AdRequest adRequest = new AdRequest.Builder()
                // Check the LogCat to get your test device ID
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);





        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-4633575141542408/2858038028");

//        AdRequest adRequest2 = new AdRequest.Builder()
//                .build();
//
//        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest2);







        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(Main3Activity.this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                        rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(Main3Activity.this, "onRewardedVideoAdLeftApplication",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(Main3Activity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                Toast.makeText(Main3Activity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {

            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(Main3Activity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                showRewardedVideo();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(Main3Activity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(Main3Activity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        });

        loadRewardedVideoAd();





    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-4633575141542408/3851733109",new AdRequest.Builder().build());

        // showing the ad to user

    }

    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }
    public void LoadWebPull(){




        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(110);
        //mwebView.loadUrl("https://minitask.fun");
        //force links open in webview only
        //swipe.setRefreshing(true);



        String url = mWebView.getUrl();
        mWebView.loadUrl(url);




    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(Main3Activity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if(mWebView!=null){


                            mWebView.loadUrl("https://m.youtube.com/channel/UCdEV9YYWU3-W0iRxybraKRw");
                            isWriteStoragePermissionGranted();

                        }
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mWebView.loadUrl("https://m.youtube.com/channel/UCdEV9YYWU3-W0iRxybraKRw");

                        isReadStoragePermissionGranted();

                    }
                    //startLocationUpdates();
                } else {
                    // Permission Denied
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mWebView.loadUrl("https://m.youtube.com/channel/UCdEV9YYWU3-W0iRxybraKRw");

                    }
                    //startLocationUpdates();
                } else {
                    // Permission Denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
    public class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("lllll", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }


        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }

    }

    //flipscreen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:


                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {

                        new AlertDialog.Builder(this)
                                .setTitle("Close App")
                                .setMessage("Are you sure you want to exit from app?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();

                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

// To handle "Back" key press event for WebView to go back to previous screen.
/*@Override
public boolean onKeyDown(int keyCode, KeyEvent event)
{
    if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
        web.goBack();
        return true;
    }
    return super.onKeyDown(keyCode, event);
}*/
}