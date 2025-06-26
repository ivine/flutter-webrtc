//
//  FilterVideoStream.h
//  Runner
//
//  Created by c1 on 2025/6/26.
//

#import <Foundation/Foundation.h>
#import <WebRTC/WebRTC.h>

@protocol ExternalVideoProcessingDelegate
- (RTC_OBJC_TYPE(RTCVideoFrame) * _Nonnull)onFrame:(RTC_OBJC_TYPE(RTCVideoFrame) * _Nonnull)frame;
@end

NS_ASSUME_NONNULL_BEGIN

@interface FilterVideoStream : NSObject<ExternalVideoProcessingDelegate>

@end

NS_ASSUME_NONNULL_END
