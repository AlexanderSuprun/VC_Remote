package com.vcremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Adapter for network list
 */
public class Adapter extends ArrayAdapter<String> {

    private Context context;
    private String[] rData;

    Adapter (Context c, String[] data) {
        super(c, R.layout.list_item, R.id.list_item_title, data);
        this.context = c;
        this.rData = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        TextView title = listItem.findViewById(R.id.list_item_title);
        title.setText(rData[position]);

        return listItem;
    }
}
