package com.t.example_shell_webview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSON
import com.grouter.GActivityCenter
import com.grouter.GComponentCenter
import com.grouter.GRouter
import com.grouter.demo.base.model.User
import com.grouter.demo.base.service.UserService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val response = GRouter.getInstance().taskBuilder("getLoginUser").put("uid", 1).execute()
        Log.e("getLoginUser", "结果${response.string()}")
        open_web_view.setOnClickListener {
            GActivityCenter.WebViewActivity().url("http://baidu.com").start(this)
        }
        val userService = GComponentCenter.UserServiceImpl()
        val user = userService.getUser(1)
        Log.e("getUser", "结果${JSON.toJSONString(user)}")

        val accountService = GComponentCenter.AccountServiceImpl()
        val loginUser: User? = accountService.getLoginUser()
        Log.e("loginUser", "结果${JSON.toJSONString(loginUser)}")

        GRouter.getInstance().getComponent("userService",UserService::class.java)

        GRouter.getInstance().taskBuilder("grouter://task/getUser?uid=1").execute().value(User::class.java)
    }
}
