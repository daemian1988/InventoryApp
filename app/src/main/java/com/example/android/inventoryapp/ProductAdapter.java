package com.example.android.inventoryapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Daemian on 1/7/2016.
 */

public class ProductAdapter extends ArrayAdapter<ProductClass> {

    public ProductAdapter(Activity context, ArrayList<ProductClass> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_list_item, parent, false);
        }

        ProductClass currentProduct = getItem(position);

        TextView tvProductName = (TextView) listItemView.findViewById(R.id.dbProductName);
        tvProductName.setText(currentProduct.getProductName());

        TextView tvProductQty = (TextView) listItemView.findViewById(R.id.dbProductQantity);
        tvProductQty.setText(Integer.toString(currentProduct.getProductQty()));

        TextView tvProductPrice = (TextView) listItemView.findViewById(R.id.dbProductPrice);
        tvProductPrice.setText(Float.toString(currentProduct.getProductPrice()));

        TextView tvProductSold = (TextView) listItemView.findViewById(R.id.dbProductSold);
        tvProductSold.setText(Integer.toString(currentProduct.getProductQtySold()));

        return listItemView;
    }


}
