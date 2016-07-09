package com.example.android.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.android.inventoryapp.R.id.addProdErrMsg;

public class AddProduct extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    ProductValidator pv = new ProductValidator();
    ProductDbHelper db = new ProductDbHelper(this);
    Uri uri;
    TextView errorMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnAddImaget = (Button)findViewById(R.id.addImage);
        Button btnSaveProduct = (Button)findViewById(R.id.saveProd);
         errorMsg = (TextView)findViewById(addProdErrMsg);

        final EditText editProdName = (EditText)findViewById(R.id.editProdName);
        final EditText editProdPrice = (EditText)findViewById(R.id.editProdPrice);
        final EditText editProdQty = (EditText)findViewById(R.id.editProdQuantity);

        btnSaveProduct.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {


                if(validateField(editProdName,editProdPrice,editProdQty ))
                {
                    if(uri == null)
                    {
                        errorMsg.setText("Image required!!");
                    }
                    else {

                        errorMsg.setText("");
                        String prodName = editProdName.getText().toString();
                        String prodPrice = editProdPrice.getText().toString();
                        String prodQty = editProdQty.getText().toString();

                        ProductClass pc = new ProductClass();
                        pc.setProductName(prodName);
                        pc.setProductPrice(Float.parseFloat(prodPrice));
                        pc.setProductQty(Integer.parseInt(prodQty));
                        pc.setProductImgLink(uri.toString());
                        pc.setProductQtySold(0);
                        db.addProduct(pc);

                        String test = locatePath(uri);
                        String test2 = test;

                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }

        });

        btnAddImaget.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                checkWriteToExternalPerms();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });
    }

    public String locatePath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    private void checkWriteToExternalPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
            }
        } else {
        }
    }
    private boolean validateField(EditText name, EditText price, EditText qty)
    {
        if(!pv.checkBlank(name) && !pv.checkBlank(price) && !pv.checkBlank(qty))
        {
            if(!pv.checkIsFloat(price))
            {
                errorMsg.setText("Price is in wrong format");
                return false;
            }
            else
            {
                if(!pv.checkIsInteger(qty))
                {
                    errorMsg.setText("Quantity is in wrong format");
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        {
            errorMsg.setText("Missing field detected.");
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
