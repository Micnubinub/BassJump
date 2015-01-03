package tbs.jumpsnew.managers;

import android.content.Context;
import android.widget.Toast;

import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.ListViewLib;
import tbs.jumpsnew.utility.StoreItem;
import tbs.jumpsnew.utility.Utility;

public class StoreManager {

    public static AdManager adManager;
    private static Context context;
    private static ListViewLib.StoreListener storeListener = new ListViewLib.StoreListener() {
        @Override
        public void onBuyItem(StoreItem item) {
            final int coins = Utility.getCoins(context);
            if (coins < item.price)
                this.onFailedToBuyItem(item);
            else
                Utility.saveCoins(context, coins - item.price);
        }

        @Override
        public void onEquipItem(StoreItem item) {
            switch (item.type) {
                case SHAPE:
                    Utility.equipShape(context, item.tag);
                    break;
                case SONG:
                    Utility.equipSong(context, item.tag);
                    break;
            }
        }

        @Override
        public void onFailedToBuyItem(StoreItem item) {
            toast("You don't have enough money");
        }

        @Override
        public void onStoreOpened() {
            //toast("store opened");
            adManager = new AdManager(context);
            adManager.loadFullscreenAd();
            adManager.loadVideoAd();
        }

        @Override
        public void onStoreClosed() {
            toast("store closed");
        }
    };
    final ListViewLib listViewLib;

    public StoreManager(Context context) {
        listViewLib = new ListViewLib(context);
        listViewLib.setStoreItems(Utility.getStoreItems(context));
        listViewLib.setStoreListener(storeListener);
        this.context = context;
    }

    private static void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showStore() {
        listViewLib.show();
    }


}
