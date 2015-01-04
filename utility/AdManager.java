package tbs.jumpsnew.utility;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import tbs.jumpsnew.R;

public class AdManager {
    private static InterstitialAd fullscreenAd;
    private static InterstitialAd videoAd;

    public AdManager(Context context) {
        fullscreenAd = new InterstitialAd(context);
        fullscreenAd.setAdUnitId(context.getResources().getString(R.string.fullscreen_id));
        videoAd = new InterstitialAd(context);
        videoAd.setAdUnitId(context.getResources().getString(R.string.video_id));
    }

//    public void loadBannerAd() { // Get And Load Banner Ad
//        AdRequest adRequest = new AdRequest.Builder().build();
//        bannerAd.setAdSize(AdSize.BANNER);
//        bannerAd.setVisibility(View.VISIBLE);
//        bannerAd.loadAd(adRequest);
//    }

    public void loadFullscreenAd() { // Get And Load Fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        fullscreenAd.loadAd(adRequest);
    }

    public void loadVideoAd() { // Get And Load Video Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        videoAd.loadAd(adRequest);
    }

//    public AdView getBannerAd() {
//        return bannerAd;
//    }

    public InterstitialAd getFullscreenAd() {
        return fullscreenAd;
    }

    public InterstitialAd getVideoAd() {
        return videoAd;
    }
}
