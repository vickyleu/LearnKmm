import Flutter
import shared
class GeyanHybridPlugin : CommonPluginImpl<PlatformImpl>, FlutterPlugin{

override class func registerWithRegistrar(registrar:NSObject<FlutterPluginRegistrar>){
    let plugin = GeyanHybridPlugin.init("abcdefd")
     plugin.registerWithRegistrar(registrar)
}
override func createMethodChannel()-> PlatformImpl{
    return PlatformImpl()
}

}