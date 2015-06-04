package com.example.steven.cameraapi;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class takePhoto extends Activity implements
        SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {
    Camera mCamera;
    SurfaceView mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_apimain);

        mPreview = (SurfaceView)findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        //Needed for support prior to Android 3.0
        mPreview.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera = Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        Log.d("CAMERA", "Destroy");
    }

    public void onCancelClick(View v) {
        finish();
        Intent i = new Intent(takePhoto.this,CameraAPIMainActivity.class);
        startActivity(i);
    }

    public void onSnapClick(View v) {
        //Snap a photo
        mCamera.takePicture(this, null, null, this);
    }
    //Camera Callback Methods
    @Override
    public void onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        //Store the picture off somewhere
        //Here, we chose to save to external storage
        try {
           /*File directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            FileOutputStream out =
                    new FileOutputStream(new File(directory, "picture.jpg"));
            out.write(data);
            out.flush();
            out.close();*/

            //passing byte array
            Intent i = new Intent(takePhoto.this,ViewPicture.class);
            i.putExtra("imageArray",data);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Surface Callback Methods
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }
}
