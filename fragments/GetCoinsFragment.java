package tbs.jumpsnew.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.R;
import tbs.jumpsnew.managers.StoreManager;
import tbs.jumpsnew.ui.CustomDialog;
import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.Utility;

public class GetCoinsFragment extends Fragment {
    private static final AdManager adManager = StoreManager.adManager;
    private static final AdListener fullScreenListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            fullClicked = false;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Utility.saveCoins(Game.context, Utility.getCoins(Game.context) + 3);
            CustomDialog.setNumCoins(Utility.getCoins(Game.context));
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            if (fullClicked)
                adManager.getFullscreenAd().show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            fullClicked = false;
            toast("Failed to load Ad");
        }
    };
    private static boolean vidClicked, fullClicked;
    private static final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.video_ad:
                    vidClicked = true;
                    if (adManager.getVideoAd().isLoaded())
                        adManager.getVideoAd().show();
                    else
                        adManager.loadVideoAd();
                    break;
                case R.id.fullscreen_ad:
                    fullClicked = true;
                    if (adManager.getFullscreenAd().isLoaded())
                        adManager.getFullscreenAd().show();
                    else
                        adManager.loadFullscreenAd();
                    break;
                case R.id.iap:
                    toast("Coming soon");
                    break;
            }
        }
    };
    private static final AdListener videoListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            vidClicked = false;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Utility.saveCoins(Game.context, Utility.getCoins(Game.context) + 5);
            CustomDialog.setNumCoins(Utility.getCoins(Game.context));
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            if (vidClicked)
                adManager.getVideoAd().show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            vidClicked = false;
            toast("Failed to load Ad");
        }
    };

    public GetCoinsFragment() {

    }

    private static void toast(String msg) {
        Toast.makeText(Game.context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adManager.getVideoAd().setAdListener(videoListener);
        adManager.getFullscreenAd().setAdListener(fullScreenListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.get_coins, container, false);
        view.findViewById(R.id.iap).setOnClickListener(listener);
        view.findViewById(R.id.video_ad).setOnClickListener(listener);
        view.findViewById(R.id.fullscreen_ad).setOnClickListener(listener);
        return view;
    }
}
