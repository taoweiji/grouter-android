package mock

import android.app.Application
import com.alibaba.fastjson.JSON
import com.grouter.GRouter
import com.grouter.demo.BuildConfig

class MockWebViewApplication : MockBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        GRouter.getInstance().addInterceptor(DebugMockIntercepter())
    }
}