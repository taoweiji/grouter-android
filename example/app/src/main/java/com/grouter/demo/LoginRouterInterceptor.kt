package com.grouter.demo

import android.content.Context
import android.widget.Toast
import com.grouter.*
import com.grouter.demo.activity.LoginActivity

import com.grouter.demo.activity.UserActivity
import com.grouter.demo.base.model.User
import com.grouter.demo.webview.activity.WebViewActivity
import java.lang.Exception

import java.util.HashSet

@Suppress("unused")
@RouterInterceptor
class LoginRouterInterceptor : GRouterInterceptor() {
    /**
     * 没有登录状态下可以访问的页面
     */
    private val visitorAccess = listOf(
            UserActivity::class.java.name,
            MainActivity::class.java.name,
            WebViewActivity::class.java.name,
            LoginActivity::class.java.name
    )
    private var loginUser: User? = null

    override fun init(context: Context, buildType: String?, params: MutableMap<String, Any>?) {
        super.init(context, buildType, params)
        loginUser = params?.get("loginUser") as User?
    }

    override fun process(request: ActivityRequest): Boolean {
//        if (loginUser == null && !visitorAccess.contains(request.activityClass)) {
//            Toast.makeText(request.context, "请登录", Toast.LENGTH_SHORT).show()
//            request.onContinue(LoginActivity::class.java)
//            GActivityCenter.LoginActivity().start(request.context)
//            request.onInterrupt(Exception("无访问权限"))
//            return true
//        }
        return super.process(request)
    }

    override fun onError(request: ActivityRequest, exception: Exception): Boolean {
        exception.printStackTrace()
        Toast.makeText(request.context, "访问出错：$exception.message", Toast.LENGTH_SHORT).show()
//        GActivityCenter.ErrorActivity().start(request.context)
        return super.onError(request, exception)
    }
}
