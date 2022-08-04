package com.example.feature_detection_android_ndk_opencv4android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.feature_detection_android_ndk_opencv4android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'feature_detection_android_ndk_opencv4android' library on application startup.
    static {
        System.loadLibrary("feature_detection_android_ndk_opencv4android");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'feature_detection_android_ndk_opencv4android' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}