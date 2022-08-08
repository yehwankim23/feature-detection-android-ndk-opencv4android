package com.example.feature_detection_android_ndk_opencv4android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("feature_detection_android_ndk_opencv4android");
    }

    private CameraBridgeViewBase cameraBridgeView;
    private final BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                cameraBridgeView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public native void drawKeypoints(long mat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);

        cameraBridgeView = findViewById(R.id.java_camera_view);
        cameraBridgeView.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeView.setCvCameraViewListener(this);
        cameraBridgeView.setCameraIndex(0); // front = 1
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraBridgeView.setCameraPermissionGranted();
        } else {
            Toast.makeText(this, "CAMERA PERMISSION DENIED", Toast.LENGTH_LONG).show();
            finish();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (cameraBridgeView != null) {
            cameraBridgeView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, loaderCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cameraBridgeView != null) {
            cameraBridgeView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //
    }

    @Override
    public void onCameraViewStopped() {
        //
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        drawKeypoints(frame.getNativeObjAddr());
        return frame;
    }
}
