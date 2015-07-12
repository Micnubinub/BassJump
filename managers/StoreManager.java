package tbs.jumpsnew.managers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.fragments.GetCoinsFragment;
import tbs.jumpsnew.ui.CustomDialog;
import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.ListViewLib;
import tbs.jumpsnew.utility.StoreItem;
import tbs.jumpsnew.utility.Utility;

public class StoreManager {
    // public static AdManager adManager;
    private static Context context;
    private static final ListViewLib.StoreListener storeListener = new ListViewLib.StoreListener() {
        @Override
        public boolean onBuyItem(StoreItem item) {
            final int coins = Utility.getCoins(context);
            if (item.bought) {
                this.onEquipItem(item);
                return false;
            } else if (coins < item.price) {
                this.onFailedToBuyItem(item);
                return false;
            } else {
                Utility.saveCoins(context, coins - item.price);
                switch (item.type) {
                    case COLOR:
                        Utility.addBoughtColors(context, item.tag);
                        break;
                    case SONG:
                        Utility.addBoughtSongs(context, item.tag);
                        break;
                    case SHAPE:
                        Utility.addBoughtShapes(context, item.tag);
                        break;
                }
                item.bought = true;
                CustomDialog.setNumCoins(coins - item.price);
            }
            return true;
        }

        @Override
        public void onEquipItem(StoreItem item) {
            switch (item.type) {
                case SHAPE:
                    Utility.equipShape(context, item.tag);
                    break;
                case SONG:
                    Log.e("equip", item.tag);
                    if (item.equipped)
                        Utility.removeEquippedSongs(context, item.tag);
                    else
                        Utility.addEquippedSongs(context, item.tag);

                    try {
                        if (Game.mediaPlayer == null || !Game.mediaPlayer.isPlaying())
                            Game.playSong();
                    } catch (Exception r) {
                        r.printStackTrace();
                    }

                    if (!Game.isMusicEnabled) {
                        MainActivity.preferences.put("musicOn", "on");
                        Game.isMusicEnabled = true;
                    }
                    break;
            }
        }

        @Override
        public void onFailedToBuyItem(StoreItem item) {
            toast("You don't have enough money");
        }

        @Override
        public void onStoreOpened() {
            try {
                final AdManager adManager = GetCoinsFragment.adManager;
                if (adManager == null) {
                    GetCoinsFragment.adManager = new AdManager(context);
                    GetCoinsFragment.adManager.loadFullscreenAd();
                    GetCoinsFragment.adManager.loadVideoAd();
                } else {
                    if (!adManager.getFullscreenAd().isLoaded())
                        adManager.loadFullscreenAd();

                    if (adManager.getVideoAd().isLoaded())
                        adManager.loadVideoAd();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStoreClosed() {
            Utility.refreshSongs();
        }
    };

    private static ListViewLib listViewLib;

    public StoreManager(Context context) {
        listViewLib = new ListViewLib(context);
        listViewLib.setStoreListener(storeListener);
        StoreManager.context = context;
    }

    private static void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showStore() {
        listViewLib.show();
    }

}
