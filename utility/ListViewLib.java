package tbs.jumpsnew.utility;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.R;

/**
 * Created by root on 29/12/14.
 */
public class ListViewLib {
    private static final View.OnClickListener storeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buyItem((int) v.getTag());
        }
    };
    private static final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    private static ArrayList<StoreItem> storeItems;
    private static StoreListener storeListener;
    // private static int numCoins;
    // , descriptionTextSize, priceTextSize, itemNameTextSize,buyButtonTextSize;
    private static TextView title;
    private static TextView coinText;
    private static Adapter adapter;
    // private static Drawable listItemBackground;
    private static View view;
    private static Context context;
    private final ListView listView;
    //Todo make advertise our apps half done
    private Dialog dialog;
    //    private int nameTextColor, priceTextColor, itemTextColor, buyTextColor,
//            descriptionTextColor, coinTextColor, titleTextColor;
//    private int listViewDividerHeight, listViewPadding, listViewMargin;
//    private Drawable storeItemBackground;
//    private Typeface storeItemFont;
//    private Drawable buyButtonBackground;


    public ListViewLib(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.store, null);
        title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(Game.font);

        coinText = (TextView) view.findViewById(R.id.coins);
        coinText.setTypeface(Game.font);

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        listView = (ListView) view.findViewById(R.id.list);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

    }

    private static void buyItem(int position) {
        final StoreItem item = storeItems.get(position);
        storeListener.onBuyItem(item);
    }

    public static void setNumCoins(int numCoins) {
        try {
            coinText.setText(String.valueOf(numCoins));
            //    ListViewLib.numCoins = numCoins;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void show() {
        dialog = new Dialog(context, R.style.CustomDialog);
        try {
            dialog.setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareListView();
        dialog.show();
        if (storeListener != null)
            storeListener.onStoreOpened();

    }

    public void close() {
        try {
            dialog.dismiss();
            if (storeListener != null)
                storeListener.onStoreClosed();
        } catch (Exception e) {

        }
    }

    public ListView getListView() {
        return listView;
    }

    private void prepareListView() {
        adapter = new Adapter();
        listView.setAdapter(adapter);
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public void setStoreItems(ArrayList<StoreItem> storeItems) {
        this.storeItems = storeItems;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void setStoreListener(StoreListener storeListener) {
        this.storeListener = storeListener;
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
        void onBuyItem(StoreItem item);

        void onEquipItem(StoreItem item);

        void onFailedToBuyItem(StoreItem item);

        void onStoreOpened();

        void onStoreClosed();
    }

    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return storeItems.size();
        }

        @Override
        public Object getItem(int position) {
            return storeItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListItem view = new ListItem(context);
            final StoreItem item = storeItems.get(position);
            view.setName(item.name);
            view.setDescription(item.description);
            view.setBought(item.bought);
            switch (item.type) {
                case SHAPE:
                    view.setIcon(Utility.getShape(context, item.tag));
                    break;
                case SONG:
                    final ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.song1));
                    view.setIcon(imageView);
                    break;
                case COLOR:
                    view.setIcon(Utility.getColor(context, item.tag));
                    break;
            }

            view.setPrice(String.valueOf(item.price));
            view.setPosition(position);
            view.getView().findViewById(R.id.buy_equip).setTag(position);
            view.getView().findViewById(R.id.buy_equip).setOnClickListener(storeClickListener);
            view.getView().setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, dpToPixels(96)));
            return view.getView();
        }
    }

    private class ListItem {
        private final View view, coinContainer;
        private final TextView name;
        private final TextView price;
        private final TextView description;
        private final Button buy;
        private final FrameLayout icon;
        private int position;

        public ListItem(Context context) {
            view = View.inflate(context, R.layout.store_item, null);

            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
            buy = (Button) view.findViewById(R.id.buy_equip);
            icon = (FrameLayout) view.findViewById(R.id.icon);
            coinContainer = view.findViewById(R.id.rl);

            price.setTypeface(Game.font);
            description.setTypeface(Game.font);
            name.setTypeface(Game.font);
            buy.setTypeface(Game.font);

//            if (buyButtonBackground != null) {
//                try {
//                    buy.setBackground(buyButtonBackground);
//                } catch (Exception e) {
//                    buy.setBackgroundDrawable(buyButtonBackground);
//                }
//            }
        }

        public View getView() {
            return view;
        }

        public void setBought(boolean bought) {
            buy.setText(bought ? "Equip" : "Buy");
            coinContainer.setVisibility(bought ? View.GONE : View.VISIBLE);
        }

        public void setDescription(String description) {
            this.description.setText(description);
        }

        public void setIcon(View icon) {
            this.icon.addView(icon, params);
        }

        public void setPrice(String price) {
            this.price.setText(price);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
