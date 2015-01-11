package tbs.jumpsnew.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.R;
import tbs.jumpsnew.utility.ListViewLib;
import tbs.jumpsnew.utility.StoreItem;
import tbs.jumpsnew.utility.Utility;

/**
 * Created by root on 4/01/15.
 */
public class Adapter extends BaseAdapter {
    private static final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    final ArrayList<StoreItem> storeItems;
    private final Context context;
    private final View.OnClickListener storeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (ListViewLib.buyItem(storeItems.get(pos))) {
                storeItems.get(pos).bought = true;
                notifyDataSetChanged();
            }
        }
    };

    public Adapter(Context context, ArrayList<StoreItem> storeItems) {
        this.storeItems = storeItems;
        this.context = context;
    }

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
        switch (item.type) {
            case SHAPE:
                view.setIcon(Utility.getShape(context, item.tag));
                break;
            case SONG:
                final ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.song1));
                view.setName(item.description);
                view.setDescription(item.name);
                view.setIcon(imageView);
                break;
            case COLOR:
                view.setIcon(Utility.getColor(context, item.tag));
                break;
        }

        view.setPrice(" " + Utility.formatNumber(item.price)); // GAP
        view.setBought(item.bought);
        view.setPosition(position);
        view.getView().findViewById(R.id.buy_equip).setTag(position);
        view.getView().findViewById(R.id.buy_equip).setOnClickListener(storeClickListener);
        if (item.type == StoreItem.Type.COLOR) {
            if (item.bought) {
                ((Button) (view.getView().findViewById(R.id.buy_equip))).setText("Added");
                ((view.getView().findViewById(R.id.buy_equip))).setOnClickListener(null);
            }

        }
        view.getView().setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, ListViewLib.dpToPixels(96)));
        return view.getView();
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
            coinContainer = view.findViewById(R.id.coin_icon);

            price.setTypeface(Game.font);
            description.setTypeface(Game.font);
            name.setTypeface(Game.font);
            buy.setTypeface(Game.font);
            name.setMaxLines(1);

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
            buy.setText(bought ? "Use" : "Buy");
            price.setText(bought ? "Sold" : price.getText());
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
