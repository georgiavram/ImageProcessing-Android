package com.example.vsco;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!MainActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            findViewById(R.id.takePhotoBtn).setVisibility(View.GONE);
        }
        else{
            init();
        }

    }

    @Override
    protected void onResume(){
     super.onResume();
    }

    private void init(){

        final Button browsePhoto = findViewById(R.id.browseImageBtn);
        browsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });
        final Button takePhoto = findViewById(R.id.takePhotoBtn);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra( android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picture = null;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    assert extras != null;
                    picture = (Bitmap) extras.get("data");
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();
                    Intent i = new Intent(this, MenuActivity.class);
                    i.putExtra("image", byteArray);
                    startActivity(i);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Intent i = new Intent(this, MenuActivity.class);
                    i.putExtra("path", selectedImage.toString());
                    startActivity(i);
                }
                break;
        }
    }
}

