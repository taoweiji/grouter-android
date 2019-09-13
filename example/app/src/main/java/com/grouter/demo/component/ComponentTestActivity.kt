package com.grouter.demo.component

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.grouter.GComponentCenter
import com.grouter.GRouter
import com.grouter.GTaskCenter
import com.grouter.demo.R
import com.grouter.demo.base.service.UserService

import kotlinx.android.synthetic.main.activity_component_test.*

class ComponentTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_test)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val userService = GComponentCenter.UserServiceImpl(this)
            GComponentCenter.FeedServiceImpl()
//            GComponentCenter.getFeedService(this)
            userService.sayHello()
            val feedService = GComponentCenter.FeedServiceImpl()
//            feedService.listUser()
        }
//        val userService1 = GRouter.getInstance().getComponent("base/user",UserService::class.java)
//        val userService2 = GRouter.getInstance().getComponent(UserService::class.java)
        val userService = GComponentCenter.UserServiceImpl()
        GComponentCenter.UserFragment()
//        val map = GTaskCenter.UserLoginTask().execute().map()
//        map["uid"]

    }
}
