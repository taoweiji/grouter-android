package com.t.example_shell_webview

import com.grouter.GRouter

class MockWebViewApplication : MockBaseApplication() {
    override fun onCreate() {
        GRouter.getInstance().addInterceptor(DebugMockInterceptor())
        super.onCreate()
    }
}