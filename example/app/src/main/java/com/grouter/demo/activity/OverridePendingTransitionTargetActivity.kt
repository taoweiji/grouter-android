package com.grouter.demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.grouter.GActivityBuilder
import com.grouter.GActivityCenter
import com.grouter.GActivityUtils

import com.grouter.RouterActivity
import com.grouter.demo.R
import kotlinx.android.synthetic.main.activity_override_pending_transition.*

@RouterActivity
class OverridePendingTransitionTargetActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ThemeOverlay_AppCompat_Dark)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_override_pending_transition_target)
        title = javaClass.simpleName
        val data = arrayOf("从右离开", "从左离开", "顶部离开", "底部离开", "淡出")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        list_view.adapter = adapter
        list_view.onItemClickListener = this
        
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> GActivityUtils.finishTransitionRightOut(this)
            1 -> GActivityUtils.finishTransitionLeftOut(this)
            2 -> GActivityUtils.finishTransitionTopOut(this)
            3 -> GActivityUtils.finishTransitionBottomOut(this)
            4 -> GActivityUtils.finishTransitionFadeOut(this)
        }
    }
}
