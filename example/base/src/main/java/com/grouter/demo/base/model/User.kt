package com.grouter.demo.base.model

import java.io.Serializable

/**
 * Created by Wiki on 19/7/30.
 */
class User() : Serializable {
    var uid: Int? = null
    var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }

    constructor(uid: Int?, name: String?) : this() {
        this.uid = uid
        this.name = name
    }

}
