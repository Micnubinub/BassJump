package tbs.jumpsnew.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tbs.jumpsnew.R;
import tbs.jumpsnew.ui.Adapter;


public class GetCoinsFragment extends Fragment {
    private static final String TITLE = "Get Coins";
    private static ListView listView;

    public GetCoinsFragment() {
    }

    public static void setListAdapter(Adapter adapter) {
        listView.setAdapter(adapter);
    }

    public static String getTitle() {
        return TITLE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }
}
