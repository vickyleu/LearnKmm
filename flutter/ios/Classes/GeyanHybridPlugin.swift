import Flutter
import shared
class GeyanHybridPlugin : CommonPluginImpl<PlatformImpl>, FlutterPlugin{

 public static func register(with registrar: FlutterPluginRegistrar){
   let plugin = GeyanHybridPlugin.init(channelName : "abcdefd")
     plugin.registerWithRegistrar(registrar)
 }

override func createMethodChannel()-> PlatformImpl{
    return PlatformImpl()
}

}
