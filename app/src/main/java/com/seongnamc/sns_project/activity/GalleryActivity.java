package com.seongnamc.sns_project.activity;

import androidx.annotation.NonNull;

import com.seongnamc.sns_project.R;
import com.seongnamc.sns_project.Utility;
import com.seongnamc.sns_project.adapter.GalleryAdapter;

import android.Manifest;
import android.app.Activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.seongnamc.sns_project.Utility.GALLERY_IMAGE;
import static com.seongnamc.sns_project.Utility.GALLERY_VIDIO;
import static com.seongnamc.sns_project.Utility.INTENT_MEDIA;
import static com.seongnamc.sns_project.Utility.showToast;


public class GalleryActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setToolbarTitle("갤러리");
        if (ContextCompat.checkSelfPermission(
                this , Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }else{
                showToast(GalleryActivity.this , getResources().getString(R.string.please_grant_permission));
            }
        }
        else{
            recycleInit();
        }


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recycleInit();
                }  else {
                    showToast(GalleryActivity.this , getResources().getString(R.string.please_grant_permission));
                    finish();

                }
                return;
        }
    }


    private void recycleInit(){
        final int numberOfColumns = 3;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.galleryView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        // use a linear layout manager

        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }
    public ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        String PathOfImage = null;
        Intent intent = getIntent();
        String[] projection;

        final int media = intent.getIntExtra(INTENT_MEDIA, GALLERY_IMAGE);

        if(media == GALLERY_VIDIO) {
            uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
        }
        else{
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        }


        cursor = activity.getContentResolver().query(uri, projection, null, null, null);


        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

}
