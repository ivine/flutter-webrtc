#import <AVFoundation/AVFoundation.h>
#import <Foundation/Foundation.h>
#import "FlutterWebRTCPlugin.h"

@interface FlutterWebRTCPlugin (CameraUtils)

- (void)mediaStreamTrackHasTorch:(nonnull RTCMediaStreamTrack*)track result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetTorch:(nonnull RTCMediaStreamTrack*)track
                           torch:(BOOL)torch
                          result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetZoom:(nonnull RTCMediaStreamTrack*)track
                           zoomLevel:(double)zoomLevel
                          result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetFocusMode:(nonnull RTCMediaStreamTrack*)track
                           focusMode:(nonnull NSString*)focusMode
                          result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetFocusPoint:(nonnull RTCMediaStreamTrack*)track
                           focusPoint:(nonnull NSDictionary*)focusPoint
                          result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetExposureMode:(nonnull RTCMediaStreamTrack*)track
                           exposureMode:(nonnull NSString*)exposureMode
                          result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSetExposurePoint:(nonnull RTCMediaStreamTrack*)track
                           exposurePoint:(nonnull NSDictionary*)exposurePoint
                            result:(nonnull FlutterResult)result;

- (void)mediaStreamTrackSwitchCamera:(nonnull RTCMediaStreamTrack*)track result:(nonnull FlutterResult)result;

- (NSInteger)selectFpsForFormat:(nonnull AVCaptureDeviceFormat*)format targetFps:(NSInteger)targetFps;

- (nullable AVCaptureDeviceFormat*)selectFormatForDevice:(nonnull AVCaptureDevice*)device
                                    targetWidth:(NSInteger)targetWidth
                                   targetHeight:(NSInteger)targetHeight;

- (nullable AVCaptureDevice*)findDeviceForPosition:(AVCaptureDevicePosition)position;

- (nullable AVCaptureDeviceFormat*)selectFormatForDevice:(nonnull AVCaptureDevice*)device
                                        formatIdentifier:(nullable NSString *)formatIdentifier;

@end



NS_ASSUME_NONNULL_BEGIN

@interface AVCaptureDeviceFormat (UniqueID)

/// 获取格式的稳定唯一标识符
- (NSString *)xn_stableID;

/// 打印格式的详细信息
- (void)xn_printFormatInfo;

/// 获取格式的详细描述字符串
- (NSString *)xn_detailedDescription;

- (NSString *)xn_pixelFormat;

@end

NS_ASSUME_NONNULL_END
