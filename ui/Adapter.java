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
            final int pos = (Integer) v.getTag(0);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.store_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.buy = (Button) convertView.findViewById(R.id.buy_equip);
            holder.icon = (FrameLayout) convertView.findViewById(R.id.icon);
            holder.coinContainer = convertView.findViewById(R.id.coin_icon);
            holder.iconImageView = convertView.findViewById(R.id.icon_image_view);
            convertView.setTag(1, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(1);
        }
        holder.iconImageView.setVisibility(View.GONE);

        holder.price.setTypeface(Game.font);
        holder.description.setTypeface(Game.font);
        holder.name.setTypeface(Game.font);
        holder.buy.setTypeface(Game.font);
        holder.name.setMaxLines(1);

        final StoreItem item = storeItems.get(position);
        holder.name.setText(item.name);
        holder.description.setText(item.description);

        holder.price.setText(" " + Utility.formatNumber(item.price)); // GAP
        holder.buy.setText(item.bought ? "Use" : "Buy");
        holder.price.setText(item.bought ? "Sold" : holder.price.getText());
        holder.coinContainer.setVisibility(item.bought ? View.GONE : View.VISIBLE);
        convertView.setTag(0, position);
        final Button button = (Button) (convertView.findViewById(R.id.buy_equip));
        button.setTag(position);
        button.setOnClickListener(storeClickListener);

        try {
            for (int i = 1; i < holder.icon.getChildCount(); i++) {
                try {
                    holder.icon.removeViewAt(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (item.type) {
            case SHAPE:
                holder.icon.addView(Utility.getShape(context, item.tag), params);
                break;
            case SONG:
                //Todo
                holder.iconImageView.setVisibility(View.VISIBLE);
                holder.name.setText(item.description);
                holder.description.setText(item.name);

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
                holder.icon.addView(Utility.getColor(context, item.tag));
                if (item.bought) {
                    if (item.tag.equals(Utility.COLOR_RED)) {
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

        }

        return convertView;
    }

    private static class ViewHolder {
        TextView name, description, price;
        Button buy;
        FrameLayout icon;
        View coinContainer, iconImageView;
    }

}
