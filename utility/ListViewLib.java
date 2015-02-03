package tbs.jumpsnew.utility;

import android.content.Context;
import android.util.TypedValue;

import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.R;
import tbs.jumpsnew.ui.CustomDialog;

/**
 * Created by root on 29/12/14.
 */
public class ListViewLib {

    private static StoreListener storeListener;
    // private static int numCoins;
    // , descriptionTextSize, priceTextSize, itemNameTextSize,buyButtonTextSize;

    // private static Drawable listItemBackground;
//    private static View view;
    private static Context context;
    private CustomDialog dialog;

    //    private int nameTextColor, priceTextColor, itemTextColor, buyTextColor,
//            descriptionTextColor, coinTextColor, titleTextColor;
//    private int listViewDividerHeight, listViewPadding, listViewMargin;
//    private Drawable storeItemBackground;
//    private Typeface storeItemFont;
//    private Drawable buyButtonBackground;


    public ListViewLib(Context context) {
        ListViewLib.context = context;

    }


    public static boolean buyItem(StoreItem item) {
        return storeListener.onBuyItem(item);
    }


//    public void setItemNameTextSize(int itemNameTextSize) {
//        this.itemNameTextSize = itemNameTextSize;
//    }
//
//    public void setDescriptionTextSize(int descriptionTextSize) {
//        this.descriptionTextSize = descriptionTextSize;
//    }
//
//    public void setBuyButtonTextSize(int buyButtonTextSize) {
//        this.buyButtonTextSize = buyButtonTextSize;
//    }

    public static int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public void show() {
        if (dialog == null) {
            dialog = new CustomDialog();
            dialog.setStyle(R.style.CustomDialog, R.style.DialogTheme);
        }
        dialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "tag");

        if (storeListener != null)
            storeListener.onStoreOpened();

    }


    public void setStoreListener(StoreListener storeListener) {
        ListViewLib.storeListener = storeListener;
    }

//    public void setListItemBackground(Drawable listItemBackground) {
//        this.listItemBackground = listItemBackground;
//    }
//
//    public void setStoreItemBackground(Drawable storeItemBackground) {
//        this.storeItemBackground = storeItemBackground;
//    }
//
//    private void setStoreItemFont(Typeface typeface) {
//        storeItemFont = typeface;
//    }
//
//    public void setNameTextColor(int nameTextColor) {
//        this.nameTextColor = nameTextColor;
//    }
//
//    public void setPriceTextColor(int priceTextColor) {
//        this.priceTextColor = priceTextColor;
//    }
//
//    public void setDescriptionTextColor(int descriptionTextColor) {
//        this.descriptionTextColor = descriptionTextColor;
//    }
//
//    public void setCoinTextColor(int coinTextColor) {
//        this.coinTextColor = coinTextColor;
//    }
//
//    public void setTitleTextColor(int titleTextColor) {
//        this.titleTextColor = titleTextColor;
//    }
//
//    public void setTitle(String title) {
//        this.title.setText(title);
//    }
//
//    public void setPriceTextSize(int priceTextSize) {
//        this.priceTextSize = priceTextSize;
//    }
//
//    public void setBuyButtonBackground(Drawable buyButtonBackground) {
//        this.buyButtonBackground = buyButtonBackground;
//    }

    public interface StoreListener {
        boolean onBuyItem(StoreItem item);

        void onEquipItem(StoreItem item);

        void onFailedToBuyItem(StoreItem item);

        void onStoreOpened();

        void onStoreClosed();
    }


}
