package com.cloudwebrtc.flutterflutterexample.flutter_webrtc_example;

import android.content.Context;
import android.util.Log;

import com.pixpark.gpupixel.FaceDetector;
import com.pixpark.gpupixel.GPUPixel;
import com.pixpark.gpupixel.GPUPixelFilter;
import com.pixpark.gpupixel.GPUPixelSinkRawData;
import com.pixpark.gpupixel.GPUPixelSourceRawData;

public class BeautyProcessor {

    private static final String TAG = "BeautyProcessor";

    private GPUPixelSourceRawData mSourceRawData;
    private GPUPixelFilter mBeautyFilter;
    public GPUPixelFilter mFaceReshapeFilter;
    private GPUPixelFilter mLipstickFilter;
    private GPUPixelSinkRawData mSinkRawData;
    private FaceDetector mFaceDetector;

    private boolean isInitialized = false;

    // volatile 确保多线程环境可见性
    private static volatile BeautyProcessor instance;

    // 私有构造函数，防止外部直接实例化
    private BeautyProcessor(Context context) {
        GPUPixel.Init(context);
        Log.i(TAG, "GPUPixel.Init" );
        initChain();
    }

    // 分离初始化，必须调用一次传入 Context
    public static void init(Context context) {
        if (instance == null) {
            synchronized (BeautyProcessor.class) {
                if (instance == null) {
                    instance = new BeautyProcessor(context.getApplicationContext());
                }
            }
        }
    }

    // 无参获取实例，必须先调用 init()
    public static BeautyProcessor getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BeautyProcessor is not initialized. Call init(context) first.");
        }
        return instance;
    }

    private void initChain() {
        mSourceRawData = GPUPixelSourceRawData.Create();
        mBeautyFilter = GPUPixelFilter.Create(GPUPixelFilter.BEAUTY_FACE_FILTER);
        mFaceReshapeFilter = GPUPixelFilter.Create(GPUPixelFilter.FACE_RESHAPE_FILTER);
        mLipstickFilter = GPUPixelFilter.Create(GPUPixelFilter.LIPSTICK_FILTER);
        mSinkRawData = GPUPixelSinkRawData.Create();
        mFaceDetector = FaceDetector.Create();

        if (mBeautyFilter == null || mFaceReshapeFilter == null
                || mLipstickFilter == null || mSinkRawData == null
                || mFaceDetector == null) {
            Log.e(TAG, "One or more GPUPixel components failed to initialize");
            return;
        }

        // 组装滤镜链
        mSourceRawData.AddSink(mBeautyFilter);
        mBeautyFilter.AddSink(mFaceReshapeFilter);
        mFaceReshapeFilter.AddSink(mLipstickFilter);
        mLipstickFilter.AddSink(mSinkRawData);

        isInitialized = true;
    }

    public byte[] process(byte[] yuvData, int width, int height) {
        if (!isInitialized || yuvData == null || width <= 0 || height <= 0) {
            Log.w(TAG, "Invalid input to process()");
            return null;
        }

        long start = System.currentTimeMillis();

        mSourceRawData.ProcessData(
                yuvData,
                width,
                height,
                width * 4,
                GPUPixelSourceRawData.FRAME_TYPE_YUVI420
        );

        // 获取处理后 RGBA 图像
        byte[] rgba = mSinkRawData.GetRgbaBuffer();
        int outWidth = mSinkRawData.GetWidth();
        int outHeight = mSinkRawData.GetHeight();

        if (rgba == null || mFaceDetector == null || outWidth <= 0 || outHeight <= 0) {
            Log.w(TAG, "Failed to get RGBA buffer or face detector unavailable");
            return null;
        }

        // 人脸检测
//        float[] landmarks = mFaceDetector.detect(
//                rgba,
//                outWidth,
//                outHeight,
//                outWidth * 4,
//                FaceDetector.GPUPIXEL_MODE_FMT_VIDEO,
//                FaceDetector.GPUPIXEL_FRAME_TYPE_YUVI420
//        );
//
//        // 设置人脸特征点
//        if (landmarks != null && landmarks.length > 0) {
//            mFaceReshapeFilter.SetProperty("face_landmark", landmarks);
//            mLipstickFilter.SetProperty("face_landmark", landmarks);
//        }

        Log.d(TAG, "Frame processed in " + (System.currentTimeMillis() - start) + " ms");

        // 返回 I420 格式输出
        return mSinkRawData.GetI420Buffer();
    }


    public void setBeautyLevel(float level) {
        if (mBeautyFilter != null) {
            mBeautyFilter.SetProperty("skin_smoothing", clamp(1, 0f, 1f));
        }
    }

    // 其他参数的设置方法（美白、瘦脸、大眼、口红等）可按需补充

    private float clamp(float v, float lo, float hi) {
        return v < lo ? lo : (v > hi ? hi : v);
    }

    public void release() {
        if (mSourceRawData != null) {
            mSourceRawData.Destroy();
            mSourceRawData = null;
        }
        if (mFaceDetector != null) {
            mFaceDetector.destroy();
            mFaceDetector = null;
        }
        if (mBeautyFilter != null) {
            mBeautyFilter.Destroy();
            mBeautyFilter = null;
        }
        if (mFaceReshapeFilter != null) {
            mFaceReshapeFilter.Destroy();
            mFaceReshapeFilter = null;
        }
        if (mLipstickFilter != null) {
            mLipstickFilter.Destroy();
            mLipstickFilter = null;
        }
        if (mSinkRawData != null) {
            mSinkRawData.Destroy();
            mSinkRawData = null;
        }

        isInitialized = false;
        instance = null;

        Log.i(TAG, "BeautyProcessor released");
    }
}
