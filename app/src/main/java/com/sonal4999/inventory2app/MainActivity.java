package com.sonal4999.inventory2app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setTitle("Inventory Store");
        final DbHelper db_object = new DbHelper(this);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_text));
        final ArrayList<String> list = db_object.getDataList();
        CustomAdaptor arrayAdapter = new CustomAdaptor(MainActivity.this, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent infoIntent = new Intent(MainActivity.this, Detail_Product.class);
                String itemSelected = ((TextView) view.findViewById(R.id.product_content)).getText().toString();

                infoIntent.putExtra("listItem", itemSelected);
                startActivity(infoIntent);
                Toast.makeText(MainActivity.this, "click on "+itemSelected, Toast.LENGTH_SHORT).show();

            }
        });

        Button add = (Button) findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(addIntent);
            }
        });

        // Refresh List
        Button refresh = (Button) findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView listView1 = (ListView) findViewById(R.id.list_view);
                listView1.setEmptyView(findViewById(R.id.empty_text));
                ArrayList<String> list = db_object.getDataList();
                Toast.makeText(MainActivity.this, "  Refresh list ", Toast.LENGTH_SHORT).show();
                CustomAdaptor arrayAdapter = new CustomAdaptor(MainActivity.this, list);
                listView1.setAdapter(arrayAdapter);


            }
        });

        // Delete ALL DATA
        Button delete = (Button) findViewById(R.id.delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                db_object.delete_all();
                                Toast.makeText(MainActivity.this, "Data Deleted Please Refresh the List", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setMessage("you want to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


            }

    }

