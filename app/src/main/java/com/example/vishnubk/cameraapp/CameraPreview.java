package com.example.vishnubk.cameraapp;

import android.content.Context;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by vishnubk on 22/1/16.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG ="" ;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera=camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0){
                mCamera.cancelAutoFocus();
            }
        }
    };




    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.stopPreview();
            Camera.Parameters p = mCamera.getParameters();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

            mCamera.setParameters(p);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.autoFocus(null);
            mCamera.startFaceDetection();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                p.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
                //parameters.set("rotation",90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                p.set("orientation", "landscape");
               // parameters.set("rotation", 90);
                mCamera.setDisplayOrientation(180);
            }

            mCamera.startPreview();
        } catch (IOException e) {
           // Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }





    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }



        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
        // start preview with new settings
        try {  mCamera.stopPreview();
            Camera.Parameters p = mCamera.getParameters();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(p);

            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.autoFocus(null);
            mCamera.startFaceDetection();

        } catch (Exception e){
            //Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }



    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("down", "focusing now");

            mCamera.autoFocus(null);
        }

        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
