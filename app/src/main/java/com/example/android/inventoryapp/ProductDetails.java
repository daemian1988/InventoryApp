package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;

import static com.example.android.inventoryapp.R.id.imageView;

public class ProductDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textViewProductName = (TextView)findViewById(R.id.textViewProdName2);
        TextView textViewProductPrice = (TextView)findViewById(R.id.textViewProdPrice2);
        TextView textViewProductQty = (TextView)findViewById(R.id.textViewProdQty2);
        TextView textViewProductSold = (TextView)findViewById(R.id.textViewProdSold2);
        ImageView iv = (ImageView)findViewById(R.id.imageViewDetails) ;

        ProductClass pc = (ProductClass)getIntent().getSerializableExtra("class");
        textViewProductName.setText(pc.getProductName());
        textViewProductPrice.setText(Float.toString(pc.getProductPrice()));
        textViewProductQty.setText(Integer.toString(pc.getProductQty()));
        textViewProductSold.setText(Integer.toString(pc.getProductQtySold()));

        String selectedImagePath = pc.getProductImgLink();
        Log.d(ProductDetails.class.getSimpleName(), "Path exists " +pc.getProductImgLink());
        File imgFile = new File(selectedImagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv.setImageBitmap(myBitmap);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
