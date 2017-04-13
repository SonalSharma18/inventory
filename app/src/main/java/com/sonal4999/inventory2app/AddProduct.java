package com.sonal4999.inventory2app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddProduct extends AppCompatActivity {
    private int request_image = 1;
    byte[] image;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == request_image && resultCode == RESULT_OK && data!=null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView testImage = (ImageView) findViewById(R.id.testImage);
            testImage.setImageBitmap(imageBitmap);

            // Convert Bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            image = stream.toByteArray();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle("Add Product");
        final DbHelper db = new DbHelper(this);
        Button add = (Button) findViewById(R.id.addproduct);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ProductName = (EditText) findViewById(R.id.product_name);
                EditText Quantity = (EditText) findViewById(R.id.Prod_quantity);
                EditText Price = (EditText) findViewById(R.id.price);
                String productName = ProductName.getText().toString();
                if (productName.matches("")) {
                    Toast.makeText(AddProduct.this, "Please enter a Product Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String input_quantity = Quantity.getText().toString();
                if (input_quantity.matches("")) {
                    Toast.makeText(AddProduct.this, "Please enter quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(input_quantity);
                String input_price = Price.getText().toString();
                if (input_price.matches("")) {
                    Toast.makeText(AddProduct.this, "Please enter price.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price = Integer.parseInt(input_price);
                if(image == null) {
                    Toast.makeText(AddProduct.this, "Please set an image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.insertQuery(productName, quantity, price, image);
                Intent mainIntent = new Intent(AddProduct.this, MainActivity.class);
                startActivity(mainIntent);
                String message = "Product Added! Successfully";
                Toast.makeText(AddProduct.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Capture Image
        Button add_image = (Button) findViewById(R.id.addImage);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, request_image);
                }
            }
        });

    }
}
