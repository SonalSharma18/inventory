package com.sonal4999.inventory2app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Detail_Product extends AppCompatActivity {
    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    setTitle("Product Detail");
    final DbHelper db = new DbHelper(this);
    Intent get_list = getIntent();
    String productName = get_list.getExtras().getString("listItem");
    int pos = productName.indexOf("\nQuantity");
    final String selected_product = productName.substring(0, pos);

    final Cursor cur = db.getData(selected_product);

    if (cur.moveToFirst()) {

        TextView tName = (TextView) findViewById(R.id.text_name);
        tName.setText(selected_product);

        int price = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_PRICE));
        TextView tPrice = (TextView) findViewById(R.id.product_price);
        tPrice.setText("$" + price);

        int quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
        TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
        tQuantity.setText("" + quantity);
    }
    // Decrease quantity by 1
    Button decrese_btn = (Button) findViewById(R.id.decrease_p);
    decrese_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cur.moveToFirst()) {
                int quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
                if (quantity > 0) {
                    db.updateQuery(selected_product, quantity, -1);
                    quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
                    TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
                    tQuantity.setText("" + quantity);
                    Toast.makeText(Detail_Product.this, "Refresh the app", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Detail_Product.this, "It's empty! Order Now!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    // Increase quantity by 1
    Button increment_btn = (Button) findViewById(R.id.increse_q);
    increment_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cur.moveToFirst()) {
                int quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
                db.updateQuery(selected_product, quantity, 1);
                quantity = cur.getInt(cur.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
                TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
                tQuantity.setText("" + quantity);
                Toast.makeText(Detail_Product.this, "Refresh the app", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // Refresh List When you click on refresh the app will go to main activity and show the incremented
    //or decremented value of quantity it will refresh the app as whole
    Button refresh = (Button) findViewById(R.id.refresh_data);
    refresh.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent mainIntent = new Intent(Detail_Product.this, MainActivity.class);
            startActivity(mainIntent);

        }
    });
    // Order Now
    Button order = (Button) findViewById(R.id.order_of_product);
    order.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String productName = "";
            if (cur.moveToFirst()) {
                productName = cur.getString(cur.getColumnIndex(Database_Column.Inventory_table.DB_PRODUCT_NAME));
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_TEXT, "Got An Order for " + productName);
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    });

    // delete row
    Button delete = (Button) findViewById(R.id.delete_data);
    delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (db.deleteQuery(selected_product)) {
                                Intent home_intent = new Intent(Detail_Product.this, MainActivity.class);
                                startActivity(home_intent);
                                Toast.makeText(Detail_Product.this, "Deleted!", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(Detail_Product.this);
            ab.setMessage("you want to delete?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    });

}
}

