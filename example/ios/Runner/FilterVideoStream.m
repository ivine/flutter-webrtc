//
//  FilterVideoStream.m
//  Runner
//
//  Created by c1 on 2025/6/26.
//

#import "FilterVideoStream.h"

@implementation FilterVideoStream
- (RTC_OBJC_TYPE(RTCVideoFrame) * _Nonnull)onFrame:(RTC_OBJC_TYPE(RTCVideoFrame) * _Nonnull)frame {
    NSLog(@"asdilasdj");
    return frame;
}
@end
