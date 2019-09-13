package com.grouter.demo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.grouter.GRouter
import com.grouter.RouterActivity
import com.grouter.RouterField
import com.grouter.demo.base.model.User

@RouterActivity("user", name = "用户信息页", description = "展示用户信息")
class UserActivity : AppCompatActivity() {
    @RouterField
    var uid: Int? = null
    @RouterField
    var name: String? = null
    @RouterField
    var user: User? = null
    @RouterField
    var users: List<User>? = null
    @RouterField
    var uidArray: IntArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val time = System.currentTimeMillis()
        GRouter.inject(this)
//        Log.e("注入耗时", (System.currentTimeMillis() - time).toString())
        Log.e("uid", uid.toString())
        title = javaClass.simpleName
        user?.let { user ->
            Toast.makeText(this, "传递对象 User(uid:${user.uid},name:${user.name})", Toast.LENGTH_SHORT).show()
        }
        users?.let { users ->
            Toast.makeText(this, "传递List" + JSON.toJSONString(users), Toast.LENGTH_SHORT).show()
        }
        uidArray?.let { uidArray ->
            Toast.makeText(this, "传递数组" + JSON.toJSONString(uidArray), Toast.LENGTH_SHORT).show()
        }
        if (name != null) {
            Toast.makeText(this, "uid:$uid,name:$name", Toast.LENGTH_SHORT).show()
        }
//        ActivityUtils.getResult(this).value = User()
//        ActivityUtils.getResult(this).params["User"] = User()
    }
}
