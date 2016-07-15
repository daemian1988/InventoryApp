package com.example.android.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
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

        Button btnAddImaget = (Button) findViewById(R.id.addImage);
        Button btnSaveProduct = (Button) findViewById(R.id.saveProd);
        errorMsg = (TextView) findViewById(addProdErrMsg);

        final EditText editProdName = (EditText) findViewById(R.id.editProdName);
        final EditText editProdPrice = (EditText) findViewById(R.id.editProdPrice);
        final EditText editProdQty = (EditText) findViewById(R.id.editProdQuantity);
        final EditText editEmail = (EditText) findViewById(R.id.editSupEmail);

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if (validateField(editProdName, editProdPrice, editProdQty, editEmail)) {
                    if (uri == null) {
                        errorMsg.setText("Image required!!");
                    } else {

                        errorMsg.setText("");
                        String prodName = editProdName.getText().toString();
                        String prodPrice = editProdPrice.getText().toString();
                        String prodQty = editProdQty.getText().toString();
                        String email = editEmail.getText().toString();

                        ProductClass pc = new ProductClass();
                        pc.setProductName(prodName);
                        pc.setProductPrice(Float.parseFloat(prodPrice));
                        pc.setProductQty(Integer.parseInt(prodQty));
                        pc.setProductImgLink(uri.toString());
                        pc.setProductQtySold(0);
                        pc.setSupplierEmail(email);
                        db.addProduct(pc);

                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }

        });

        btnAddImaget.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                checkWriteToExternalPerms();
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        });
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

    private boolean validateField(EditText name, EditText price, EditText qty, EditText email) {
        if (!pv.checkBlank(name) && !pv.checkBlank(price) && !pv.checkBlank(qty) && !pv.checkBlank(email)) {
            if (!pv.checkIsFloat(price)) {
                errorMsg.setText("Price is in wrong format");
                return false;
            } else {
                if (!pv.checkIsInteger(qty)) {
                    errorMsg.setText("Quantity is in wrong format");
                    return false;
                } else {
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
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null

            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

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
