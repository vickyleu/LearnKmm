import Flutter
import shared
// public class SwiftGeyanHybridPlugin : CommonPluginImpl<PlatformImpl>, FlutterPlugin{
//
//     public static func register(with registrar: FlutterPluginRegistrar){
//         let plugin = SwiftGeyanHybridPlugin.init(channelName : "abcdefd")
//         plugin.registerWithRegistrar(registrar: registrar as! NSObject)
//     }
//
//     public override func createMethodChannel()-> PlatformImpl{
//         return PlatformImpl()
//     }
//
// }



public class SwiftGeyanHybridPlugin: NSObject
//public class SwiftGeyanHybridPlugin: CommonPluginImpl<PlatformImpl>
, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
             let plugin = SwiftGeyanHybridPluginProxy.init(channelName : "abcdefd")
        plugin.registerWithRegistrar(registrar: registrar)
    }
    
    
}

class SwiftGeyanHybridPluginProxy : CommonPluginImpl<PlatformImpl> {
    public override func createMethodChannel() -> PlatformImpl {
           return PlatformImpl()
    }
}
