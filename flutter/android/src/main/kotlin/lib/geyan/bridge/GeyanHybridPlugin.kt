package lib.geyan.bridge
import com.uoocuniversity.geyanlib.PlatformImpl
import com.uoocuniversity.geyanlib.common.CommonPluginImpl

class GeyanHybridPlugin : CommonPluginImpl<PlatformImpl>("abcdefd") {
    override fun createMethodChannel(): PlatformImpl {
        return PlatformImpl()
    }

}