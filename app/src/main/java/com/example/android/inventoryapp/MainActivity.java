package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ProductClass> productStorage = new ArrayList<ProductClass>();
    ListView lv;
    TextView tv;
    ProductAdapter adapter;
    ProductDbHelper db = new ProductDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = (Button) findViewById(R.id.addProduct);


        lv = (ListView) findViewById(R.id.listProduct);
        adapter = new ProductAdapter(MainActivity.this, productStorage);
        lv.setAdapter(adapter);

        if (db.getProductCount() == 0) {
            tv = (TextView) findViewById(R.id.noProduct);
            tv.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.VISIBLE);

            productStorage.addAll(db.getAllProducts());
            adapter.notifyDataSetChanged();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductDetails.class);
                intent.putExtra("class", productStorage.get(position));
                startActivityForResult(intent, 0);
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddProduct.class);

                startActivityForResult(i, 0);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(lv.getVisibility() == View.GONE)
        {
            lv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
        adapter.clear();
        productStorage.clear();
        productStorage.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();

        if(productStorage != null)
        {
            if(productStorage.size() == 0)
            {
                lv.setVisibility(View.GONE);
            }
            else
            {
                lv.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            lv.setVisibility(View.GONE);
        }
    }
}

