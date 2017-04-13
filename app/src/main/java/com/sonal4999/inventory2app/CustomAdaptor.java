package com.sonal4999.inventory2app;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdaptor extends ArrayAdapter<String> {
    public CustomAdaptor(Context context, ArrayList<String> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final String current_item = getItem(position);

        TextView set_item = (TextView) listItemView.findViewById(R.id.product_content);
        set_item.setText(current_item);
        Button btn = (Button) listItemView.findViewById(R.id.track_item);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DbHelper db = new DbHelper(getContext());
                int pos = current_item.indexOf("\nQuantity");
                final String selected_item = current_item.substring(0, pos);
                final Cursor cur = db.getData(selected_item);
                if (cur.moveToFirst()) {
                    int quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
                    if (quantity > 0) {
                        db.updateQuery(selected_item, quantity, -1);
                        Toast.makeText(getContext(), "Refresh! now", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "It's empty! Order Now!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return listItemView;
    }
}
