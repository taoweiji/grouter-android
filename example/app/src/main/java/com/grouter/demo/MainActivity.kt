package com.grouter.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.grouter.*
import com.grouter.demo.activity.UserActivity

import com.grouter.demo.base.model.User
import com.grouter.demo.delegate.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

@RouterActivity(exported = false)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        GRouter.getInstance().openAppCheck(this)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            GActivityCenter.OverridePendingTransitionActivity().start(this)
            //            GActivityCenter.UserActivity().users(ArrayList<User>()).start(this)

//            GActivityCenter.WebViewActivity().asBottomIn().start(this)

            // 二级跳转，先跳转到SettingsActivity，然后跳转到AboutUsActivity
//            GActivityCenter.SettingsActivity().nextNav(GActivityCenter.AboutUsActivity()).start(this)
        }
        button2.setOnClickListener {
            // 自动化构造器跳转
            GActivityCenter.UserActivity().name("Wiki").uid(1).start(this)
        }
        button3.setOnClickListener {
            // 拼接参数构造器
            GRouter.getInstance().activityBuilder("user").put("name", "Wiki").put("uid", 1).start(this)
            // 如果是不同的工程，使用不同的scheme，可以通过带上scheme跳转
            // GRouter.getInstance().activityBuilder("grouter","user").start(this)
        }
        button4.setOnClickListener {
            // URL方式跳转
            GRouter.getInstance().startActivity(this, "grouter://activity/user?name=Wiki&uid=1")
        }
        button5.setOnClickListener {
            // 传递对象
            GActivityCenter.UserActivity().user(User(1, "Wiki")).start(this)
        }
        button6.setOnClickListener {
            // 传递List
            GActivityCenter.UserActivity().users(listOf(User(1, "Wiki"), User(2, "Sui"))).start(this)
        }
        button7.setOnClickListener {
            // 传递数组
            GActivityCenter.UserActivity().uidArray(arrayOf(1, 2, 3, 4).toIntArray()).start(this)
        }
        button8.setOnClickListener {
            // 指定 FLAG
            GActivityCenter.SettingsActivity().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).start(this)
        }
        button9.setOnClickListener {
            // 自定义 转场动画(常规方式)
            GActivityCenter.SettingsActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out).start(this)
        }


//        val accountService = GDelegateCenter.UserViewModel(this)
//        accountService.getUser(1)

//        val result = GRouter.getInstance().startActivity(this,"")

//        UserViewModel(this)
//        val uvm = GDelegateCenter.UserViewModel()
//        result.setResultListener()

//        val response = GRouter.getInstance().getTask("getUser").put("uid",1).put("name","Wiki").execute()
//        val response = GRouter.getInstance().getTask("grouter://task/getUser?uid=1&name=Wiki").execute()
//        var response = GTaskCenter.GetUserTask().uid(1).name("Wiki").execute()
//
//        class UserCopy :Serializable{
//            var uid: Int = 0
//            var name: String = ""
//        }

//        val userCpoy = response.value(UserCopy::class.java)
//        Log.e("userCpoy", userCpoy!!.name)

//        GRouter.getInstance().startActivity(this,"www.baidu.com")

//        startActivity(Intent(this,SecondActivity::class.java))
//        overridePendingTransition(enterAnim, exitAnim)

//
//        val user = response.value as User?
//        val userId = response.map["uid"]
//        val text = response.string
//
//
//
//
//        val accountServiceDelegate = GDelegateCenter.AccountService(this)
//        GActivityBuilder.defaultEnterAnim = R.anim.abc_fade_in
//        GActivityBuilder.defaultEnterAnim = R.anim.abc_fade_out

//        val name = GTaskCenter.GetUserTask().name("Wiki").uid(1).execute().map()["name"]
//        Log.e("name", name.toString())
        GRouter.getInstance().taskBuilder("grouter", "getUser").put("name", "Wiki").put("uid", "1").execute()


//        val response = GRouter.getInstance().taskBuilder("getLoginUser").put("uid", 1).execute()
//        Log.e("getLoginUser", "结果${response.string()}")
    }
}
