package com.example.android.inventoryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.width;

public class MainActivity extends AppCompatActivity {

    ArrayList<ProductClass> productStorage;
    ListView lv;
    ProductDbHelper db = new ProductDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = (Button)findViewById(R.id.addProduct);
        lv = (ListView)findViewById(R.id.listProduct);

        if(db.getProductCount() == 0)
        {
            TextView tv = (TextView)findViewById(R.id.noProduct);
            tv.setVisibility(View.VISIBLE);
        }
        else
        {
            lv.setVisibility(View.VISIBLE);

            productStorage = db.getAllContacts();
            ProductAdapter adapter = new ProductAdapter(MainActivity.this, productStorage);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Intent intent = new Intent(MainActivity.this, ProductDetails.class);
                    intent.putExtra("class", productStorage.get(position));
                    startActivityForResult(intent,0);
                }
            });

        }


        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddProduct.class);
                startActivityForResult(i, 0);

            }
        });

    }

    public ListView getListView()
    {

        ListView lv = (ListView)findViewById(R.id.listProduct);
        return lv;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK) {
            productStorage = db.getAllContacts();
            ProductAdapter adapter = new ProductAdapter(MainActivity.this, productStorage);
            lv.setAdapter(adapter);
        }
    }
}

