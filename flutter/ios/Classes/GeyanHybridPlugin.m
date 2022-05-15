#import "GeyanHybridPlugin.h"
#if __has_include(<geyan_plugin/geyan_plugin-Swift.h>)
#import <geyan_plugin/geyan_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "geyan_plugin-Swift.h"
#endif

@implementation GeyanHybridPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftGeyanHybridPlugin registerWithRegistrar:registrar];
}
@end
