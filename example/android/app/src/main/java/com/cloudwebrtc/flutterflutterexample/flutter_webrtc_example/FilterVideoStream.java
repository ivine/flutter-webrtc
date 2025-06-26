package com.cloudwebrtc.flutterflutterexample.flutter_webrtc_example;

import com.cloudwebrtc.webrtc.video.LocalVideoTrack;

public class FilterVideoStream implements LocalVideoTrack.ExternalVideoFrameProcessing {
    @Override
    public org.webrtc.VideoFrame onFrame(org.webrtc.VideoFrame frame) {
        System.out.println("FilterVideoStream.processVideoFrame called");
        return frame;
    }
}