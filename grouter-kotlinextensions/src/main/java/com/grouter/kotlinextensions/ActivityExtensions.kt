package com.grouter.kotlinextensions

import android.app.Activity
import com.grouter.GActivityUtils

//fun Activity.

/**
 * 从右边离开
 * 对应 [com.grouter.GActivityUtils).transitionRightIn]
 */
fun Activity.finishAsRightOut() {
    GActivityUtils.finishTransitionRightOut(this)
}

/**
 * 从左边离开
 * 对应 [com.grouter.GActivityUtils).transitionLeftIn]
 */
fun Activity.finishAsLeftOut() {
    GActivityUtils.finishTransitionLeftOut(this)
}

/**
 * 从底部离开
 * 对应 [com.grouter.GActivityUtils).transitionBottomIn]
 */
fun Activity.finishAsBottomOut() {
    GActivityUtils.finishTransitionBottomOut(this)
}

/**
 * 从顶部离开
 * 对应 [com.grouter.GActivityUtils).transitionTopIn]
 */
fun Activity.finishAsTopOut() {
    GActivityUtils.finishTransitionTopOut(this)
}


/**
 * 淡出
 * 对应 [com.grouter.GActivityUtils).transitionFadeIn]
 */
fun Activity.finishAsFadeOut() {
    GActivityUtils.finishTransitionFadeOut(this)
}