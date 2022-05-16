import Flutter
import shared

public class SwiftGeyanHybridPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let plugin = SwiftGeyanHybridPluginProxy.init(channelName : "geyan_plugin")
        plugin.registerWithRegistrar(registrar: registrar)
    }
}

class SwiftGeyanHybridPluginProxy : CommonPluginImpl<PlatformImpl> {
    public override func createMethodChannel() -> PlatformImpl {
        return PlatformImpl()
    }
}
