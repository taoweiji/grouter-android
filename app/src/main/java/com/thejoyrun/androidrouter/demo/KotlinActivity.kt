package com.thejoyrun.androidrouter.demo

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.thejoyrun.router.RouterActivity
import com.thejoyrun.router.routerBooleanArgOr
import com.thejoyrun.router.routerIntArgOr
import com.thejoyrun.router.routerStringArgOr

@RouterActivity("fourth")
class KotlinActivity : AppCompatActivity() {
    private val uid: Int by routerIntArgOr("uid")
    private val age: Int by routerIntArgOr("age")
    private val name: String by routerStringArgOr("name")
    private val man by routerBooleanArgOr("man", true)
    private val manger: Boolean by routerBooleanArgOr("manger", false)
    private val formActivity: String by routerStringArgOr("formActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val builder = StringBuilder()
        builder.append("uid:$uid").append('\n')
        builder.append("age:$age").append('\n')
        builder.append("name:$name").append('\n')
        builder.append("man:$man").append('\n')
        builder.append("manger:$manger").append('\n')
        builder.append("formActivity:" + formActivity.toString()).append('\n')
        val textView = findViewById<View>(R.id.text) as TextView
        textView.text = builder.toString()
    }
}