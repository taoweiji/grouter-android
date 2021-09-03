package com.grouter.demo.other.service

import android.util.Log

import com.grouter.RouterComponent
import com.grouter.demo.base.model.User
import com.grouter.demo.base.service.AccountService

@RouterComponent(protocol = AccountService::class, name = "")
class AccountServiceImpl : AccountService {
    override fun getLoginUser(): User? {
        return User(101, "Wiki")
    }

    var stringBuilder: StringBuilder? = null

    constructor() {}

    constructor(stringBuilder: StringBuilder) {
        this.stringBuilder = stringBuilder
    }

    override fun sayHi() {
        Log.e("AccountServiceImpl", "sayHi")
    }

    override fun getUser(uid: Int): User? {
        return null
    }
}
