package com.grouter.demo.delegate

import android.content.Context
import com.grouter.RouterDelegate
import com.grouter.RouterDelegateConstructor
import com.grouter.RouterDelegateMethod
import com.grouter.demo.base.model.Moment
import java.util.*

@RouterDelegate
class MomentViewModel {
    private var context: Context

    @RouterDelegateConstructor
    constructor(context: Context) {
        this.context = context
    }

    @RouterDelegateMethod
    fun getMoment(uid: Int): Moment {
        return Moment()
    }

    @RouterDelegateMethod
    fun listMoment(ids: IntArray): List<Moment> {
        return ArrayList()
    }

    @RouterDelegateMethod
    fun listMoment2(ids: IntArray): Vector<Moment>? {
        return null
    }


    @RouterDelegateMethod
    fun delete(moment: Moment) {

    }





}
