package com.yss1.king3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.yss1.lib7.JmeFragmentBase;

public class JmeFragment extends JmeFragmentBase {
    public JmeFragment() {
        // Set main project class (fully qualified path)
        super();
        appClass = "com.yss1.king3.Main";
        splashPicID = R.drawable.splash;
        //GP_ACTIVATE=true;
        //GP_MULTIPLAYER=true;
        MIN_PLAYERS=3;
        AD_BANNER=true;
    }
//          public MainActivity(){
//        super();
//        appClass = "com.yss1.king.Main";
//        splashPicID = R.drawable.splash;
//        GP_ACTIVATE=true;
//    }

//    @Override
// public void initDialogID() {
////     id_dialog_txt_res=R.id.etText;
////     id_dialog_res=R.layout.dialog1;
////     id_view_txt_res=R.id.viText;
////     id_view_res=R.layout.alert1;
// }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        mSettings = getActivity().getApplication().getSharedPreferences("King3.conf", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        // CookieManager.getInstance().setAcceptCookie(true);
    }


    @Override
    public void onStart() {
        super.onStart(); //To change body of generated methods, choose Tools | Templates.
        if (!firstStart) return;
        firstStart=false;
//        if (mTracker==null)
//        {
//        analytics = GoogleAnalytics.getInstance(getActivity());
//        mTracker = analytics.newTracker(getString(R.string.ga_id));
//        }
//      if (interstitialAd==null)
//          {
//           interstitialAd = new InterstitialAd(getActivity());
//           //interstitialAd.setAdUnitId("ca-app-pub-7845806301928575/7791171848");
//           interstitialAd.setAdUnitId(getString(R.string.ad_id));
//          }
//      if (interstitialAd!=null)
//       {
//        interstitialAd.setAdListener(new AdListener() {
//             @Override
//             public void onAdLoaded() {
//                 if (jmeapp!=null) jmeapp.adLoaded();
//
//             }
//             @Override
//             public void onAdClosed() {
//                 if (jmeapp!=null) jmeapp.adClosed();
//             }
//         });
//       }
        try
        {
//      if (mGoogleApiClient==null && GP_ACTIVATE)
//      {
//             mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//            .addConnectionCallbacks(this)
//            .addOnConnectionFailedListener(this)
//            .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
//            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
//            .build();
//      }
        }
        catch(Exception Ex)
        {
            writeLog(Ex.getMessage());
        }

        try
        {
            if (!trakerSent)
            {
//        mTracker.setScreenName("KING2.MAIN");
//        mTracker.send(new HitBuilders.AppViewBuilder().build());

                trakerSent=true;
            }
        }
        catch(Exception Ex)
        {
            Log.d("Yss", "TrackerError:"+Ex.getMessage());
        }


    }

    @Override
    public void setIds(){
        super.setIds();

        //set id for mAdView and m InterstitialAD
        mInterstitialAdID=getString(R.string.interstitial_ad_unit_id);
        mAdViewID=getString(R.string.ad_view_unit_id);
        mClientID="478273268981-t9gk34mr8uml2lb61uggg43pl9f98eks.apps.googleusercontent.com";
        mURLdb="https://king3-f9817-default-rtdb.europe-west1.firebasedatabase.app/";

    }

    @Override
    public int getID(int idFor) {
        switch (idFor) {
            case 1:
                return R.layout.alert1;
            case 2:
                return R.layout.dialog1;
            case 101:
                return R.id.viText;
            case 202:
                return R.id.etText;
        }
        return 0;
    }

}
