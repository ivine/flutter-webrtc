package com.cloudwebrtc.flutterflutterexample.flutter_webrtc_example;

import android.os.Bundle;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 BeautyProcessor 单例，传入应用上下文
        BeautyProcessor.init(getApplicationContext());
        BeautyProcessor processor = BeautyProcessor.getInstance();

        // 这里可以根据需求调用设置，比如初始化默认美颜参数
        processor.setBeautyLevel(1.5f);
        processor.mFaceReshapeFilter.SetProperty("thin_face", 1050 / 160.0f);
        processor.mFaceReshapeFilter.SetProperty("big_eye", 1300 / 40.0f);
    }
}
