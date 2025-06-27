package com.cloudwebrtc.flutterflutterexample.flutter_webrtc_example;

import com.cloudwebrtc.webrtc.video.LocalVideoTrack;
import org.webrtc.VideoFrame;
import org.webrtc.VideoFrame.I420Buffer;
import org.webrtc.JavaI420Buffer;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FilterVideoStream implements LocalVideoTrack.ExternalVideoFrameProcessing {
    private volatile byte[] processedYuvData;
    private final Object lock = new Object();

    @Override
    public VideoFrame onFrame(VideoFrame frame) {
        I420Buffer i420Buffer = frame.getBuffer().toI420();
        try {
            int width = i420Buffer.getWidth();
            int height = i420Buffer.getHeight();

            // 拆分原始 I420 数据
            ByteBuffer y = i420Buffer.getDataY();
            ByteBuffer u = i420Buffer.getDataU();
            ByteBuffer v = i420Buffer.getDataV();

            int ySize = width * height;
            int uSize = ySize / 4;
            int vSize = uSize;

            byte[] yuvData = new byte[ySize + uSize + vSize];

            y.get(yuvData, 0, ySize);
            u.get(yuvData, ySize, uSize);
            v.get(yuvData, ySize + uSize, vSize);

            // 发送给 BeautyProcessor 处理
            synchronized (lock) {
                processedYuvData = null;
                processedYuvData = BeautyProcessor.getInstance().process(yuvData, width, height);
                // 等待处理完成（最多等待 30ms）
                lock.wait(30);
            }

            int uvSize = ySize / 4; // 1/4 of Y

            if (processedYuvData != null && processedYuvData.length >= ySize + 2 * uvSize) {
                ByteBuffer processedY = ByteBuffer.wrap(processedYuvData, 0, ySize);
                ByteBuffer processedU = ByteBuffer.wrap(processedYuvData, ySize, uvSize);
                ByteBuffer processedV = ByteBuffer.wrap(processedYuvData, ySize + uvSize, uvSize);

                // 使用标准 I420 stride（除非你的处理结果使用自定义 stride）
                int strideY = width;
                int strideU = width / 2;
                int strideV = width / 2;

//                I420Buffer processedBuffer = JavaI420Buffer.wrap(
//                        width, height,
//                        processedY, strideY,
//                        processedU, strideU,
//                        processedV, strideV,
//                        null /* releaseCallback */
//                );

                return new VideoFrame((VideoFrame.Buffer) ByteBuffer.wrap(processedYuvData), frame.getRotation(), frame.getTimestampNs());
            }

            // 如果处理失败，直接返回原始帧
            return frame;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return frame;
        } finally {
            i420Buffer.release();
            return frame;
        }
    }
}
