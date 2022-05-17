package lib.geyan.bridge
import com.uoocuniversity.geyanlib.PlatformImpl
import com.uoocuniversity.geyanlib.common.CommonPluginImpl
import io.flutter.plugin.common.PluginRegistry


class GeyanHybridPlugin : CommonPluginImpl<PlatformImpl>("geyan_plugin") {
    override fun createMethodChannel(): PlatformImpl {
        return PlatformImpl()
    }

   companion object{
       @JvmStatic
       fun registerWith(registrarFor: PluginRegistry.Registrar) {
           val instance = GeyanHybridPlugin()

       }
   }

}