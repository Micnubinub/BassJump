package tbs.jumpsnew.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
            int pos = (Integer) v.getTag();
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.store_item, null);
        }

        final TextView name = (TextView) convertView.findViewById(R.id.name);
        final TextView description = (TextView) convertView.findViewById(R.id.description);
        final TextView price = (TextView) convertView.findViewById(R.id.price);
        final Button buy = (Button) convertView.findViewById(R.id.buy_equip);
        final FrameLayout icon = (FrameLayout) convertView.findViewById(R.id.icon);
        final View coinContainer = convertView.findViewById(R.id.coin_icon);
        final View iconImageView = convertView.findViewById(R.id.icon_image_view);
        iconImageView.setVisibility(View.GONE);

        price.setTypeface(Game.font);
        description.setTypeface(Game.font);
        name.setTypeface(Game.font);
        buy.setTypeface(Game.font);
        name.setMaxLines(1);

        final StoreItem item = storeItems.get(position);
        name.setText(item.name);
        description.setText(item.description);

        price.setText(" " + Utility.formatNumber(item.price)); // GAP
        buy.setText(item.bought ? "Use" : "Buy");
        price.setText(item.bought ? "Sold" : price.getText());
        coinContainer.setVisibility(item.bought ? View.GONE : View.VISIBLE);
        convertView.setTag(position);
        final Button button = (Button) (convertView.findViewById(R.id.buy_equip));
        button.setTag(position);
        button.setOnClickListener(storeClickListener);

        try {
            for (int i = 1; i < icon.getChildCount(); i++) {
                try {
                    icon.removeViewAt(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (item.type) {
            case SHAPE:
                icon.addView(Utility.getShape(context, item.tag), params);
                break;
            case SONG:
                //Todo
                iconImageView.setVisibility(View.VISIBLE);
                name.setText(item.description);
                description.setText(item.name);

                if (item.bought) {
                    button.setText(item.equipped ? "Remove" : "Use");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.equipped)
                                Utility.removeEquippedSongs(context, item.tag);
                            else
                                Utility.addEquippedSongs(context, item.tag);

                            item.equipped = !item.equipped;
                            try {
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case COLOR:
                icon.addView(Utility.getColor(context, item.tag));
                if (item.bought) {
                    if (item.tag.equals(Utility.COLOR_BLUE) || item.tag.equals(Utility.COLOR_RED)) {
                        button.setText("Added");
                        button.setOnClickListener(null);
                    } else {
                        button.setText(item.equipped ? "Remove" : "Use");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (item.equipped)
                                    Utility.removeEquippedColors(context, item.tag);
                                else
                                    Utility.addEquippedColors(context, item.tag);
                                item.equipped = !item.equipped;
                                try {
                                    notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }
                break;
            case BACKGROUND:
                icon.addView(Utility.getColor(context, item.tag));
                if (item.bought) {
                    button.setText(item.equipped ? "Equipped" : "Equip");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (StoreItem storeItem : storeItems) {
                                if (storeItem.equipped)
                                    storeItem.equipped = false;
                            }
                            Utility.equipBackground(context, item.tag);
                            item.equipped = true;
                            try {
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }
                break;
        }

        return convertView;
    }

}
