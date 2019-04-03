@file:Suppress("unused")

package com.thejoyrun.router

import android.app.Activity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Activity.routerIntArgOr(argName: String, defaultValue: Int = 0): ReadWriteProperty<Activity, Int> =
        ArgLazy(argName) { _, _: KProperty<*> -> readIntArgOr(argName, defaultValue) }

fun Activity.routerBooleanArgOr(argName: String, defaultValue: Boolean = false): ReadWriteProperty<Activity, Boolean> =
        ArgLazy(argName) { _, _: KProperty<*> -> readBooleanArgOr(argName, defaultValue) }

fun Activity.routerLongArgOr(argName: String, defaultValue: Long = 0L): ReadWriteProperty<Activity, Long> =
        ArgLazy(argName) { _, _: KProperty<*> -> readLongArgOr(argName, defaultValue) }

fun Activity.routerDoubleArgOr(argName: String, defaultValue: Double = 0.toDouble()): ReadWriteProperty<Activity, Double> =
        ArgLazy(argName) { _, _: KProperty<*> -> readDoubleArgOr(argName, defaultValue) }

fun Activity.routerFloatArgOr(argName: String, defaultValue: Float = 0f): ReadWriteProperty<Activity, Float> =
        ArgLazy(argName) { _, _: KProperty<*> -> readFloatArgOr(argName, defaultValue) }

fun Activity.routerStringArgOr(argName: String, defaultValue: String? = null): ReadWriteProperty<Activity, String> =
        ArgLazy(argName) { _, _: KProperty<*> -> readStringArgOr(argName, defaultValue) }


fun Activity.readIntArgOr(argName: String, defaultValue: Int): Int = readIntArg(this, argName, defaultValue)

fun Activity.readBooleanArgOr(argName: String, defaultValue: Boolean): Boolean = readBooleanArg(this, argName, defaultValue)

fun Activity.readLongArgOr(argName: String, defaultValue: Long): Long = readLongArg(this, argName, defaultValue)

fun Activity.readDoubleArgOr(argName: String, defaultValue: Double): Double = readDoubleArg(this, argName, defaultValue)

fun Activity.readFloatArgOr(argName: String, defaultValue: Float): Float = readFloatArg(this, argName, defaultValue)

fun Activity.readStringArgOr(argName: String, defaultValue: String?): String = readStringArg(this, argName, defaultValue)


fun readIntArg(activity: Activity, argName: String, defaultValue: Int): Int {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getInt(argName, defaultValue)
}

fun readBooleanArg(activity: Activity, argName: String, defaultValue: Boolean): Boolean {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getBoolean(argName, defaultValue)
}

fun readLongArg(activity: Activity, argName: String, defaultValue: Long): Long {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getLong(argName, defaultValue)
}

fun readDoubleArg(activity: Activity, argName: String, defaultValue: Double): Double {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getDouble(argName, defaultValue)
}

fun readFloatArg(activity: Activity, argName: String, defaultValue: Float): Float {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getFloat(argName, defaultValue)
}

fun readStringArg(activity: Activity, argName: String, defaultValue: String?): String {
    return SafeBundle(activity.intent?.extras, activity.intent?.data).getString(argName, defaultValue)
}

class ArgLazy<in REF, OUT : Any>(private val argName: String, private val initializer: (REF, KProperty<*>) -> OUT?) : ReadWriteProperty<REF, OUT> {
    private object EMPTY

    private var arg: Any = EMPTY

    override fun getValue(thisRef: REF, property: KProperty<*>): OUT {
        if (arg == EMPTY) {
            arg = requireNotNull(initializer(thisRef, property)) { "Not found arg '$argName' from arguments." }
        }
        @Suppress("UNCHECKED_CAST")
        return arg as OUT
    }

    override fun setValue(thisRef: REF, property: KProperty<*>, value: OUT) {
        this.arg = value
    }
}